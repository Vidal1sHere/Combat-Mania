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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.functions.SetFireworkExplosionFunction;
import net.minecraft.world.level.storage.loot.functions.SetFireworksFunction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.ArrayList;
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
        public static void loadFireworkArrow(CrossbowLoadEvent event) {
            if (event.getEntity().level().isClientSide()) return;
            if (!event.getAmmo().is(CMItems.FIREWORK_ARROW)) return;

            int count = 1;
            if (event.getEntity().level() instanceof ServerLevel level) {
                count = EnchantmentHelper.processProjectileCount(level, event.getWeapon(), event.getEntity(), 1);
            }
            
            ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
            Fireworks fireworksComponent = event.getAmmo().get(DataComponents.FIREWORKS);
            if (fireworksComponent != null) {
                firework.set(DataComponents.FIREWORKS, fireworksComponent);
            }
            
            List<ItemStack> list = new ArrayList<>();
            for (int i = 0; i < count; i++) { list.add(firework); } 

            event.setProjectiles(list);
        }
       
        @SubscribeEvent
        public static void createFireworkArrow(BowShootEvent event) {
            if (event.isCrossbow()) return;
            if (!event.getAmmo().is(CMItems.FIREWORK_ARROW)) return;
            if (event.getVelocity() < 3.0F) {
                event.setCanceled(true);
                return;
            }

            event.setProjectile(
                    ((FireworkArrowItem) event.getAmmo().getItem())
                            .createFirework(event.getEntity().level(), event.getAmmo(), event.getEntity())
            );

            event.setVelocity(1.6F);

            event.getWeapon().set(CMDataComponents.FIREWORK_ARROW, false);
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
            event.getBow().set(CMDataComponents.FIREWORK_ARROW, false);
        }
        
        @SubscribeEvent
        public static void clearFireworkDataOnSwap(ItemInHandChangeEvent event) {
            if (event.getPreviousStack().getOrDefault(CMDataComponents.FIREWORK_ARROW, false)) {
                event.getPreviousStack().set(CMDataComponents.FIREWORK_ARROW, false);
            }
        }
    }
}
