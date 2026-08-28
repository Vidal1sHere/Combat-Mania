package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public interface IStrikeWeapon extends IAnimatedItem {

    DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot);

    default DeferredHolder<StrikeStyle, ? extends StrikeStyle> getPrimaryStyle() {
        return getStyle(StrikeSlot.PRIMARY);
    }

    default DeferredHolder<StrikeStyle, ? extends StrikeStyle> getSecondaryStyle() {
        return getStyle(StrikeSlot.SECONDARY);
    }

    default HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        return null;
    }

    private static int styleToId(StrikeStyle style) {
        if (style == null) return -1;
        return CMStrikeStyles.REGISTRY.getId(style);
    }

    private static StrikeStyle idToStyle(int id) {
        return CMStrikeStyles.REGISTRY.byId(id);
    }

    default void setActiveStyle(ItemStack stack, StrikeStyle style) {
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return;
        stack.set(CMDataComponents.ACTIVE_STRIKE_STYLE.get(), styleToId(style));
    }

    default StrikeStyle getActiveStyle(ItemStack stack) {
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return null;
        return idToStyle(stack.getOrDefault(CMDataComponents.ACTIVE_STRIKE_STYLE.get(), -1));
    }

    default void setLastUsedStyle(ItemStack stack, StrikeStyle style) {
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return;
        stack.set(CMDataComponents.LAST_USED_STRIKE_STYLE.get(), styleToId(style));
    }

    default StrikeStyle getLastUsedStyle(ItemStack stack) {
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return null;
        return idToStyle(stack.getOrDefault(CMDataComponents.LAST_USED_STRIKE_STYLE.get(), -1));
    }

    default boolean hasActiveStyle(ItemStack stack) {
        return getActiveStyle(stack) != null;
    }

    @Override
    default void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        StrikeStyle style = getLastUsedStyle(stack);
        if (style == null) return;
        style.setupAnim(stack, hand, player, state, ageInTicks, partialTick);
    }

    @Override
    default ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        StrikeStyle style = getLastUsedStyle(stack);
        if (style == null) return null;
        return style.getAnimation(stack, hand, player, state);
    }

    @Override
    default float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        StrikeStyle style = getLastUsedStyle(stack);
        if (style == null) return 1.0F;
        return style.getAnimationSpeed(stack, hand, player, state);
    }
    
    static boolean isStrikeWeapon(ItemStack stack) {
        if (stack.getItem() instanceof IVanillaStrikeWeapon weapon) {
            return weapon.isEnabled();
        }
        
        return stack.getItem() instanceof IStrikeWeapon;
    }
}