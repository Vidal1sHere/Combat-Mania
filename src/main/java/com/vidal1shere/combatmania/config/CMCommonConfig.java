package com.vidal1shere.combatmania.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CMCommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

//    public static final ModConfigSpec.BooleanValue SPEAR_ITEMS = BUILDER
//            .comment("Add spear items in the creative menu")
//            .define("spearItems", false);
//
//    public static final ModConfigSpec.BooleanValue GLAIVE_ITEMS = BUILDER
//            .comment("Add glaive items in the creative menu")
//            .define("glaiveItems", false);
//
//    public static final ModConfigSpec.BooleanValue DAGGER_ITEMS = BUILDER
//            .comment("Add dagger items in the creative menu")
//            .define("daggerItems", false);

    public static final ModConfigSpec.BooleanValue MODIFY_SWORDS = BUILDER
            .comment("Modifies vanilla swords to use Strike Styles")
            .define("modifyVanillaSwords", false);

    public static final ModConfigSpec.BooleanValue MODIFY_AXES = BUILDER
            .comment("Modifies vanilla axes to use Strike Styles")
            .define("modifyVanillaAxes", false);
    
    public static final ModConfigSpec.BooleanValue MODIFY_TRIDENT = BUILDER
            .comment("Modifies vanilla trident to use Strike Styles")
            .define("modifyVanillaTrident", false);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
