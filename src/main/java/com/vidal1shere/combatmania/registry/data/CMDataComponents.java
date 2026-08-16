package com.vidal1shere.combatmania.registry.data;

import com.mojang.serialization.Codec;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.data.component.SmashProperties;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CMDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, CombatMania.MOD_ID);

    public static final Supplier<DataComponentType<Integer>> ACTIVE_STRIKE_STYLE =
            register("active_strike_style", b -> b
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Integer>> LAST_USED_STRIKE_STYLE =
            register("last_used_strike_style", b -> b
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<JoustProperties>> JOUST =
            register("joust", b -> b
                    .persistent(JoustProperties.CODEC)
                    .networkSynchronized(JoustProperties.STREAM_CODEC)
            );

    public static final Supplier<DataComponentType<SmashProperties>> SMASH =
            register("smash", b -> b
                    .persistent(SmashProperties.CODEC)
                    .networkSynchronized(SmashProperties.STREAM_CODEC)
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<EquipmentSlot>> EQUIPPED_SLOT =
            register("equipped_slot", b -> b
                    .persistent(EquipmentSlot.CODEC)
                    .networkSynchronized(
                            ByteBufCodecs.STRING_UTF8.map(
                                    EquipmentSlot::byName,
                                    EquipmentSlot::getName
                            )
                    )
            );

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<QuiverContents>> QUIVER_CONTENTS =
            register("quiver_contents", b -> b
                    .persistent(QuiverContents.CODEC)
                    .networkSynchronized(QuiverContents.STREAM_CODEC)
            );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
