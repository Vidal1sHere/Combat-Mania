package com.vidal1shere.combatmania.client.model.armor;

import com.vidal1shere.combatmania.item.armor.FlamingQuiverItem;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public class FlamingQuiverModel extends BasicQuiverModel<FlamingQuiverItem> {
    @Override
    public ResourceLocation getTextureResource(FlamingQuiverItem animatable) {
        return CMUtil.modResLoc("textures/armor/flaming_quiver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FlamingQuiverItem animatable) {
        return null;
    }
}
