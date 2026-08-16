package com.vidal1shere.combatmania.config;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ItemConfigCondition(ModConfigSpec.BooleanValue config) implements ICondition {
    public static final MapCodec<ItemConfigCondition> CODEC =
            MapCodec.unit(new ItemConfigCondition(CMItemConfig.SPEAR_ITEMS));

    @Override
    public boolean test(IContext context) {
        return config.get();
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}