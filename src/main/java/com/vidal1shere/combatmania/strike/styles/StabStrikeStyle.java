package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.client.animation.item.DaggerAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.item.DaggerItem;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import java.util.function.BiFunction;

import static com.vidal1shere.combatmania.util.CustomProperties.Key;

public class StabStrikeStyle extends StrikeStyle {

    public static final Key<Float> BACKSTAB_MAX_REACH = new Key<>(2F);
    public static final Key<Float> BACKSTAB_ANGLE = new Key<>(30.0F);
    public static final Key<BiFunction<ItemStack, Player, Float>> BACKSTAB_MULTIPLIER = 
            new Key<>(StabStrikeStyle::getBackStabMultiplier);

    public StabStrikeStyle(StrikeProperties properties) {
        super(properties);
    }
    
    public float getBackStabAngle() {
        return properties().get(BACKSTAB_ANGLE);
    }

    public float getBackStabMaxReach() {
        return properties().get(BACKSTAB_MAX_REACH);
    }
    
    public static float getBackStabMultiplier(ItemStack stack, Player player) {
        double damage = stack.getAttributeModifiers()
                .modifiers()
                .stream()
                .filter(entry -> entry.attribute().equals(Attributes.ATTACK_DAMAGE))
                .mapToDouble(entry -> entry.modifier().amount())
                .sum();

        return (float) (damage * 0.48125D + 0.59375D);
    }
    
    private boolean isBackstab(Player player, Entity target) {
        return isBackstab(player, target, getBackStabAngle());
    }

    public static boolean isBackstab(Player player, Entity target, float angle) {
        Vec3 targetToPlayer = player.position().subtract(target.position()).normalize();

        float bodyRotation = target.getYRot();
        Vec3 targetForward = Vec3.directionFromRotation(0.0F, bodyRotation).normalize();

        double dot = targetForward.dot(targetToPlayer);
        double angl = -CMUtil.angleToDot(angle);

//        System.out.println("dot: " + dot);
//        System.out.println("angle: " + angl);
//        System.out.println("backstab: " + (dot <= angl ? "true" : "false"));

        return dot <= angl;
    }

    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        if (!ctx.hasTarget()) return;

        Entity target = ctx.target();
        ItemStack itemstack = ctx.itemStack();
        float baseDamage = ctx.baseDamage();
        float strengthScale = ctx.strengthScale();
        float backstabMultiplier = properties().get(BACKSTAB_MULTIPLIER).apply(itemstack, player);

        if (!CommonHooks.onPlayerAttackTarget(player, target)) { 
            return; 
        }
        
        if (!target.isAttackable() || target.skipAttackInteraction(player)) { 
            return; 
        }
        
        double distSq = player.distanceToSqr(target);
        double reach = ctx.reach();
        
        if (distSq > reach) { 
            return; 
        }

        var damagesource = player.damageSources().playerAttack(player);
        float enchBonus = getEnchantedDamage(player, itemstack, target, baseDamage, damagesource) - baseDamage;
        float f = baseDamage * (0.2F + strengthScale * strengthScale * 0.8F);
        float f1 = enchBonus * strengthScale;

        boolean fullStrength = strengthScale > 0.9F;

        f += itemstack.getItem().getAttackDamageBonus(target, f, damagesource);

        boolean backstab = isBackstab(player, target) 
                && fullStrength
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && target instanceof LivingEntity 
                && player.distanceToSqr(target) <= getBackStabMaxReach();

        CriticalHitEvent critEvent = CommonHooks.fireCriticalHit(player, target, backstab, backstab ? backstabMultiplier : 1.0F);
        backstab = critEvent.isCriticalHit();
        if (backstab) f *= critEvent.getDamageMultiplier();

        float totalDamage = f + f1;
        
        float targetHealthBefore = 0.0F;
        if (target instanceof LivingEntity living) targetHealthBefore = living.getHealth();

        Vec3 targetMotionBefore = target.getDeltaMovement();
        boolean hurt = target.hurt(damagesource, totalDamage);

        if (!hurt) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
            return;
        }
        
        player.setDeltaMovement(player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
        player.setSprinting(false);

        if (target instanceof ServerPlayer sp && sp.hurtMarked) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            sp.hurtMarked = false;
            sp.setDeltaMovement(targetMotionBefore);
        }

        if (backstab) {
            DaggerItem.queueBackStab(player);
            
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
            player.crit(target);
        } else {
            if (fullStrength) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
            } else {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
            }
        }

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
                            itemstack == player.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
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
    }

    private float getEnchantedDamage(Player player, ItemStack weapon, Entity entity, float damage, DamageSource damageSource) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return damage;

        return EnchantmentHelper.modifyDamage(serverLevel, weapon, entity, damageSource, damage);
    }

    @Override
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        state.setActiveDefinition(DaggerItem.backStabQueued(player) ? DaggerAnimations.backStab() : DaggerAnimations.stab());
        state.start(tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
        DaggerItem.clearBackStab(player);
    }
}