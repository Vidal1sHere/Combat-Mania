package com.vidal1shere.combatmania.network;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeActiveData;
import com.vidal1shere.combatmania.strike.StrikeContext;
import com.vidal1shere.combatmania.strike.StrikeCooldownData;
import com.vidal1shere.combatmania.strike.StrikeStyle;
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
                new Type<>(ResourceLocation.fromNamespaceAndPath("combat_mania", "strike_request"));

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
                ItemStack stack = player.getMainHandItem();
                if (!(stack.getItem() instanceof IStrikeWeapon weapon)) { 
                    return; 
                }

                StrikeStyle style = payload.isPrimary() ? weapon.getPrimaryStyle() : weapon.getSecondaryStyle();
                if (style == null) return;

                StrikeActiveData active = player.getData(CMAttachments.STRIKE_ACTIVE);
                StrikeCooldownData cooldown = player.getData(CMAttachments.STRIKE_COOLDOWN);

                if (payload.action() == Action.START) {
                    float scale = payload.isPrimary() ? cooldown.getPrimaryScale() : cooldown.getSecondaryScale();
                    if (scale < style.getMinStrengthScale()) { 
                        return; 
                    }

                    Entity target = payload.targetId() != -1 ? player.level().getEntity(payload.targetId()) : null;

                    StrikeContext ctx = new StrikeContext(
                            player, stack, style, target,
                            (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                            (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                            (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                            scale, player.level()
                    );

                    active.setActive(payload.isPrimary(), true);
                    style.onStart(ctx);

                    if (style.getMaxDuration() == 0) {
                        int duration = (int) (1.0 / player.getAttributeValue(style.getSpeedAttribute()) * 20);
                        cooldown.exhaust(payload.isPrimary(), duration, style.overlap());
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new Confirm(payload.isPrimary()));
                    }
                } else {
                    if (!active.isActive(payload.isPrimary())) return;

                    StrikeContext ctx = new StrikeContext(
                            player, stack, style, null,
                            (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE),
                            (float) player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
                            (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK),
                            1.0f, player.level()
                    );

                    style.onStop(ctx, active.getTicks(payload.isPrimary()));
                    active.setActive(payload.isPrimary(), false);

                    if (style.getMaxDuration() > 0) {
                        int duration = (int) (1.0 / player.getAttributeValue(style.getSpeedAttribute()) * 20);
                        cooldown.exhaust(payload.isPrimary(), duration, style.overlap());
                        PacketDistributor.sendToPlayer((ServerPlayer) player, new Confirm(payload.isPrimary()));
                    }
                }
            });
        }
    }

    // -------------------------------------------------------------------------
    // Confirm (server → client): ACK pra fazer swing
    // -------------------------------------------------------------------------

    public static class Confirm extends StrikePayload {

        public static final Type<Confirm> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath("combat_mania", "strike_confirm"));

        public static final StreamCodec<ByteBuf, Confirm> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.BOOL, Confirm::isPrimary,
                        Confirm::new
                );


        public Confirm(boolean isPrimary) { super(Action.START, isPrimary, -1); }

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }

        public static void handle(Confirm payload, IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                player.resetAttackStrengthTicker();
                player.swing(InteractionHand.MAIN_HAND);
            });
        }
    }
}