package com.vidal1shere.combatmania.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.entity.ItemProjectile;
import com.vidal1shere.combatmania.registry.item.CMItemDisplayContexts;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class ItemProjectileRenderer<T extends Entity> extends EntityRenderer<T> {
    private final ItemRenderer itemRenderer;
    private final Function<? super T, ItemStack> renderItem;

    public ItemProjectileRenderer(EntityRendererProvider.Context context, Function<? super T, ItemStack> renderItem) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.renderItem = renderItem;
    }

    public static <T extends Entity & ItemProjectile> ItemProjectileRenderer<T> of(EntityRendererProvider.Context context) {
        return new ItemProjectileRenderer<>(context, ItemProjectile::getRenderItem);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ItemStack stack = this.renderItem.apply(entity);

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

            this.itemRenderer.renderStatic(stack, CMItemDisplayContexts.THROWN, packedLight, OverlayTexture.NO_OVERLAY,
                    poseStack, buffer, entity.level(), entity.getId());
            poseStack.popPose();
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
