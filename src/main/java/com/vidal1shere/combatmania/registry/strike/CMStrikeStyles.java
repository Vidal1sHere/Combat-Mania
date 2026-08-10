package com.vidal1shere.combatmania.registry.strike;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;
import com.vidal1shere.combatmania.item.DaggerItem;
import com.vidal1shere.combatmania.item.GlaiveItem;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.strike.StrikeProperties;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike.styles.*;
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
            STRIKE_STYLES.register("vanilla_sweep", VanillaSweepStrikeStyle::new);

    public static final DeferredHolder<StrikeStyle, SweepStrikeStyle> SWEEP =
            STRIKE_STYLES.register("sweep", () -> new SweepStrikeStyle(StrikeProperties.builder()
                    .minStrengthScale(0.05F)
                    .build()));

    public static final DeferredHolder<StrikeStyle, SweepStrikeStyle> DAGGER_SWEEP =
            STRIKE_STYLES.register("dagger_sweep", () -> new SweepStrikeStyle(StrikeProperties.builder()
                    .minStrengthScale(0.5F)
                    .set(SweepStrikeStyle.CAN_CRIT, false)
                    .set(SweepStrikeStyle.CAN_SPRINT_KNOCKBACK, false)
                    .set(SweepStrikeStyle.SWEEP_PARTICLE, DaggerItem::sweepParticle)
                    .overlap(false)
                    .build()));

    public static final DeferredHolder<StrikeStyle, SweepStrikeStyle> GLAIVE_SWEEP =
            STRIKE_STYLES.register("glaive_sweep", () -> new SweepStrikeStyle(StrikeProperties.builder()
                    .minStrengthScale(0.25F)
                    .swingHand(false)
                    .overlap(false)
                    .set(SweepStrikeStyle.SWEEP_PARTICLE, GlaiveItem::sweepParticle)
                    .set(SweepStrikeStyle.ANIMATION_FUNCTION, GlaiveItem::glaiveAnimation)
                    .build()));

    public static final DeferredHolder<StrikeStyle, CleaveStrikeStyle> CLEAVE =
            STRIKE_STYLES.register("cleave", () -> new CleaveStrikeStyle(StrikeProperties.builder()
                    .minStrengthScale(0.05F)
                    .overlap(true)
                    .build()));
    
    public static final DeferredHolder<StrikeStyle, JabStrikeStyle> JAB =
            STRIKE_STYLES.register("jab", () -> new JabStrikeStyle(StrikeProperties.builder()
                    .speedAttribute(CMAttributes.JAB_SPEED)
                    .swingHand(false)
                    .build()));

    public static final DeferredHolder<StrikeStyle, StabStrikeStyle> STAB =
            STRIKE_STYLES.register("stab", () -> new StabStrikeStyle(StrikeProperties.builder()
                    .speedAttribute(CMAttributes.STAB_SPEED)
                    .swingHand(false)
                    .overlap(false)
                    .minStrengthScale(0.5F)
                    .build()));

    public static final DeferredHolder<StrikeStyle, JabStrikeStyle> SPEAR_JAB =
            STRIKE_STYLES.register("spear_jab", () -> new JabStrikeStyle(StrikeProperties.builder()
                    .speedAttribute(CMAttributes.JAB_SPEED)
                    .swingHand(false)
                    .overlap(true)
                    .build()));

    public static final DeferredHolder<StrikeStyle, JabStrikeStyle> TRIDENT_JAB =
            STRIKE_STYLES.register("trident_jab", () -> new JabStrikeStyle(StrikeProperties.builder()
                    .speedAttribute(CMAttributes.JAB_SPEED)
                    .swingHand(false)
                    .set(JabStrikeStyle.ANIMATION_FUNCTION, (i, h, p, s) -> 
                            SpearAnimations.tridentJab())
                    .build()));

    public static final DeferredHolder<StrikeStyle, JoustStrikeStyle> JOUST =
            STRIKE_STYLES.register("joust", () -> new JoustStrikeStyle(StrikeProperties.builder()
                    .speedAttribute(CMAttributes.JOUST_SPEED)
                    .minStrengthScale(0.1f)
                    .swingHand(false)
                    .lowerHand(true)
                    .overlap(true)
                    .build()));

    public static void register(IEventBus eventBus) {
        STRIKE_STYLES.register(eventBus);
    }
}