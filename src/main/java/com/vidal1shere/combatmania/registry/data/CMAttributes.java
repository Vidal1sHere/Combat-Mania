package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.registry.item.CMExtendedToolTiers;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

public class CMAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = 
            DeferredRegister.create(Registries.ATTRIBUTE, CombatMania.MOD_ID);

    // ------ Custom Attributes
    public static final ResourceLocation BASE_ENTITY_INTERACTION_RANGE_ID = CMUtil.modResLoc("base_entity_interaction_range");
    public static final ResourceLocation BASE_JAB_SPEED_ID = CMUtil.modResLoc("base_jab_speed");
    public static final ResourceLocation BASE_STAB_SPEED_ID = CMUtil.modResLoc("base_stab_speed");
    public static final ResourceLocation BASE_JOUST_SPEED_ID = CMUtil.modResLoc("base_joust_speed");
    public static final ResourceLocation BASE_JOUST_DAMAGE_MULTIPLIER_ID = CMUtil.modResLoc("base_joust_multiplier");
    public static final ResourceLocation BASE_MAX_JOUST_DAMAGE_ID = CMUtil.modResLoc("base_max_joust_damage");
    public static final ResourceLocation BASE_SHOOT_POWER_ID = CMUtil.modResLoc("shoot_power");
    public static final ResourceLocation BASE_ATTACK_STAGGER_ID = CMUtil.modResLoc("base_attack_stagger");
    public static final ResourceLocation BASE_STAGGER_RESISTANCE_ID = CMUtil.modResLoc("base_attack_stagger");

    // ------ Modifier Keys
    public static final ResourceLocation STAGGER_MOVEMENT_SPEED_MODIFIER = CMUtil.modResLoc("stagger_movement_speed");

    public static final DeferredHolder<Attribute, Attribute> JAB_SPEED =
            registerAttribute("jab_speed", 4.0, 0.0, 1024.0);

    public static final DeferredHolder<Attribute, Attribute> STAB_SPEED =
            registerAttribute("stab_speed", 4.0, 0.0, 1024.0);

    public static final DeferredHolder<Attribute, Attribute> JOUST_SPEED =
            registerAttribute("joust_speed", 4.0, 0.0, 1024.0);

    public static final DeferredHolder<Attribute, Attribute> JOUST_DAMAGE_MULTIPLIER =
            registerAttribute("joust_multiplier",0.0, 0.0, 50.0);

    public static final DeferredHolder<Attribute, Attribute> MAX_JOUST_DAMAGE = 
            registerAttribute("max_joust_damage", 0.0, 0.0, 1024.0);

    public static final DeferredHolder<Attribute, Attribute> SHOOT_POWER = 
            registerAttribute("shoot_power", 1.0, 0.0, 1024.0);

    public static final DeferredHolder<Attribute, Attribute> ATTACK_STAGGER = 
            registerAttribute("attack_stagger", 0.0, 0.0, 5.0);

    public static final DeferredHolder<Attribute, Attribute> STAGGER_RESISTANCE =
            registerAttribute("stagger_resistance", 0.0, 0.0, 1.0);
    
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
    
    public static void registerAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, JAB_SPEED);
        event.add(EntityType.PLAYER, STAB_SPEED);
        event.add(EntityType.PLAYER, JOUST_SPEED);
        event.add(EntityType.PLAYER, JOUST_DAMAGE_MULTIPLIER);
        event.add(EntityType.PLAYER, MAX_JOUST_DAMAGE);
        event.add(EntityType.PLAYER, SHOOT_POWER);
        
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, ATTACK_STAGGER);
            event.add(type, STAGGER_RESISTANCE);
        }
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
    
    public static void modifyItemAttributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (CMCommonConfig.MODIFY_AXES.get() && stack.getItem() instanceof AxeItem item && itemId.getNamespace().equals("minecraft")) {
            IExtendedTier tier = CMExtendedToolTiers.getVanillaTier(item.getTier());
            if (tier == null) return;
            
            event.addModifier(
                    CMAttributes.ATTACK_STAGGER,
                    new AttributeModifier(
                            BASE_ATTACK_STAGGER_ID,
                            tier.getAttackStagger(),
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            );
        }
        
        if (CMCommonConfig.MODIFY_MACE.get() && stack.is(Items.MACE)) {
            event.addModifier(
                    CMAttributes.ATTACK_STAGGER,
                    new AttributeModifier(
                            BASE_ATTACK_STAGGER_ID, 
                            VanillaModifiers.MACE_ATTACK_STAGGER, 
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            );
        }
        
        if (CMCommonConfig.MODIFY_TRIDENT.get() && stack.is(Items.TRIDENT)) {
            event.removeModifier(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID);
            event.addModifier(
                    CMAttributes.JAB_SPEED,
                    new AttributeModifier(
                            CMAttributes.BASE_JAB_SPEED_ID,
                            VanillaModifiers.TRIDENT_JAB_SPEED,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            );
        }
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

    public static class VanillaModifiers {
        public static final float TRIDENT_JAB_SPEED = -2.95f;
        public static final float MACE_ATTACK_STAGGER = 2.0f;
    }
}
