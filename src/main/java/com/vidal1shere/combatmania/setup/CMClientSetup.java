package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.extension.client.IMinecraftInstance;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownJavelinRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownTrident2dRenderer;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.particle.CustomParticle;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public class CMClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CMItemProperties.addCustomItemsProperties();
            
            ClientUtil.INSTANCE = new IMinecraftInstance() {
                @Override
                public @Nullable Player player() {
                    return Minecraft.getInstance().player;
                }
            };
        });

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
