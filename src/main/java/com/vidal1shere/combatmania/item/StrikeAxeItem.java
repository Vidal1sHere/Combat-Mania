package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike_styles.CleaveStrikeStyle;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;

public class StrikeAxeItem extends AxeItem implements IStrikeWeapon {
    public StrikeAxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public StrikeStyle getPrimaryStyle() {
        return new CleaveStrikeStyle();
    }

    @Override
    public @Nullable StrikeStyle getSecondaryStyle() {
        return null;
    }
}
