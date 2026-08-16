package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.extension.item.ClientMaceExtensions;
import com.vidal1shere.combatmania.client.model.TieredMaceHeadModel;
import com.vidal1shere.combatmania.client.model.VanillaMaceHeadModel;
import com.vidal1shere.combatmania.client.renderer.item.VanillaMaceRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownJavelinRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownTrident2dRenderer;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.constants.TieredMaceModels;
import com.vidal1shere.combatmania.item.StrikeMaceItem;
import com.vidal1shere.combatmania.particle.CustomParticle;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public class CMClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CMItemProperties.addCustomItemsProperties();
        });

    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new ClientMaceExtensions.Tiered(), CMItems.getItems()
                .stream()
                .filter(item -> item.get() instanceof StrikeMaceItem)
                .map(holder -> (Item) holder.get())
                .toArray(Item[]::new));
        
        event.registerItem(new ClientMaceExtensions.Vanilla(), Items.MACE);
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TieredMaceHeadModel.LAYER_LOCATION, TieredMaceHeadModel::createBodyLayer);
        event.registerLayerDefinition(VanillaMaceHeadModel.LAYER_LOCATION, VanillaMaceHeadModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (TieredMaceModels tier : TieredMaceModels.values()) {
            event.register(ModelResourceLocation.standalone(tier.getFullModel()));
            event.register(ModelResourceLocation.standalone(tier.getHandleModel()));
        }

        event.register(ModelResourceLocation.standalone(VanillaMaceRenderer.MACE_MODELS.getFullModel()));
        event.register(ModelResourceLocation.standalone(VanillaMaceRenderer.MACE_MODELS.getHandleModel()));
    }
    
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CMEntities.THROWN_JAVELIN.get(), ThrownJavelinRenderer::new);

        if (CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get()) {
            event.registerEntityRenderer(EntityType.TRIDENT, ThrownTrident2dRenderer::new);
        }
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CMParticles.LEFT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.SLASH_ATTACK.get(), CustomParticle.Provider::new);
    }
}
