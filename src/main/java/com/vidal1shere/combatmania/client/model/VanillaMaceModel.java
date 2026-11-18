package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public final class VanillaMaceModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(CMUtil.modResLoc("mace"), "main");

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition handle = mesh.getRoot().addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.rotation(0.0F, 0.0F, -Mth.PI / 4.0F));
        handle.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(-0.8F)), PartPose.offset(0.0F, -5.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 16);
    }
}
