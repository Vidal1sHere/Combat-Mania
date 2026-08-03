package com.vidal1shere.combatmania.client.animation.data;

import com.vidal1shere.combatmania.client.animation.Interpolation;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered list of {@link Keyframe}s with per-segment {@link Interpolation} and optional looping.
 *
 * <pre>{@code
 * AnimationTrack track = AnimationTrack.builder()
 *     .keyframe(Keyframe.identity(0f))
 *     .keyframe(new Keyframe(0.15f, 0, -0.1f, 0, -15, 0, 0, 1f), Interpolation.SMOOTH)
 *     .keyframe(Keyframe.identity(0.3f), Interpolation.EASE_OUT)
 *     .loop(true)
 *     .build();
 * }</pre>
 */
public final class AnimationTrack {

    private final List<Keyframe> keyframes;
    private final List<Interpolation> segments;
    private final boolean loop;
    private final float duration;

    private AnimationTrack(List<Keyframe> keyframes, List<Interpolation> segments, boolean loop) {
        if (keyframes.isEmpty()) throw new IllegalArgumentException("AnimationTrack needs at least one keyframe");
        this.keyframes = List.copyOf(keyframes);
        this.segments = List.copyOf(segments);
        this.loop = loop;
        this.duration = keyframes.get(keyframes.size() - 1).time();
    }

    public Keyframe evaluate(float time, float partial) {
        float t = time + partial / 20f;

        if (duration <= 0f) return keyframes.get(0);

        if (loop) {
            t = t % duration;
            if (t < 0) t += duration;
        } else {
            t = Math.max(0f, Math.min(t, duration));
        }

        int i = findSegment(t);

        if (i >= keyframes.size() - 1) return keyframes.get(keyframes.size() - 1);

        Keyframe from = keyframes.get(i);
        Keyframe to = keyframes.get(i + 1);
        float segLen = to.time() - from.time();
        float localT = (segLen <= 0f) ? 1f : (t - from.time()) / segLen;

        return segments.get(i).apply(from, to, localT);
    }

    public float getDuration() {
        return duration;
    }

    public boolean isLoop() {
        return loop;
    }

    private int findSegment(float t) {
        for (int i = 0; i < keyframes.size() - 1; i++) {
            if (t <= keyframes.get(i + 1).time()) return i;
        }
        return keyframes.size() - 1;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final List<Keyframe> keyframes = new ArrayList<>();
        private final List<Interpolation> segments = new ArrayList<>();
        private boolean loop = false;

        public Builder keyframe(Keyframe kf) {
            return keyframe(kf, Interpolation.LINEAR);
        }

        public Builder keyframe(Keyframe kf, Interpolation interpolation) {
            keyframes.add(kf);
            segments.add(keyframes.size() > 1 ? interpolation : Interpolation.LINEAR);
            return this;
        }

        public Builder loop(boolean loop) {
            this.loop = loop;
            return this;
        }

        public AnimationTrack build() {
            List<Keyframe> kfSorted = new ArrayList<>(keyframes);
            List<Interpolation> segSorted = new ArrayList<>(segments);

            for (int i = 1; i < kfSorted.size(); i++) {
                for (int j = i; j > 0 && kfSorted.get(j - 1).time() > kfSorted.get(j).time(); j--) {
                    Keyframe tmpK = kfSorted.remove(j);
                    kfSorted.add(j - 1, tmpK);
                    Interpolation tmpS = segSorted.remove(j);
                    segSorted.add(j - 1, tmpS);
                }
            }

            return new AnimationTrack(kfSorted, segSorted, loop);
        }
    }
}