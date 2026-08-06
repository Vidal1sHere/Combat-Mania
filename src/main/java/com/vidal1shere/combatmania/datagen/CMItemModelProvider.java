package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.CMUtil;
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
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.loaders.SeparateTransformsModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.LinkedHashMap;

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
        spearItem(CMItems.WOODEN_SPEAR);
        spearItem(CMItems.STONE_SPEAR);
        spearItem(CMItems.GOLDEN_SPEAR);
        spearItem(CMItems.IRON_SPEAR);
        spearItem(CMItems.DIAMOND_SPEAR);
        spearItem(CMItems.NETHERITE_SPEAR);
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

    private void spearItem(DeferredItem<?> item) {
        String itemName = item.getId().getPath();
        ResourceLocation spearTexture = CMUtil.modResLoc("item/" + itemName);
        ResourceLocation spearInHandBase = CMUtil.modResLoc("item/spear_in_hand");

        ItemModelBuilder onHandModel = this.withExistingParent(itemName + "_on_hand", spearInHandBase)
                .texture("layer0", CMUtil.modResLoc("item/" + itemName + "_in_hand"));

        this.withExistingParent(itemName, mcLoc("item/handheld"))
                .customLoader(SeparateTransformsModelBuilder::begin)
                .base(onHandModel)
                .perspective(ItemDisplayContext.GUI,
                        this.withExistingParent(itemName + "_gui", mcLoc("item/generated"))
                                .texture("layer0", spearTexture)
                )
                .perspective(ItemDisplayContext.GROUND,
                        this.withExistingParent(itemName + "_ground", mcLoc("item/generated"))
                                .texture("layer0", spearTexture)
                )
                .perspective(ItemDisplayContext.FIXED,
                        this.withExistingParent(itemName + "_fixed", mcLoc("item/generated"))
                                .texture("layer0", spearTexture)
                )
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
}
