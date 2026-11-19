package com.vidal1shere.combatmania.datagen;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.damage.CMDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CMDamageTypeTagProvider extends DamageTypeTagsProvider {
    public CMDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, CombatMania.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_COOLDOWN)
                .add(CMDamageTypes.FLURRY);

        tag(DamageTypeTags.IS_PLAYER_ATTACK)
                .add(CMDamageTypes.FLURRY);

        tag(DamageTypeTags.NO_KNOCKBACK)
                .add(CMDamageTypes.FLURRY);
    }
}
