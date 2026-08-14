package com.vidal1shere.combatmania;

import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.registry.block.CMBlocks;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.minecraft.world.item.*;
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
        modContainer.registerConfig(ModConfig.Type.COMMON, CMCommonConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CMClientConfig.SPEC);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

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

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            if (CMClientConfig.DAGGER_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMClientConfig.LANCE_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMClientConfig.JAVELIN_ITEMS.get()) {
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.WOODEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.STONE_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.IRON_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.GOLDEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.DIAMOND_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
            
            if (CMClientConfig.GLAIVE_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMClientConfig.SPEAR_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.NETHERITE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}
