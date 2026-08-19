package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.item.armor.MultiSlotArmorItem;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "onEquipItem",
            at = @At("HEAD")
    )
    private void cm$playEquipSound(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
        Equipable equipable = Equipable.get(newItem);
        if (!(equipable instanceof MultiSlotArmorItem armor)) return;

        LivingEntity self = (LivingEntity) (Object) this;
        if (self.level().isClientSide()) return;
        if (!armor.getAllowedSlots().contains(slot)) return;
        if (ItemStack.isSameItem(oldItem, newItem)) return;
        if (newItem.isEmpty() && oldItem.isEmpty()) return;

        self.level().playSeededSound(
                null, self.getX(), self.getY(), self.getZ(),
                equipable.getEquipSound(), self.getSoundSource(), 1.0F, 1.0F, self.getRandom().nextLong()
        );
    }

    @Inject(
            method = "onEquipItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V"
            ),
            cancellable = true
    )
    private void cm$muteOriginalEquipSound(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem, CallbackInfo ci) {
        Equipable equipable = Equipable.get(newItem);
        if (!(equipable instanceof MultiSlotArmorItem)) return;
        ci.cancel();
    }
}
