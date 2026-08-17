package com.vidal1shere.combatmania.client.renderer.projectile;

import com.vidal1shere.combatmania.entity.FlamingArrow;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FlamingArrowRenderer extends ArrowRenderer<FlamingArrow> {
    public FlamingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public ResourceLocation getTextureLocation(FlamingArrow entity) {
        return CMUtil.modResLoc("textures/entity/projectiles/flaming_arrow.png");
    }
}