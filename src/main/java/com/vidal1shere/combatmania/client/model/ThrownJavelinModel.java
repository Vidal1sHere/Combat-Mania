package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.entity.ThrownJavelin;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ThrownJavelinModel extends GeoModel<ThrownJavelin> {
    @Override
    public ResourceLocation getModelResource(ThrownJavelin animatable) {
        return animatable.getModelResource();
    }

    @Override
    public ResourceLocation getTextureResource(ThrownJavelin animatable) {
        return animatable.getTextureResource();
    }

    @Override
    public ResourceLocation getAnimationResource(ThrownJavelin animatable) {
        return null;
    }
}
