package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class TieredMaceModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(CMUtil.modResLoc("tiered_mace"), "main");

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition handle = mesh.getRoot().addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.rotation(0.0F, 0.0F, -Mth.PI / 4.0F));
        PartDefinition head = handle.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -4.5F, 0.0F, 0.0F, 0.0F, Mth.PI / 4.0F));
        PartDefinition spikes = head.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 0.0F));
        spike(spikes, "top_right", 0, PartPose.offset(0.0F, -5.0F, 0.0F));
        spike(spikes, "top_left", 0, PartPose.offsetAndRotation(-2.5F, -2.5F, 0.0F, 0.0F, 0.0F, -Mth.HALF_PI));
        spike(spikes, "front", 4, PartPose.offsetAndRotation(0.0F, -2.5F, -2.5F, Mth.HALF_PI, 0.0F, 0.0F));
        spike(spikes, "back", 4, PartPose.offsetAndRotation(0.0F, -2.5F, 2.5F, -Mth.HALF_PI, 0.0F, 0.0F));
        spike(spikes, "bottom_right", 8, PartPose.offsetAndRotation(2.5F, -2.5F, 0.0F, 0.0F, 0.0F, Mth.HALF_PI));
        spike(spikes, "bottom_left", 8, PartPose.rotation(-Mth.PI, 0.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 16);
    }

    private static void spike(PartDefinition spikes, String name, int v, PartPose pose) {
        PartDefinition spike = spikes.addOrReplaceChild(name, CubeListBuilder.create(), pose);
        spike.addOrReplaceChild("plane_a", CubeListBuilder.create().texOffs(25, v).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -Mth.PI / 4.0F, 0.0F));
        spike.addOrReplaceChild("plane_b", CubeListBuilder.create().texOffs(25, v).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, Mth.PI / 4.0F, 0.0F));
    }
}
