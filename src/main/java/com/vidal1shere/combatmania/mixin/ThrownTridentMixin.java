package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.entity.ItemProjectile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin implements ItemProjectile {
    @Unique
    private static final EntityDataAccessor<ItemStack> CM$DATA_ITEM =
            SynchedEntityData.defineId(ThrownTrident.class, EntityDataSerializers.ITEM_STACK);

    @Unique
    private ThrownTrident cm$self() {
        return (ThrownTrident) (Object) this;
    }

    @Unique
    private void cm$syncItem() {
        cm$self().getEntityData().set(CM$DATA_ITEM, cm$self().getWeaponItem().copy());
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void cm$defineItem(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(CM$DATA_ITEM, ItemStack.EMPTY);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void cm$initFromShooter(Level level, LivingEntity shooter, ItemStack pickupItemStack, CallbackInfo ci) {
        cm$syncItem();
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
    private void cm$initAtPosition(Level level, double x, double y, double z, ItemStack pickupItemStack, CallbackInfo ci) {
        cm$syncItem();
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void cm$readItem(CompoundTag compound, CallbackInfo ci) {
        cm$syncItem();
    }

    @Override
    public ItemStack getRenderItem() {
        return cm$self().getEntityData().get(CM$DATA_ITEM);
    }
}
