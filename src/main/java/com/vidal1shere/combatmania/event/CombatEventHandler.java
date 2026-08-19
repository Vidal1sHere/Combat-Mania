package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.event.game.*;
import com.vidal1shere.combatmania.item.FireworkArrowItem;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.item.CMItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.storage.loot.functions.SetFireworkExplosionFunction;
import net.minecraft.world.level.storage.loot.functions.SetFireworksFunction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.List;

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
    public static class Bow {
        @SubscribeEvent
        public static void createFireworkArrow(ProjectileCreateEvent event) {
            if (!event.getAmmo().is(CMItems.FIREWORK_ARROW)) return;
            
            event.setProjectile(
                    ((FireworkArrowItem) event.getAmmo().getItem())
                            .createFirework(event.getShooter().level(), event.getAmmo(), event.getShooter())
            );
            
            event.setVelocity(1.6F);
        }

        @SubscribeEvent
        public static void craftFireworkArrow(PlayerEvent.ItemCraftedEvent event) {
            ItemStack result = event.getCrafting();
            if (!result.is(CMItems.FIREWORK_ARROW.get())) return;

            for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
                ItemStack stack = event.getInventory().getItem(i);
                if (stack.is(Items.FIREWORK_ROCKET)) {
                    result.set(DataComponents.FIREWORKS, 
                            stack.getOrDefault(DataComponents.FIREWORKS, SetFireworksFunction.DEFAULT_VALUE));
                    result.set(DataComponents.FIREWORK_EXPLOSION, 
                            stack.getOrDefault(DataComponents.FIREWORK_EXPLOSION, SetFireworkExplosionFunction.DEFAULT_VALUE));
                    break;
                }
            }
        }

        @SubscribeEvent
        public static void applyFireworkData(LivingGetProjectileEvent event) {
            if (event.getProjectileItemStack().isEmpty()) 
                return;
            evaluate(event.getProjectileWeaponItemStack(), event.getProjectileItemStack());
        }

        @SubscribeEvent
        public static void applyFireworkDataOnQuiver(QuiverGetProjectileEvent event) {
            evaluate(event.getWeapon(), event.getProjectile());
        }
        
        
        // todo kill yourself
        private static void evaluate(ItemStack weapon, ItemStack ammo) {
            if (!weapon.is(Items.BOW))
                return;
            
            if (weapon.getOrDefault(CMDataComponents.FIREWORK_ARROW, false)) {
                return;
            }
            
            if (!ammo.is(CMItems.FIREWORK_ARROW.get())) {
                weapon.set(CMDataComponents.FIREWORK_ARROW, false);
                return;
            }

            weapon.set(CMDataComponents.FIREWORK_ARROW, true);
        }
        
        @SubscribeEvent
        public static void clearFireworkDataOnLoose(ArrowLooseEvent event) {
            if (event.hasAmmo() && event.getBow().getOrDefault(CMDataComponents.FIREWORK_ARROW, false)) {
                if (!event.getEntity().hasInfiniteMaterials()) {
                    event.getEntity().getCooldowns().addCooldown(event.getBow().getItem(), FireworkArrowItem.COOLDOWN);
                }
            }
            
            event.getBow().set(CMDataComponents.FIREWORK_ARROW, false);
        }
    }
}
