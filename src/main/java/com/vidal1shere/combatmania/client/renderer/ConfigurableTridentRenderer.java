package com.vidal1shere.combatmania.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.entity.ItemProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ThrownTrident;

public class ConfigurableTridentRenderer extends EntityRenderer<ThrownTrident> {
    private final ThrownTridentRenderer modelRenderer;
    private final ItemProjectileRenderer<ThrownTrident> itemRenderer;

    public ConfigurableTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.modelRenderer = new ThrownTridentRenderer(context);
        this.itemRenderer = new ItemProjectileRenderer<>(context, entity -> ((ItemProjectile) entity).getRenderItem());
    }

    private EntityRenderer<ThrownTrident> current() {
        return CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get() ? this.itemRenderer : this.modelRenderer;
    }

    @Override
    public void render(ThrownTrident entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.current().render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownTrident entity) {
        return this.current().getTextureLocation(entity);
    }
}
