package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.CombatMania;
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
    
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
