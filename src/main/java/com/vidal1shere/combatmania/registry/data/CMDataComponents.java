package com.vidal1shere.combatmania.registry.data;

import com.mojang.serialization.Codec;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
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

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name,
                                                                                           UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
