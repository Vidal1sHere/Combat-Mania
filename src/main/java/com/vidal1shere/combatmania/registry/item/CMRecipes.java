package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.recipe.FireworkArrowRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CMRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(Registries.RECIPE_SERIALIZER, CombatMania.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, FireworkArrowRecipe.Serializer> FIREWORK_ARROW_SERIALIZER =
        SERIALIZERS.register("firework_arrow", FireworkArrowRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}