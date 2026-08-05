package com.vidal1shere.combatmania.client.animation;

/** Controls what happens when an animation reaches its last keyframe. */
public enum LoopType {

    /** Plays once and stops. The state is marked as finished. */
    PLAY_ONCE,

    /** Loops back to the beginning and plays indefinitely. */
    LOOP,

    /** Plays once and freezes on the last keyframe indefinitely. */
    HOLD_ON_LAST_FRAME
}