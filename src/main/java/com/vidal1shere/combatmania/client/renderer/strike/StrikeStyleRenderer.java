package com.vidal1shere.combatmania.client.renderer.strike;

import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.hud.config.StrikeIndicator;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface StrikeStyleRenderer<T extends StrikeStyle> {

    default void onAnimStart(T style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    default void onAnimStop(T style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {}

    default void setupAnim(T style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {}

    @Nullable
    default ItemAnimationDefinition getAnimation(T style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return state.getActiveDefinition();
    }

    default float getAnimationSpeed(T style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        if (!style.syncAnimationSpeed()) return 1.0F;
        return (float) player.getAttributeValue(style.getSpeedAttribute());
    }

    default InteractionHand[] animatedHands(T style, ItemStack stack, Player player) {
        return new InteractionHand[]{InteractionHand.MAIN_HAND};
    }

    @Nullable
    default StrikeIndicator getCustomStrikeIndicator(T style) { return null; }

    default void preRenderStrikeIndicator(T style, GuiGraphics guiGraphics, Player player, ItemStack stack, StrikeIndicator indicator, int x, int y, boolean rotated, float partialTick) {}

    default void postRenderStrikeIndicator(T style, GuiGraphics guiGraphics, Player player, ItemStack stack, StrikeIndicator indicator, int x, int y, boolean rotated, float partialTick) {}
}