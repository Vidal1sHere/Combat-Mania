package com.vidal1shere.combatmania.event;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemKeyframeAnimations;
import com.vidal1shere.combatmania.client.animation.keyframe.ItemTransformAccumulator;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationStates;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class ItemAnimationHandler {

    private static final ItemTransformAccumulator accumulator = new ItemTransformAccumulator();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if (!(item instanceof IAnimatedItem animated)) return;

        InteractionHand hand = event.getHand();
        ItemAnimationStates states = player.getData(CMAttachments.ITEM_ANIMATIONS);
        ItemAnimationState state = states.get(hand);

        float ageInTicks = player.tickCount + event.getPartialTick();
        float speed = animated.getAnimationSpeed(stack, hand, player, state);

        animated.setupAnim(stack, hand, player, state, ageInTicks, event.getPartialTick());

        if (!state.isStarted()) return;

        ItemAnimationDefinition definition = animated.getAnimation(stack, hand, player, state);
        if (definition == null) return;


        ItemKeyframeAnimations.animate(
            event.getPoseStack(),
            definition,
            state,
            accumulator,
            ageInTicks,
            speed
        );
        
        if (definition.debug()) {
            ItemTransformAccumulator.debugRenderPivot(event.getPoseStack(), event.getMultiBufferSource(), definition.pivot());
        }
    }
}