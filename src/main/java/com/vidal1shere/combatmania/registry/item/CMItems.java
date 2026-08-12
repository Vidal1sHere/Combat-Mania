package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CombatMania.MOD_ID);

    public static final DeferredItem<StrikeSwordItem> STRIKE_SWORD = ITEMS.register(
            "strike_sword", () -> new StrikeSwordItem(CMExtendedToolTiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(CMExtendedToolTiers.DIAMOND, 2.0F, -3.2F)))
    );

    public static final DeferredItem<DaggerItem> WOODEN_DAGGER = ITEMS.register(
            "wooden_dagger", () -> new DaggerItem(CMExtendedToolTiers.WOOD, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(CMExtendedToolTiers.WOOD, 2.0F)))
    );

    public static final DeferredItem<DaggerItem> STONE_DAGGER = ITEMS.register(
            "stone_dagger", () -> new DaggerItem(CMExtendedToolTiers.STONE, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(CMExtendedToolTiers.STONE, 2.0F)))
    );

    public static final DeferredItem<DaggerItem> GOLDEN_DAGGER = ITEMS.register(
            "golden_dagger", () -> new DaggerItem(CMExtendedToolTiers.GOLD, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(CMExtendedToolTiers.GOLD, 2.0F)))
    );

    public static final DeferredItem<DaggerItem> IRON_DAGGER = ITEMS.register(
            "iron_dagger", () -> new DaggerItem(CMExtendedToolTiers.IRON, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(CMExtendedToolTiers.IRON, 2.0F)))
    );

    public static final DeferredItem<DaggerItem> DIAMOND_DAGGER = ITEMS.register(
            "diamond_dagger", () -> new DaggerItem(CMExtendedToolTiers.DIAMOND, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(CMExtendedToolTiers.DIAMOND, 2.0F)))
    );
    
    public static final DeferredItem<GlaiveItem> WOODEN_GLAIVE = ITEMS.register(
            "wooden_glaive", () -> new GlaiveItem(CMExtendedToolTiers.WOOD, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(CMExtendedToolTiers.WOOD, 2.0F, -3.2F, -2.46F)))
    );

    public static final DeferredItem<GlaiveItem> STONE_GLAIVE = ITEMS.register(
            "stone_glaive", () -> new GlaiveItem(CMExtendedToolTiers.STONE, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(CMExtendedToolTiers.STONE, 2.0F, -3.2F, -2.67F)))
    );

    public static final DeferredItem<GlaiveItem> GOLDEN_GLAIVE = ITEMS.register(
            "golden_glaive", () -> new GlaiveItem(CMExtendedToolTiers.GOLD, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(CMExtendedToolTiers.GOLD, 2.0F, -3.0F, -2.95F)))
    );

    public static final DeferredItem<GlaiveItem> IRON_GLAIVE = ITEMS.register(
            "iron_glaive", () -> new GlaiveItem(CMExtendedToolTiers.IRON, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(CMExtendedToolTiers.IRON, 2.0F, -3.1F, -2.95F)))
    );

    public static final DeferredItem<GlaiveItem> DIAMOND_GLAIVE = ITEMS.register(
            "diamond_glaive", () -> new GlaiveItem(CMExtendedToolTiers.DIAMOND, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(CMExtendedToolTiers.DIAMOND, 2.0F, -3.0F, -3.05F)))
    );

    public static final DeferredItem<LanceItem> WOODEN_LANCE = ITEMS.register(
            "wooden_lance", () -> new LanceItem(CMExtendedToolTiers.WOOD, new Item.Properties()
                    .attributes(LanceItem.createAttributes(CMExtendedToolTiers.WOOD, 10.0F, -2.8F)))
    );

    public static final DeferredItem<LanceItem> STONE_LANCE = ITEMS.register(
            "stone_lance", () -> new LanceItem(CMExtendedToolTiers.STONE, new Item.Properties()
                    .attributes(LanceItem.createAttributes(CMExtendedToolTiers.STONE, 10.0F, -2.8F)))
    );

    public static final DeferredItem<LanceItem> GOLDEN_LANCE = ITEMS.register(
            "golden_lance", () -> new LanceItem(CMExtendedToolTiers.GOLD, new Item.Properties()
                    .attributes(LanceItem.createAttributes(CMExtendedToolTiers.GOLD, 10.0F, -2.8F)))
    );

    public static final DeferredItem<LanceItem> IRON_LANCE = ITEMS.register(
            "iron_lance", () -> new LanceItem(CMExtendedToolTiers.IRON, new Item.Properties()
                    .attributes(LanceItem.createAttributes(CMExtendedToolTiers.IRON, 10.0F, -2.8F)))
    );

    public static final DeferredItem<LanceItem> DIAMOND_LANCE = ITEMS.register(
            "diamond_lance", () -> new LanceItem(CMExtendedToolTiers.DIAMOND, new Item.Properties()
                    .attributes(LanceItem.createAttributes(CMExtendedToolTiers.DIAMOND, 10.0F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> WOODEN_SPEAR = ITEMS.register(
            "wooden_spear", () -> new SpearItem(CMExtendedToolTiers.WOOD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.WOOD, 2.0F, -2.46F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> STONE_SPEAR = ITEMS.register(
            "stone_spear", () -> new SpearItem(CMExtendedToolTiers.STONE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.STONE, 2.0F, -2.67F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> GOLDEN_SPEAR = ITEMS.register(
            "golden_spear", () -> new SpearItem(CMExtendedToolTiers.GOLD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.GOLD, 2.0F, -2.95F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> IRON_SPEAR = ITEMS.register(
            "iron_spear", () -> new SpearItem(CMExtendedToolTiers.IRON, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.IRON, 2.0F, -2.95F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> DIAMOND_SPEAR = ITEMS.register(
            "diamond_spear", () -> new SpearItem(CMExtendedToolTiers.DIAMOND, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.DIAMOND, 2.0F, -3.05F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> NETHERITE_SPEAR = ITEMS.register(
            "netherite_spear", () -> new SpearItem(CMExtendedToolTiers.NETHERITE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(CMExtendedToolTiers.NETHERITE, 2.0F, -3.13F, -2.8F)))
    );
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
