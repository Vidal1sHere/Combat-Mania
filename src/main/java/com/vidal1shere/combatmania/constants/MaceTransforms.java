package com.vidal1shere.combatmania.constants;

import com.vidal1shere.combatmania.api.extension.item.ITransformProfile;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public enum MaceTransforms implements ITransformProfile {
    WOOD("wooden_mace"),
    STONE("stone_mace"),
    IRON("iron_mace"),
    GOLD("golden_mace"),
    DIAMOND("diamond_mace");

    public final ResourceLocation fullModel;
    public final ResourceLocation handleModel;
    public final ResourceLocation headTexture;

    MaceTransforms(String name) {
        this.fullModel = CMUtil.modResLoc("item/" + name);
        this.handleModel = CMUtil.modResLoc("item/" + name + "_handle");
        this.headTexture = CMUtil.modResLoc("textures/item/" + name + "_head.png");
    }

    @Override
    public ResourceLocation getFullModel() {
        return fullModel;
    }

    @Override
    public ResourceLocation getHandleModel() {
        return handleModel;
    }

    @Override
    public ResourceLocation getHeadTexture() {
        return headTexture;
    }
}
