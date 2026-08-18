package com.vidal1shere.combatmania.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.api.IVanillaStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemKeyframeAnimations;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemTransformAccumulator;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationStates;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    
    @Unique
    private static final ItemTransformAccumulator ACCUMULATOR = new ItemTransformAccumulator();

    @Shadow
    private ItemStack mainHandItem;

    @ModifyVariable(
            method = "tick",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"),
            ordinal = 0
    )
    private float suppressAttackDip(float attackStrength) {
        if (IVanillaStrikeWeapon.isVanillaDisabled(mainHandItem)) {
            return attackStrength;
        }
        
        if (mainHandItem.getItem() instanceof IAnimatedItem) {
            return 1.0F;
        }
        
        return attackStrength;
    }

    @Inject(
            method = "renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    ordinal = 1
            )
    )
    private void combatmania_applyItemAnimation(
            AbstractClientPlayer player,
            float partialTicks,
            float pitch,
            InteractionHand hand,
            float swingProgress,
            ItemStack stack,
            float equippedProgress,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int combinedLight,
            CallbackInfo ci
    ) {
        if (!(stack.getItem() instanceof IAnimatedItem animated)) return;
        if (!(player instanceof LocalPlayer)) return;

        ItemAnimationStates states = player.getData(CMAttachments.ITEM_ANIMATIONS);
        ItemAnimationState state = states.get(hand);

        float ageInTicks = player.tickCount + partialTicks;

        animated.setupAnim(stack, hand, player, state, ageInTicks, partialTicks);

        if (!state.isStarted()) return;

        ItemAnimationDefinition definition = animated.getAnimation(stack, hand, player, state);
        if (definition == null) return;

        float speed = animated.getAnimationSpeed(stack, hand, player, state);

        ItemKeyframeAnimations.animate(poseStack, definition, state, ACCUMULATOR, ageInTicks, speed);

        if (definition.debug()) {
            ItemTransformAccumulator.debugRenderPivot(poseStack, buffer, definition.pivot());
        }
    }
}