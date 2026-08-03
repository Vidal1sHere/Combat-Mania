package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.network.StrikePayload;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeActiveData;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeCooldownData;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
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
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CombatEventHandler {

    @EventBusSubscriber(value = Dist.CLIENT, modid = CombatMania.MOD_ID)
    public static class Client {

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;

            ItemStack stack = player.getItemInHand(event.getHand());
            if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

            StrikeStyle style;
            boolean isPrimary;

            if (event.isAttack()) {
                style = weapon.getPrimaryStyle();
                isPrimary = true;
                if (style == null) return;
            } else if (event.isUseItem()) {
                style = weapon.getSecondaryStyle();
                isPrimary = false;
                if (style == null) return;
            } else return;

            StrikeCooldownData cooldown = player.getData(CMAttachments.STRIKE_COOLDOWN);
            StrikeActiveData active = player.getData(CMAttachments.STRIKE_ACTIVE);

            if (active.isBlocked(isPrimary)) {
                event.setCanceled(true);
                event.setSwingHand(false);
                return;
            }

            float scale = isPrimary ? cooldown.getPrimaryScale() : cooldown.getSecondaryScale();
            if (scale < style.getMinStrengthScale()) {
                event.setCanceled(true);
                event.setSwingHand(false);
                return;
            }

            Entity target = mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.ENTITY
                    ? ((EntityHitResult) mc.hitResult).getEntity()
                    : null;

            active.setActive(isPrimary, true);
            PacketDistributor.sendToServer(new StrikePayload.Request(
                    StrikePayload.Action.START, isPrimary, target != null ? target.getId() : -1));

            event.setCanceled(true);
            event.setSwingHand(false);
        }

        @SubscribeEvent
        public static void onClientTick(PlayerTickEvent.Pre event) {
            if (!event.getEntity().level().isClientSide()) return;

            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;

            ItemStack stack = player.getMainHandItem();
            if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

            StrikeActiveData active = player.getData(CMAttachments.STRIKE_ACTIVE);

            checkStop(mc, player, weapon, active, true);
            checkStop(mc, player, weapon, active, false);
        }

        private static void checkStop(Minecraft mc, Player player, IStrikeWeapon weapon,
                                      StrikeActiveData active, boolean isPrimary) {
            if (!active.isActive(isPrimary)) return;

            StrikeStyle style = isPrimary ? weapon.getPrimaryStyle() : weapon.getSecondaryStyle();
            if (style == null) return;

            boolean keyDown = isPrimary ? mc.options.keyAttack.isDown() : mc.options.keyUse.isDown();
            int ticks = active.getTicks(isPrimary);

            if (!keyDown || (style.getMaxDuration() > 0 && ticks >= style.getMaxDuration())) {
                PacketDistributor.sendToServer(new StrikePayload.Request(
                        StrikePayload.Action.STOP, isPrimary, -1));
                active.setActive(isPrimary, false);
            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

        StrikeActiveData active = player.getData(CMAttachments.STRIKE_ACTIVE);

        tickSlot(player, stack, weapon, active, true);
        tickSlot(player, stack, weapon, active, false);
    }

    private static void tickSlot(Player player, ItemStack stack, IStrikeWeapon weapon,
                                 StrikeActiveData active, boolean isPrimary) {
        if (!active.isActive(isPrimary)) return;

        StrikeStyle style = isPrimary ? weapon.getPrimaryStyle() : weapon.getSecondaryStyle();
        if (style == null) return;

        active.tick(isPrimary);

        StrikeContext ctx = new StrikeContext(
                player, stack, style, null,
                (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                1.0f, player.level()
        );

        style.onTick(ctx, active.getTicks(isPrimary));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof IStrikeWeapon)) return;

        StrikeActiveData active = player.getData(CMAttachments.STRIKE_ACTIVE);
        if (active.isBlocked(false)) event.setCanceled(true);
    }
}