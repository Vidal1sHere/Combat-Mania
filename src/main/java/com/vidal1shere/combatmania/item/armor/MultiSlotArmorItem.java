package com.vidal1shere.combatmania.item.armor;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.Set;

public abstract class MultiSlotArmorItem extends ArmorItem {
    public MultiSlotArmorItem(Holder<ArmorMaterial> material, Type startingType, Properties properties) {
        super(material, startingType, properties);
    }

    public abstract Set<EquipmentSlot> getAllowedSlots();

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return stack.getOrDefault(CMDataComponents.EQUIPPED_SLOT, getDefaultSlot());
    }

    public EquipmentSlot getDefaultSlot() {
        return getAllowedSlots().iterator().next();
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot slot, LivingEntity entity) {
        return getAllowedSlots().contains(slot);
    }
    
    @EventBusSubscriber(modid = CombatMania.MOD_ID)
    private static class Handler {
        @SubscribeEvent
        public static void saveNewArmorSlot(LivingEquipmentChangeEvent event) {
            ItemStack newStack = event.getTo();

            if (!(newStack.getItem() instanceof MultiSlotArmorItem))
                return;

            EquipmentSlot slot = event.getSlot();

            if (!slot.isArmor())
                return;

            EquipmentSlot current = newStack.getOrDefault(CMDataComponents.EQUIPPED_SLOT, null);
            if (slot.equals(current))
                return;

            newStack.set(CMDataComponents.EQUIPPED_SLOT, slot);
        }
    }
}
