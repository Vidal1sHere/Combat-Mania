package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CMRecipeProvider extends RecipeProvider implements IConditionBuilder {
    
    public CMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, CombatMania.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    protected static void smithingUpgrade(RecipeOutput recipeOutput, Object template,
                                          Object base, Object addition,
                                          RecipeCategory category, Object result) {
        Item t = unwrapItem(template);
        Item b = unwrapItem(base);
        Item a = unwrapItem(addition);
        Item r = unwrapItem(result);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(t),
                        Ingredient.of(b),
                        Ingredient.of(a),
                        category,
                        r
                ).unlocks("has_" + getItemName(result), has(r))
                .save(recipeOutput, getItemName(result) + "_smithing");
    }
    
    private static Item unwrapItem(Object obj) {
        if (obj instanceof Item) return (Item) obj;
        if (obj instanceof DeferredItem) return ((DeferredItem<? extends Item>) obj).get();
        throw new IllegalArgumentException("Parameter is not Item nor DeferredItem: " + obj);
    }

    private static String getItemName(Object obj) {
        if (obj instanceof Item) {
            return BuiltInRegistries.ITEM.getKey((Item) obj).getPath();
        }
        if (obj instanceof DeferredItem) {
            return ((DeferredItem<?>) obj).getId().getPath();
        }
        throw new IllegalArgumentException("Parameter is not Item nor DeferredItem: " + obj);
    }
}
