package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public final class TieredHammerModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(CMUtil.modResLoc("tiered_hammer"), "main");
    public static final ModelLayerLocation FIRST_PERSON_LAYER = new ModelLayerLocation(CMUtil.modResLoc("tiered_hammer"), "first_person");

    public static LayerDefinition createLayer() {
        return create(CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -7.0F, -3.5F, 12.0F, 7.0F, 7.0F), 0.0F);
    }

    public static LayerDefinition createFirstPersonLayer() {
        return create(CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.125F, -3.5F, 12.0F, 7.0F, 7.0F, new CubeDeformation(-1.5F, -0.875F, -0.875F)), -1.875F);
    }

    private static LayerDefinition create(CubeListBuilder head, float headY) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition handle = mesh.getRoot().addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.rotation(0.0F, 0.0F, -Mth.PI / 4.0F));
        handle.addOrReplaceChild("head", head, PartPose.offsetAndRotation(0.0F, headY, 0.0F, 0.0F, Mth.PI, 0.0F));
        return LayerDefinition.create(mesh, 48, 16);
    }
}
