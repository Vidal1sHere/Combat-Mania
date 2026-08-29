package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.ShockwaveData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.event.game.*;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
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
    public static class ShockwaveStrike {
        @SubscribeEvent
        public static void clearShockwaveOnWeaponSwap(ItemInHandChangeEvent event) {
            ShockwaveData.reset(event.getEntity(), true);
        }

        @SubscribeEvent
        public static void clearShockwaveOnDeath(LivingDeathEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;
            ShockwaveData.reset(player, true);
        }

        @SubscribeEvent
        public static void clearShockwaveOnLeave(PlayerEvent.PlayerLoggedOutEvent event) {
            ShockwaveData.reset(event.getEntity(), true);
        }

        @SubscribeEvent
        public static void clearShockwaveOnJoin(PlayerEvent.PlayerLoggedInEvent event) {
            ShockwaveData.reset(event.getEntity(), true);
        }
        
        @SubscribeEvent
        public static void preventFallDamageOnSwing(LivingFallEvent event) {
            if (!(event.getEntity() instanceof Player player))
                return;
            
            ShockwaveData data = ShockwaveData.get(player);
            ShockwaveData.Stage stage = data.getStage();
            
            if (stage == ShockwaveData.Stage.SWING || (stage == ShockwaveData.Stage.HIT && !data.isEarlyHit())) {
                event.setCanceled(true);
            }
        }
    }
}
