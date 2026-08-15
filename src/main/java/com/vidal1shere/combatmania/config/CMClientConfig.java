package com.vidal1shere.combatmania.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CMClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SPEAR_ITEMS = BUILDER
            .comment("Add spear items in the creative menu")
            .define("spearItems", false);

    public static final ModConfigSpec.BooleanValue GLAIVE_ITEMS = BUILDER
            .comment("Add glaive items in the creative menu")
            .define("glaiveItems", false);

    public static final ModConfigSpec.BooleanValue DAGGER_ITEMS = BUILDER
            .comment("Add dagger items in the creative menu")
            .define("daggerItems", false);

    public static final ModConfigSpec.BooleanValue LANCE_ITEMS = BUILDER
            .comment("Add lance items in the creative menu")
            .define("lanceItems", false);

    public static final ModConfigSpec.BooleanValue JAVELIN_ITEMS = BUILDER
            .comment("Add javelin items in the creative menu")
            .define("javelinItems", false);

    public static final ModConfigSpec.BooleanValue MACE_ITEMS = BUILDER
            .comment("Add mace items in the creative menu")
            .define("maceItems", false);

    public static final ModConfigSpec.BooleanValue TWO_DIMENSIONAL_TRIDENT = BUILDER
            .comment("Makes the trident two-dimensional to match javelins")
            .define("2DTrident", false);

    public static final ModConfigSpec.BooleanValue THREE_DIMENSIONAL_MACES = BUILDER
            .comment("Makes all maces three-dimensional")
            .define("3DMaces", false);
    
    public static final ModConfigSpec SPEC = BUILDER.build();
}
