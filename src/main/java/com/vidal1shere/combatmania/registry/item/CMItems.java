package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;

public class CMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CombatMania.MOD_ID);
    
    public static final DeferredItem<DaggerItem> WOODEN_DAGGER = ITEMS.register(
            "wooden_dagger", () -> new DaggerItem(Tiers.WOOD, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(Tiers.WOOD, 2.0F, -2.0F)))
    );

    public static final DeferredItem<DaggerItem> STONE_DAGGER = ITEMS.register(
            "stone_dagger", () -> new DaggerItem(Tiers.STONE, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(Tiers.STONE, 2.0F, -2.0F)))
    );

    public static final DeferredItem<DaggerItem> GOLDEN_DAGGER = ITEMS.register(
            "golden_dagger", () -> new DaggerItem(Tiers.GOLD, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(Tiers.GOLD, 2.0F, -2.0F)))
    );

    public static final DeferredItem<DaggerItem> IRON_DAGGER = ITEMS.register(
            "iron_dagger", () -> new DaggerItem(Tiers.IRON, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(Tiers.IRON, 2.0F, -2.0F)))
    );

    public static final DeferredItem<DaggerItem> DIAMOND_DAGGER = ITEMS.register(
            "diamond_dagger", () -> new DaggerItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(DaggerItem.createAttributes(Tiers.DIAMOND, 2.0F, -2.0F)))
    );
    
    public static final DeferredItem<GlaiveItem> WOODEN_GLAIVE = ITEMS.register(
            "wooden_glaive", () -> new GlaiveItem(Tiers.WOOD, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(Tiers.WOOD, 2.0F, -3.2F)))
    );

    public static final DeferredItem<GlaiveItem> STONE_GLAIVE = ITEMS.register(
            "stone_glaive", () -> new GlaiveItem(Tiers.STONE, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(Tiers.STONE, 2.0F, -3.2F)))
    );

    public static final DeferredItem<GlaiveItem> GOLDEN_GLAIVE = ITEMS.register(
            "golden_glaive", () -> new GlaiveItem(Tiers.GOLD, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(Tiers.GOLD, 2.0F, -3.0F)))
    );

    public static final DeferredItem<GlaiveItem> IRON_GLAIVE = ITEMS.register(
            "iron_glaive", () -> new GlaiveItem(Tiers.IRON, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(Tiers.IRON, 2.0F, -3.1F)))
    );

    public static final DeferredItem<GlaiveItem> DIAMOND_GLAIVE = ITEMS.register(
            "diamond_glaive", () -> new GlaiveItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(GlaiveItem.createAttributes(Tiers.DIAMOND, 2.0F, -3.0F)))
    );

    public static final DeferredItem<LanceItem> WOODEN_LANCE = ITEMS.register(
            "wooden_lance", () -> new LanceItem(Tiers.WOOD, new Item.Properties()
                    .attributes(LanceItem.createAttributes(Tiers.WOOD, 5.0F, -2.4F)))
    );

    public static final DeferredItem<LanceItem> STONE_LANCE = ITEMS.register(
            "stone_lance", () -> new LanceItem(Tiers.STONE, new Item.Properties()
                    .attributes(LanceItem.createAttributes(Tiers.STONE, 5.0F, -2.4F)))
    );

    public static final DeferredItem<LanceItem> GOLDEN_LANCE = ITEMS.register(
            "golden_lance", () -> new LanceItem(Tiers.GOLD, new Item.Properties()
                    .attributes(LanceItem.createAttributes(Tiers.GOLD, 5.0F, -2.4F)))
    );

    public static final DeferredItem<LanceItem> IRON_LANCE = ITEMS.register(
            "iron_lance", () -> new LanceItem(Tiers.IRON, new Item.Properties()
                    .attributes(LanceItem.createAttributes(Tiers.IRON, 5.0F, -2.4F)))
    );

    public static final DeferredItem<LanceItem> DIAMOND_LANCE = ITEMS.register(
            "diamond_lance", () -> new LanceItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(LanceItem.createAttributes(Tiers.DIAMOND, 5.0F, -2.4F)))
    );

    public static final DeferredItem<SpearItem> WOODEN_SPEAR = ITEMS.register(
            "wooden_spear", () -> new SpearItem(Tiers.WOOD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.WOOD, 2.0F, -2.46F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> STONE_SPEAR = ITEMS.register(
            "stone_spear", () -> new SpearItem(Tiers.STONE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.STONE, 2.0F, -2.67F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> GOLDEN_SPEAR = ITEMS.register(
            "golden_spear", () -> new SpearItem(Tiers.GOLD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.GOLD, 2.0F, -2.95F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> IRON_SPEAR = ITEMS.register(
            "iron_spear", () -> new SpearItem(Tiers.IRON, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.IRON, 2.0F, -2.95F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> DIAMOND_SPEAR = ITEMS.register(
            "diamond_spear", () -> new SpearItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.DIAMOND, 2.0F, -3.05F, -2.8F)))
    );

    public static final DeferredItem<SpearItem> NETHERITE_SPEAR = ITEMS.register(
            "netherite_spear", () -> new SpearItem(Tiers.NETHERITE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.NETHERITE, 2.0F, -3.13F, -2.8F)))
    );

    public static final DeferredItem<SwordItem> WOODEN_RAPIER = ITEMS.register(
        "wooden_rapier", () -> new SwordItem(Tiers.WOOD, new Item.Properties()
            .attributes(RapierItem.createAttributes(Tiers.WOOD)))
    );

    public static final DeferredItem<SwordItem> STONE_RAPIER = ITEMS.register(
        "stone_rapier", () -> new SwordItem(Tiers.STONE, new Item.Properties()
            .attributes(RapierItem.createAttributes(Tiers.STONE)))
    );

    public static final DeferredItem<SwordItem> GOLDEN_RAPIER = ITEMS.register(
        "golden_rapier", () -> new SwordItem(Tiers.GOLD, new Item.Properties()
            .attributes(RapierItem.createAttributes(Tiers.GOLD)))
    );

    public static final DeferredItem<SwordItem> IRON_RAPIER = ITEMS.register(
        "iron_rapier", () -> new SwordItem(Tiers.IRON, new Item.Properties()
            .attributes(RapierItem.createAttributes(Tiers.IRON)))
    );

    public static final DeferredItem<SwordItem> DIAMOND_RAPIER = ITEMS.register(
        "diamond_rapier", () -> new SwordItem(Tiers.DIAMOND, new Item.Properties()
            .attributes(RapierItem.createAttributes(Tiers.DIAMOND)))
    );

    public static final DeferredItem<KatanaItem> WOODEN_KATANA = ITEMS.register(
        "wooden_katana", () -> new KatanaItem(Tiers.WOOD, new Item.Properties()
            .attributes(KatanaItem.createAttributes(Tiers.WOOD, 3.0F, -3.2F)))
    );

    public static final DeferredItem<KatanaItem> STONE_KATANA = ITEMS.register(
        "stone_katana", () -> new KatanaItem(Tiers.STONE, new Item.Properties()
            .attributes(KatanaItem.createAttributes(Tiers.STONE, 3.0F, -3.2F)))
    );

    public static final DeferredItem<KatanaItem> GOLDEN_KATANA = ITEMS.register(
        "golden_katana", () -> new KatanaItem(Tiers.GOLD, new Item.Properties()
            .attributes(KatanaItem.createAttributes(Tiers.GOLD, 3.0F, -3.0F)))
    );

    public static final DeferredItem<KatanaItem> IRON_KATANA = ITEMS.register(
        "iron_katana", () -> new KatanaItem(Tiers.IRON, new Item.Properties()
            .attributes(KatanaItem.createAttributes(Tiers.IRON, 3.0F, -3.1F)))
    );

    public static final DeferredItem<KatanaItem> DIAMOND_KATANA = ITEMS.register(
        "diamond_katana", () -> new KatanaItem(Tiers.DIAMOND, new Item.Properties()
            .attributes(KatanaItem.createAttributes(Tiers.DIAMOND, 3.0F, -3.0F)))
    );

    public static final DeferredItem<JavelinItem> WOODEN_JAVELIN = ITEMS.register(
            "wooden_javelin", () -> new JavelinItem(Tiers.WOOD, new Item.Properties()
                    .attributes(JavelinItem.createAttributes(Tiers.WOOD, 2.0F, -2.46F, 0.5F)))
    );

    public static final DeferredItem<JavelinItem> STONE_JAVELIN = ITEMS.register(
            "stone_javelin", () -> new JavelinItem(Tiers.STONE, new Item.Properties()
                    .attributes(JavelinItem.createAttributes(Tiers.STONE, 2.0F, -2.67F, 1.0F)))
    );

    public static final DeferredItem<JavelinItem> GOLDEN_JAVELIN = ITEMS.register(
            "golden_javelin", () -> new JavelinItem(Tiers.GOLD, new Item.Properties()
                    .attributes(JavelinItem.createAttributes(Tiers.GOLD, 2.0F, -2.95F, 2.5F)))
    );

    public static final DeferredItem<JavelinItem> IRON_JAVELIN = ITEMS.register(
            "iron_javelin", () -> new JavelinItem(Tiers.IRON, new Item.Properties()
                    .attributes(JavelinItem.createAttributes(Tiers.IRON, 2.0F, -2.95F, 1.5F)))
    );

    public static final DeferredItem<JavelinItem> DIAMOND_JAVELIN = ITEMS.register(
            "diamond_javelin", () -> new JavelinItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(JavelinItem.createAttributes(Tiers.DIAMOND, 2.0F, -3.05F, 2.0F)))
    );

    public static final DeferredItem<TieredMaceItem> WOODEN_MACE = ITEMS.register(
            "wooden_mace", () -> new TieredMaceItem(Tiers.WOOD, new Item.Properties()
                    .attributes(TieredMaceItem.createAttributes(Tiers.WOOD, 2.0F, -2.9F)))
    );

    public static final DeferredItem<TieredMaceItem> STONE_MACE = ITEMS.register(
            "stone_mace", () -> new TieredMaceItem(Tiers.STONE, new Item.Properties()
                    .attributes(TieredMaceItem.createAttributes(Tiers.STONE, 1.5F, -3.05F)))
    );

    public static final DeferredItem<TieredMaceItem> GOLDEN_MACE = ITEMS.register(
            "golden_mace", () -> new TieredMaceItem(Tiers.GOLD, new Item.Properties()
                    .attributes(TieredMaceItem.createAttributes(Tiers.GOLD, 2.5F, -2.75F)))
    );

    public static final DeferredItem<TieredMaceItem> IRON_MACE = ITEMS.register(
            "iron_mace", () -> new TieredMaceItem(Tiers.IRON, new Item.Properties()
                    .attributes(TieredMaceItem.createAttributes(Tiers.IRON, 1.0F, -3.2F)))
    );

    public static final DeferredItem<TieredMaceItem> DIAMOND_MACE = ITEMS.register(
            "diamond_mace", () -> new TieredMaceItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(TieredMaceItem.createAttributes(Tiers.DIAMOND, 0.5F, -3.35F)))
    );

    public static final DeferredItem<HammerItem> WOODEN_HAMMER = ITEMS.register(
            "wooden_hammer", () -> new HammerItem(Tiers.WOOD, new Item.Properties()
                    .attributes(HammerItem.createAttributes(Tiers.WOOD, 1.0F, -3.2F)))
    );

    public static final DeferredItem<HammerItem> STONE_HAMMER = ITEMS.register(
            "stone_hammer", () -> new HammerItem(Tiers.STONE, new Item.Properties()
                    .attributes(HammerItem.createAttributes(Tiers.STONE, 1.0F, -3.2F)))
    );

    public static final DeferredItem<HammerItem> IRON_HAMMER = ITEMS.register(
            "iron_hammer", () -> new HammerItem(Tiers.IRON, new Item.Properties()
                    .attributes(HammerItem.createAttributes(Tiers.IRON, 1.0F, -3.2F)))
    );

    public static final DeferredItem<HammerItem> GOLDEN_HAMMER = ITEMS.register(
            "golden_hammer", () -> new HammerItem(Tiers.GOLD, new Item.Properties()
                    .attributes(HammerItem.createAttributes(Tiers.GOLD, 1.0F, -3.2F)))
    );

    public static final DeferredItem<HammerItem> DIAMOND_HAMMER = ITEMS.register(
            "diamond_hammer", () -> new HammerItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(HammerItem.createAttributes(Tiers.DIAMOND, 1.0F, -3.2F)))
    );

    public static final DeferredItem<ClaymoreItem> WOODEN_CLAYMORE = ITEMS.register(
        "wooden_claymore", () -> new ClaymoreItem(Tiers.WOOD, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.WOOD, 3.5F, -3.0F)))
    );

    public static final DeferredItem<ClaymoreItem> STONE_CLAYMORE = ITEMS.register(
        "stone_claymore", () -> new ClaymoreItem(Tiers.STONE, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.STONE, 3.5F, -3.0F)))
    );

    public static final DeferredItem<ClaymoreItem> GOLDEN_CLAYMORE = ITEMS.register(
        "golden_claymore", () -> new ClaymoreItem(Tiers.GOLD, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.GOLD, 3.5F, -3.0F)))
    );

    public static final DeferredItem<ClaymoreItem> IRON_CLAYMORE = ITEMS.register(
        "iron_claymore", () -> new ClaymoreItem(Tiers.IRON, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.IRON, 3.5F, -3.0F)))
    );

    public static final DeferredItem<ClaymoreItem> DIAMOND_CLAYMORE = ITEMS.register(
        "diamond_claymore", () -> new ClaymoreItem(Tiers.DIAMOND, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.DIAMOND, 3.5F, -3.0F)))
    );

    public static final DeferredItem<ClaymoreItem> NETHERITE_CLAYMORE = ITEMS.register(
        "netherite_claymore", () -> new ClaymoreItem(Tiers.NETHERITE, new Item.Properties()
            .attributes(ClaymoreItem.createAttributes(Tiers.NETHERITE, 3.5F, -3.0F)))
    );

    public static final DeferredItem<GreatswordItem> WOODEN_GREATSWORD = ITEMS.register(
        "wooden_greatsword", () -> new GreatswordItem(Tiers.WOOD, new Item.Properties()
            .attributes(GreatswordItem.createAttributes(Tiers.WOOD, 3.5F, -2.8F, -3.1F)))
    );

    public static final DeferredItem<GreatswordItem> STONE_GREATSWORD = ITEMS.register(
        "stone_greatsword", () -> new GreatswordItem(Tiers.STONE, new Item.Properties()
            .attributes(GreatswordItem.createAttributes(Tiers.STONE, 3.5F, -2.9F, -3.2F)))
    );

    public static final DeferredItem<GreatswordItem> GOLDEN_GREATSWORD = ITEMS.register(
        "golden_greatsword", () -> new GreatswordItem(Tiers.GOLD, new Item.Properties()
            .attributes(GreatswordItem.createAttributes(Tiers.GOLD, 3.5F, -3.0F, -3.3F)))
    );

    public static final DeferredItem<GreatswordItem> IRON_GREATSWORD = ITEMS.register(
        "iron_greatsword", () -> new GreatswordItem(Tiers.IRON, new Item.Properties()
            .attributes(GreatswordItem.createAttributes(Tiers.IRON, 3.5F, -3.1F, -3.4F)))
    );

    public static final DeferredItem<GreatswordItem> DIAMOND_GREATSWORD = ITEMS.register(
        "diamond_greatsword", () -> new GreatswordItem(Tiers.DIAMOND, new Item.Properties()
            .attributes(GreatswordItem.createAttributes(Tiers.DIAMOND, 3.5F, -3.2F, -3.5F)))
    );
    
    public static Collection<DeferredHolder<Item, ? extends Item>> getItems() {
        return ITEMS.getEntries();
    }
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
