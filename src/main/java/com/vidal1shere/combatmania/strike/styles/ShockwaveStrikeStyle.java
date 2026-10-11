package com.vidal1shere.combatmania.strike.styles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.definition.HammerAnimations;
import com.vidal1shere.combatmania.client.animation.item.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.hud.config.StrikeIndicator;
import com.vidal1shere.combatmania.data.attachment.ShockwaveData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.data.component.ShockwaveProperties;
import com.vidal1shere.combatmania.event.game.StaggerAttackEvent;
import com.vidal1shere.combatmania.network.ShockwaveParticlePayload;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.util.Color;
import com.vidal1shere.combatmania.util.RaycastUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static com.vidal1shere.combatmania.util.CustomProperties.PropertyKey;

public class ShockwaveStrikeStyle extends StrikeStyle {

    public static final PropertyKey<ShockwaveAnimationSupplier> SHOCKWAVE_ANIMATION = new PropertyKey<>(ShockwaveStrikeStyle::defaultShockwaveAnimation);
    private static final int MIN_SWING_TICKS = 4;
    
    public ShockwaveStrikeStyle(StrikeProperties properties) {
        super(StrikeProperties.builder(properties)
                .maxDuration(ShockwaveStrikeStyle::getMaxDuration)
                .build());
    }
    
    public static int getMaxDuration(ItemStack stack, LivingEntity entity) {
        ShockwaveData data = entity.getData(CMAttachments.SHOCKWAVE);
        if (data.getStage() == ShockwaveData.Stage.NONE) {
            return 1;
        }
        
        return 72000;
    }

    @Override
    public void onStart(StrikeContext ctx) {
        ShockwaveData.startLift(ctx.player());
    }

    @Override
    public void onTick(StrikeContext ctx, int ticksHeld) {
        Player player = ctx.player();
        ShockwaveData data = ShockwaveData.get(player);
        ShockwaveProperties props = ctx.itemStack().getOrDefault(CMDataComponents.SHOCKWAVE, new ShockwaveProperties());
        
//        player.sendSystemMessage(Component.literal("" + ticksHeld));
        
        switch (data.getStage()) {
            case LIFT -> {
                ShockwaveData.tickLift(player);
                if (data.getLiftTicks() >= props.maxLiftTicks()) {
                    ShockwaveData.startSwing(player);
                    beginSwing(player, props);
                }
            }
            case SWING -> {
                int swingTicks = ticksHeld - props.maxLiftTicks();
                if (swingTicks >= MIN_SWING_TICKS && player.onGround()) {
                    ShockwaveData.startHit(player, false);
                } else if (!player.onGround()) {
                    ShockwaveData.tickSwing(player);
                }
            }
            case HIT -> {
                performHit(player, ctx.itemStack(), props, data);
                ShockwaveData.reset(player, true);
            }
            case NONE -> {}
        }
    }

    @Override
    public void onStop(StrikeContext ctx, int ticksHeld) {
        Player player = ctx.player();
        ShockwaveData data = ShockwaveData.get(player);
        ShockwaveProperties props = ctx.itemStack().getOrDefault(CMDataComponents.SHOCKWAVE, new ShockwaveProperties());

        switch (data.getStage()) {
            case LIFT -> {
                ShockwaveData.startHit(player, true);
                performHit(player, ctx.itemStack(), props, data);
                ShockwaveData.reset(player, false);
            }
            case SWING -> {
                ShockwaveData.startHit(player, false);
                performHit(player, ctx.itemStack(), props, data);
                ShockwaveData.reset(player, false);
            }
            case HIT, NONE -> ShockwaveData.reset(player, true);
        }
    }

    private void beginSwing(Player player, ShockwaveProperties props) {
        Vec3 current = player.getDeltaMovement();
        player.setDeltaMovement(current.x, current.y - props.fallSpeed(), current.z);
        if (player instanceof ServerPlayer sp) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
        }
    }

    private void performHit(Player player, ItemStack stack, ShockwaveProperties props, ShockwaveData data) {
        float fallDistance = data.getFallDistance();
        float liftRatio = props.maxLiftTicks() > 0 ? Math.min(1.0F, (float) data.getLiftTicks() / props.maxLiftTicks()) : 1.0F;
        float radius = props.getEffectiveRadius(fallDistance, data.getLiftTicks());

        Vec3 shockwaveCenter = player.position().add(0.0, 0.05, 0.0);
        BlockPos shockwaveBlock = player.getOnPos();
        double blockInteractionRange = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
        double entityInteractionRange = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        HitResult hitResult = RaycastUtil.pick(player, blockInteractionRange, entityInteractionRange);

        if (hitResult instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            shockwaveCenter = new Vec3(target.getX(), target.getY(0.05), target.getZ());
            shockwaveBlock = BlockPos.containing(target.getX(), target.getY() - 0.01, target.getZ());
        } else if (hitResult instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
            Vec3 hitLocation = blockHit.getLocation();
            shockwaveCenter = new Vec3(hitLocation.x, hitLocation.y + 0.05, hitLocation.z);
            shockwaveBlock = blockHit.getBlockPos();
        } else if (hitResult.getType() == HitResult.Type.MISS) {
            if (liftRatio >= 1.0F && player.onGround()) {
                shockwaveCenter = player.position();
                shockwaveBlock = player.getOnPos();
            } else {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
                return;
            }
        }

        if (radius > 1) {
            if (player.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.EXPLOSION,
                        shockwaveCenter.x, shockwaveCenter.y, shockwaveCenter.z,
                        (int)(radius * 3), radius * 0.3, 0.1, radius * 0.3, 0.0);
            }
        }

        if (hitResult instanceof EntityHitResult) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
        } else if (hitResult instanceof BlockHitResult blockHit) {
            BlockState state = player.level().getBlockState(blockHit.getBlockPos());
            SoundType soundType = state.getSoundType(player.level(), blockHit.getBlockPos(), player);

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    soundType.getBreakSound(), player.getSoundSource(), 1.0F, 1.0F);
        } else {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
        }
        
        SoundEvent hammerHitSound = liftRatio <= 0.25 ? SoundEvents.MACE_SMASH_AIR :
                liftRatio < 1.0 ? SoundEvents.MACE_SMASH_GROUND : SoundEvents.MACE_SMASH_GROUND_HEAVY;

        player.level().playSound(null, shockwaveCenter.x, shockwaveCenter.y, shockwaveCenter.z,
                hammerHitSound, player.getSoundSource(), 1.0F, 1.0F);

        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                player, new ShockwaveParticlePayload(shockwaveBlock, Math.min(1000, Math.round(100 * radius)), radius, 2.5));

        AABB searchBox = new AABB(
                shockwaveCenter.x - radius,
                shockwaveCenter.y - radius,
                shockwaveCenter.z - radius,
                shockwaveCenter.x + radius,
                shockwaveCenter.y + radius,
                shockwaveCenter.z + radius
        );

        List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class, searchBox, shockwavePredicate(player));

        var damagesource = player.damageSources().playerAttack(player);

        for (LivingEntity target : entities) {
            if (data.hasHitEntity(target)) continue;

            if (!CommonHooks.onPlayerAttackTarget(player, target)) continue;
            if (!target.isAttackable() || target.skipAttackInteraction(player)) continue;

            float baseDamage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float rawDamage = baseDamage + (props.knockbackPower() * liftRatio) + (props.fallRadiusMultiplier() * fallDistance);
            if (props.maxDamage() > 0) rawDamage = Math.min(props.maxDamage(), rawDamage);
            float damage = getEnchantedDamage(player, stack, target, rawDamage, damagesource);
            
            float healthBefore = target.getHealth();
            boolean hurt = target.hurt(damagesource, damage);
            if (!hurt) continue;

            ShockwaveData.registerEntity(player, target);

            Vec3 dir = target.position().subtract(shockwaveCenter);
            double power = getKnockbackPower(liftRatio, target, dir, props);
            Vec3 knockback = dir.normalize().scale(power);
            double verticalPower = power * getVerticalKnockbackMultiplier(dir.length(), props.knockbackRadius());

            if (power > 0) {
                target.push(knockback.x, verticalPower, knockback.z);
                if (target instanceof ServerPlayer sp) {
                    sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
                }
            }

            float attackStagger = (float) player.getAttributeValue(CMAttributes.ATTACK_STAGGER);

            if (attackStagger > 0.0F) {
                float resistance = (float) target.getAttributeValue(CMAttributes.STAGGER_RESISTANCE);

                float strength = attackStagger * (1.0F - resistance);

                if (strength > 0.0F) {
                    int duration = Mth.floor(strength * 8.0F);

                    StaggerAttackEvent event = new StaggerAttackEvent(target, player, strength, duration);

                    NeoForge.EVENT_BUS.post(event);

                    if (!event.isCanceled()) {
                        StaggerData.applyStagger(target, event.getStrength(), event.getDuration());
                    }
                }
            }

            Entity actualTarget = ((Entity) target) instanceof PartEntity<?> pe ? pe.getParent() : target;
            Level level = player.level();
            if (level instanceof ServerLevel sl) {
                if (actualTarget instanceof LivingEntity le) {
                    boolean itemHurt = stack.hurtEnemy(le, player);
                    EnchantmentHelper.doPostAttackEffects(sl, target, damagesource);
                    if (!stack.isEmpty() && itemHurt) stack.postHurtEnemy(le, player);
                    if (stack.isEmpty()) {
                        EventHooks.onPlayerDestroyItem(player, stack.copy(), stack == player.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                    }
                }
            }

            float dealt = healthBefore - target.getHealth();
            player.awardStat(Stats.DAMAGE_DEALT, Math.round(dealt * 10.0F));
            if (player.level() instanceof ServerLevel sl && dealt > 2.0F) {
                int count = (int)(dealt * 0.5F);
                sl.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                        target.getX(), target.getY(0.5), target.getZ(),
                        count, 0.1, 0.0, 0.1, 0.2);
            }

            player.setLastHurtMob(target);
        }

        player.resetFallDistance();
        player.causeFoodExhaustion(0.1F);
    }

    private double getKnockbackPower(float liftRatio, LivingEntity entity, Vec3 dir, ShockwaveProperties props) {
        double radius = props.knockbackRadius();
        double power = props.knockbackPower();
        double kbResistance = 1.0 - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        return (radius - dir.length()) * power * kbResistance * liftRatio;
    }

    private double getVerticalKnockbackMultiplier(double distance, double radius) {
        double proximity = Math.min(1.0, distance / radius);
        return 0.5 + proximity * 0.5;
    }

    private Predicate<LivingEntity> shockwavePredicate(Player player) {
        return entity -> {
            if (entity.isSpectator()) return false;
            if (entity == player) return false;
            if (player.isAlliedTo(entity)) return false;
            if (entity instanceof TamableAnimal t && t.isTame() && player.getUUID().equals(t.getOwnerUUID())) return false;
            if (entity instanceof ArmorStand a && a.isMarker()) return false;
            return true;
        };
    }

    private float getEnchantedDamage(Player player, ItemStack weapon, Entity entity, float damage, DamageSource source) {
        if (!(player.level() instanceof ServerLevel sl)) return damage;
        return EnchantmentHelper.modifyDamage(sl, weapon, entity, source, damage);
    }

    @Override
    public void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        if (hand != InteractionHand.MAIN_HAND) return;

        ShockwaveData data = player.getData(CMAttachments.SHOCKWAVE);
        ShockwaveData.Stage stage = data.getStage();

        if (!data.hasQueuedAnimation()) {
            return;
        }

        ItemAnimationState.LoopType loopType = switch (stage) {
            case NONE, HIT -> ItemAnimationState.LoopType.PLAY_ONCE;
            default -> ItemAnimationState.LoopType.HOLD_ON_LAST_FRAME;
        };

        ItemAnimationDefinition definition = properties().get(SHOCKWAVE_ANIMATION)
                .get(stack, hand, player, state, data.getStage());

//        System.out.println("-----");
//        System.out.println("[Shockwave SetupAnim] ANIMATION QUEUED");
//        System.out.println("[Shockwave SetupAnim] STAGE: " + stage.name());
//        System.out.println("[Shockwave SetupAnim] STATE STARTED BEFORE?: " + (state.isStarted() ? "true" : false));
//        System.out.println("[Shockwave SetupAnim] ANIMATION QUEUED BEFORE?: " + (data.hasQueuedAnimation() ? "true" : false));
//        System.out.println("[Shockwave SetupAnim] ANIMATION DEFINITION BEFORE: " + (definition == null ? "null" : definition));
        ShockwaveData.consumeAnimation(player);
        state.setActiveDefinition(definition);
        state.start(player.tickCount, loopType);
//        System.out.println("[Shockwave SetupAnim] STATE STARTED AFTER?: " + (state.isStarted() ? "true" : false));
//        System.out.println("[Shockwave SetupAnim] ANIMATION QUEUED AFTER?: " + (data.hasQueuedAnimation() ? "true" : false));
//        System.out.println("[Shockwave SetupAnim] ANIMATION DEFINITION AFTER: " + (definition == null ? "null" : definition));
    }

    @Override
    public void onAnimStop(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        ShockwaveData data = player.getData(CMAttachments.SHOCKWAVE);
        
        if (!data.isEarlyHit()) {
            if (state.getLoopType() != ItemAnimationState.LoopType.PLAY_ONCE) {
                state.stop();
            }
            return;
        }
        
        ItemAnimationDefinition definition = properties().get(SHOCKWAVE_ANIMATION)
                .get(stack, hand, player, state, ShockwaveData.Stage.HIT);

        ShockwaveData.consumeEarlyHit(player);
        state.setActiveDefinition(definition);
        state.start(player.tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
    }

    public static ItemAnimationDefinition defaultShockwaveAnimation(ItemStack stack, InteractionHand hand, Player player,
                                                                ItemAnimationState state, ShockwaveData.Stage stage) {
        return switch (stage) {
            case NONE -> null;
            case LIFT -> HammerAnimations.shockwaveLift();
            case SWING -> HammerAnimations.shockwaveSwing();
            case HIT -> {
                if (ShockwaveData.get(player).isEarlyHit()) {
                    yield HammerAnimations.shockwaveEarlyHit();
                } else {
                    yield HammerAnimations.shockwaveHit();
                }
            }
        };
    }

    @Override
    public @Nullable StrikeIndicator getCustomStrikeIndicator() {
        return StrikeIndicator.SHOCKWAVE;
    }

    @FunctionalInterface
    public interface ShockwaveAnimationSupplier {
        ItemAnimationDefinition get(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, ShockwaveData.Stage stage);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void preRenderStrikeIndicator(GuiGraphics guiGraphics, Player player, ItemStack stack, StrikeIndicator indicator, int x, int y, boolean rotated, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.options.hideGui) return;
        
        ShockwaveData data = ShockwaveData.get(player);
        if (data.getStage() == ShockwaveData.Stage.NONE) return;

        ShockwaveProperties properties = stack.getOrDefault(CMDataComponents.SHOCKWAVE, new ShockwaveProperties());
        float elapsedTicks = (player.tickCount - data.getLiftStartTick()) + partialTick;
        float scale = Math.min(1.0F, elapsedTicks / properties.maxLiftTicks());
        int hexColor1;
        int hexColor2;
        float colorScale;

        if (scale < 0.5F) {
            hexColor1 = 0xFF0000;
            hexColor2 = 0xFFFF00;
            colorScale = scale / 0.5F;
        } else {
            hexColor1 = 0xFFFF00;
            hexColor2 = 0x00C000;
            colorScale = (scale - 0.5F) / 0.5F;
        }
        
        Color color = Color.lerpRGBA(new Color(hexColor1, 0.5F), new Color(hexColor2, 0.5F), colorScale);
        renderShockwaveOutline(guiGraphics, scale, x, y, rotated, color);

        if (!(mc.crosshairPickEntity instanceof LivingEntity le) || !le.isAlive()) {
            renderShockwaveIndicator(guiGraphics, x, y, rotated);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderShockwaveIndicator(GuiGraphics guiGraphics, int x, int y, boolean rotated) {
        int width = StrikeIndicator.SHOCKWAVE.width();
        int height = StrikeIndicator.SHOCKWAVE.height();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        
        guiGraphics.pose().pushPose();

        if (rotated) {
            guiGraphics.pose().translate(x + width / 2.0F, y + height / 2.0F, 0.0F);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0F));
            x = -width / 2;
            y = -height / 2;
        }

        guiGraphics.blitSprite(StrikeIndicator.SHOCKWAVE.getBackgroundSprite(), x, y, width, height);
        guiGraphics.blitSprite(StrikeIndicator.SHOCKWAVE.getProgressSprite(), width, height, 0, 0, x, y, width, height);

        guiGraphics.pose().popPose();

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    @OnlyIn(Dist.CLIENT)
    private void renderShockwaveOutline(GuiGraphics guiGraphics, float scale, int x, int y, boolean rotated, Color color) {
        int width = StrikeIndicator.SHOCKWAVE.width();
        int height = StrikeIndicator.SHOCKWAVE.height();
        int outlineWidth = 18;
        int outlineHeight = 12;
        int progress = Math.min(outlineWidth, (int)(scale * outlineWidth));

        float alpha = color.alpha;
        float red = color.getRedF();
        float green = color.getGreenF();
        float blue = color.getBlueF();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(red, green, blue, alpha);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + width / 2.0F, y + height / 2.0F, 0.0F);

        if (rotated) {
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0F));
        }

        guiGraphics.blitSprite(CMUtil.modResLoc("hud/shockwave_indicator_outline"), outlineWidth, outlineHeight, 0, 0, -outlineWidth / 2, -outlineHeight / 2, progress, outlineHeight);

        guiGraphics.pose().popPose();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}