package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.strikeapi.api.attack.Attack;
import com.vidal1shere.strikeapi.api.attack.AttackTags;
import com.vidal1shere.strikeapi.api.datagen.DatapackTagsProvider;
import com.vidal1shere.strikeapi.registry.StrikeRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CMAttackTagsProvider extends DatapackTagsProvider<Attack> {
    protected CMAttackTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, StrikeRegistries.ATTACK_KEY, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(AttackTags.MELEE).add(
            id("melee/blunt/blunt"),
            id("melee/blunt/tier/wooden_blunt"),
            id("melee/blunt/tier/stone_blunt"),
            id("melee/blunt/tier/golden_blunt"),
            id("melee/blunt/tier/iron_blunt"),
            id("melee/blunt/tier/diamond_blunt"),
            id("melee/backstab"),
            id("melee/short_sweep"),
            id("melee/strong_overhead"),
            id("melee/uppercut"),
            id("melee/wide_sweep")
        );

        tag(AttackTags.PIERCE).add(
            id("pierce/short_pierce"),
            id("pierce/weak_pierce")
        );

        tag(AttackTags.SWEEPING).add(
            id("area/heavy_sweep"),
            id("area/spinning_sweep")
        );

        tag(AttackTags.SMASHING).add(
            id("melee/blunt/blunt"),
            id("melee/blunt/tier/wooden_blunt"),
            id("melee/blunt/tier/stone_blunt"),
            id("melee/blunt/tier/golden_blunt"),
            id("melee/blunt/tier/iron_blunt"),
            id("melee/blunt/tier/diamond_blunt"),
            id("shockwave/shockwave"),
            id("shockwave/heavy_shockwave"),
            id("shockwave/tier/wooden_shockwave"),
            id("shockwave/tier/stone_shockwave"),
            id("shockwave/tier/golden_shockwave"),
            id("shockwave/tier/iron_shockwave"),
            id("shockwave/tier/iron_heavy_shockwave"),
            id("shockwave/tier/diamond_shockwave"),
            id("shockwave/tier/diamond_heavy_shockwave")
        );

        tag(AttackTags.KINETIC).add(
            id("kinetic/wooden_kinetic"),
            id("kinetic/stone_kinetic"),
            id("kinetic/golden_kinetic"),
            id("kinetic/iron_kinetic"),
            id("kinetic/diamond_kinetic")
        );
    }

    private ResourceKey<Attack> id(String name) {
        return ResourceKey.create(StrikeRegistries.ATTACK_KEY, CMUtil.modResLoc(name));
    }
}
