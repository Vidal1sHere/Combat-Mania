package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.styles.JoustStrikeStyle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

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
        public static void clearJoustOnWeaponSwap(PlayerTickEvent.Pre event) {
            Player player = event.getEntity();
            ItemStack stack = player.getMainHandItem();
            boolean changed = false;

            if (!IStrikeWeapon.isStrikeWeapon(stack)) {
                changed = true;
            } else if (!(((IStrikeWeapon) stack.getItem()).getActiveStyle(stack) instanceof JoustStrikeStyle)) {
                changed = true;
            }

            if (changed) {
                JoustData.reset(player);
            }
        }
        
        @SubscribeEvent
        public static void clearJoustOnDeath(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;
            JoustData.reset(player);
        }
        
        @SubscribeEvent
        public static void clearJoustOnLeave(PlayerEvent.PlayerLoggedInEvent event) {
            JoustData.reset(event.getEntity());
        }
    }
}
