package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = 
            DeferredRegister.create(Registries.ATTRIBUTE, CombatMania.MOD_ID);

    // ------ Custom Attributes
    public static final ResourceLocation BASE_SHOOT_POWER_ID = CMUtil.modResLoc("shoot_power");

    // ------ Modifier Keys

    public static final DeferredHolder<Attribute, Attribute> SHOOT_POWER = 
            registerAttribute("shoot_power", 1.0, 0.0, 1024.0);
    
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, SHOOT_POWER);
    }
    
    private static DeferredHolder<Attribute, Attribute> registerAttribute(String id, double defaultValue, double min, double max) {
        return ATTRIBUTES.register(id,
                () -> new NamedRangedAttribute(
                        CMUtil.modResLoc("base_" + id),
                        "attribute.name.combat_mania." + id,
                        defaultValue,
                        min,
                        max
                ).setSyncable(true));
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
