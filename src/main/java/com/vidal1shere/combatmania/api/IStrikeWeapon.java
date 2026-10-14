package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeRenderers;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeStyleRenderer;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
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

    default StrikeStyle getActiveStyle(Player player, StrikeSlot slot) {
        StrikeData data = player.getData(CMAttachments.STRIKE);
        ResourceLocation id = data.getActiveStyle(slot);
        if (id == null) return null;

        var registryOpt = player.level().registryAccess().registry(CombatMania.REGISTRY_KEY);
        return registryOpt.map(registry -> registry.get(id)).orElse(null);
    }

    default StrikeStyle getLastUsedStyle(Player player) {
        StrikeData data = player.getData(CMAttachments.STRIKE);
        ResourceLocation id = data.getLastUsedStyle();
        if (id == null) return null;

        var registryOpt = player.level().registryAccess().registry(CombatMania.REGISTRY_KEY);
        return registryOpt.map(registry -> registry.get(id)).orElse(null);
    }

    default boolean hasActiveStyle(Player player, StrikeSlot slot) {
        return getActiveStyle(player, slot) != null;
    }

    @Override
    default void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        StrikeStyle style = getLastUsedStyle(player);
        if (style == null) return;

        StrikeStyleRenderer<StrikeStyle> renderer = StrikeRenderers.getRenderer(style);
        if (renderer != null) {
            renderer.setupAnim(style, stack, hand, player, state, ageInTicks, partialTick);
        }
    }

    @Override
    default ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        StrikeStyle style = getLastUsedStyle(player);
        if (style == null) return null;

        StrikeStyleRenderer<StrikeStyle> renderer = StrikeRenderers.getRenderer(style);
        if (renderer != null) {
            return renderer.getAnimation(style, stack, hand, player, state);
        }
        return null;
    }

    @Override
    default float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        StrikeStyle style = getLastUsedStyle(player);
        if (style == null) return 1.0F;

        StrikeStyleRenderer<StrikeStyle> renderer = StrikeRenderers.getRenderer(style);
        if (renderer != null) {
            return renderer.getAnimationSpeed(style, stack, hand, player, state);
        }
        return 1.0F;
    }

    static boolean isStrikeWeapon(ItemStack stack) {
        if (stack.getItem() instanceof IVanillaStrikeWeapon weapon) {
            return weapon.isEnabled();
        }

        return stack.getItem() instanceof IStrikeWeapon;
    }
}