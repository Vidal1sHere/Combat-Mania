package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        return CMStrikeStyles.STAB;
    }
}
