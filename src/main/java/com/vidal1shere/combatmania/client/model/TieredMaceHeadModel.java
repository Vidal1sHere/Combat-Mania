package com.vidal1shere.combatmania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TieredMaceHeadModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(CMUtil.modResLoc("tiered_mace_head"), "main");
	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart spikes;
	private final ModelPart top_right;
	private final ModelPart top_left;
	private final ModelPart front;
	private final ModelPart back;
	private final ModelPart bottom_right;
	private final ModelPart bottom_left;

	public TieredMaceHeadModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.spikes = this.head.getChild("spikes");
		this.top_right = this.spikes.getChild("top_right");
		this.top_left = this.spikes.getChild("top_left");
		this.front = this.spikes.getChild("front");
		this.back = this.spikes.getChild("back");
		this.bottom_right = this.spikes.getChild("bottom_right");
		this.bottom_left = this.spikes.getChild("bottom_left");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition spikes = head.addOrReplaceChild("spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 2.5F, 0.0F));

		PartDefinition top_right = spikes.addOrReplaceChild("top_right", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 0.0F));

		PartDefinition cube_r1 = top_right.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = top_right.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition top_left = spikes.addOrReplaceChild("top_left", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -2.5F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r3 = top_left.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r4 = top_left.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(25, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition front = spikes.addOrReplaceChild("front", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.5F, -2.5F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r5 = front.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(25, 4).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r6 = front.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(25, 4).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition back = spikes.addOrReplaceChild("back", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.5F, 2.5F, -1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r7 = back.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(25, 4).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r8 = back.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(25, 4).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition bottom_right = spikes.addOrReplaceChild("bottom_right", CubeListBuilder.create(), PartPose.offsetAndRotation(2.5F, -2.5F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition cube_r9 = bottom_right.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(25, 8).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r10 = bottom_right.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(25, 8).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition bottom_left = spikes.addOrReplaceChild("bottom_left", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.0F, 0.0F));

		PartDefinition cube_r11 = bottom_left.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(25, 8).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r12 = bottom_left.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(25, 8).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}
	
	@Override
	public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int pColor) {
		this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pColor);
	}
}