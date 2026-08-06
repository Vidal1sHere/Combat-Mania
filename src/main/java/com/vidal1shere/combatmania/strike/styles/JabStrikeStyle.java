package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.CMUtil;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.minecraft.core.particles.ParticleTypes;

public class JabStrikeStyle extends StrikeStyle {
    
    private static final float JAB_MIN_REACH = 2.0F;
    private static final float HIT_INFLATION = 0.125F;

    public JabStrikeStyle(StrikeProperties properties) {
        super(properties);
    }

    @Override
    public Holder<Attribute> getSpeedAttribute() {
        return CMAttributes.JAB_SPEED;
    }

    @Override
    public float getMinStrengthScale() { return 1.0F; }
    
    @Override
    public boolean swingHand() {
        return false;
    }

    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        boolean wooden = player.getMainHandItem().getItem() instanceof TieredItem item && item.getTier() == Tiers.WOOD;

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                wooden ? CMSounds.WOODEN_SPEAR_JAB.get() : CMSounds.SPEAR_JAB.get(), player.getSoundSource(), 1.0F, 1.0F);

        Entity target = findTarget(player, ctx.baseReach(), HIT_INFLATION);
        if (target == null) return;

        if (player.distanceToSqr(target) < JAB_MIN_REACH * JAB_MIN_REACH) return;

        ItemStack itemstack = ctx.itemStack();
        float baseDamage = ctx.baseDamage();
        float strengthScale = ctx.strengthScale();

        if (!CommonHooks.onPlayerAttackTarget(player, target)) return;
        if (!target.isAttackable() || target.skipAttackInteraction(player)) return;

        var damagesource = player.damageSources().playerAttack(player);
        float enchBonus = getEnchantedDamage(player, itemstack, target, baseDamage, damagesource) - baseDamage;

        float f  = baseDamage * (0.2F + strengthScale * strengthScale * 0.8F);
        float f1 = enchBonus * strengthScale;

        if (f <= 0.0F && f1 <= 0.0F) return;

        f += itemstack.getItem().getAttackDamageBonus(target, f, damagesource);
        float totalDamage = f + f1;

        float targetHealthBefore = 0.0F;
        if (target instanceof LivingEntity living) targetHealthBefore = living.getHealth();

        Vec3 targetMotionBefore = target.getDeltaMovement();
        boolean hurt = target.hurt(damagesource, totalDamage);

        if (!hurt) {
            player.resetAttackStrengthTicker();
            return;
        }

        float kbStrength = getKnockbackStrength(player, itemstack, target, damagesource);
        if (kbStrength > 0.0F) {
            if (target instanceof LivingEntity le) {
                le.knockback(kbStrength * 0.5F,
                        Mth.sin(player.getYRot() * (float)(Math.PI / 180F)),
                        -Mth.cos(player.getYRot() * (float)(Math.PI / 180F)));
            } else {
                target.push(
                        -Mth.sin(player.getYRot() * (float)(Math.PI / 180F)) * kbStrength * 0.5F,
                        0.1,
                        Mth.cos(player.getYRot() * (float)(Math.PI / 180F)) * kbStrength * 0.5F);
            }
            player.setDeltaMovement(player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            player.setSprinting(false);
        }

        if (target instanceof ServerPlayer sp && sp.hurtMarked) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            sp.hurtMarked = false;
            sp.setDeltaMovement(targetMotionBefore);
        }

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                wooden ? CMSounds.WOODEN_SPEAR_HIT.get() : CMSounds.SPEAR_HIT.get(), player.getSoundSource(), 1.0F, 1.0F);

        if (f1 > 0.0F) player.magicCrit(target);
        player.setLastHurtMob(target);

        Entity actualTarget = target instanceof PartEntity<?> pe ? pe.getParent() : target;
        Level level = player.level();
        if (level instanceof ServerLevel sl) {
            boolean itemHurt = false;
            if (actualTarget instanceof LivingEntity le) {
                itemHurt = itemstack.hurtEnemy(le, player);
            }
            EnchantmentHelper.doPostAttackEffects(sl, target, damagesource);

            if (!itemstack.isEmpty() && actualTarget instanceof LivingEntity le) {
                if (itemHurt) itemstack.postHurtEnemy(le, player);
                if (itemstack.isEmpty()) {
                    EventHooks.onPlayerDestroyItem(player, itemstack.copy(),
                            itemstack == player.getMainHandItem()
                                    ? InteractionHand.MAIN_HAND
                                    : InteractionHand.OFF_HAND);
                }
            }
        }

        if (target instanceof LivingEntity le) {
            float dealt = targetHealthBefore - le.getHealth();
            player.awardStat(Stats.DAMAGE_DEALT, Math.round(dealt * 10.0F));
            if (level instanceof ServerLevel sl && dealt > 2.0F) {
                int count = (int)(dealt * 0.5F);
                sl.sendParticles(ParticleTypes.DAMAGE_INDICATOR,
                        target.getX(), target.getY(0.5), target.getZ(),
                        count, 0.1, 0.0, 0.1, 0.2);
            }
        }

        player.causeFoodExhaustion(0.1F);
        player.resetAttackStrengthTicker();
    }

    private Entity findTarget(Player player, float reach, float inflation) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 viewVec = player.getViewVector(1.0F);
        Vec3 endPos = eyePos.add(viewVec.scale(reach));

        AABB searchBox = player.getBoundingBox()
                .expandTowards(viewVec.scale(reach))
                .inflate(1.0, 1.0, 1.0);

        EntityHitResult hit = CMUtil.getEntityHitResult(
                player, eyePos, endPos, searchBox,
                e -> !e.isSpectator() && e.isPickable(),
                reach * reach,
                inflation
        );

        return hit != null ? hit.getEntity() : null;
    }

    private float getKnockbackStrength(Player player, ItemStack itemstack, Entity target, DamageSource damagesource) {
        if (!(player.level() instanceof ServerLevel sl)) return (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        return (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK)
                + EnchantmentHelper.modifyKnockback(sl, itemstack, target, damagesource, 0);
    }

    private float getEnchantedDamage(Player player, ItemStack weapon, Entity entity, float damage, DamageSource damageSource) {
        if (!(player.level() instanceof ServerLevel sl)) return damage;
        return EnchantmentHelper.modifyDamage(sl, weapon, entity, damageSource, damage);
    }

    private static ItemAnimationDefinition idle() {
        return ItemAnimationDefinition.Builder
                .withLength(1.0f)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.5f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.5f, -1.1f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(0.0f, -0.5f, -1.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, -0.5f, -1.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(-70f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.degreeVec(-70f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    @Override
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        state.start(tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
    }

    @Override
    public ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player) {
        return idle();
    }
}