package com.vidal1shere.combatmania.client.animation.item;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import org.joml.Vector3f;

public class DaggerAnimations {
    public static final Vector3f DAGGER_PIVOT = new Vector3f(0.63f, -0.5f, -0.72f);
    
    public static ItemAnimationDefinition stab() {
        return ItemAnimationDefinition.Builder
                .withLength(0.55f)
                .pivot(DAGGER_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.2f, -0.2f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3f, KeyframeVecs.posVec(0.0f, -0.2f, -0.1f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(-110f, 0.0f, 0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(-110f, 0.0f, 0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition backStab() {
        return ItemAnimationDefinition.Builder
                .withLength(0.75f)
                .pivot(DAGGER_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.5f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(0.0f, 0.1f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.35f, KeyframeVecs.posVec(0.0f, -0.2f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.posVec(0.0f, -0.2f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(-200f, -20.0f, 0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.4f, KeyframeVecs.degreeVec(-200f, -20.0f, 0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.7f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
}
