package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike_styles.SweepStrikeStyle;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public StrikeStyle getPrimaryStyle() {
        return new SweepStrikeStyle();
    }

    @Override
    public @Nullable StrikeStyle getSecondaryStyle() {
        return null;
    }
}
