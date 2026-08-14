package com.vidal1shere.combatmania;

import com.vidal1shere.combatmania.client.renderer.projectile.ThrownJavelinRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownTrident2dRenderer;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.particle.CustomParticle;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CombatMania.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public class CombatManiaClient {
    public CombatManiaClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        CMItemProperties.addCustomItemsProperties();

        EntityRenderers.register(CMEntities.THROWN_JAVELIN.get(), ThrownJavelinRenderer::new);
        
        if (CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get()) {
            EntityRenderers.register(EntityType.TRIDENT, ThrownTrident2dRenderer::new);
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
