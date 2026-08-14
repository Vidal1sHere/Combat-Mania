package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.entity.ReplacedThrownTrident;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ThrownTrident2dModel extends DefaultedEntityGeoModel<ReplacedThrownTrident> {
    
    public ThrownTrident currentEntity;
    
    public ThrownTrident2dModel() {
        super(CMUtil.modResLoc("trident"));
    }

    @Override
    public ResourceLocation getModelResource(ReplacedThrownTrident animatable) {
        return CMUtil.modResLoc("geo/projectile/trident_2d.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ReplacedThrownTrident animatable) {
        return CMUtil.modResLoc("textures/item/trident_in_hand.png");
    }
}
