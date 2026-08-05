package com.vidal1shere.combatmania.registry.strike;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike_styles.CleaveStrikeStyle;
import com.vidal1shere.combatmania.strike_styles.SweepStrikeStyle;
import com.vidal1shere.combatmania.strike_styles.VanillaSweepStrikeStyle;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class CMStrikeStyles {

    public static final ResourceKey<Registry<StrikeStyle>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(CombatMania.MOD_ID, "strike_style"));

    public static final Registry<StrikeStyle> REGISTRY =
            new RegistryBuilder<>(REGISTRY_KEY).create();

    public static final DeferredRegister<StrikeStyle> STRIKE_STYLES =
            DeferredRegister.create(REGISTRY_KEY, CombatMania.MOD_ID);

    public static final DeferredHolder<StrikeStyle, VanillaSweepStrikeStyle> VANILLA_SWEEP =
            STRIKE_STYLES.register("vanill_sweep", VanillaSweepStrikeStyle::new);

    public static final DeferredHolder<StrikeStyle, SweepStrikeStyle> SWEEP =
            STRIKE_STYLES.register("sweep", SweepStrikeStyle::new);

    public static final DeferredHolder<StrikeStyle, CleaveStrikeStyle> CLEAVE =
            STRIKE_STYLES.register("cleave", CleaveStrikeStyle::new);

    public static void register(IEventBus eventBus) {
        STRIKE_STYLES.register(eventBus);
    }
}