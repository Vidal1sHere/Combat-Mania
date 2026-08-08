package com.vidal1shere.combatmania.strike.styles;

import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
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

import java.util.function.BiPredicate;

import static com.vidal1shere.combatmania.util.CustomProperties.Key;

public class JabStrikeStyle extends StrikeStyle {

    public static final Key<Float> JAB_MIN_REACH = new Key<>(2.0F);
    public static final Key<Float> HIT_INFLATION = new Key<>(0.125F);
    public static final Key<BiPredicate<ItemStack, Player>> IS_LIGHT = new Key<>((s, p) -> 
            s.getItem() instanceof TieredItem t && t.getTier() == Tiers.WOOD);
    public static final Key<SoundEvent> JAB_SOUND = new Key<>(CMSounds.SPEAR_JAB.get());
    public static final Key<SoundEvent> LIGHT_JAB_SOUND = new Key<>(CMSounds.WOODEN_SPEAR_JAB.get());
    public static final Key<SoundEvent> HIT_SOUND = new Key<>(CMSounds.SPEAR_HIT.get());
    public static final Key<SoundEvent> LIGHT_HIT_SOUND = new Key<>(CMSounds.WOODEN_SPEAR_HIT.get());
    public static final Key<AnimationGetter> ANIMATION_GETTER = new Key<>((s, h, p, st) -> 
            SpearAnimations.spearJab());

    public JabStrikeStyle(StrikeProperties properties) {
        super(properties);
    }

    @Override
    public void onStart(StrikeContext ctx) {
        Player player = ctx.player();
        ItemStack itemstack = ctx.itemStack();
        boolean isLight = properties().get(IS_LIGHT).test(itemstack, player);

        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                isLight ? properties().get(LIGHT_JAB_SOUND) : properties().get(JAB_SOUND), player.getSoundSource(), 1.0F, 1.0F);

        Entity target = findTarget(player, ctx.baseReach(), properties().get(HIT_INFLATION));
        if (target == null) return;

        float minReach = properties().get(JAB_MIN_REACH);
        if (player.distanceToSqr(target) < minReach * minReach) return;

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
                isLight ? properties().get(HIT_SOUND) : properties().get(LIGHT_HIT_SOUND), player.getSoundSource(), 1.0F, 1.0F);

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

    @Override
    public void onAnimStart(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        state.setActiveDefinition(properties().get(ANIMATION_GETTER).get(stack, hand, player, state));
        state.start(tickCount, ItemAnimationState.LoopType.PLAY_ONCE);
    }
    
    @FunctionalInterface
    public interface AnimationGetter {
        ItemAnimationDefinition get(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state);
    }
}