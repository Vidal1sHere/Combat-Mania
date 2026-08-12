package com.vidal1shere.combatmania.client.animation.item;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import org.joml.Vector3f;

public class LanceAnimations {
    public static final Vector3f LANCE_PIVOT = new Vector3f(0.71f, -0.64f, -0.61f);

    public static ItemAnimationDefinition joustEngaged() {
        return ItemAnimationDefinition.Builder
                .withLength(JoustProperties.ENGAGED_DURATION)
                .pivot(LANCE_PIVOT)
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
                .pivot(LANCE_PIVOT)
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
                .pivot(LANCE_PIVOT)
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

//    public static ItemAnimationDefinition joustEngaged() {
//        return ItemAnimationDefinition.Builder
//                .withLength(JoustProperties.ENGAGED_DURATION)
//                .pivot(LANCE_PIVOT)
//                .addChannel("position", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.POSITION,
//                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
////                        new Keyframe(0.15f, KeyframeVecs.posVec(0.0f, 0.1f, 0.035f), AnimationChannel.Interpolations.LINEAR),
////                        new Keyframe(0.35f, KeyframeVecs.posVec(0, 0, 0.02f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.15f, KeyframeVecs.posVec(0, -0.35f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.35f, KeyframeVecs.posVec(0, -0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR)
//                ))
//                .addChannel("rotation", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.ROTATION,
//                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
////                        new Keyframe(0.1f, KeyframeVecs.degreeVec(5.0F, -2.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
////                        new Keyframe(0.25f, KeyframeVecs.degreeVec(0f, 90f, 0.0f), AnimationChannel.Interpolations.LINEAR),
////                        new Keyframe(0.45f, KeyframeVecs.degreeVec(0f, 90f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.25f, KeyframeVecs.degreeVec(-180.0f, 70.0f, 90.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.5f, KeyframeVecs.degreeVec(-180.0f, 70.0f, 90.0f), AnimationChannel.Interpolations.LINEAR)
//                        
//                ))
//                .build();
//    }
//
//    public static ItemAnimationDefinition joustTired() {
//        return ItemAnimationDefinition.Builder
//                .withLength(JoustProperties.TIRED_DURATION)
//                .pivot(LANCE_PIVOT)
//                .addChannel("position", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.POSITION,
//                        new Keyframe(0.0f, KeyframeVecs.posVec(0, -0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR)
//                ))
//                .addChannel("rotation", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.ROTATION,
//                        new Keyframe(0.0f, KeyframeVecs.degreeVec(-180.0f, 70.0f, 90.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(2.75f, KeyframeVecs.degreeVec(-120.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
//                ))
//                .build();
//    }
//
//    public static ItemAnimationDefinition joustDisengaged() {
//        return ItemAnimationDefinition.Builder
//                .withLength(JoustProperties.DISENGAGED_DURATION)
//                .pivot(LANCE_PIVOT)
//                .addChannel("position", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.POSITION,
//                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, -0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.85f, KeyframeVecs.posVec(0.0f, -0.3f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(1.1f, KeyframeVecs.posVec(0.0f, -0.35f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(3.25f, KeyframeVecs.posVec(0.0f, -0.35f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(3.5f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
//                ))
//                .addChannel("rotation", new ItemAnimationChannel(
//                        ItemAnimationTarget.Targets.ROTATION,
//                        new Keyframe(0f, KeyframeVecs.degreeVec(-120.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.6f, KeyframeVecs.degreeVec(-120.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(0.85f, KeyframeVecs.degreeVec(-100f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(1.1f, KeyframeVecs.degreeVec(-150f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(3.25f, KeyframeVecs.degreeVec(-150f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
//                        new Keyframe(3.5f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
//                ))
//                .build();
//    }
}
