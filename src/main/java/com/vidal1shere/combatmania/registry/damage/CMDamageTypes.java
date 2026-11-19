package com.vidal1shere.combatmania.registry.damage;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class CMDamageTypes {
    public static final ResourceKey<DamageType> FLURRY = create("flurry");

    public static void bootstrap(BootstrapContext<DamageType> ctx) {
        registerAttack(ctx, FLURRY);
    }

    private static void registerAttack(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageKey) {
        register(context, damageKey, new DamageType(damageKey.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.1F));
    }

    private static void register(BootstrapContext<DamageType> context, ResourceKey<DamageType> damageKey, DamageType type) {
        context.register(damageKey, type);
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, CMUtil.modResLoc(name));
    }
}
