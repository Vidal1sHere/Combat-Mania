package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.client.animation.data.AnimationTrack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Implement on your {@link net.minecraft.world.item.Item} to opt in to the first-person animation system.
 *
 * <pre>{@code
 * public class MyItem extends Item implements IAnimatedItem {
 *
 *     private static final AnimationTrack IDLE = AnimationTrack.builder()
 *         .keyframe(Keyframe.identity(0f))
 *         .keyframe(new Keyframe(0.5f, 0, -0.02f, 0, 0, 0, 0, 1f), Interpolation.SMOOTH)
 *         .keyframe(Keyframe.identity(1f), Interpolation.SMOOTH)
 *         .loop(true)
 *         .build();
 *
 *     @Override
 *     public AnimationTrack getAnimation(LocalPlayer player, HumanoidArm arm, ItemStack stack) {
 *         return IDLE;
 *     }
 * }
 * }</pre>
 */
public interface IAnimatedItem {

    /**
     * Returns the track that should play right now, or {@code null} to fall back to vanilla rendering.
     * Called every render frame, so switching tracks dynamically is safe.
     */
    @Nullable
    AnimationTrack getAnimation(LocalPlayer player, HumanoidArm arm, ItemStack stack);

    /**
     * Whether to mirror the animation for the left hand by flipping X translation and Y/Z rotation.
     * Set to {@code false} if the left-hand animation is defined explicitly.
     */
    default boolean mirrorForLeftHand() {
        return true;
    }
}