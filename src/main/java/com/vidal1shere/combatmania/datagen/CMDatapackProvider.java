package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CMDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
//            .add(Registries.ENCHANTMENT, COTNEnchantments::bootstrap)
//            .add(Registries.CONFIGURED_FEATURE, COTNConfiguredFeatures::bootstrap)
//            .add(Registries.PLACED_FEATURE, COTNPlacedFeatures::bootstrap)
//            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, COTNBiomeModifiers::bootstrap)
//            .add(Registries.JUKEBOX_SONG, COTNJukeboxSongs::bootstrap)
//            .add(Registries.DAMAGE_TYPE, CMDamageTypes::bootstrap);
              ;
    
    public CMDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(CombatMania.MOD_ID));
    }
    
    
}
