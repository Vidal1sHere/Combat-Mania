package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.network.StrikePayload;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike.styles.JoustStrikeStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CombatEventHandler {

    @EventBusSubscriber(value = Dist.CLIENT, modid = CombatMania.MOD_ID)
    public static class Client {

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null)
                return;

            ItemStack stack = player.getItemInHand(event.getHand());
            if (!(stack.getItem() instanceof IStrikeWeapon weapon))
                return;

            DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder;
            StrikeSlot slot;

            if (event.isAttack()) {
                slot = StrikeSlot.PRIMARY;
            } else if (event.isUseItem()) {
                slot = StrikeSlot.SECONDARY;
            } else
                return;

            styleHolder = weapon.getStyle(slot);
            if (styleHolder == null)
                return;

            StrikeStyle style = styleHolder.get();
            StrikeData strikeData = player.getData(CMAttachments.STRIKE);

            if (strikeData.isBlocked(slot, weapon)) {
                event.setCanceled(true);
                event.setSwingHand(false);
                return;
            }

            float scale = strikeData.getScale(slot);
            if (scale < style.getMinStrengthScale()) {
                event.setCanceled(true);
                event.setSwingHand(false);
                return;
            }

            Entity target = mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult) mc.hitResult).getEntity() : null;

            PacketDistributor.sendToServer(new StrikePayload.Request(StrikePayload.Action.START, slot == StrikeSlot.PRIMARY, target != null ? target.getId() : -1));

            event.setCanceled(true);
            event.setSwingHand(false);
        }

        @SubscribeEvent
        public static void onClientTick(PlayerTickEvent.Pre event) {
            if (!event.getEntity().level().isClientSide())
                return;

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null)
                return;

            ItemStack stack = player.getMainHandItem();
            StrikeData strikeData = player.getData(CMAttachments.STRIKE);

            checkStop(mc, player, stack, strikeData, StrikeSlot.PRIMARY);
            checkStop(mc, player, stack, strikeData, StrikeSlot.SECONDARY);
        }

        private static void checkStop(Minecraft mc, Player player, ItemStack stack, StrikeData strikeData, StrikeSlot slot) {
            if (!strikeData.isActive(slot)) {
                if (slot == StrikeSlot.SECONDARY) {
                    System.out.println("not active on " + (player.level().isClientSide() ? "client" : "server"));
                }
                return;
            }
            
            boolean isPrimary = slot == StrikeSlot.PRIMARY;

            if (!(stack.getItem() instanceof IStrikeWeapon weapon)) {
                PacketDistributor.sendToServer(new StrikePayload.Request(StrikePayload.Action.STOP, isPrimary, -1));
                return;
            }
            
            if (!isPrimary) {
                System.out.println("active on " + (player.level().isClientSide() ? "client" : "server"));
            }

            DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder = weapon.getStyle(slot);
            if (styleHolder == null) {
                return;
            }

            StrikeStyle style = styleHolder.get();

            boolean keyDown = isPrimary ? mc.options.keyAttack.isDown() : mc.options.keyUse.isDown();

            int ticks = strikeData.getActiveTicks(slot);

            if (!keyDown || (style.getMaxDuration(stack, player) > 0 && ticks >= style.getMaxDuration(stack, player))) {
                PacketDistributor.sendToServer(new StrikePayload.Request(StrikePayload.Action.STOP, isPrimary, -1));
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

        StrikeData strikeData = player.getData(CMAttachments.STRIKE);

        strikeData.tickActive();

        tickSlot(player, stack, weapon, strikeData, StrikeSlot.PRIMARY);
        tickSlot(player, stack, weapon, strikeData, StrikeSlot.SECONDARY);

        player.syncData(CMAttachments.STRIKE);
    }

    private static void tickSlot(Player player, ItemStack stack, IStrikeWeapon weapon,
                                 StrikeData strikeData, StrikeSlot slot) {
        if (!strikeData.isActive(slot)) return;
        
        DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder = weapon.getStyle(slot);

        if (styleHolder == null) return;

        StrikeStyle style = styleHolder.get();
        
        StrikeContext ctx = new StrikeContext(
                player, stack, style, null,
                (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                1.0f, player.level()
        );

        style.onTick(ctx, strikeData.getActiveTicks(slot));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

        StrikeData strikeData = player.getData(CMAttachments.STRIKE);
        if (strikeData.isBlocked(StrikeSlot.SECONDARY, weapon)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity entity))
            return;

        StaggerData data = entity.getData(CMAttachments.STAGGER);

        data.tick(entity);
    }

    @SubscribeEvent
    public static void removeJoustData(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        JoustData joustData = player.getData(CMAttachments.JOUST);
        boolean changed = false;
        
        if (!(stack.getItem() instanceof IStrikeWeapon)) {
            changed = true;
        } else if (!(((IStrikeWeapon) stack.getItem()).getActiveStyle(stack) instanceof JoustStrikeStyle)) {
            changed = true;
        }
        
        if (changed) {
            joustData.reset();
            player.syncData(CMAttachments.JOUST);
        }
    }
}