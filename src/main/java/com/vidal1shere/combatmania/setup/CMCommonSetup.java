package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CMCommonSetup {

    @SubscribeEvent
    public static void registeCreativeItems(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_DAGGER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_LANCE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.WOODEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.STONE_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.IRON_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.GOLDEN_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.TRIDENT), CMItems.DIAMOND_JAVELIN.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertBefore(new ItemStack(Items.MACE), CMItems.WOODEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.MACE), CMItems.STONE_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.MACE), CMItems.IRON_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.MACE), CMItems.GOLDEN_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertBefore(new ItemStack(Items.MACE), CMItems.DIAMOND_MACE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            
            event.insertAfter(new ItemStack(Items.MACE), CMItems.DIAMOND_HAMMER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.MACE), CMItems.GOLDEN_HAMMER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.MACE), CMItems.IRON_HAMMER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.MACE), CMItems.STONE_HAMMER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.MACE), CMItems.WOODEN_HAMMER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_GLAIVE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.NETHERITE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.DIAMOND_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.GOLDEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.IRON_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.STONE_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_AXE), CMItems.WOODEN_SPEAR.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_GREATSWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_GREATSWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_GREATSWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_GREATSWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_GREATSWORD.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_KATANA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_KATANA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_KATANA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_KATANA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_KATANA.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_RAPIER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_RAPIER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_RAPIER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_RAPIER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_RAPIER.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.NETHERITE_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.DIAMOND_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.GOLDEN_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.IRON_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.STONE_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(new ItemStack(Items.NETHERITE_SWORD), CMItems.WOODEN_CLAYMORE.get().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
