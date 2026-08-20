package com.vidal1shere.combatmania.constants;

import com.vidal1shere.combatmania.api.extension.item.IMaceTexture;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public enum TieredHammerModels implements IMaceTexture {
    WOOD("wooden_hammer"),
    STONE("stone_hammer"),
    IRON("iron_hammer"),
    GOLD("golden_hammer"),
    DIAMOND("diamond_hammer");

    public final ResourceLocation fullModel;
    public final ResourceLocation handleModel;
    public final ResourceLocation headTexture;

    TieredHammerModels(String name) {
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
