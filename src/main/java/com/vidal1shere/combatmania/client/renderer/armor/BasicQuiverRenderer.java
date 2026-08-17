package com.vidal1shere.combatmania.client.renderer.armor;

import com.vidal1shere.combatmania.client.model.armor.BasicQuiverModel;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public abstract class BasicQuiverRenderer<T extends QuiverItem> extends GeoArmorRenderer<T> {
    public BasicQuiverRenderer(BasicQuiverModel<T> model) {
        super(model);
    }

    @Override
    public void prepForRender(Entity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> baseModel, MultiBufferSource bufferSource, float partialTick, float limbSwing, float limbSwingAmount, float netHeadYaw, float headPitch) {
        ((BasicQuiverModel<?>) this.model).currentItemStack = stack;
        ((BasicQuiverModel<?>) this.model).currentSlot = slot;
        super.prepForRender(entity, stack, slot, baseModel, bufferSource, partialTick, limbSwing, limbSwingAmount, netHeadYaw, headPitch);
    }
}
