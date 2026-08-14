package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.data.CMTags;
import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CMItemTagProvider extends ItemTagsProvider {
    public CMItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CombatMania.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(CMTags.Items.JAVELIN)
                .add(
                        CMItems.WOODEN_JAVELIN.get(),
                        CMItems.STONE_JAVELIN.get(),
                        CMItems.GOLDEN_JAVELIN.get(),
                        CMItems.IRON_JAVELIN.get(),
                        CMItems.DIAMOND_JAVELIN.get()
                );
        
        this.tag(ItemTags.TRIDENT_ENCHANTABLE)
                .addTag(CMTags.Items.JAVELIN);
                
    }
}
