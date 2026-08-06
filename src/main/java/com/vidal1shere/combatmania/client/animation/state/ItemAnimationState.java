package com.vidal1shere.combatmania.client.animation.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;

/**
 * Tracks the playback state of a single first-person item animation.
 * Mirrors the structure of vanilla's AnimationState with an added LoopType.
 */
public class ItemAnimationState {

    public enum LoopType {
        PLAY_ONCE,
        LOOP,
        HOLD_ON_LAST_FRAME
    }

    private static final long STOPPED = Long.MAX_VALUE;

    public static final Codec<ItemAnimationState> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.LONG.fieldOf("last_time").forGetter(state -> state.lastTime),
                    Codec.LONG.fieldOf("accumulated_time").forGetter(ItemAnimationState::getAccumulatedTime),
                    Codec.STRING.xmap(
                            ItemAnimationState.LoopType::valueOf,
                            ItemAnimationState.LoopType::name
                    ).fieldOf("loop_type").forGetter(ItemAnimationState::getLoopType)
            ).apply(instance, ItemAnimationState::new)
    );

    private long lastTime = Long.MAX_VALUE;
    private long accumulatedTime;
    private LoopType loopType = LoopType.PLAY_ONCE;

    public ItemAnimationState() {}

    public ItemAnimationState(long lastTime, long accumulatedTime, LoopType loopType) {
        this.lastTime = lastTime;
        this.accumulatedTime = accumulatedTime;
        this.loopType = loopType;
    }

    public void start(int tickCount) {
        this.lastTime = (long) tickCount * 1000L / 20L;
        this.accumulatedTime = 0L;
    }

    public void start(int tickCount, LoopType loopType) {
        this.loopType = loopType;
        this.start(tickCount);
    }

    public void startIfStopped(int tickCount) {
        if (!this.isStarted()) {
            this.start(tickCount);
        }
    }

    public void startIfStopped(int tickCount, LoopType loopType) {
        if (!this.isStarted()) {
            this.start(tickCount, loopType);
        }
    }

    public void animateWhen(boolean condition, int tickCount) {
        if (condition) {
            this.startIfStopped(tickCount);
        } else {
            this.stop();
        }
    }

    public void stop() {
        this.lastTime = STOPPED;
    }

    public void updateTime(float ageInTicks, float speed) {
        if (this.isStarted()) {
            long current = Mth.lfloor((double) (ageInTicks * 1000.0F / 20.0F));
            this.accumulatedTime += (long) ((float) (current - this.lastTime) * speed);
            this.lastTime = current;
        }
    }

    public long getAccumulatedTime() {
        return this.accumulatedTime;
    }

    public LoopType getLoopType() {
        return this.loopType;
    }

    public boolean isStarted() {
        return this.lastTime != STOPPED;
    }
}