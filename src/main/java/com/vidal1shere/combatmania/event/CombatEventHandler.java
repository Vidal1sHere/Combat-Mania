package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.event.custom.ItemInHandChangeEvent;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class CombatEventHandler {
    @EventBusSubscriber(modid = CombatMania.MOD_ID)
    public static class Stagger {
        @SubscribeEvent
        public static void onLivingTick(EntityTickEvent.Pre event) {
            if (!(event.getEntity() instanceof LivingEntity entity))
                return;

            StaggerData data = entity.getData(CMAttachments.STAGGER);

            data.tick(entity);
        }
    }
    
    @EventBusSubscriber(modid = CombatMania.MOD_ID)
    public static class JoustStrike {
        @SubscribeEvent
        public static void clearJoustOnWeaponSwap(ItemInHandChangeEvent event) {
            JoustData.reset(event.getEntity());
        }
        
        @SubscribeEvent
        public static void clearJoustOnDeath(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;
            JoustData.reset(player);
        }
        
        @SubscribeEvent
        public static void clearJoustOnLeave(PlayerEvent.PlayerLoggedOutEvent event) {
            JoustData.reset(event.getEntity());
        }

        @SubscribeEvent
        public static void clearJoustOnJoin(PlayerEvent.PlayerLoggedInEvent event) {
            JoustData.reset(event.getEntity());
        }
    }
    
    @EventBusSubscriber(modid = CombatMania.MOD_ID)
    public static class Quiver {
        @SubscribeEvent
        public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
            ItemStack newStack = event.getTo();

            if (!(newStack.getItem() instanceof QuiverItem)) return;

            EquipmentSlot slot = event.getSlot();

            if (!slot.isArmor()) return;

            EquipmentSlot current = newStack.getOrDefault(CMDataComponents.EQUIPPED_SLOT, null);
            if (slot.equals(current)) return;

            newStack.set(CMDataComponents.EQUIPPED_SLOT, slot);
        }
    }
}
