package com.vidal1shere.combatmania.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.api.extension.item.ITransformProfile;
import com.vidal1shere.combatmania.client.model.VanillaMaceHeadModel;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class VanillaMaceRenderer extends BlockEntityWithoutLevelRenderer {
    private final ItemRenderer renderer;
    public final VanillaMaceHeadModel headModel;
    private BakedModel handleModel;
    private BakedModel fullModel;
    private boolean modelsLoaded;
    
    public static final ITransformProfile MACE_MODELS = new ITransformProfile() {
        @Override
        public ResourceLocation getFullModel() {
            return CMUtil.mcResLoc("item/mace");
        }

        @Override
        public ResourceLocation getHandleModel() {
            return CMUtil.modResLoc("item/mace_handle");
        }

        @Override
        public ResourceLocation getHeadTexture() {
            return CMUtil.modResLoc("textures/item/mace_head.png");
        }
    };

    public VanillaMaceRenderer(ItemRenderer rendererDispatcher, EntityModelSet modelSet) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), modelSet);
        this.renderer = rendererDispatcher;
        this.headModel = new VanillaMaceHeadModel(modelSet.bakeLayer(VanillaMaceHeadModel.LAYER_LOCATION));
    }

    private void loadModelsIfNeeded() {
        if (modelsLoaded) return;
        this.handleModel = renderer.getItemModelShaper().getModelManager()
                .getModel(ModelResourceLocation.standalone(MACE_MODELS.getHandleModel()));
        this.fullModel = renderer.getItemModelShaper().getModelManager()
                .getModel(ModelResourceLocation.standalone(MACE_MODELS.getFullModel()));
        modelsLoaded = true;
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLightIn, int combinedOverlayIn) {
        loadModelsIfNeeded();
        
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
            render3D(poseStack, bufferSource, itemStack, transformType, combinedLightIn, combinedOverlayIn, leftHand);
        } else {
            render2D(poseStack, bufferSource, itemStack, transformType, combinedLightIn, combinedOverlayIn, leftHand);
        }

        poseStack.popPose();
    }

    private void render3D(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, ItemDisplayContext transformType, int light, int overlay, boolean leftHanded) {
        renderer.render(itemStack, transformType, leftHanded, poseStack, bufferSource, light, overlay, handleModel);

        poseStack.pushPose();

        ItemTransform transform = handleModel.getTransforms().getTransform(transformType);
        applyTransform(transform, leftHanded, poseStack);
        poseStack.mulPose(Axis.ZP.rotationDegrees(135));
        poseStack.translate(0, -21.0 / 16, 0);
        poseStack.scale(0.8f, 0.8f, 0.8f);

        RenderType renderType = RenderType.entityCutoutNoCull(MACE_MODELS.getHeadTexture());
        headModel.renderToBuffer(poseStack,
                ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, itemStack.hasFoil()),
                light, overlay);

        poseStack.popPose();
    }

    private void render2D(PoseStack poseStack, MultiBufferSource bufferSource, ItemStack itemStack, ItemDisplayContext transformType, int light, int overlay, boolean leftHanded) {
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