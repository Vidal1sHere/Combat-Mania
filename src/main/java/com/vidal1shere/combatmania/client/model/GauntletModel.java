package com.vidal1shere.combatmania.client.model;

import com.vidal1shere.combatmania.item.GauntletItem;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class GauntletModel {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(CMUtil.modResLoc("gauntlet"), "main");
    public static final ModelLayerLocation MIRRORED_LAYER = new ModelLayerLocation(CMUtil.modResLoc("gauntlet"), GauntletItem.MIRRORED_LAYER);

    public static LayerDefinition createLayer() {
        return create(false);
    }

    public static LayerDefinition createMirroredLayer() {
        return create(true);
    }

    private static LayerDefinition create(boolean mirror) {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -1.5F, 3.0F));
        root.addOrReplaceChild("fist", CubeListBuilder.create().mirror(mirror).texOffs(0, 0).addBox(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 6.0F), PartPose.offset(0.0F, 0.0F, -3.0F));
        return LayerDefinition.create(mesh, 32, 16);
    }
}
