package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getPrimaryStyle() {
        return CMStrikeStyles.STAB;
    }

    @Override
    public @Nullable DeferredHolder<StrikeStyle, ? extends StrikeStyle> getSecondaryStyle() {
        return null;
    }
}
