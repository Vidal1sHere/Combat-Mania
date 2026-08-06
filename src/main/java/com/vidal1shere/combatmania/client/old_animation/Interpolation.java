package com.vidal1shere.combatmania.client.old_animation;

import com.vidal1shere.combatmania.client.old_animation.data.Keyframe;

/** Interpolation mode between two consecutive keyframes. */
public enum Interpolation {

    HOLD {
        @Override
        public Keyframe apply(Keyframe from, Keyframe to, float t) {
            return from;
        }
    },

    LINEAR {
        @Override
        public Keyframe apply(Keyframe from, Keyframe to, float t) {
            return Keyframe.lerp(from, to, t);
        }
    },

    SMOOTH {
        @Override
        public Keyframe apply(Keyframe from, Keyframe to, float t) {
            return Keyframe.smoothStep(from, to, t);
        }
    },

    EASE_IN {
        @Override
        public Keyframe apply(Keyframe from, Keyframe to, float t) {
            return Keyframe.lerp(from, to, t * t);
        }
    },

    EASE_OUT {
        @Override
        public Keyframe apply(Keyframe from, Keyframe to, float t) {
            return Keyframe.lerp(from, to, 1f - (1f - t) * (1f - t));
        }
    };

    public abstract Keyframe apply(Keyframe from, Keyframe to, float t);
}