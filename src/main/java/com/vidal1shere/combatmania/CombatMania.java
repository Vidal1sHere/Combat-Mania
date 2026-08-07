package com.vidal1shere.combatmania;

import com.vidal1shere.combatmania.registry.block.CMBlocks;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CombatMania.MOD_ID)
public class CombatMania {
    public static final String MOD_ID = "combat_mania";
    
    public CombatMania(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);

        CMStrikeStyles.register(modEventBus);
        CMDataComponents.register(modEventBus);
        CMAttachments.register(modEventBus);
        CMAttributes.register(modEventBus);
        CMItemProperties.addCustomItemsProperties();
        CMItems.register(modEventBus);
        CMBlocks.register(modEventBus);
        CMParticles.register(modEventBus);
        CMSounds.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, CMConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            if (CMConfig.SPEAR_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), new ItemStack(CMItems.WOODEN_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.WOODEN_SPEAR.get()), new ItemStack(CMItems.STONE_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.STONE_SPEAR.get()), new ItemStack(CMItems.IRON_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.IRON_SPEAR.get()), new ItemStack(CMItems.GOLDEN_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.GOLDEN_SPEAR.get()), new ItemStack(CMItems.DIAMOND_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.DIAMOND_SPEAR.get()), new ItemStack(CMItems.NETHERITE_SPEAR.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            
            if (CMConfig.GLAIVE_ITEMS.get()) {
                event.insertAfter(new ItemStack(CMItems.NETHERITE_SPEAR.get()), new ItemStack(CMItems.WOODEN_GLAIVE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.WOODEN_GLAIVE.get()), new ItemStack(CMItems.STONE_GLAIVE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.STONE_GLAIVE.get()), new ItemStack(CMItems.IRON_GLAIVE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.IRON_GLAIVE.get()), new ItemStack(CMItems.GOLDEN_GLAIVE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(CMItems.GOLDEN_GLAIVE.get()), new ItemStack(CMItems.DIAMOND_GLAIVE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
