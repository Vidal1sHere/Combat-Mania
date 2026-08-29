package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.config.CMItemConfig;
import com.vidal1shere.combatmania.config.ItemConfigCondition;
import com.vidal1shere.combatmania.registry.item.CMExtendedToolTiers;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CMRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final Map<IExtendedTier, Item[]> WOOD_TO_DIAMOND_WEAPON_OUTPUTS = Map.of(
            CMExtendedToolTiers.WOOD, new Item[]{ 
                    CMItems.WOODEN_SPEAR.get(), 
                    CMItems.WOODEN_GLAIVE.get(), 
                    CMItems.WOODEN_DAGGER.get(), 
                    CMItems.WOODEN_LANCE.get(), 
                    CMItems.WOODEN_JAVELIN.get(), 
                    CMItems.WOODEN_MACE.get(), 
                    CMItems.WOODEN_HAMMER.get(), 
            },
            CMExtendedToolTiers.STONE, new Item[]{
                    CMItems.STONE_SPEAR.get(), 
                    CMItems.STONE_GLAIVE.get(), 
                    CMItems.STONE_DAGGER.get(), 
                    CMItems.STONE_LANCE.get(), 
                    CMItems.STONE_JAVELIN.get(), 
                    CMItems.STONE_MACE.get(),
                    CMItems.STONE_HAMMER.get()
            },
            CMExtendedToolTiers.IRON, new Item[]{ 
                    CMItems.IRON_SPEAR.get(), 
                    CMItems.IRON_GLAIVE.get(), 
                    CMItems.IRON_DAGGER.get(), 
                    CMItems.IRON_LANCE.get(), 
                    CMItems.IRON_JAVELIN.get(), 
                    CMItems.IRON_MACE.get(),
                    CMItems.IRON_HAMMER.get()    
            },
            CMExtendedToolTiers.GOLD, new Item[]{ 
                    CMItems.GOLDEN_SPEAR.get(), 
                    CMItems.GOLDEN_GLAIVE.get(), 
                    CMItems.GOLDEN_DAGGER.get(), 
                    CMItems.GOLDEN_LANCE.get(), 
                    CMItems.GOLDEN_JAVELIN.get(), 
                    CMItems.GOLDEN_MACE.get(),
                    CMItems.GOLDEN_HAMMER.get()
            },
            CMExtendedToolTiers.DIAMOND, new Item[]{ 
                    CMItems.DIAMOND_SPEAR.get(), 
                    CMItems.DIAMOND_GLAIVE.get(), 
                    CMItems.DIAMOND_DAGGER.get(), 
                    CMItems.DIAMOND_LANCE.get(), 
                    CMItems.DIAMOND_JAVELIN.get(), 
                    CMItems.DIAMOND_MACE.get(),
                    CMItems.DIAMOND_HAMMER.get()
            }
    );
    
    public CMRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        for (IExtendedTier tier : CMExtendedToolTiers.values()) {
            if (tier == CMExtendedToolTiers.NETHERITE) continue;
            spear(recipeOutput, tier);
            glaive(recipeOutput, tier);
            dagger(recipeOutput, tier);
            lance(recipeOutput, tier);
            javelin(recipeOutput, tier);
            mace(recipeOutput, tier);
            hammer(recipeOutput, tier);
        }
    }

    private static void weapon(RecipeOutput recipeOutput, IExtendedTier tier,
                               ModConfigSpec.BooleanValue config, int outputIndex,
                               String... pattern) {
        Ingredient material = getMaterial(tier);
        Item[] outputs = WOOD_TO_DIAMOND_WEAPON_OUTPUTS.get(tier);
        if (material == null || outputs == null) return;

        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, outputs[outputIndex]);
        for (String row : pattern) builder = builder.pattern(row);

        builder.define('M', material)
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))
                .save(recipeOutput.withConditions(new ItemConfigCondition(config)));
    }

    protected static void spear(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.SPEAR_ITEMS, 0, 
                " MM", 
                " SM", 
                "S  "
        );
    }
    protected static void glaive(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.GLAIVE_ITEMS, 1, 
                " MM", 
                "MS ", 
                "S  "
        );
    }
    protected static void dagger(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.DAGGER_ITEMS, 2, 
                " M", 
                "S "
        );
    }
    protected static void lance(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.LANCE_ITEMS, 3, 
                "  M", 
                "MM ", 
                "SM "
        );
    }
    protected static void javelin(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.JAVELIN_ITEMS, 4, 
                "  M", 
                " S ", 
                "S  "
        );
    }
    protected static void mace(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.MACE_ITEMS, 5, 
                " MM", 
                " MM", 
                "S  "
        );
    }
    protected static void hammer(RecipeOutput recipeOutput, IExtendedTier tier) {
        weapon(recipeOutput, tier, CMItemConfig.HAMMER_ITEMS, 6,
                " M ",
                "MMM",
                "SM "
        );
    }
    
    private static Ingredient getMaterial(IExtendedTier tier) {
        return switch (tier) {
            case CMExtendedToolTiers.WOOD -> Ingredient.of(ItemTags.PLANKS);
            case CMExtendedToolTiers.STONE -> Ingredient.of(ItemTags.STONE_TOOL_MATERIALS);
            case CMExtendedToolTiers.IRON -> Ingredient.of(Items.IRON_INGOT);
            case CMExtendedToolTiers.GOLD -> Ingredient.of(Items.GOLD_INGOT);
            case CMExtendedToolTiers.DIAMOND -> Ingredient.of(Items.DIAMOND);
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
