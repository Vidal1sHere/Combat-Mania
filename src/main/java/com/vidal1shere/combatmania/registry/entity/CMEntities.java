package com.vidal1shere.combatmania.registry.entity;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.entity.FlamingArrow;
import com.vidal1shere.combatmania.entity.ThrownJavelin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CMEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, CombatMania.MOD_ID);

    public static final Supplier<EntityType<ThrownJavelin>> THROWN_JAVELIN =
            ENTITY_TYPES.register("thrown_javelin", () -> EntityType.Builder.<ThrownJavelin>of(ThrownJavelin::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("thrown_javelin"));

    public static final Supplier<EntityType<FlamingArrow>> FLAMING_ARROW =
            ENTITY_TYPES.register("flaming_arrow", () -> EntityType.Builder.<FlamingArrow>of(FlamingArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("flaming_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
