package com.vidal1shere.combatmania.client.renderer.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.client.model.ThrownTrident2dModel;
import com.vidal1shere.combatmania.entity.ReplacedThrownTrident;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

public class ThrownTrident2dRenderer extends GeoReplacedEntityRenderer<ThrownTrident, ReplacedThrownTrident> {
    public ThrownTrident2dRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ThrownTrident2dModel(), new ReplacedThrownTrident());
    }

    @Override
    public void render(ThrownTrident entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        ((ThrownTrident2dModel) this.getGeoModel()).currentEntity = entity;
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, ReplacedThrownTrident animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        ThrownTrident entity = ((ThrownTrident2dModel) this.getGeoModel()).currentEntity;
        if (entity == null) {
            super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
            return;
        }
        
        poseStack.pushPose();

        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
        poseStack.popPose();
    }
}
