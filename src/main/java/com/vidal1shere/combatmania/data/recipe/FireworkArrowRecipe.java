package com.vidal1shere.combatmania.data.recipe;

import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.item.CMRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class FireworkArrowRecipe extends CustomRecipe {

    public FireworkArrowRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasFirework = false;
        boolean hasStick = false;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (stack.is(Items.FIREWORK_ROCKET)) hasFirework = true;
            else if (stack.is(Items.STICK)) hasStick = true;
            else return false;
        }

        return hasFirework && hasStick;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack firework = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(Items.FIREWORK_ROCKET)) {
                firework = stack;
                break;
            }
        }

        ItemStack result = new ItemStack(CMItems.FIREWORK_ARROW.get());
        result.set(DataComponents.FIREWORKS, firework.get(DataComponents.FIREWORKS));
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CMRecipes.FIREWORK_ARROW_SERIALIZER.get();
    }

    public static class Serializer extends SimpleCraftingRecipeSerializer<FireworkArrowRecipe> {
        public Serializer() {
            super(FireworkArrowRecipe::new);
        }
    }
}