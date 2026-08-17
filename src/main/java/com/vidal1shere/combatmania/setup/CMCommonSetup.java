package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.config.CMItemConfig;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataMaps;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CMCommonSetup {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeModificationEvent event) {
        CMAttributes.registerAttributes(event);
    }

    @SubscribeEvent
    public static void registerDataMap(RegisterDataMapTypesEvent event) {
        CMDataMaps.register(event);
    }
    
    @SubscribeEvent
    public static void modifyItemAttributes(ItemAttributeModifierEvent event) {
        CMAttributes.modifyItemAttributes(event);
    }

    @SubscribeEvent
    public static void modifyItemTooltips(ItemTooltipEvent event) {
        QuiverItem.modifyTooltip(event);
    }

    @SubscribeEvent
    public static void registerNewRegistry(NewRegistryEvent event) {
        event.register(CMStrikeStyles.REGISTRY);
    }

    @SubscribeEvent
    public static void registeCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            if (CMItemConfig.DAGGER_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.LANCE_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.JAVELIN_ITEMS.get()) {
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.WOODEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.STONE_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.IRON_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.GOLDEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.DIAMOND_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.MACE_ITEMS.get()) {
                event.insertBefore(new ItemStack(Items.MACE), CMItems.WOODEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.MACE), CMItems.STONE_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.MACE), CMItems.IRON_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.MACE), CMItems.GOLDEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.MACE), CMItems.DIAMOND_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.GLAIVE_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.SPEAR_ITEMS.get()) {
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.NETHERITE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }

            if (CMItemConfig.QUIVER_ITEMS.get()) {
                event.insertBefore(new ItemStack(Items.ARROW), CMItems.LEATHER_QUIVER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                event.insertBefore(new ItemStack(Items.ARROW), CMItems.FLAMING_QUIVER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            }
        }
    }
}
