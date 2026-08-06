package com.vidal1shere.combatmania.client.old_animation.data;

import com.vidal1shere.combatmania.client.old_animation.LoopType;

/** Runtime playback state for a single animation. */
public final class AnimationState {

    private final AnimationTrack track;
    private float elapsedSeconds;
    private boolean finished;

    public AnimationState(AnimationTrack track) {
        this.track = track;
        this.elapsedSeconds = 0f;
        this.finished = false;
    }

    public void tick(float deltaSeconds) {
        if (finished) return;
        elapsedSeconds += deltaSeconds;

        if (track.getLoopType() == LoopType.PLAY_ONCE && elapsedSeconds >= track.getDuration()) {
            elapsedSeconds = track.getDuration();
            finished = true;
        }
    }

    public Keyframe evaluate(float partialTick) {
        return track.evaluate(elapsedSeconds, partialTick);
    }

    public boolean isFinished() {
        return finished;
    }

    public AnimationTrack getTrack() {
        return track;
    }

    public float getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void reset() {
        elapsedSeconds = 0f;
        finished = false;
    }
}