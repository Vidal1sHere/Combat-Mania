package com.vidal1shere.combatmania.client.animation.item;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationChannel;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemAnimationTarget;
import com.vidal1shere.combatmania.client.animation.keyframe.KeyframeVecs;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import org.joml.Vector3f;

public class SwordAnimations {
    public static final Vector3f SWORD_PIVOT = new Vector3f(0.63f, -0.5f, -0.72f);

    public static ItemAnimationDefinition sweep() {
        return ItemAnimationDefinition.Builder
                .withLength(0.5f)
                .pivot(SWORD_PIVOT)
                .addChannel("position", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.POSITION,
                        new Keyframe(0.0f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.posVec(-1.0f, -0.25f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.48f, KeyframeVecs.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .addChannel("rotation", new ItemAnimationChannel(
                        ItemAnimationTarget.Targets.ROTATION,
                        new Keyframe(0.0f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.1f, KeyframeVecs.degreeVec(0.0f, 190.0f, -100.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.15f, KeyframeVecs.degreeVec(0.0f, 160.0f, -100.0f), AnimationChannel.Interpolations.LINEAR),
                        new Keyframe(0.48f, KeyframeVecs.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.LINEAR)
                ))
                .build();
    }

    public static ItemAnimationDefinition joustEngaged() {
        return SpearAnimations.joustEngaged(SWORD_PIVOT);
    }

    public static ItemAnimationDefinition joustTired() {
        return SpearAnimations.joustTired(SWORD_PIVOT);
    }

    public static ItemAnimationDefinition joustDisengaged() {
        return SpearAnimations.joustDisengaged(SWORD_PIVOT);
    }
}
