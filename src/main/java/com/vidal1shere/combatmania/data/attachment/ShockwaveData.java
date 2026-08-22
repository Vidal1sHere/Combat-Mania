package com.vidal1shere.combatmania.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ShockwaveData {

    public static final Codec<ShockwaveData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("stage").forGetter(d -> d.stage.ordinal()),
            Codec.INT.fieldOf("lift_ticks").forGetter(d -> d.liftTicks)
    ).apply(instance, (ordinal, liftTicks) -> {
        ShockwaveData data = new ShockwaveData();
        data.stage = Stage.byOrdinal(ordinal);
        data.liftTicks = liftTicks;
        return data;
    }));

    public static final StreamCodec<ByteBuf, ShockwaveData> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> {
                buf.writeInt(data.stage.ordinal());
                buf.writeInt(data.liftTicks);
                buf.writeBoolean(data.queuedAnimation);
            },
            buf -> {
                ShockwaveData data = new ShockwaveData();
                data.stage = Stage.byOrdinal(buf.readInt());
                data.liftTicks = buf.readInt();
                data.queuedAnimation = buf.readBoolean();
                data.hitEntities.clear();
                return data;
            }
    );

    private Stage stage = Stage.NONE;
    private int liftTicks = 0;
    private boolean queuedAnimation = false;
    private final List<UUID> hitEntities = new ArrayList<>();

    public ShockwaveData() {}

    public Stage getStage() { return stage; }
    public int getLiftTicks() { return liftTicks; }
    public boolean hasQueuedAnimation() { return queuedAnimation; }

    public boolean hasHitEntity(Entity entity) {
        if (entity == null) return false;
        return hitEntities.contains(entity.getUUID());
    }

    // -- static helpers --

    public static ShockwaveData get(Player player) {
        return player.getData(CMAttachments.SHOCKWAVE);
    }

    public static void startLift(Player player) {
        ShockwaveData data = get(player);
        data.stage = Stage.LIFT;
        data.liftTicks = 0;
        data.queuedAnimation = true;
        data.hitEntities.clear();
        player.syncData(CMAttachments.SHOCKWAVE);
    }

    public static void tickLift(Player player) {
        ShockwaveData data = get(player);
        if (data.stage == Stage.LIFT) {
            data.liftTicks++;
        }
    }

    public static void startSwing(Player player) {
        ShockwaveData data = get(player);
        data.stage = Stage.SWING;
        data.queuedAnimation = true;
        player.syncData(CMAttachments.SHOCKWAVE);
    }

    public static void startHit(Player player) {
        ShockwaveData data = get(player);
        data.stage = Stage.HIT;
        data.queuedAnimation = true;
        player.syncData(CMAttachments.SHOCKWAVE);
    }

    public static void reset(Player player) {
        ShockwaveData data = get(player);
        data.stage = Stage.NONE;
        data.liftTicks = 0;
        data.queuedAnimation = false;
        data.hitEntities.clear();
        player.syncData(CMAttachments.SHOCKWAVE);
    }

    public static void registerEntity(Player player, Entity entity) {
        if (entity == null) return;
        get(player).hitEntities.add(entity.getUUID());
    }

    public static void consumeAnimation(Player player) {
        get(player).queuedAnimation = false;
    }

    public enum Stage {
        NONE,
        LIFT,
        SWING,
        HIT;

        public static Stage byOrdinal(int ordinal) {
            for (Stage s : values()) {
                if (s.ordinal() == ordinal) return s;
            }
            return NONE;
        }
    }
}