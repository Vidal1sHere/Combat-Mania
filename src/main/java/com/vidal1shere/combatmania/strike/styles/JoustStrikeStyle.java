package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.api.extension.entity.JoustMotionAware;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

import static com.vidal1shere.combatmania.util.CustomProperties.Key;

public class JoustStrikeStyle extends StrikeStyle {

    public static final Key<Float> JOUST_MIN_SCALE = new Key<>(1.0F);
    public static final Key<Float> HIT_INFLATION = new Key<>(0.125F);
    public static final Key<Float> MIN_SPEED_FOR_DAMAGE = new Key<>(4.6F);
    public static final Key<Float> MIN_SPEED_FOR_KNOCKBACK = new Key<>(5.1F);
    public static final Key<BiPredicate<ItemStack, Player>> IS_LIGHT = new Key<>((s, p) ->
            s.getItem() instanceof TieredItem t && CMUtil.isWoodTier(t.getTier()));
    public static final Key<SoundEvent> ENGAGE_SOUND = new Key<>(CMSounds.SPEAR_USE.get());
    public static final Key<SoundEvent> LIGHT_ENGAGE_SOUND = new Key<>(CMSounds.WOODEN_SPEAR_USE.get());
    public static final Key<SoundEvent> HIT_SOUND = new Key<>(CMSounds.SPEAR_HIT.get());
    public static final Key<SoundEvent> LIGHT_HIT_SOUND = new Key<>(CMSounds.WOODEN_SPEAR_HIT.get());
    
    public JoustStrikeStyle(StrikeProperties properties) {
        super(StrikeProperties.builder(properties)
                .maxDuration(JoustStrikeStyle::getMaxDuration)
                .build());
    }

    public static int getMaxDuration(ItemStack stack, LivingEntity entity) {
        return stack.getOrDefault(CMDataComponents.JOUST, new JoustProperties()).getTotalDuration();
    }
    
    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        ItemStack itemstack = ctx.itemStack();
        boolean isLight = properties().get(IS_LIGHT).test(itemstack, player);
        
        JoustData.start(player);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                isLight ? properties().get(LIGHT_ENGAGE_SOUND) : properties().get(ENGAGE_SOUND), 
                player.getSoundSource(), 1.0F, 1.0F);
    }
    
    @Override
    public void onTick(StrikeContext ctx, int ticksHeld) {
        Player player = ctx.player();
        ItemStack stack = ctx.itemStack();
        JoustProperties props = stack.getOrDefault(CMDataComponents.JOUST, new JoustProperties());
        JoustData joust = player.getData(CMAttachments.JOUST);

        progressStage(player, joust, props, ticksHeld);

        if (ticksHeld < props.getEquipDurationTicks()) return;

        Collection<EntityHitResult> hits = findTargets(player, ctx.baseReach());

        for (EntityHitResult hit : hits) {
            tryHit(player, stack, hit.getEntity(), joust, props, ctx.slot());
        }
    }
    
    @Override
    public void onStop(StrikeContext ctx, int ticksHeld) {
        System.out.println("[JOUST] STOP ticks=" + ticksHeld);
        JoustData.reset(ctx.player());
    }
    
    @Override
    public float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        JoustData.Stage stage = player.getData(CMAttachments.JOUST).getStage();
        JoustProperties props = stack.getOrDefault(CMDataComponents.JOUST, new JoustProperties());
        return props.getSpeedForStage(stage);
    }

    private void progressStage(Player player, JoustData joust, JoustProperties props, int ticksHeld) {
        JoustData.Stage oldStage = joust.getStage();
        int engagedEnd = props.getDurationTicks(JoustData.Stage.ENGAGED);
        int tiredEnd = engagedEnd + props.getDurationTicks(JoustData.Stage.TIRED);

        JoustData.Stage stage;
        if (ticksHeld < engagedEnd) {
            stage = JoustData.Stage.ENGAGED;
        } else if (ticksHeld < tiredEnd) {
            stage = JoustData.Stage.TIRED;
        } else {
            stage = JoustData.Stage.DISENGAGED;
        }
        
        if (stage != oldStage) {
            JoustData.increaseStage(player);
        }
    }

    // -- target detection --

    private Collection<EntityHitResult> findTargets(Player player, float reach) {
        Vec3 eyePosition = player.getEyePosition(1.0F);
        Vec3 look = player.getViewVector(1.0F);

        Vec3 attackerMovement = getChargeMovement(player);

        // the forward movement of the attacker extends the effective hit range
        double movementComponent = attackerMovement.dot(look);

        Vec3 from = eyePosition;
        Vec3 to = eyePosition.add(look.scale(reach + Math.max(0.0, movementComponent)));

        // clip the attack path against blocks first
        BlockHitResult blockHit = player.level().clip(
                new ClipContext(eyePosition, to,
                        ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,
                        player
                )
        );

        if (blockHit.getType() != HitResult.Type.MISS) {
            to = blockHit.getLocation();

            // a block was hit before the attack actually reached its range
            if (eyePosition.distanceToSqr(to) < eyePosition.distanceToSqr(from)) {
                return List.of();
            }
        }

        // build a search volume along the attack segment
        AABB searchArea = AABB.ofSize(from, 0.0, 0.0, 0.0).expandTowards(to.subtract(from)).inflate(1.0);

        List<EntityHitResult> hits = new ArrayList<>();

        for (Entity entity : player.level().getEntities(
                player,
                searchArea,
                target -> target.isAttackable()
                        && !target.isSpectator()
                        && target != player
        )) {
            AABB boundingBox = entity.getBoundingBox().inflate(properties().get(HIT_INFLATION));

            Optional<Vec3> hit = boundingBox.clip(from, to);

            if (hit.isPresent()) {
                hits.add(new EntityHitResult(entity, hit.get()));
            }
        }

        return hits;
    }

    private void tryHit(Player player, ItemStack stack, Entity target,
                        JoustData joust, JoustProperties props, StrikeSlot slot) {
        if (!CommonHooks.onPlayerAttackTarget(player, target)) return;
        if (player.isPassengerOfSameVehicle(target)) return;
        if (joust.hasHitEntity(target)) return;
        if (!target.isAttackable() || target.skipAttackInteraction(player)) return;
        
        StrikeData data = player.getData(CMAttachments.STRIKE);
        float scale = data.getScale(slot);
        float minScale = properties().get(JOUST_MIN_SCALE);
        if (scale < minScale) return;
        
        float relativeSpeed = getRelativeSpeed(player, target);
        float minDamageSpeed = properties().get(MIN_SPEED_FOR_DAMAGE);
        if (relativeSpeed < minDamageSpeed) return;

        var damagesource = player.damageSources().playerAttack(player);
        float damage = props.getDamageMultiplier() * relativeSpeed;
        damage = getEnchantedDamage(player, stack, target, damage, damagesource);

        float targetHealthBefore = target instanceof LivingEntity le ? le.getHealth() : 0;
        Vec3 targetMotionBefore = target.getDeltaMovement();
        boolean hurt = target.hurt(damagesource, damage);
        if (!hurt) return;

        JoustData.registerEntity(player, target);
        
        int duration = (int) (1.0 / player.getAttributeValue(getSpeedAttribute()) * 20);
        data.exhaust(slot, duration);
        player.syncData(CMAttachments.STRIKE);

        float minKnockbackSpeed = properties().get(MIN_SPEED_FOR_KNOCKBACK);
        float minDismountSpeed = props.getMinDismountSpeed();
        boolean isLight = properties().get(IS_LIGHT).test(stack, player);

        player.level().playSound(null, 
                player.getX(), player.getY(), player.getZ(),
                isLight ? properties().get(HIT_SOUND)
                        : properties().get(LIGHT_HIT_SOUND),
                player.getSoundSource(), 1.0F, 1.0F
        );

        if (joust.canPerformAction(JoustData.Action.KNOCKBACK) && relativeSpeed >= minKnockbackSpeed) {
            applyKnockback(player, stack, target, damagesource);
        }

        if (joust.canPerformAction(JoustData.Action.DISMOUNT) && relativeSpeed >= minDismountSpeed) {
            target.stopRiding();
        }

        if (target instanceof ServerPlayer sp && sp.hurtMarked) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            sp.hurtMarked = false;
            sp.setDeltaMovement(targetMotionBefore);
        }

        Entity actualTarget = target instanceof PartEntity<?> pe ? pe.getParent() : target;

        Level level = player.level();

        if (level instanceof ServerLevel sl) {
            if (actualTarget instanceof LivingEntity le) {
                boolean itemHurt = stack.hurtEnemy(le, player);

                EnchantmentHelper.doPostAttackEffects(sl, target, damagesource);

                if (!stack.isEmpty() && itemHurt) {
                    stack.postHurtEnemy(le, player);
                }

                if (stack.isEmpty()) {
                    EventHooks.onPlayerDestroyItem(player, stack.copy(),
                            stack == player.getMainHandItem()
                                    ? InteractionHand.MAIN_HAND
                                    : InteractionHand.OFF_HAND
                    );
                }
            }
        }

        if (target instanceof LivingEntity le) {
            float dealt = targetHealthBefore - le.getHealth();

            player.awardStat(Stats.DAMAGE_DEALT, Math.round(dealt * 10.0F));
            if (level instanceof ServerLevel sl && dealt > 2.0F) {
                int count = (int) (dealt * 0.5F);

                sl.sendParticles(
                        ParticleTypes.DAMAGE_INDICATOR,
                        target.getX(), target.getY(0.5), target.getZ(),
                        count, 0.1, 0.0, 0.1, 0.2
                );
            }
        }

        player.level().playSound(null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F
        );

        player.setLastHurtMob(target);
        player.causeFoodExhaustion(0.1F);
    }

    @Override
    public void onAnimStop(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
//        state.start(tickCount);
    }

    @Override
    public void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        JoustData data = player.getData(CMAttachments.JOUST);
        JoustData.Stage stage = data.getStage();
       
        if (!data.hasQueuedAnimation()) {
            return;
        }
        
        ItemAnimationState.LoopType loopType = switch (stage) {
            case NONE, DISENGAGED -> ItemAnimationState.LoopType.PLAY_ONCE;
            default -> ItemAnimationState.LoopType.HOLD_ON_LAST_FRAME;
        };
        
        JoustData.consumeAnimation(player);
        state.start(player.tickCount, loopType);
    }

    @Override
    public ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        JoustData data = player.getData(CMAttachments.JOUST);
        return switch (data.getStage()) {
            case NONE -> null;
            case ENGAGED -> SpearAnimations.joustEngaged();
            case TIRED -> SpearAnimations.joustTired();
            case DISENGAGED -> SpearAnimations.joustDisengaged();
        };
    }

    // -- helpers --

    private Vec3 getChargeMovement(Player player) {
        Entity vehicle = player.getVehicle();

        if (vehicle != null) {
            return vehicle.getDeltaMovement();
        }

        return player.getDeltaMovement();
    }

    // todo fix the bug where walking backwards boosts your relative speed
    private float getRelativeSpeed(Player player, Entity target) {
        Vec3 attackerMovement = ((JoustMotionAware) player).getJoustMovement();
        Vec3 targetMovement = target.getDeltaMovement();
        Vec3 relativeMovement = attackerMovement.subtract(targetMovement);

        Vec3 toTarget = target.position().subtract(player.position());
        Vec3 horizontalToTarget = new Vec3(toTarget.x, 0.0, toTarget.z);

        if (horizontalToTarget.lengthSqr() <= 1.0E-6) {
            return 0.0F;
        }

        Vec3 directionToTarget = horizontalToTarget.normalize();

        double closingSpeed = relativeMovement.dot(directionToTarget);

        return (float) Math.max(0.0, closingSpeed * 20.0);
    }

    private void applyKnockback(Player player, ItemStack stack, Entity target, DamageSource damagesource) {
        float kbStrength = (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (player.level() instanceof ServerLevel sl) {
            kbStrength += EnchantmentHelper.modifyKnockback(sl, stack, target, damagesource, 0);
        }
        if (kbStrength <= 0) return;

        float yRad = player.getYRot() * (float)(Math.PI / 180F);
        if (target instanceof LivingEntity le) {
            le.knockback(kbStrength * 0.5F, Mth.sin(yRad), -Mth.cos(yRad));
        } else {
            target.push(-Mth.sin(yRad) * kbStrength * 0.5F, 0.1, Mth.cos(yRad) * kbStrength * 0.5F);
        }
    }

    private float getEnchantedDamage(Player player, ItemStack weapon, Entity entity,
                                     float damage, DamageSource damageSource) {
        if (!(player.level() instanceof ServerLevel sl)) return damage;
        return EnchantmentHelper.modifyDamage(sl, weapon, entity, damageSource, damage);
    }
}