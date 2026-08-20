package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.data.component.SmashProperties;
import com.vidal1shere.combatmania.event.game.StaggerAttackEvent;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;

import java.util.List;
import java.util.function.Predicate;

public class SmashStrikeStyle extends StrikeStyle {

    public static final CustomProperties.PropertyKey<SoundEvent> WEAK_HIT_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.PLAYER_ATTACK_STRONG);
    public static final CustomProperties.PropertyKey<SoundEvent> STRONG_HIT_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.PLAYER_ATTACK_CRIT);
    public static final CustomProperties.PropertyKey<SoundEvent> LIGHT_SMASH_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.MACE_SMASH_GROUND);
    public static final CustomProperties.PropertyKey<SoundEvent> HEAVY_SMASH_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.MACE_SMASH_GROUND_HEAVY);
    public static final CustomProperties.PropertyKey<SoundEvent> AIR_SMASH_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.MACE_SMASH_AIR);
    public static final CustomProperties.PropertyKey<SoundEvent> MISS_SOUND = new CustomProperties.PropertyKey<>(SoundEvents.PLAYER_ATTACK_NODAMAGE);
    
    public SmashStrikeStyle(StrikeProperties properties) {
        super(properties);
    }

    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        if (!ctx.hasTarget()) return;

        Entity target = ctx.target();
        ItemStack itemStack = ctx.itemStack();
        SmashProperties properties = itemStack.getOrDefault(CMDataComponents.SMASH, new SmashProperties());
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
        float enchBonus = CMUtil.getEnchantedDamage(player, itemStack, target, baseDamage, damagesource) - baseDamage;
        float f = baseDamage * (0.2F + strengthScale * strengthScale * 0.8F);
        float f1 = enchBonus * strengthScale;
        
        f += itemStack.getItem().getAttackDamageBonus(target, f, damagesource);
        
        Vec3 targetMotionBefore = target.getDeltaMovement();
        float targetHealthBefore = 0.0F;
        
        if (target instanceof LivingEntity living) {
            targetHealthBefore = living.getHealth();
            
            if (canSmashAttack(player, properties)) {
                float smashBonus = getSmashDamageBonus(itemStack, living, damagesource);
                f += smashBonus;
            }
        }
        
        float totalDamage = f + f1;
        
        player.sendSystemMessage(Component.literal(totalDamage + ""));
        boolean hurt = target.hurt(damagesource, totalDamage);

        if (!hurt) {
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    properties().get(MISS_SOUND), player.getSoundSource(), 1.0F, 1.0F);
            return;
        }
        
        boolean smashAttack = canSmashAttack(player, properties);
        
        if (smashAttack && player instanceof ServerPlayer serverPlayer) {
            ServerLevel serverLevel = (ServerLevel) serverPlayer.level();
            if (serverPlayer.isIgnoringFallDamageFromCurrentImpulse() && serverPlayer.currentImpulseImpactPos != null) {
                if (serverPlayer.currentImpulseImpactPos.y > serverPlayer.position().y) {
                    serverPlayer.currentImpulseImpactPos = serverPlayer.position();
                }
            } else {
                serverPlayer.currentImpulseImpactPos = serverPlayer.position();
            }

            serverPlayer.setIgnoreFallDamageFromCurrentImpulse(true);
            serverPlayer.setDeltaMovement(serverPlayer.getDeltaMovement().with(Direction.Axis.Y, 0.01F));
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
            if (target.onGround()) {
                serverPlayer.setSpawnExtraParticlesOnFall(true);
                SoundEvent soundEvent = serverPlayer.fallDistance > properties.heavyThreshold() ? 
                        properties().get(HEAVY_SMASH_SOUND) :
                        properties().get(LIGHT_SMASH_SOUND);
                serverLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), soundEvent, serverPlayer.getSoundSource(), 1.0F, 1.0F);
            } else {
                serverLevel.playSound(null, serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), 
                        properties().get(AIR_SMASH_SOUND), serverPlayer.getSoundSource(), 1.0F, 1.0F);
            }

            knockback(serverLevel, serverPlayer, target, properties);
        } else {
            float kbStrength = CMUtil.getKnockbackStrength(player, itemStack, target, damagesource);
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
        }
        
        if (canHeavySmashAttack(player, properties) && target instanceof LivingEntity living) {
            float attackStagger = (float) player.getAttributeValue(CMAttributes.ATTACK_STAGGER);

            if (attackStagger > 0.0F) {
                float resistance = (float) living.getAttributeValue(CMAttributes.STAGGER_RESISTANCE);

                float strength = attackStagger * (1.0F - resistance);

                if (strength > 0.0F) {
                    int duration = Mth.floor(strength * 8.0F);

                    StaggerAttackEvent event = new StaggerAttackEvent(living, player, strength, duration);

                    NeoForge.EVENT_BUS.post(event);

                    if (!event.isCanceled()) {
                        StaggerData.applyStagger(living, event.getStrength(), event.getDuration());
                    }
                }
            }
        }
        
        if (player instanceof ServerPlayer sp && sp.level() instanceof ServerLevel sl) {
            if (smashAttack) {
                sl.playSound(null, sp.getX(), sp.getY(), sp.getZ(),
                        properties().get(STRONG_HIT_SOUND), sp.getSoundSource(), 1.0F, 1.0F);
            } else {
                sl.playSound(null, sp.getX(), sp.getY(), sp.getZ(),
                        properties().get(WEAK_HIT_SOUND), sp.getSoundSource(), 1.0F, 1.0F);
            }
        }

        if (target instanceof ServerPlayer sp && sp.hurtMarked) {
            sp.connection.send(new ClientboundSetEntityMotionPacket(sp));
            sp.hurtMarked = false;
            sp.setDeltaMovement(targetMotionBefore);
        }

        if (f1 > 0.0F) player.magicCrit(target);
        player.setLastHurtMob(target);

        Entity actualTarget = target instanceof PartEntity<?> pe ? pe.getParent() : target;
        Level level = player.level();
        if (level instanceof ServerLevel sl) {
            boolean itemHurt = false;
            if (actualTarget instanceof LivingEntity le) {
                itemHurt = itemStack.hurtEnemy(le, player);
            }
            EnchantmentHelper.doPostAttackEffects(sl, target, damagesource);

            if (!itemStack.isEmpty() && actualTarget instanceof LivingEntity le) {
                if (itemHurt) itemStack.postHurtEnemy(le, player);
                if (itemStack.isEmpty()) {
                    EventHooks.onPlayerDestroyItem(player, itemStack.copy(),
                            itemStack == player.getMainHandItem() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
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

        player.resetFallDistance();
        player.causeFoodExhaustion(0.1F);
    }

    public static double getKnockbackPower(Player player, LivingEntity entity, Vec3 entityPos, SmashProperties props) {
        double radius = props.knockbackRadius();
        double power = props.knockbackPower();
        double multiplier = player.fallDistance > props.heavyThreshold() ? 2 : 1;
        double kbResistance = (double) 1.0F - entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        
        return (radius - entityPos.length()) * power * multiplier * kbResistance;
    }

    public static boolean canSmashAttack(LivingEntity entity, SmashProperties props) {
        return entity.fallDistance > props.lightThreshold() && !entity.isFallFlying();
    }
    
    public static boolean canHeavySmashAttack(LivingEntity entity, SmashProperties props) {
        return entity.fallDistance > props.heavyThreshold();
    }

    public static float getSmashDamageBonus(ItemStack stack, Entity target, DamageSource damageSource) {
        Entity directEntity = damageSource.getDirectEntity();

        if (!(directEntity instanceof LivingEntity livingEntity)) {
            return 0.0F;
        }

        SmashProperties smashProperties = stack.getOrDefault(CMDataComponents.SMASH, new SmashProperties());

        if (!canSmashAttack(livingEntity, smashProperties)) {
            return 0.0F;
        }

        float firstFallDistance = smashProperties.lightThreshold() * 2.0F;
        float secondFallDistance = firstFallDistance + smashProperties.heavyThreshold();
        float fallDistance = livingEntity.fallDistance;

        float lowFallMultiplier = smashProperties.lowFallMultiplier();
        float mediumFallMultiplier = smashProperties.mediumFallMultiplier();
        float highFallMultiplier = smashProperties.highFallMultiplier();

        float firstBaseDamage = firstFallDistance * lowFallMultiplier;
        float secondBaseDamage = firstBaseDamage + (secondFallDistance - firstFallDistance) * mediumFallMultiplier;

        float smashDamage;

        if (fallDistance <= firstFallDistance) {
            smashDamage = lowFallMultiplier * fallDistance;
        } else if (fallDistance <= secondFallDistance) {
            smashDamage = firstBaseDamage + mediumFallMultiplier * (fallDistance - firstFallDistance);
        } else {
            smashDamage = secondBaseDamage + highFallMultiplier * (fallDistance - secondFallDistance);
        }
        
        float softDamageCap = smashProperties.maxDamage();

        if (softDamageCap > 0.0F) {
            smashDamage = Math.min(softDamageCap, smashDamage);
        }

        Level level = livingEntity.level();

        if (level instanceof ServerLevel serverLevel) {
            ItemStack weapon = livingEntity.getWeaponItem();
            float enchantmentDamage = EnchantmentHelper.modifyFallBasedDamage(serverLevel, weapon, target, damageSource, 0.0F);
            smashDamage += enchantmentDamage * fallDistance;
        }
        
        if (softDamageCap > 0.0F) {
            return Math.min(softDamageCap * 2, smashDamage);
        }

        return smashDamage;
    }

    private static void knockback(Level level, Player player, Entity entity, SmashProperties properties) {
        level.levelEvent(2013, entity.getOnPos(), 750);

        double knockbackRadius = properties.knockbackRadius();
        AABB searchBox = entity.getBoundingBox().inflate(knockbackRadius);
        Predicate<LivingEntity> predicate = knockbackPredicate(player, entity);
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchBox, predicate);

        entities.forEach(livingEntity -> {
            Vec3 direction = livingEntity.position().subtract(entity.position());
            double knockbackPower = getKnockbackPower(player, livingEntity, direction, properties);
            Vec3 knockback = direction.normalize().scale(knockbackPower);

            if (knockbackPower > 0.0F) {
                //livingEntity.push(knockback.x, properties.getKnockbackPower(), knockback.z);
                livingEntity.push(knockback.x, knockbackPower, knockback.z);

                if (livingEntity instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));
                }
            }
        });
    }

    private static Predicate<LivingEntity> knockbackPredicate(Player player, Entity entity) {
        return livingEntity -> {
            boolean isNotSpectator = !livingEntity.isSpectator();
            boolean isNotAttacker = livingEntity != player && livingEntity != entity;
            boolean isNotAllied = !player.isAlliedTo(livingEntity);

            boolean isOwnedTamable = false;

            if (livingEntity instanceof TamableAnimal tamableAnimal) {
                boolean isTame = tamableAnimal.isTame();
                boolean isOwner = player.getUUID().equals(tamableAnimal.getOwnerUUID());
                isOwnedTamable = isTame && isOwner;
            }

            boolean isNotOwnedTamable = !isOwnedTamable;

            boolean isValidArmorStand = true;

            if (livingEntity instanceof ArmorStand armorStand) {
                isValidArmorStand = !armorStand.isMarker();
            }

            double distance = entity.distanceToSqr(livingEntity);
            double maxDistance = Math.pow(3.5F, 2.0F);
            boolean isInRange = distance <= maxDistance;

            return isNotSpectator
                    && isNotAttacker
                    && isNotAllied
                    && isNotOwnedTamable
                    && isValidArmorStand
                    && isInRange;
        };
    }

    @Override
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        state.setActiveDefinition(supplyAnimation(stack, hand, player, state));
        state.start(tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
    }
}
