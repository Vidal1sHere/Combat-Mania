package com.vidal1shere.combatmania.client.model.armor;

import com.vidal1shere.combatmania.item.armor.LeatherQuiverItem;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public class LeatherQuiverModel extends BasicQuiverModel<LeatherQuiverItem> {

    @Override
    public ResourceLocation getTextureResource(LeatherQuiverItem animatable) {
        return CMUtil.modResLoc("textures/armor/leather_quiver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LeatherQuiverItem animatable) {
        return null;
    }
}