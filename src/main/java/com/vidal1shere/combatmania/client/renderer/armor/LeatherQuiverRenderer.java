package com.vidal1shere.combatmania.client.renderer.armor;

import com.vidal1shere.combatmania.client.model.armor.LeatherQuiverModel;
import com.vidal1shere.combatmania.item.armor.LeatherQuiverItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class LeatherQuiverRenderer extends GeoArmorRenderer<LeatherQuiverItem> {
    public LeatherQuiverRenderer() {
        super(new LeatherQuiverModel());
    }

    @Override
    public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel, MultiBufferSource bufferSource, float partialTick, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        ((LeatherQuiverModel) this.model).currentItemStack = stack;
        ((LeatherQuiverModel) this.model).currentSlot = slot;
        super.prepForRender(entity, stack, slot, baseModel, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
    }
}