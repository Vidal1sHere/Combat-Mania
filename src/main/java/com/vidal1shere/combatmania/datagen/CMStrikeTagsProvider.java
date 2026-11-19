package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.strikeapi.StrikeAPI;
import com.vidal1shere.strikeapi.api.attack.Attack;
import com.vidal1shere.strikeapi.api.datagen.DatapackTagsProvider;
import com.vidal1shere.strikeapi.api.strike.Strike;
import com.vidal1shere.strikeapi.registry.StrikeRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CMStrikeTagsProvider extends DatapackTagsProvider<Strike> {
    protected CMStrikeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, StrikeRegistries.STRIKE_KEY, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        
    }

    private ResourceKey<Attack> id(String name) {
        return ResourceKey.create(StrikeRegistries.ATTACK_KEY, StrikeAPI.id(name));
    }
}
