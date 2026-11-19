package com.vidal1shere.combatmania.datagen;

import com.google.gson.JsonObject;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.item.CMItemDisplayContexts;
import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.client.renderer.block.model.BlockModel;
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
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.annotation.Nullable;
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
        daggerItem(CMItems.WOODEN_DAGGER);
        daggerItem(CMItems.STONE_DAGGER);
        daggerItem(CMItems.GOLDEN_DAGGER);
        daggerItem(CMItems.IRON_DAGGER);
        daggerItem(CMItems.DIAMOND_DAGGER);

        separateTransformsItem(CMItems.WOODEN_GLAIVE, "glaive");
        separateTransformsItem(CMItems.STONE_GLAIVE, "glaive");
        separateTransformsItem(CMItems.GOLDEN_GLAIVE, "glaive");
        separateTransformsItem(CMItems.IRON_GLAIVE, "glaive");
        separateTransformsItem(CMItems.DIAMOND_GLAIVE, "glaive");

        separateTransformsItem(CMItems.WOODEN_GREATSWORD, "greatsword");
        separateTransformsItem(CMItems.STONE_GREATSWORD, "greatsword");
        separateTransformsItem(CMItems.GOLDEN_GREATSWORD, "greatsword");
        separateTransformsItem(CMItems.IRON_GREATSWORD, "greatsword");
        separateTransformsItem(CMItems.DIAMOND_GREATSWORD, "greatsword");

        separateTransformsItem(CMItems.WOODEN_KATANA, "katana");
        separateTransformsItem(CMItems.STONE_KATANA, "katana");
        separateTransformsItem(CMItems.GOLDEN_KATANA, "katana");
        separateTransformsItem(CMItems.IRON_KATANA, "katana");
        separateTransformsItem(CMItems.DIAMOND_KATANA, "katana");

        separateTransformsItem(CMItems.WOODEN_LANCE, "lance");
        separateTransformsItem(CMItems.STONE_LANCE, "lance");
        separateTransformsItem(CMItems.GOLDEN_LANCE, "lance");
        separateTransformsItem(CMItems.IRON_LANCE, "lance");
        separateTransformsItem(CMItems.DIAMOND_LANCE, "lance");

        separateTransformsItem(CMItems.WOODEN_RAPIER, "item_32");
        separateTransformsItem(CMItems.STONE_RAPIER, "item_32");
        separateTransformsItem(CMItems.GOLDEN_RAPIER, "item_32");
        separateTransformsItem(CMItems.IRON_RAPIER, "item_32");
        separateTransformsItem(CMItems.DIAMOND_RAPIER, "item_32");

        separateTransformsItem(CMItems.WOODEN_SPEAR, "spear");
        separateTransformsItem(CMItems.STONE_SPEAR, "spear");
        separateTransformsItem(CMItems.GOLDEN_SPEAR, "spear");
        separateTransformsItem(CMItems.IRON_SPEAR, "spear");
        separateTransformsItem(CMItems.DIAMOND_SPEAR, "spear");
        separateTransformsItem(CMItems.NETHERITE_SPEAR, "spear");

        javelinItem(CMItems.WOODEN_JAVELIN);
        javelinItem(CMItems.STONE_JAVELIN);
        javelinItem(CMItems.GOLDEN_JAVELIN);
        javelinItem(CMItems.IRON_JAVELIN);
        javelinItem(CMItems.DIAMOND_JAVELIN);
    }

    public ItemModelBuilder basicItem(DeferredItem<?> item) {
        return this.basicItem(item.get());
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

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc.getNamespace() + ":item/" + armorItemResLoc.getPath())
                        .texture("layer1", trimResLoc);

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

    private void daggerItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        withExistingParent(itemName, CMUtil.modResLoc("item/handheld/dagger"))
                .texture("layer0", CMUtil.modResLoc("item/" + itemName));
    }

    private void separateTransformsItem(DeferredItem<?> item, String handheld) {
        String itemName = item.getId().getPath();
        separateTransformsModel(itemName, itemName, handheld, null);
    }

    private ItemModelBuilder separateTransformsModel(String path, String itemName, String handheld, @Nullable String thrown) {
        ResourceLocation texture = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation inHandTexture = CMUtil.modResLoc("item/" + itemName + "_in_hand");
        ResourceLocation inHandBase = CMUtil.modResLoc("item/handheld/" + handheld);
        return getBuilder(path)
                .guiLight(BlockModel.GuiLight.FRONT)
                .texture("particle", texture)
                .customLoader((parent, existingFileHelper) ->
                        new InlineSeparateTransformsModelBuilder(parent, existingFileHelper, inHandBase, inHandTexture, texture,
                                thrown != null ? CMUtil.modResLoc("item/handheld/" + thrown) : null))
                .end();
    }

    private void javelinItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        separateTransformsModel("item/javelin/" + itemName + "_throwing", itemName, "spear_throwing", "javelin_thrown");
        separateTransformsModel(itemName, itemName, "spear", "javelin_thrown")
                .override()
                .predicate(mcLoc("throwing"), 1)
                .model(new ModelFile.UncheckedModelFile(CMUtil.modResLoc("item/javelin/" + itemName + "_throwing")))
                .end();
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
        @Nullable
        private final ResourceLocation thrownBase;
        private Consumer<JsonObject> overrides = j -> {};

        private InlineSeparateTransformsModelBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper, 
                                                     ResourceLocation inHandBase, ResourceLocation inHandTexture, 
                                                     ResourceLocation itemTexture, @Nullable ResourceLocation thrownBase) {
            super(CMUtil.neoforgeResLoc("separate_transforms"), parent, existingFileHelper, true);

            this.inHandBase = inHandBase;
            this.inHandTexture = inHandTexture;
            this.itemTexture = itemTexture;
            this.thrownBase = thrownBase;
        }

        private InlineSeparateTransformsModelBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper,
                                                     ResourceLocation inHandBase, ResourceLocation inHandTexture, 
                                                     ResourceLocation itemTexture, Consumer<JsonObject> overrides) {
            super(CMUtil.neoforgeResLoc("separate_transforms"), parent, existingFileHelper, true);

            this.inHandBase = inHandBase;
            this.inHandTexture = inHandTexture;
            this.itemTexture = itemTexture;
            this.thrownBase = null;
            this.overrides = overrides;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            json.add("base", createInHandModel());
            json.addProperty("loader", "neoforge:separate_transforms");

            JsonObject perspectives = new JsonObject();
            perspectives.add("fixed", createGeneratedModel());
            perspectives.add("ground", createGeneratedModel());
            perspectives.add("gui", createGeneratedModel());
            if (thrownBase != null) {
                perspectives.add(CMItemDisplayContexts.THROWN.getSerializedName(), createModel(thrownBase, inHandTexture));
            }

            json.add("perspectives", perspectives);
            
            overrides.accept(json);

            return json;
        }

        private JsonObject createInHandModel() {
            return createModel(inHandBase, inHandTexture);
        }

        private static JsonObject createModel(ResourceLocation parent, ResourceLocation texture) {
            JsonObject model = new JsonObject();
            model.addProperty("parent", parent.toString());

            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", texture.toString());

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
