package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CMRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final Map<Tiers, Item[]> WOOD_TO_DIAMOND_WEAPON_OUTPUTS = Map.of(
        Tiers.WOOD, new Item[]{ 
            CMItems.WOODEN_SPEAR.get(), 
            CMItems.WOODEN_GLAIVE.get(), 
            CMItems.WOODEN_DAGGER.get(), 
            CMItems.WOODEN_LANCE.get(), 
            CMItems.WOODEN_JAVELIN.get(), 
            CMItems.WOODEN_MACE.get(), 
            CMItems.WOODEN_HAMMER.get(), 
            CMItems.WOODEN_RAPIER.get(), 
            CMItems.WOODEN_KATANA.get(), 
            CMItems.WOODEN_GREATSWORD.get(), 
            CMItems.WOODEN_CLAYMORE.get()
        },
        Tiers.STONE, new Item[]{
            CMItems.STONE_SPEAR.get(), 
            CMItems.STONE_GLAIVE.get(), 
            CMItems.STONE_DAGGER.get(), 
            CMItems.STONE_LANCE.get(), 
            CMItems.STONE_JAVELIN.get(), 
            CMItems.STONE_MACE.get(),
            CMItems.STONE_HAMMER.get(),
            CMItems.STONE_RAPIER.get(),
            CMItems.STONE_KATANA.get(),
            CMItems.STONE_GREATSWORD.get(),
            CMItems.STONE_CLAYMORE.get()
        },
        Tiers.IRON, new Item[]{ 
            CMItems.IRON_SPEAR.get(), 
            CMItems.IRON_GLAIVE.get(), 
            CMItems.IRON_DAGGER.get(), 
            CMItems.IRON_LANCE.get(), 
            CMItems.IRON_JAVELIN.get(), 
            CMItems.IRON_MACE.get(),
            CMItems.IRON_HAMMER.get(),
            CMItems.IRON_RAPIER.get(),
            CMItems.IRON_KATANA.get(),
            CMItems.IRON_GREATSWORD.get(),
            CMItems.IRON_CLAYMORE.get()
        },
        Tiers.GOLD, new Item[]{ 
            CMItems.GOLDEN_SPEAR.get(), 
            CMItems.GOLDEN_GLAIVE.get(), 
            CMItems.GOLDEN_DAGGER.get(), 
            CMItems.GOLDEN_LANCE.get(), 
            CMItems.GOLDEN_JAVELIN.get(), 
            CMItems.GOLDEN_MACE.get(),
            CMItems.GOLDEN_HAMMER.get(),
            CMItems.GOLDEN_RAPIER.get(),
            CMItems.GOLDEN_KATANA.get(),
            CMItems.GOLDEN_GREATSWORD.get(),
            CMItems.GOLDEN_CLAYMORE.get()
        },
        Tiers.DIAMOND, new Item[]{ 
            CMItems.DIAMOND_SPEAR.get(), 
            CMItems.DIAMOND_GLAIVE.get(), 
            CMItems.DIAMOND_DAGGER.get(), 
            CMItems.DIAMOND_LANCE.get(), 
            CMItems.DIAMOND_JAVELIN.get(), 
            CMItems.DIAMOND_MACE.get(),
            CMItems.DIAMOND_HAMMER.get(),
            CMItems.DIAMOND_RAPIER.get(),
            CMItems.DIAMOND_KATANA.get(),
            CMItems.DIAMOND_GREATSWORD.get(),
            CMItems.DIAMOND_CLAYMORE.get()
        }
    );
    
    public CMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        for (Tiers tier : Tiers.values()) {
            if (tier == Tiers.NETHERITE) continue;
            spear(recipeOutput, tier);
            glaive(recipeOutput, tier);
            dagger(recipeOutput, tier);
            lance(recipeOutput, tier);
            javelin(recipeOutput, tier);
            mace(recipeOutput, tier);
            hammer(recipeOutput, tier);
            rapier(recipeOutput, tier);
            katana(recipeOutput, tier);
            greatsword(recipeOutput, tier);
            claymore(recipeOutput, tier);
        }
    }

    private static void weapon(RecipeOutput recipeOutput, Tier tier, int outputIndex,
                               String... pattern) {
        Ingredient material = getMaterial(tier);
        Item[] outputs = WOOD_TO_DIAMOND_WEAPON_OUTPUTS.get(tier);
        if (material == null || outputs == null) return;

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, outputs[outputIndex]);
        for (String row : pattern) builder = builder.pattern(row);

        builder.define('M', material)
            .define('S', Items.STICK)
            .unlockedBy("has_stick", has(Items.STICK))
            .save(recipeOutput);
    }

    protected static void spear(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 0, 
            " MM", 
            " SM", 
            "S  "
        );
    }
    protected static void glaive(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 1, 
            " MM", 
            "MS ", 
            "S  "
        );
    }
    protected static void dagger(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 2, 
            " M", 
            "S "
        );
    }
    protected static void lance(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 3, 
            "  M", 
            "MM ", 
            "SM "
        );
    }
    protected static void javelin(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 4, 
            "  M", 
            " S ", 
            "S  "
        );
    }
    protected static void mace(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 5, 
            " MM", 
            " MM", 
            "S  "
        );
    }
    protected static void hammer(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 6,
            " M ",
            "MMM",
            "SM "
        );
    }
    protected static void rapier(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 7,
            " M ",
            " M ",
            "MS "
        );
    }
    protected static void katana(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 8,
            "MM",
            "M ",
            "S "
        );
    }
    protected static void greatsword(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 9,
            " MM",
            "MMM",
            "SM "
        );
    }
    protected static void claymore(RecipeOutput recipeOutput, Tier tier) {
        weapon(recipeOutput, tier, 10,
            " M ",
            "MMM",
            " S "
        );
    }
    
    private static Ingredient getMaterial(Tier tier) {
        return switch (tier) {
            case Tiers.WOOD -> Ingredient.of(ItemTags.PLANKS);
            case Tiers.STONE -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS);
            case Tiers.IRON -> Ingredient.of(Items.IRON_INGOT);
            case Tiers.GOLD -> Ingredient.of(Items.GOLD_INGOT);
            case Tiers.DIAMOND -> Ingredient.of(Items.DIAMOND);
            default -> null;
        };
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
