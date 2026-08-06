package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public class CMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, CombatMania.MOD_ID);

    public static final ResourceLocation BASE_JAB_SPEED_ID = CMUtil.modResLoc("base_jab_speed");

    public static final DeferredHolder<Attribute, Attribute> JAB_SPEED =
            ATTRIBUTES.register("jab_speed",
                    () -> new NamedRangedAttribute(
                            BASE_JAB_SPEED_ID,
                            "attribute.name.combat_mania.jab_speed",
                            4.0,
                            0.0,
                            1024.0
                    ).setSyncable(true));
    
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, JAB_SPEED);
    }
    
    public static class NamedRangedAttribute extends RangedAttribute {
        private final ResourceLocation baseId;

        public NamedRangedAttribute(ResourceLocation baseId, String descriptionId, double defaultValue, double min, double max) {
            super(descriptionId, defaultValue, min, max);
            this.baseId = baseId;
        }

        @Override
        public @Nullable ResourceLocation getBaseId() {
            return baseId;
        }
    }
}
