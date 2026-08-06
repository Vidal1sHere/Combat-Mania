package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.item.SpearItem;
import com.vidal1shere.combatmania.item.StrikeAxeItem;
import com.vidal1shere.combatmania.item.StrikeSwordItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CMItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CombatMania.MOD_ID);

    public static final DeferredItem<StrikeSwordItem> STRIKE_SWORD = ITEMS.register(
            "strike_sword", () -> new StrikeSwordItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 3, -2.4F)))
    );

    public static final DeferredItem<StrikeAxeItem> STRIKE_AXE = ITEMS.register(
            "strike_axe", () -> new StrikeAxeItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(AxeItem.createAttributes(Tiers.DIAMOND, 5.0F, -3.0F)))
    );

    public static final DeferredItem<SpearItem> WOODEN_SPEAR = ITEMS.register(
            "wooden_spear", () -> new SpearItem(Tiers.WOOD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.WOOD, 2.0F, -2.46F)))
    );

    public static final DeferredItem<SpearItem> STONE_SPEAR = ITEMS.register(
            "stone_spear", () -> new SpearItem(Tiers.STONE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.STONE, 2.0F, -2.67F)))
    );

    public static final DeferredItem<SpearItem> GOLDEN_SPEAR = ITEMS.register(
            "golden_spear", () -> new SpearItem(Tiers.GOLD, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.GOLD, 2.0F, -2.95F)))
    );

    public static final DeferredItem<SpearItem> IRON_SPEAR = ITEMS.register(
            "iron_spear", () -> new SpearItem(Tiers.IRON, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.IRON, 2.0F, -2.95F)))
    );

    public static final DeferredItem<SpearItem> DIAMOND_SPEAR = ITEMS.register(
            "diamond_spear", () -> new SpearItem(Tiers.DIAMOND, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.DIAMOND, 2.0F, -3.05F)))
    );

    public static final DeferredItem<SpearItem> NETHERITE_SPEAR = ITEMS.register(
            "netherite_spear", () -> new SpearItem(Tiers.NETHERITE, new Item.Properties()
                    .attributes(SpearItem.createAttributes(Tiers.NETHERITE, 2.0F, -3.13F)))
    );
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
