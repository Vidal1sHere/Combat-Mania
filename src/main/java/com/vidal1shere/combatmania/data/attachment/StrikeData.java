package com.vidal1shere.combatmania.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StrikeData {

    public static final Codec<StrikeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("primary_ticker").forGetter(d -> d.primaryTicker),
            Codec.INT.fieldOf("secondary_ticker").forGetter(d -> d.secondaryTicker),
            Codec.INT.fieldOf("primary_duration").forGetter(d -> d.primaryDuration),
            Codec.INT.fieldOf("secondary_duration").forGetter(d -> d.secondaryDuration)
    ).apply(instance, StrikeData::new));

    public static final StreamCodec<ByteBuf, StrikeData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeInt(data.primaryTicker);
                        buf.writeInt(data.secondaryTicker);
                        buf.writeInt(data.primaryDuration);
                        buf.writeInt(data.secondaryDuration);

                        buf.writeBoolean(data.primaryActive);
                        buf.writeBoolean(data.secondaryActive);

                        buf.writeInt(data.primaryActiveTicks);
                        buf.writeInt(data.secondaryActiveTicks);
                    },
                    buf -> {
                        StrikeData data = new StrikeData();

                        data.primaryTicker = buf.readInt();
                        data.secondaryTicker = buf.readInt();
                        data.primaryDuration = buf.readInt();
                        data.secondaryDuration = buf.readInt();

                        data.primaryActive = buf.readBoolean();
                        data.secondaryActive = buf.readBoolean();

                        data.primaryActiveTicks = buf.readInt();
                        data.secondaryActiveTicks = buf.readInt();

                        return data;
                    }
            );

    // cooldown
    private int primaryTicker = 0;
    private int secondaryTicker = 0;
    private int primaryDuration = 0;
    private int secondaryDuration = 0;

    // active state
    private boolean primaryActive = false;
    private boolean secondaryActive = false;
    private int primaryActiveTicks = 0;
    private int secondaryActiveTicks = 0;

    public StrikeData() {}

    private StrikeData(int primaryTicker, int secondaryTicker, int primaryDuration, int secondaryDuration) {
        this.primaryTicker = primaryTicker;
        this.secondaryTicker = secondaryTicker;
        this.primaryDuration = primaryDuration;
        this.secondaryDuration = secondaryDuration;
    }

    // -- tick --
    
    public void tickCooldown() {
        if (primaryTicker < primaryDuration) primaryTicker++;
        if (secondaryTicker < secondaryDuration) secondaryTicker++;
    }
    
    public void tickActive() {
        if (primaryActive) primaryActiveTicks++;
        if (secondaryActive) secondaryActiveTicks++;
    }

    // -- cooldown --

    public void exhaust(StrikeSlot slot, int duration) {
        if (slot == StrikeSlot.PRIMARY) {
            primaryTicker = 0;
            primaryDuration = duration;
        } else {
            secondaryTicker = 0;
            secondaryDuration = duration;
        }
    }

    public float getScale(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) {
            return primaryDuration == 0 ? 1.0F : Math.min(1.0F, (float) primaryTicker / primaryDuration);
        } else {
            return secondaryDuration == 0 ? 1.0F : Math.min(1.0F, (float) secondaryTicker / secondaryDuration);
        }
    }

    // -- active state --

    public void setActive(StrikeSlot slot, boolean active) {
        if (slot == StrikeSlot.PRIMARY) {
            primaryActive = active;
            if (!active) primaryActiveTicks = 0;
        } else {
            secondaryActive = active;
            if (!active) secondaryActiveTicks = 0;
        }
    }

    public boolean isActive(StrikeSlot slot) {
        return slot == StrikeSlot.PRIMARY ? primaryActive : secondaryActive;
    }

    public int getActiveTicks(StrikeSlot slot) {
        return slot == StrikeSlot.PRIMARY ? primaryActiveTicks : secondaryActiveTicks;
    }

    // -- overlap / block check --

    public boolean isBlocked(StrikeSlot slot, IStrikeWeapon weapon) {
        StrikeSlot other = slot.other();

        DeferredHolder<StrikeStyle, ? extends StrikeStyle> otherHolder = weapon.getStyle(other);

        if (otherHolder == null) return false;

        StrikeStyle otherStyle = otherHolder.get();

        if (otherStyle.overlap()) return false;

        return getScale(other) < otherStyle.getMinStrengthScale();
    }
}