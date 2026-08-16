package com.vidal1shere.combatmania.client.model.armor;

import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.item.armor.LeatherQuiverItem;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class LeatherQuiverModel extends GeoModel<LeatherQuiverItem> {
    
    public ItemStack currentItemStack;
    public EquipmentSlot currentSlot;

    @Override
    public ResourceLocation getModelResource(LeatherQuiverItem animatable) {
        return CMUtil.modResLoc("geo/armor/leather_quiver.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LeatherQuiverItem animatable) {
        return CMUtil.modResLoc("textures/armor/leather_quiver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LeatherQuiverItem animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(LeatherQuiverItem animatable, long instanceId, AnimationState<LeatherQuiverItem> animationState) {
        if (currentItemStack == null || currentSlot == null) return;

        String part = currentSlot == EquipmentSlot.LEGS ? "leg" : "back";

        QuiverContents contents = currentItemStack.getOrDefault(
                CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY
        );

        int count = contents.count();
        int max = QuiverContents.MAX_ARROWS;
        boolean show0 = count > 0;
        boolean show1 = count >= max / 2;
        boolean show2 = count >= max;

        for (int i = 0; i < 3; i++) {
            String boneName = "arrow_" + part + "_" + i;
            Optional<GeoBone> bone = this.getBone(boneName);
            if (bone.isEmpty()) continue;

            boolean visible = switch (i) {
                case 0 -> show0;
                case 1 -> show1;
                case 2 -> show2;
                default -> false;
            };
            bone.get().setHidden(!visible);
        }
        
    }
}