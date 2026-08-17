package com.vidal1shere.combatmania.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;
import java.util.function.Consumer;

public class CMItemModelProvider extends ItemModelProvider {
    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }
    
    public CMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CombatMania.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheldDagger(CMItems.WOODEN_DAGGER);
        handheldDagger(CMItems.STONE_DAGGER);
        handheldDagger(CMItems.GOLDEN_DAGGER);
        handheldDagger(CMItems.IRON_DAGGER);
        handheldDagger(CMItems.DIAMOND_DAGGER);
        
        spearItem(CMItems.WOODEN_GLAIVE);
        spearItem(CMItems.STONE_GLAIVE);
        spearItem(CMItems.GOLDEN_GLAIVE);
        spearItem(CMItems.IRON_GLAIVE);
        spearItem(CMItems.DIAMOND_GLAIVE);

        lanceItem(CMItems.WOODEN_LANCE);
        lanceItem(CMItems.STONE_LANCE);
        lanceItem(CMItems.GOLDEN_LANCE);
        lanceItem(CMItems.IRON_LANCE);
        lanceItem(CMItems.DIAMOND_LANCE);
        
        spearItem(CMItems.WOODEN_SPEAR);
        spearItem(CMItems.STONE_SPEAR);
        spearItem(CMItems.GOLDEN_SPEAR);
        spearItem(CMItems.IRON_SPEAR);
        spearItem(CMItems.DIAMOND_SPEAR);
        spearItem(CMItems.NETHERITE_SPEAR);

        javelinItem(CMItems.WOODEN_JAVELIN);
        javelinItem(CMItems.STONE_JAVELIN);
        javelinItem(CMItems.GOLDEN_JAVELIN);
        javelinItem(CMItems.IRON_JAVELIN);
        javelinItem(CMItems.DIAMOND_JAVELIN);
        
        quiverItem(CMItems.LEATHER_QUIVER);
        quiverItem(CMItems.FLAMING_QUIVER);
    }

    private void trimmedArmorItem(DeferredItem<ArmorItem> itemDeferredItem) {
        if (itemDeferredItem.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = armorItem.toString();
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = ResourceLocation.parse(armorItemPath);
                ResourceLocation trimResLoc = ResourceLocation.parse(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = ResourceLocation.parse(currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemDeferredItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc.getNamespace()  + ":item/" + trimNameResLoc.getPath()))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0", CMUtil.modResLoc("item/" + itemDeferredItem.getId().getPath()));
            });
        }
    }

    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture", CMUtil.modResLoc("block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", CMUtil.modResLoc("block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", CMUtil.modResLoc("block/" + baseBlock.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/handheld")).texture("layer0", CMUtil.modResLoc("item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldDagger(DeferredItem<?> item) {
        return withExistingParent(item.getId().getPath(),
                CMUtil.modResLoc("item/handheld_dagger"))
                .texture("layer0", CMUtil.modResLoc("item/" + item.getId().getPath()));
    }

    private void spearItem(DeferredItem<?> item) {
        handheldTransformedItem(item, "handheld_spear");
    }

    private void lanceItem(DeferredItem<?> item) {
        handheldTransformedItem(item, "handheld_lance");
    }

    private void javelinItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        ResourceLocation texture = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation inHandTexture = CMUtil.modResLoc("item/" + itemName + "_in_hand");
        ResourceLocation inHandBase = CMUtil.modResLoc("item/handheld_spear");
        ResourceLocation throwingBase = CMUtil.modResLoc("item/spear_throwing");

        this.withExistingParent(itemName + "_in_hand", inHandBase).texture("layer0", inHandTexture);

        this.getBuilder(itemName).customLoader((parent, existingFileHelper) ->
                new InlineSeparateTransformsModelBuilder(parent, existingFileHelper, inHandBase, inHandTexture, texture, json -> {
                    JsonArray overrides = new JsonArray();
                    JsonObject override = new JsonObject();
                    JsonObject predicate = new JsonObject();

                    predicate.addProperty("minecraft:throwing", 1);
                    override.add("predicate", predicate);
                    override.addProperty("model", "combat_mania:item/" + itemName + "_throwing");

                    overrides.add(override);
                    json.add("overrides", overrides);
                })
        );

        this.getBuilder(itemName + "_throwing").customLoader((parent, existingFileHelper) ->
                new InlineSeparateTransformsModelBuilder(parent, existingFileHelper, throwingBase, inHandTexture, texture)
        );
    }

    private void handheldTransformedItem(DeferredItem<?> item, String handHeldName) {
        handheldTransformedItem(item, handHeldName, j -> {});
    }

    private void handheldTransformedItem(DeferredItem<?> item, String handHeldName, Consumer<JsonObject> mutator) {
        String itemName = item.getId().getPath();
        ResourceLocation texture = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation inHandTexture = CMUtil.modResLoc("item/" + itemName + "_in_hand");
        ResourceLocation inHandBase = CMUtil.modResLoc("item/" + handHeldName);

        this.withExistingParent(itemName + "_in_hand", inHandBase)
                .texture("layer0", inHandTexture);

        this.getBuilder(itemName)
                .customLoader((parent, existingFileHelper) ->
                        new InlineSeparateTransformsModelBuilder(
                                parent,
                                existingFileHelper,
                                inHandBase,
                                inHandTexture,
                                texture,
                                mutator
                        )
                );
    }
    
    private void quiverItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        ResourceLocation quiver = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation quiver_0 = CMUtil.modResLoc("item/" + itemName + "_filled_0");
        ResourceLocation quiver_1 = CMUtil.modResLoc("item/" + itemName + "_filled_1");
        ResourceLocation quiver_2 = CMUtil.modResLoc("item/" + itemName + "_filled_2");

        this.withExistingParent(itemName, mcLoc("item/generated"))
                .texture("layer0", quiver)
                .override()
                .predicate(CMUtil.modResLoc("filled"), 1)
                .model(new ModelFile.UncheckedModelFile(quiver_0))
                .end()
                .override()
                .predicate(CMUtil.modResLoc("filled"), 2)
                .model(new ModelFile.UncheckedModelFile(quiver_1))
                .end()
                .override()
                .predicate(CMUtil.modResLoc("filled"), 3)
                .model(new ModelFile.UncheckedModelFile(quiver_2))
                .end();

        this.withExistingParent(itemName + "_filled_0", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(quiver))
                .texture("layer0", quiver_0);
        this.withExistingParent(itemName + "_filled_1", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(quiver))
                .texture("layer0", quiver_1);
        this.withExistingParent(itemName + "_filled_2", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(quiver))
                .texture("layer0", quiver_2);
    }

    private void bowItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        ResourceLocation bow = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation bow_0 = CMUtil.modResLoc("item/" + itemName + "_pulling_0");
        ResourceLocation bow_1 = CMUtil.modResLoc("item/" + itemName + "_pulling_1");
        ResourceLocation bow_2 = CMUtil.modResLoc("item/" + itemName + "_pulling_2");

        this.withExistingParent(itemName, mcLoc("item/generated"))
                .texture("layer0", bow)
                .transforms()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(-80, 260, -40)
                .translation(-1, -2, 2.5f)
                .scale(0.9f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                .rotation(-80, -280, 40)
                .translation(-1, -2, 2.5f)
                .scale(0.9f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -90, 25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                .rotation(0, 90, -25)
                .translation(1.13f, 3.2f, 1.13f)
                .scale(0.68f)
                .end()
                .end()
                .override()
                .predicate(mcLoc("pulling"), 1)
                .model(new ModelFile.UncheckedModelFile(bow_0))
                .end()
                .override()
                .predicate(mcLoc("pulling"), 1)
                .predicate(mcLoc("pull"), 0.65f)
                .model(new ModelFile.UncheckedModelFile(bow_1))
                .end()
                .override()
                .predicate(mcLoc("pulling"), 1)
                .predicate(mcLoc("pull"), 0.9f)
                .model(new ModelFile.UncheckedModelFile(bow_2))
                .end();

        this.withExistingParent(itemName + "_pulling_0", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(bow))
                .texture("layer0", bow_0);
        this.withExistingParent(itemName + "_pulling_1", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(bow))
                .texture("layer0", bow_1);
        this.withExistingParent(itemName + "_pulling_2", mcLoc("item/generated"))
                .parent(new ModelFile.UncheckedModelFile(bow))
                .texture("layer0", bow_2);
    }

    private static class InlineSeparateTransformsModelBuilder extends CustomLoaderBuilder<ItemModelBuilder> {

        private final ResourceLocation inHandBase;
        private final ResourceLocation inHandTexture;
        private final ResourceLocation itemTexture;
        private Consumer<JsonObject> overrides = j -> {};

        private InlineSeparateTransformsModelBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper, 
                                                     ResourceLocation inHandBase, ResourceLocation inHandTexture, 
                                                     ResourceLocation itemTexture) {
            super(CMUtil.neoforgeResLoc("separate_transforms"), parent, existingFileHelper, true);

            this.inHandBase = inHandBase;
            this.inHandTexture = inHandTexture;
            this.itemTexture = itemTexture;
        }

        private InlineSeparateTransformsModelBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper,
                                                     ResourceLocation inHandBase, ResourceLocation inHandTexture, 
                                                     ResourceLocation itemTexture, Consumer<JsonObject> overrides) {
            super(CMUtil.neoforgeResLoc("separate_transforms"), parent, existingFileHelper, true);

            this.inHandBase = inHandBase;
            this.inHandTexture = inHandTexture;
            this.itemTexture = itemTexture;
            this.overrides = overrides;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            json.addProperty("parent", "minecraft:item/handheld");
            json.add("base", createInHandModel());
            json.addProperty("loader", "neoforge:separate_transforms");

            JsonObject perspectives = new JsonObject();
            perspectives.add("fixed", createGeneratedModel());
            perspectives.add("ground", createGeneratedModel());
            perspectives.add("gui", createGeneratedModel());

            json.add("perspectives", perspectives);
            
            overrides.accept(json);

            return json;
        }

        private JsonObject createInHandModel() {
            JsonObject model = new JsonObject();
            model.addProperty("parent", inHandBase.toString());

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", inHandTexture.toString());

            model.add("textures", textures);

            return model;
        }

        private JsonObject createGeneratedModel() {
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:item/generated");

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", itemTexture.toString());

            model.add("textures", textures);

            return model;
        }
    }
}
