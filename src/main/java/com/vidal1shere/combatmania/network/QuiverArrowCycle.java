package com.vidal1shere.combatmania.network;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.client.gui.access.QuiverHudState;
import com.vidal1shere.combatmania.data.attachment.QuiverData;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.mixin.client.GuiMixin;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public abstract class QuiverArrowCycle implements CustomPacketPayload {

    public enum Direction { NEXT, PREVIOUS }

    protected final Direction direction;

    protected QuiverArrowCycle(Direction direction) {
        this.direction = direction;
    }
    
    public static final class Request extends QuiverArrowCycle {

        public static final Type<Request> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(CombatMania.MOD_ID, "quiver_arrow_cycle_request")
        );

        public static final StreamCodec<ByteBuf, Request> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                p -> p.direction.ordinal(),
                id -> new Request(Direction.values()[id])
        );

        public Request(Direction direction) {
            super(direction);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }

        public void handle(IPayloadContext context) {
            context.enqueueWork(() -> {
                QuiverData data = context.player().getData(CMAttachments.QUIVER);
                QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(context.player());

                if (equippedQuiver == null) return;

                if (data.equippedQuiver() == null || !data.equippedQuiver().slot().equals(equippedQuiver.slot())) {
                    data.reset(equippedQuiver);
                } else {
                    data.setEquippedQuiver(equippedQuiver);
                }

                QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
                if (contents.isEmpty()) {
                    data.setSelectedIndex(0);
                    return;
                }

                if (this.direction == Direction.NEXT) {
                    data.next(contents.size());
                } else {
                    data.previous(contents.size());
                }

                context.player().syncData(CMAttachments.QUIVER);

                PacketDistributor.sendToPlayer(
                        (ServerPlayer) context.player(),
                        new Confirm(this.direction)
                );
            });
        }
    }

    public static final class Confirm extends QuiverArrowCycle {

        public static final Type<Confirm> TYPE = new Type<>(
                ResourceLocation.fromNamespaceAndPath(CombatMania.MOD_ID, "quiver_arrow_cycle_confirm")
        );

        public static final StreamCodec<ByteBuf, Confirm> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                p -> p.direction.ordinal(),
                id -> new Confirm(Direction.values()[id])
        );

        public Confirm(Direction direction) {
            super(direction);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() { return TYPE; }

        public void handle(IPayloadContext context) {
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    QuiverHudState.onCycleConfirmed(Minecraft.getInstance().level.getGameTime());
                }
            });
        }
    }
}