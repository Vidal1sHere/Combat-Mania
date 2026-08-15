package com.vidal1shere.combatmania.constants;

import com.vidal1shere.combatmania.api.extension.item.IMaceTexture;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public enum TieredMaceTextures implements IMaceTexture {
    WOOD("wooden_mace"),
    STONE("stone_mace"),
    IRON("iron_mace"),
    GOLD("golden_mace"),
    DIAMOND("diamond_mace");

    public final ResourceLocation fullTexture;
    public final ResourceLocation handleModel;
    public final ResourceLocation headTexture;

    TieredMaceTextures(String name) {
        this.fullTexture = CMUtil.modResLoc("item/" + name);
        this.handleModel = CMUtil.modResLoc("item/" + name + "_handle");
        this.headTexture = CMUtil.modResLoc("textures/item/" + name + "_head.png");
    }

    @Override
    public ResourceLocation getFullTexture() {
        return fullTexture;
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
