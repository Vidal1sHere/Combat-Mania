package com.vidal1shere.combatmania.client.animation.keyframe;

import com.mojang.math.Axis;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

/**
 * Defines how an interpolated keyframe vector is applied to an ItemTransformAccumulator.
 */
@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface ItemAnimationTarget {

    void apply(ItemTransformAccumulator accumulator, Vector3f value);

    @OnlyIn(Dist.CLIENT)
    final class Targets {

        public static final ItemAnimationTarget POSITION = (accumulator, value) ->
            accumulator.addTranslation(value.x(), value.y(), value.z());

        public static final ItemAnimationTarget ROTATION = (accumulator, value) -> {
            accumulator.addRotation(Axis.XP.rotation(value.x()));
            accumulator.addRotation(Axis.YP.rotation(value.y()));
            accumulator.addRotation(Axis.ZP.rotation(value.z()));
        };

        public static final ItemAnimationTarget SCALE = (accumulator, value) ->
            accumulator.addScale(value.x() + 1.0f, value.y() + 1.0f, value.z() + 1.0f);

        private Targets() {}
    }
}