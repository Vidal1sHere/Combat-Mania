package com.vidal1shere.combatmania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import software.bernie.geckolib.util.InternalUtil;

@Mixin(value = InternalUtil.class, remap = false)
public class InternalUtilMixin {

    @Redirect(
        method = "tryRenderGeoArmorPiece",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Equipable;getEquipmentSlot()Lnet/minecraft/world/entity/EquipmentSlot;"
        ),
        remap = true
    )
    private static <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> EquipmentSlot 
    cm$redirectQuiverEquipmentSlot(Equipable equipable, PoseStack poseStack, MultiBufferSource bufferSource,
            T entity, ItemStack stack, EquipmentSlot equipmentSlot) {

        if (!(equipable instanceof QuiverItem)) 
            return equipable.getEquipmentSlot();

        for (EquipmentSlot s : EquipmentSlot.values()) {
            if (!s.isArmor()) continue;
            if (entity.getItemBySlot(s).getItem() instanceof QuiverItem)
                return s;
        }

        return equipable.getEquipmentSlot();
    }
}