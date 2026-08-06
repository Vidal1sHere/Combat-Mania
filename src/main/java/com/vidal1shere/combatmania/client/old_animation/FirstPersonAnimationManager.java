package com.vidal1shere.combatmania.client.old_animation;

import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.client.old_animation.data.AnimationState;
import com.vidal1shere.combatmania.client.old_animation.data.AnimationTrack;
import com.vidal1shere.combatmania.client.old_animation.data.Keyframe;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

/** Singleton that tracks and evaluates the active animation state for each arm. */
public final class FirstPersonAnimationManager {

    public static final FirstPersonAnimationManager INSTANCE = new FirstPersonAnimationManager();

    private @Nullable AnimationState rightState;
    private @Nullable AnimationState leftState;
    private @Nullable AnimationTrack rightTrack;
    private @Nullable AnimationTrack leftTrack;

    private FirstPersonAnimationManager() {}

    public void tick(float deltaSeconds) {
        if (rightState != null) rightState.tick(deltaSeconds);
        if (leftState != null) leftState.tick(deltaSeconds);
    }

    public @Nullable Keyframe evaluate(LocalPlayer player, HumanoidArm arm, ItemStack stack, float partialTick) {
        if (!(stack.getItem() instanceof IAnimatedItem animated)) return null;

        AnimationTrack desiredTrack = animated.getAnimation(player, arm, stack);
        if (desiredTrack == null) return null;

        return getOrCreateState(arm, desiredTrack).evaluate(partialTick);
    }

    private AnimationState getOrCreateState(HumanoidArm arm, AnimationTrack track) {
        if (arm == HumanoidArm.RIGHT) {
            if (rightTrack != track) {
                rightTrack = track;
                rightState = new AnimationState(track);
            }
            return rightState;
        } else {
            if (leftTrack != track) {
                leftTrack = track;
                leftState = new AnimationState(track);
            }
            return leftState;
        }
    }

    public void reset() {
        rightState = leftState = null;
        rightTrack = leftTrack = null;
    }
}