package com.vidal1shere.combatmania.client.animation.item;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import org.joml.Vector3f;

public class HammerAnimations {
    public static final Vector3f PIVOT = new Vector3f(0, 0, -0.01f);

    public static ItemAnimationDefinition whack() {
        return ItemAnimationDefinition.Builder
                .withLength(0.75f)
                .pivot(PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(-0.25f, -1.25f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(-0.5f, 0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(-0.5f, 0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(60.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(-145.0f, 0, 60.0f), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.2f, KeyframeVecs.degreeVec(-130.0f, 0, 45.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
    
    public static ItemAnimationDefinition shockwaveLift() {
        return ItemAnimationDefinition.Builder
                .withLength(0.2f)
                .pivot(PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(-0.25f, -1.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.degreeVec(60.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition shockwaveSwing() {
        return ItemAnimationDefinition.Builder
                .withLength(0.2f)
                .pivot(PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(-0.25f, -1.25f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(-0.5f, 0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(-0.5f, 0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(60.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(-145.0f, 0, 60.0f), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.2f, KeyframeVecs.degreeVec(-130.0f, 0, 45.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
    
    public static ItemAnimationDefinition shockwaveHit() {
        return ItemAnimationDefinition.Builder
                .withLength(0.55f)
                .pivot(PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(-0.5f, 0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(-130.0f, 0, 45.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition shockwaveEarlyHit() {
        return ItemAnimationDefinition.Builder
                .withLength(0.55f)
                .pivot(PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(-0.25f, -1.25f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(-0.5f, 0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(-0.5f, 0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(60.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(-145.0f, 0, 60.0f), AnimationChannel.Interpolations.CATMULLROM),
                        new Keyframe(0.2f, KeyframeVecs.degreeVec(-130.0f, 0, 45.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.55f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
}
