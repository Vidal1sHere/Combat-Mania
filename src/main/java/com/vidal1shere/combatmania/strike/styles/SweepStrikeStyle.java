package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.AnimationSupplier;
import com.vidal1shere.combatmania.util.ParticleUtil;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraft.core.particles.ParticleTypes;

import java.util.function.BiConsumer;

import static com.vidal1shere.combatmania.util.CustomProperties.Key;

public class SweepStrikeStyle extends StrikeStyle {

    public static final Key<Boolean> CAN_CRIT = new Key<>(true);
    public static final Key<Boolean> CAN_SPRINT_KNOCKBACK = new Key<>(true);
    public static final Key<Vec3> SWEEP_HITBOX = new Key<>(new Vec3(1.0, 0.25, 1.0));
    public static final Key<BiConsumer<Player, LivingEntity>> SWEEP_PARTICLE = new Key<>((p, t) -> 
            sweepParticle(p, p.onGround()));
    public static final Key<SoundEvent> SWEEP_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_SWEEP);
    public static final Key<SoundEvent> CRIT_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_CRIT);
    public static final Key<SoundEvent> WEAK_HIT_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_WEAK);
    public static final Key<SoundEvent> STRONG_HIT_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_STRONG);
    public static final Key<SoundEvent> MISS_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_NODAMAGE);
    public static final Key<SoundEvent> KNOCKBACK_SOUND = new Key<>(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
    public static final Key<AnimationSupplier> ANIMATION_FUNCTION = new Key<>(
            (s, h, p, st) -> null);

    public SweepStrikeStyle(StrikeProperties properties) {
        super(properties);
    }
    
    public AABB getSweepHitBox(Entity entity) {
        Vec3 hitbox = properties().get(SWEEP_HITBOX);
        return entity.getBoundingBox().inflate(hitbox.x, hitbox.y, hitbox.z);
    }
    
    public boolean canSprintKnockback() {
        return properties().get(CAN_SPRINT_KNOCKBACK);
    }

    public boolean canCrit() {
        return properties().get(CAN_CRIT);
    }
    
    public void spawnSweepParticle(Player player, LivingEntity target) {
        properties().get(SWEEP_PARTICLE).accept(player, target);
    }

    public static void sweepParticle(Player player, boolean horizontal) {
        double d0 = (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
        double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleUtil.sweep(false, false, !horizontal, false),
                    player.getX() + d0, player.getY(0.5F), player.getZ() + d1, 0, d0, 0.0F, d1, 0.0F);
        }
    }

    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        if (!ctx.hasTarget()) return;

        Entity target = ctx.target();
        ItemStack itemstack = ctx.itemStack();
        float baseDamage = ctx.baseDamage();
        float strengthScale = ctx.strengthScale();

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

        boolean sprintKnockback = false;
        if (canSprintKnockback() && player.isSprinting() && fullStrength) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    properties().get(KNOCKBACK_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            sprintKnockback = true;
        }

        f += itemstack.getItem().getAttackDamageBonus(target, f, damagesource);

        boolean crit = canCrit()
                && fullStrength
                && player.fallDistance > 0.0F
                && !player.onGround()
                && !player.onClimbable()
                && !player.isInWater()
                && !player.hasEffect(MobEffects.BLINDNESS)
                && !player.isPassenger()
                && target instanceof LivingEntity
                && !player.isSprinting();

        CriticalHitEvent critEvent = CommonHooks.fireCriticalHit(player, target, crit, crit ? 1.5F : 1.0F);
        crit = critEvent.isCriticalHit();
        if (crit) f *= critEvent.getDamageMultiplier();

        float totalDamage = f + f1;

        boolean sweep = false;
        if (fullStrength && !sprintKnockback) {
            double walkDelta = player.walkDist - player.walkDistO;
            if (walkDelta < player.getSpeed()) {
                ItemStack mainHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                CommonHooks.fireSweepAttack(player, target, mainHand.canPerformAction(ItemAbilities.SWORD_SWEEP));
                sweep = true;
            }
        }
        
        float targetHealthBefore = 0.0F;
        if (target instanceof LivingEntity living) targetHealthBefore = living.getHealth();

        Vec3 targetMotionBefore = target.getDeltaMovement();
        boolean hurt = target.hurt(damagesource, totalDamage);

        if (!hurt) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    properties().get(MISS_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            return;
        }

        float kbStrength = getKnockbackStrength(player, itemstack, target, damagesource) + (sprintKnockback ? 1.0F : 0.0F);
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

        if (sweep) {
            float sweepDmg = 1.0F + (float) player.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * f;
            
            if (player.onGround()) {
                double entityReachSq = Mth.square(player.entityInteractionRange());
    
                for (LivingEntity nearby : player.level().getEntitiesOfClass(LivingEntity.class, getSweepHitBox(target))) {
                    if (nearby == player || nearby == target || player.isAlliedTo(nearby)) continue;
                    if (nearby instanceof ArmorStand as && as.isMarker()) continue;
                    if (player.distanceToSqr(nearby) >= entityReachSq) continue;
    
                    float sweepEnchDmg = getEnchantedDamage(player, itemstack, nearby, sweepDmg, damagesource) * strengthScale;
                    nearby.knockback(0.4F,
                            Mth.sin(player.getYRot() * (float)(Math.PI / 180F)),
                            -Mth.cos(player.getYRot() * (float)(Math.PI / 180F)));
                    nearby.hurt(damagesource, sweepEnchDmg);
    
                    if (player.level() instanceof ServerLevel sl) {
                        EnchantmentHelper.doPostAttackEffects(sl, nearby, damagesource);
                    }
                }
            }

            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    properties().get(SWEEP_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            
            if (target instanceof LivingEntity living) spawnSweepParticle(player, living);
        }

        if (target instanceof ServerPlayer sp && sp.hurtMarked) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            sp.hurtMarked = false;
            sp.setDeltaMovement(targetMotionBefore);
        }

        if (crit) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    properties().get(CRIT_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            player.crit(target);
        } else if (!sweep) {
            if (fullStrength) {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        properties().get(STRONG_HIT_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            } else {
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        properties().get(WEAK_HIT_SOUND), player.getSoundSource(), 1.0F, 1.0F);
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

    @Override
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        if (swingHand()) return;
        
        state.setActiveDefinition(properties().get(ANIMATION_FUNCTION).get(stack, hand, player, state));
        state.start(tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
    }

    private float getKnockbackStrength(Player player, ItemStack itemstack, Entity target, DamageSource damagesource) {
        return (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK)
                + EnchantmentHelper.modifyKnockback((ServerLevel) player.level(), itemstack, target, damagesource, 0);
    }

    private float getEnchantedDamage(Player player, ItemStack weapon, Entity entity, float damage, DamageSource damageSource) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return damage;

        return EnchantmentHelper.modifyDamage(serverLevel, weapon, entity, damageSource, damage);
    }
}