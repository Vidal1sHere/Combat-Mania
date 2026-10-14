package com.vidal1shere.combatmania;

import com.mojang.logging.LogUtils;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.config.CMItemConfig;
import com.vidal1shere.combatmania.registry.block.CMBlocks;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.slf4j.Logger;

@Mod(CombatMania.MOD_ID)
public class CombatMania {
    public static final String MOD_ID = "combat_mania";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<Registry<StrikeStyle>> REGISTRY_KEY = 
            ResourceKey.createRegistryKey(CMUtil.modResLoc("strike_style"));
    public static final Registry<StrikeStyle> REGISTRY =
            new RegistryBuilder<>(REGISTRY_KEY).create();
    
    public CombatMania(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CMCommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, CMItemConfig.SPEC, "combatmania-items.toml");
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
