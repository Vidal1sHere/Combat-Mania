package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.strike.StrikeStyle;
import org.jetbrains.annotations.Nullable;

public interface IStrikeWeapon {

    StrikeStyle getPrimaryStyle();

    @Nullable
    StrikeStyle getSecondaryStyle();
}