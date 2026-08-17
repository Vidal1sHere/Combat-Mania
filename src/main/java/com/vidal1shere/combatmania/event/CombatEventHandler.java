package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.QuiverData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.event.game.ItemInHandChangeEvent;
import com.vidal1shere.combatmania.event.game.ProjectileCreateEvent;
import com.vidal1shere.combatmania.event.game.ProjectileShootEvent;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import com.vidal1shere.combatmania.network.QuiverArrowCycle;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.keybind.CMKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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
        public static void saveNewQuiverSlot(LivingEquipmentChangeEvent event) {
            ItemStack newStack = event.getTo();

            if (!(newStack.getItem() instanceof QuiverItem)) return;

            EquipmentSlot slot = event.getSlot();

            if (!slot.isArmor()) return;

            EquipmentSlot current = newStack.getOrDefault(CMDataComponents.EQUIPPED_SLOT, null);
            if (slot.equals(current)) return;

            newStack.set(CMDataComponents.EQUIPPED_SLOT, slot);
        }

        @SubscribeEvent
        public static void readOrClearQuiverData(LivingEquipmentChangeEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;

            QuiverData data = player.getData(CMAttachments.QUIVER);
            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);

            if (equippedQuiver == null) {
                if (data.equippedQuiver() != null) {
                    data.reset(null);
                    player.setData(CMAttachments.QUIVER, data);
                }
                return;
            }

            QuiverData.EquippedQuiver current = data.equippedQuiver();

            if (current == null || !current.slot().equals(equippedQuiver.slot())) {
                data.reset(equippedQuiver);
                player.setData(CMAttachments.QUIVER, data);
            } else {
                data.setEquippedQuiver(equippedQuiver);
            }
        }

        @SubscribeEvent
        public static void findQuiverAmmo(LivingGetProjectileEvent event) {
            if (!(event.getEntity() instanceof Player player)) return;

            QuiverData data = player.getData(CMAttachments.QUIVER);
            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);

            if (equippedQuiver == null) return;

            if (data.equippedQuiver() == null || !data.equippedQuiver().slot().equals(equippedQuiver.slot())) {
                data.reset(equippedQuiver);
            } else {
                data.setEquippedQuiver(equippedQuiver);
            }

            QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
            if (contents.isEmpty()) return;

            ItemStack selected = data.getSelectedStack(contents);
            if (selected == null || selected.isEmpty()) return;

            data.setQuiverAmmoUsed(true);
            player.syncData(CMAttachments.QUIVER);
            event.setProjectileItemStack(selected.copyWithCount(1));
        }
        
        @SubscribeEvent
        public static void quiverOnArrowNock(ArrowNockEvent event) {
            if (event.isCanceled()) return;

            Player player = event.getEntity();
            if (player.level().isClientSide()) return;

            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
            if (equippedQuiver == null) return;

            if (equippedQuiver.stack().getItem() instanceof QuiverItem quiver) {
                quiver.onArrowNock(event);
            }
        }

        @SubscribeEvent
        public static void quiverOnArrowCreated(ProjectileCreateEvent event) {
            Player player = (Player) event.getShooter();
            if (player.level().isClientSide()) return;

            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
            if (equippedQuiver == null) return;

            if (equippedQuiver.stack().getItem() instanceof QuiverItem quiver) {
                quiver.onArrowCreate(event);
            }
        }

        @SubscribeEvent
        public static void quiverOnArrowShot(ProjectileShootEvent event) {
            Player player = (Player) event.getShooter();
            if (player.level().isClientSide()) return;

            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
            if (equippedQuiver == null) return;

            if (equippedQuiver.stack().getItem() instanceof QuiverItem quiver) {
                quiver.onArrowShoot(event);
            }
        }

        @SubscribeEvent
        public static void consumeQuiverAmmo(ArrowLooseEvent event) {
            if (event.isCanceled()) return;

            Player player = event.getEntity();
            if (player.level().isClientSide()) return;

            QuiverData data = player.getData(CMAttachments.QUIVER);

            if (!data.quiverAmmoUsed()) return;

            data.setQuiverAmmoUsed(false);

            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
            if (equippedQuiver == null) return;

            if (data.equippedQuiver() == null || !data.equippedQuiver().slot().equals(equippedQuiver.slot())) {
                data.reset(equippedQuiver);
                return;
            }

            QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
            if (contents.isEmpty()) return;

            int index = data.getSelectedIndex(contents);
            QuiverContents.Mutable mutable = new QuiverContents.Mutable(contents);

            if (mutable.removeOne(index) == null) return;

            equippedQuiver.stack().set(CMDataComponents.QUIVER_CONTENTS, mutable.toImmutable());

            if (index >= mutable.size()) {
                data.setSelectedIndex(Math.max(0, mutable.size() - 1));
            }

            player.syncData(CMAttachments.QUIVER);
            
            if (equippedQuiver.stack().getItem() instanceof QuiverItem quiver) {
                quiver.onArrowLoose(event);
            }
        }

        @SubscribeEvent
        public static void storeArrowInHand(PlayerInteractEvent.RightClickItem event) {
            if (event.isCanceled()) return;

            Player player = event.getEntity();
            if (player.level().isClientSide()) return;

            QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
            if (equippedQuiver == null) return;

            ItemStack item = event.getItemStack();

            if (!(item.getItem() instanceof ArrowItem)) return;

            QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
            QuiverContents.Mutable mutable = new QuiverContents.Mutable(contents);

            int amount = player.isShiftKeyDown() ? item.getCount() : 1;
            ItemStack toInsert = item.copyWithCount(amount);

            int inserted = mutable.tryInsert(toInsert);

            if (inserted <= 0) return;

            item.shrink(inserted);
            equippedQuiver.stack().set(CMDataComponents.QUIVER_CONTENTS, mutable.toImmutable());
            player.syncData(CMAttachments.QUIVER);
        }

        @EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
        public static class Client {
            @SubscribeEvent
            public static void onClientTick(ClientTickEvent.Post event) {
                while (CMKeyBindings.QUIVER_ARROW_CYCLE.consumeClick()) {
                    Player player = Minecraft.getInstance().player;
                    if (player == null) continue;

                    QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
                    if (equippedQuiver == null) continue;

                    QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
                    if (contents.isEmpty()) continue;

                    QuiverArrowCycle.Direction direction = player.isShiftKeyDown() ? QuiverArrowCycle.Direction.PREVIOUS : QuiverArrowCycle.Direction.NEXT;

                    PacketDistributor.sendToServer(new QuiverArrowCycle.Request(direction));
                }
            }
        }
    }
}
