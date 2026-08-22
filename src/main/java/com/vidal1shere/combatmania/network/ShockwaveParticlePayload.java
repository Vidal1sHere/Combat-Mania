package com.vidal1shere.combatmania.network;

import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.util.ParticleUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ShockwaveParticlePayload(BlockPos pos, int power, double radius, double spacing) implements CustomPacketPayload {

    public static final Type<ShockwaveParticlePayload> TYPE = new Type<>(CMUtil.modResLoc("shockwave_particles"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ShockwaveParticlePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ShockwaveParticlePayload::pos,
            ByteBufCodecs.INT, ShockwaveParticlePayload::power,
            ByteBufCodecs.DOUBLE, ShockwaveParticlePayload::radius,
            ByteBufCodecs.DOUBLE, ShockwaveParticlePayload::spacing,
            ShockwaveParticlePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ShockwaveParticlePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (!(level instanceof ClientLevel clientLevel)) 
                return;

            ParticleUtil.spawnSmashAttackParticles(clientLevel, payload.pos(), payload.power(), payload.radius(), payload.spacing());
        });
    }
}