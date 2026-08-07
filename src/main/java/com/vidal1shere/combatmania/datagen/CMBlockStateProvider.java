package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class CMBlockStateProvider extends BlockStateProvider {
    public CMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CombatMania.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        
    }
    
    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile(CombatMania.MOD_ID + ":block/" + deferredBlock.getId().getPath()));
    }

    private void axisBlockWithItem(DeferredBlock<?> deferredBlock, ResourceLocation top, ResourceLocation side, ResourceLocation bottom) {
        ModelFile model = models().cubeBottomTop(
                deferredBlock.getId().getPath(),
                side.withPrefix("block/"),
                bottom.withPrefix("block/"),
                top.withPrefix("block/")
        );

        simpleBlock(deferredBlock.get(), model);
        simpleBlockItem(deferredBlock.get(), model);
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile(CombatMania.MOD_ID + ":block/" + deferredBlock.getId().getPath() + appendix));
    }

    private void blockLamp(DeferredBlock<?> deferredBlock) {
        getVariantBuilder(deferredBlock.get()).forAllStates(state -> {
            if((Boolean) state.getValue(BlockStateProperties.LIT)) {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll(deferredBlock.getId().getPath() + "_on",
                        CMUtil.modResLoc("block/" + deferredBlock.getId().getPath() + "_on")))};
            } else {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll(deferredBlock.getId().getPath() + "_off",
                        CMUtil.modResLoc("block/" + deferredBlock.getId().getPath() + "_off")))};
            }
        });

        simpleBlockItem(deferredBlock.get(), models().cubeAll(deferredBlock.getId().getPath() + "_off",
                CMUtil.modResLoc("block/" + deferredBlock.getId().getPath() + "_off")));
    }
}
