package com.vidal1shere.combatmania.client.animation.item.keyframe;

import net.minecraft.client.animation.Keyframe;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * An animation channel for first-person item animations.
 * Uses ItemAnimationTarget instead of AnimationChannel.Target.
 */
@OnlyIn(Dist.CLIENT)
public record ItemAnimationChannel(ItemAnimationTarget target, Keyframe... keyframes) {
}