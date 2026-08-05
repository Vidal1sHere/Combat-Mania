package com.vidal1shere.combatmania;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.Consumer;

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

    public static CompoundTag getNbt(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void updateNbt(ItemStack stack, Consumer<CompoundTag> mutator) {
        CompoundTag tag = getNbt(stack);
        mutator.accept(tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}
