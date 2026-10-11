package com.vidal1shere.combatmania.client.animation.item.definition;

import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.item.keyframe.KeyframeVecs;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import org.joml.Vector3f;

public class SpearAnimations {
    public static final Vector3f SPEAR_PIVOT = new Vector3f(0.2f, 0, 0.03f);
    
    public static ItemAnimationDefinition jab() {
        return ItemAnimationDefinition.Builder
                .withLength(1.0f)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.25f, 0.5f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.25f, -0.11f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(0.0f, -0.25f, 0.11f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, -0.25f, 0.11f), AnimationChannel.Interpolations.LINEAR),
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

    public static ItemAnimationDefinition sweep() {
        return ItemAnimationDefinition.Builder
                .withLength(0.5f)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(-1.0f, -0.15f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25f, KeyframeVecs.posVec(-1.0f, -0.1f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(0.0f, 190.0f, -90.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25f, KeyframeVecs.degreeVec(0.0f, 160.0f, -90.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition whack() {
        return ItemAnimationDefinition.Builder
                .withLength(0.5f)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.5f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, 0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25f, KeyframeVecs.posVec(0.0f, -0.2f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(30.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(-130.0f, 0.0f, 40.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.5f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition tridentJab() {
        return ItemAnimationDefinition.Builder
                .withLength(1.0f)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(0.0f, -0.25f, 0.5f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, -0.25f, -0.5f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.2f, KeyframeVecs.posVec(0.0f, -0.25f, -0.25f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.75f, KeyframeVecs.posVec(0.0f, -0.25f, -0.25f), AnimationChannel.Interpolations.LINEAR),
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

    public static ItemAnimationDefinition joustEngaged() {
        return ItemAnimationDefinition.Builder
                .withLength(JoustProperties.ENGAGED_DURATION)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, 0.1f, 0.035f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.35f, KeyframeVecs.posVec(0, 0, 0.02f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(5.0F, -2.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.25f, KeyframeVecs.degreeVec(-75f, 90.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.45f, KeyframeVecs.degreeVec(-65f, 90.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition joustTired() {
        return ItemAnimationDefinition.Builder
                .withLength(JoustProperties.TIRED_DURATION)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0, 0, 0.02f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0f, KeyframeVecs.degreeVec(-65f, 90.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(2.75f, KeyframeVecs.degreeVec(-65f, 5.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)

                ))
                .build();
    }

    public static ItemAnimationDefinition joustDisengaged() {
        return ItemAnimationDefinition.Builder
                .withLength(JoustProperties.DISENGAGED_DURATION)
                .pivot(SPEAR_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.02f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.85f, KeyframeVecs.posVec(0.0f, 0.0f, 0.02f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1f, KeyframeVecs.posVec(0.0f, -0.11f, 0.06f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.25f, KeyframeVecs.posVec(0.0f, -0.11f, 0.06f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.5f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0f, KeyframeVecs.degreeVec(-65f, 5.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.6f, KeyframeVecs.degreeVec(-65f, 5.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.85f, KeyframeVecs.degreeVec(-45f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(1.1f, KeyframeVecs.degreeVec(-100f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.25f, KeyframeVecs.degreeVec(-100f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(3.5f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }
}
