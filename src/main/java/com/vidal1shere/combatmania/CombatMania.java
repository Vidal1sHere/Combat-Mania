package com.vidal1shere.combatmania;

import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.registry.block.CMBlocks;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CombatMania.MOD_ID)
public class CombatMania {
    public static final String MOD_ID = "combat_mania";
    
    public CombatMania(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CMCommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CMClientConfig.SPEC);
        
        NeoForge.EVENT_BUS.register(this);

        CMStrikeStyles.register(modEventBus);
        CMDataComponents.register(modEventBus);
        CMAttachments.register(modEventBus);
        CMAttributes.register(modEventBus);
        CMEntities.register(modEventBus);
        CMItems.register(modEventBus);
        CMBlocks.register(modEventBus);
        CMParticles.register(modEventBus);
        CMSounds.register(modEventBus);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
