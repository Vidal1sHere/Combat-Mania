package com.vidal1shere.combatmania;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public final class CMUtil {

    public static ResourceLocation modResLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(CombatMania.MOD_ID, path);
    }

    public static ResourceLocation mcResLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation resLoc(EntityType<?> entity) {
        return EntityType.getKey(entity);
    }

    public static int secondsToTicks(double seconds) {
        return Math.round((float) (seconds * 20.0));
    }
    
}
