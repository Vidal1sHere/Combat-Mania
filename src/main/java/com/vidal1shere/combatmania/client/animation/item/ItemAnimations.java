package com.vidal1shere.combatmania.client.animation.item;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;

public class ItemAnimations {
    public static ItemAnimationDefinition identity(float length) {
        return ItemAnimationDefinition.Builder
                .withLength(length)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(length, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(length, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
    
    public static ItemAnimationDefinition spearJab() {
        return ItemAnimationDefinition.Builder
                .withLength(1.0f)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.5f, -0.8f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.5f, -1.1f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(0.0f, -0.5f, -1.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, -0.5f, -1.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(-70f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.degreeVec(-70f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition stab() {
        return ItemAnimationDefinition.Builder
                .withLength(0.55f)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.2f, -1.25f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.2f, -1.75f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.3f, KeyframeVecs.posVec(0.0f, -0.2f, -1.5f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.55f, KeyframeVecs.posVec(0.0f, -0.2f, -1.5f), AnimationChannel.Interpolations.LINEAR),
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
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(-0.5f, 0.5f, -1.25f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(-0.5f, 1.2f, -1.75f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.35f, KeyframeVecs.posVec(-0.5f, 0.4f, -1.25f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.posVec(-0.5f, 0.4f, -1.2f), AnimationChannel.Interpolations.LINEAR),
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
