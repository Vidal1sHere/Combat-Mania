package com.vidal1shere.combatmania;

import com.vidal1shere.combatmania.registry.block.CMBlocks;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CombatMania.MOD_ID)
public class CombatMania {
    public static final String MOD_ID = "combat_mania";
    
    public CombatMania(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        CMStrikeStyles.register(modEventBus);
        CMDataComponents.register(modEventBus);
        CMAttachments.register(modEventBus);
        CMAttributes.register(modEventBus);
        CMItemProperties.addCustomItemsProperties();
        CMItems.register(modEventBus);
        CMBlocks.register(modEventBus);
        CMParticles.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
