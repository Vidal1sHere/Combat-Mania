package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.network.StrikePayload;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataMaps;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CMEventSetup {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeModificationEvent event) {
        CMAttributes.registerAttributes(event);
    }

    @SubscribeEvent
    public static void registerDataMap(RegisterDataMapTypesEvent event) {
        CMDataMaps.register(event);
    }
    
    @SubscribeEvent
    public static void modifyItemAttributes(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        
        if (stack.is(Items.TRIDENT)) {
            if (!CMCommonConfig.MODIFY_TRIDENT.get()) {
                return;
            }
            
            event.removeModifier(Attributes.ATTACK_SPEED, Item.BASE_ATTACK_SPEED_ID);
            event.addModifier(
                    CMAttributes.JAB_SPEED,
                    new AttributeModifier(
                            CMAttributes.BASE_JAB_SPEED_ID,
                            -2.95F,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.MAINHAND
            );
        }
    }

    @SubscribeEvent
    public static void registerPayload(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                StrikePayload.Request.TYPE,
                StrikePayload.Request.STREAM_CODEC,
                StrikePayload.Request::handle
        );

        registrar.playToClient(
                StrikePayload.Confirm.TYPE,
                StrikePayload.Confirm.STREAM_CODEC,
                StrikePayload.Confirm::handle
        );
    }

    @SubscribeEvent
    public static void registerNewRegistry(NewRegistryEvent event) {
        event.register(CMStrikeStyles.REGISTRY);
    }
}
