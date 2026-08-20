package com.vidal1shere.combatmania.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.client.model.TieredHammerHeadModel;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.constants.HammerTransforms;
import com.vidal1shere.combatmania.item.weapon.HammerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;

public class TieredHammerRenderer extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer renderer;
    private final Map<HammerTransforms, BakedModel> handleModels = new HashMap<>();
    private final Map<HammerTransforms, BakedModel> fullModels = new HashMap<>();
    public final TieredHammerHeadModel headModel;
    private boolean modelsLoaded;

    public TieredHammerRenderer(ItemRenderer rendererDispatcher, EntityModelSet modelSet) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = rendererDispatcher;
        this.headModel = new TieredHammerHeadModel(modelSet.bakeLayer(TieredHammerHeadModel.LAYER_LOCATION));
    }

    private void loadModelsIfNeeded() {
        if (modelsLoaded) return;
        for (HammerTransforms tier : HammerTransforms.values()) {
            handleModels.put(tier, renderer.getItemModelShaper().getModelManager()
                    .getModel(ModelResourceLocation.standalone(tier.getHandleModel())));
            fullModels.put(tier, renderer.getItemModelShaper().getModelManager()
                    .getModel(ModelResourceLocation.standalone(tier.getFullModel())));
        }
        modelsLoaded = true;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        if (!(itemStack.getItem() instanceof HammerItem hammerItem)) return;

        loadModelsIfNeeded();
        
        HammerTransforms tier = hammerItem.getTierTexture();
        boolean is3D = CMClientConfig.THREE_DIMENSIONAL_MACES.get();
        boolean inHand = transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
        boolean leftHand = transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);

        if (is3D && inHand) {
            render3D(poseStack, bufferSource, itemStack, transformType, combinedLightIn, combinedOverlayIn, leftHand, tier);
        } else {
            render2D(poseStack, bufferSource, itemStack, transformType, combinedLightIn, combinedOverlayIn, leftHand, tier);
        }

        poseStack.popPose();
    }

    private void render3D(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, ItemDisplayContext transformType, int light, int overlay, boolean leftHanded, HammerTransforms tier) {
        BakedModel handleModel = handleModels.get(tier);

        renderer.render(itemStack, transformType, leftHanded, poseStack, bufferSource, light, overlay, handleModel);

        poseStack.pushPose();

        ItemTransform transform = handleModel.getTransforms().getTransform(transformType);
        applyTransform(transform, leftHanded, poseStack);
        poseStack.mulPose(Axis.ZP.rotationDegrees(135));
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.translate(0, -24.0 / 16, 0);
        if (transformType.firstPerson()) {
            poseStack.scale(0.75f, 0.75f, 0.75f);
            poseStack.translate(0, 5.5 / 16, 0);
        }

        RenderType renderType = RenderType.entityCutoutNoCull(tier.getHeadTexture());
        headModel.renderToBuffer(poseStack,
                ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, itemStack.hasFoil()),
                light, overlay);

        poseStack.popPose();
    }

    private void render2D(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, ItemDisplayContext transformType, int light, int overlay, boolean leftHanded, HammerTransforms tier) {
        BakedModel fullModel = fullModels.get(tier);
        renderer.render(itemStack, transformType, leftHanded, poseStack, bufferSource, light, overlay, fullModel);
    }

    public void applyTransform(ItemTransform transform, boolean leftHanded, PoseStack poseStack) {
        if (transform == ItemTransform.NO_TRANSFORM) return;
        float x = transform.rotation.x();
        float y = transform.rotation.y();
        float z = transform.rotation.z();
        
        if (leftHanded) {
            y = -y;
            z = -z;
        }

        int i = leftHanded ? -1 : 1;
        poseStack.translate((float) i * transform.translation.x(), transform.translation.y(), transform.translation.z());
        poseStack.mulPose(new Quaternionf().rotationXYZ(
                x * (float) (Math.PI / 180.0),
                y * (float) (Math.PI / 180.0),
                z * (float) (Math.PI / 180.0)
        ));
        poseStack.scale(transform.scale.x(), transform.scale.y(), transform.scale.z());
    }
}