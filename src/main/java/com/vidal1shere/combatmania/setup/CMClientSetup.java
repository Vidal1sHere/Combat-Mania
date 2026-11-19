package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.client.model.TieredHammerModel;
import com.vidal1shere.combatmania.client.model.TieredMaceModel;
import com.vidal1shere.combatmania.client.model.VanillaMaceModel;
import com.vidal1shere.combatmania.client.renderer.ConfigurableTridentRenderer;
import com.vidal1shere.combatmania.client.renderer.ItemProjectileRenderer;
import com.vidal1shere.combatmania.particle.CustomParticle;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;

import java.util.Arrays;
import java.util.stream.Stream;

@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public class CMClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CMItemProperties.addCustomItemsProperties();
        });

    }

    private static Stream<Item> itemsOf(Class<?> type, Class<?>... exclude) {
        return CMItems.getItems()
                .stream()
                .filter(item -> type.isInstance(item.get())
                        && Arrays.stream(exclude).noneMatch(ex -> ex.isInstance(item.get())))
                .map(holder -> (Item) holder.get());
    }
    
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CMEntities.THROWN_JAVELIN.get(), ItemProjectileRenderer::of);
        event.registerEntityRenderer(EntityType.TRIDENT, ConfigurableTridentRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TieredMaceModel.LAYER, TieredMaceModel::createLayer);
        event.registerLayerDefinition(VanillaMaceModel.LAYER, VanillaMaceModel::createLayer);
        event.registerLayerDefinition(TieredHammerModel.LAYER, TieredHammerModel::createLayer);
        event.registerLayerDefinition(TieredHammerModel.FIRST_PERSON_LAYER, TieredHammerModel::createFirstPersonLayer);
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CMParticles.LEFT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.SLASH_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_UPPERCUT_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_SHORT_UPPERCUT_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_OVERHEAD_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_SHORT_OVERHEAD_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_UPPERCUT_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SHORT_UPPERCUT_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_OVERHEAD_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SHORT_OVERHEAD_ATTACK.get(), CustomParticle.Provider::new);
    }
}
