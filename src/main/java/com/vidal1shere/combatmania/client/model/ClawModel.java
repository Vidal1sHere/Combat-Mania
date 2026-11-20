package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ClawModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(CMUtil.modResLoc("claw"), "main");

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("claw", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -4.0F, 5.0F, 2.0F, 5.0F), PartPose.offset(-1.5F, -1.0F, -3.0F));
        root.addOrReplaceChild("palm", CubeListBuilder.create().texOffs(0, 7).addBox(-2.5F, -1.0F, -2.0F, 5.0F, 2.0F, 4.0F), PartPose.offset(0.0F, -1.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 16);
    }
}
