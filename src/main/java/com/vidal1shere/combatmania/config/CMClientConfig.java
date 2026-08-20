package com.vidal1shere.combatmania.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CMClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue TWO_DIMENSIONAL_TRIDENT = BUILDER
            .comment("Makes the trident two-dimensional to match javelins")
            .define("2DTrident", false);

    public static final ModConfigSpec.BooleanValue THREE_DIMENSIONAL_MACES = BUILDER
            .comment("Makes all maces three-dimensional")
            .define("3DMaces", false);

    public static final ModConfigSpec.BooleanValue THREE_DIMENSIONAL_HAMMERS = BUILDER
            .comment("Makes all hammers three-dimensional")
            .define("3DHammers", false);
    
    public static final ModConfigSpec SPEC = BUILDER.build();
}
