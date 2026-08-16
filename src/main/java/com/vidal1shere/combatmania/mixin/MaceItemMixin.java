package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.api.IVanillaStrikeWeapon;
import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public abstract class MaceItemMixin implements IVanillaStrikeWeapon, IItemExtension {
    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.SMASH;
        return null;
    }

    @Override
    public boolean isEnabled() {
        return CMCommonConfig.MODIFY_MACE.get();
    }

    @Inject(method = "getAttackDamageBonus", at = @At("HEAD"), cancellable = true)
    private void combatMania$getAttackDamageBonus(Entity target, float damage, DamageSource damageSource, CallbackInfoReturnable<Float> cir) {
        if (!isEnabled()) {
            return;
        }

        cir.setReturnValue(0.0F);
    }

    @Inject(method = "hurtEnemy", at = @At("HEAD"), cancellable = true)
    private void combatMania$hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (!isEnabled()) {
            return;
        }

         cir.setReturnValue(true);
    }

    @Inject(method = "postHurtEnemy", at = @At("HEAD"), cancellable = true)
    private void combatMania$postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
        if (!isEnabled()) {
            return;
        }

         ci.cancel();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (isEnabled()) {
            return slotChanged;
        }

        return IItemExtension.super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }
}
