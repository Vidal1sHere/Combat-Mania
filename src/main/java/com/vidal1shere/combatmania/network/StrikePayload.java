package com.vidal1shere.combatmania.network;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeRenderers;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeStyleRenderer;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.client.animation.item.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.CMUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredHolder;

public abstract class StrikePayload implements CustomPacketPayload {

    public enum Action { START, STOP }

    protected final Action action;
    protected final boolean isPrimary;
    protected final int targetId;

    protected StrikePayload(Action action, boolean isPrimary, int targetId) {
        this.action = action;
        this.isPrimary = isPrimary;
        this.targetId = targetId;
    }

    public Action action() { return action; }
    public boolean isPrimary() { return isPrimary; }
    public int targetId() { return targetId; }

    // -------------------------------------------------------------------------
    // Request (client → server)
    // -------------------------------------------------------------------------

    public static class Request extends StrikePayload {

        public static final Type<Request> TYPE =
                new Type<>(CMUtil.modResLoc("strike_request"));

        public static final StreamCodec<ByteBuf, Request> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.idMapper(id -> Action.values()[id], Action::ordinal), Request::action,
                        ByteBufCodecs.BOOL, Request::isPrimary,
                        ByteBufCodecs.INT,  Request::targetId,
                        Request::new
                );

        public Request(Action action, boolean isPrimary, int targetId) {
            super(action, isPrimary, targetId);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }

        public static void handle(Request payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                StrikeData strikeData = player.getData(CMAttachments.STRIKE);
                StrikeSlot slot = payload.isPrimary() ? StrikeSlot.PRIMARY : StrikeSlot.SECONDARY;
                ItemStack stack = player.getMainHandItem();

                if (payload.action() == Action.STOP) {
                    if (!IStrikeWeapon.isStrikeWeapon(stack)) {
                        strikeData.setActive(slot, false);
                        player.syncData(CMAttachments.STRIKE);
                        return;
                    }

                    IStrikeWeapon weapon = (IStrikeWeapon) stack.getItem();
                    DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder =
                            payload.isPrimary() ? weapon.getPrimaryStyle() : weapon.getSecondaryStyle();

                    if (styleHolder == null) {
                        strikeData.setActive(slot, false);
                        player.syncData(CMAttachments.STRIKE);
                        return;
                    }

                    StrikeStyle style = styleHolder.get();

                    StrikeContext ctx = new StrikeContext(
                            player, stack, style, slot, null,
                            (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                            (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                            (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                            1.0f, player.level()
                    );

                    style.onStop(ctx, strikeData.getActiveTicks(slot));
                    strikeData.setActive(slot, false);
                    player.syncData(CMAttachments.STRIKE);

                    if (style.getMaxDuration(stack, player) > 0) {
                        int duration = (int) (1.0 / player.getAttributeValue(style.getSpeedAttribute()) * 20);
                        strikeData.exhaust(slot, duration);
                        PacketDistributor.sendToPlayer((ServerPlayer) player,
                                new Confirm(payload.isPrimary(), style.swingHand(), Action.STOP, duration));
                    }

                    return;
                }

                if (!IStrikeWeapon.isStrikeWeapon(stack)) {
                    return;
                }

                IStrikeWeapon weapon = (IStrikeWeapon) stack.getItem();
                DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder =
                        payload.isPrimary() ? weapon.getPrimaryStyle() : weapon.getSecondaryStyle();

                if (styleHolder == null) return;

                StrikeStyle style = styleHolder.get();
                float scale = strikeData.getScale(slot);
                if (scale < style.getMinStrengthScale()) return;

                if (strikeData.isActive(slot)) return;

                Entity target = payload.targetId() != -1 ? player.level().getEntity(payload.targetId()) : null;

                StrikeContext ctx = new StrikeContext(
                        player, stack, style, slot, target,
                        (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                        (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                        (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                        scale, player.level()
                );

                strikeData.setActive(slot, true);
                strikeData.setActiveStyle(slot, styleHolder.getId());
                style.onStart(ctx);
                player.syncData(CMAttachments.STRIKE);

                if (style.getMaxDuration(stack, player) == 0) {
                    int duration = (int) (1.0 / player.getAttributeValue(style.getSpeedAttribute()) * 20);
                    strikeData.exhaust(slot, duration);
                    PacketDistributor.sendToPlayer((ServerPlayer) player,
                            new Confirm(payload.isPrimary(), style.swingHand(), Action.START, duration));
                } else {
                    PacketDistributor.sendToPlayer((ServerPlayer) player,
                            new Confirm(payload.isPrimary(), false, Action.START, -1));
                }
            });
        }
    }

    // -------------------------------------------------------------------------
    // Confirm (server → client)
    // -------------------------------------------------------------------------

    public static class Confirm extends StrikePayload {

        public static final Type<Confirm> TYPE =
                new Type<>(CMUtil.modResLoc("strike_confirm"));

        public static final StreamCodec<ByteBuf, Confirm> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.BOOL, Confirm::isPrimary,
                        ByteBufCodecs.BOOL, Confirm::swingHand,
                        ByteBufCodecs.idMapper(id -> Action.values()[id], Action::ordinal), Confirm::action,
                        ByteBufCodecs.INT, Confirm::cooldownDuration,
                        Confirm::new
                );

        private final boolean swingHand;
        private final int cooldownDuration;

        public Confirm(boolean isPrimary, boolean swingHand, Action action, int cooldownDuration) {
            super(action, isPrimary, -1);
            this.swingHand = swingHand;
            this.cooldownDuration = cooldownDuration;
        }

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }

        public boolean swingHand() { return swingHand; }
        public int cooldownDuration() { return cooldownDuration; }

        public static void handle(Confirm payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();

                if (payload.cooldownDuration() > 0) {
                    StrikeData strikeData = player.getData(CMAttachments.STRIKE);
                    strikeData.exhaust(payload.isPrimary() ? StrikeSlot.PRIMARY : StrikeSlot.SECONDARY, payload.cooldownDuration());
                    player.resetAttackStrengthTicker();
                }

                ItemStack stack = player.getMainHandItem();
                if (!IStrikeWeapon.isStrikeWeapon(stack)) return;

                IStrikeWeapon weapon = (IStrikeWeapon) stack.getItem();
                DeferredHolder<StrikeStyle, ? extends StrikeStyle> styleHolder = payload.isPrimary() ?
                        weapon.getPrimaryStyle() :
                        weapon.getSecondaryStyle();

                if (styleHolder == null) return;

                StrikeStyle style = styleHolder.get();
                StrikeStyleRenderer<StrikeStyle> renderer = StrikeRenderers.getRenderer(style);

                if (renderer != null) {
                    for (InteractionHand hand : renderer.animatedHands(style, stack, player)) {
                        if (payload.swingHand()) {
                            player.swing(hand);
                        }

                        ItemAnimationState state = player.getData(CMAttachments.ITEM_ANIMATIONS).get(hand);

                        if (payload.action() == Action.START) {
                            renderer.onAnimStart(style, stack, hand, player, state, player.tickCount);
                        } else {
                            renderer.onAnimStop(style, stack, hand, player, state, player.tickCount);
                        }
                    }
                }
            });
        }
    }
}