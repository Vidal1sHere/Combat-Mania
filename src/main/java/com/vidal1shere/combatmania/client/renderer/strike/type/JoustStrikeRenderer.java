package com.vidal1shere.combatmania.client.renderer.strike.type;

import com.vidal1shere.combatmania.client.animation.item.definition.SpearAnimations;
import com.vidal1shere.combatmania.client.animation.item.keyframe.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.state.ItemAnimationState;
import com.vidal1shere.combatmania.client.hud.config.StrikeIndicator;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeStyleRenderer;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.styles.JoustStrikeStyle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class JoustStrikeRenderer implements StrikeStyleRenderer<JoustStrikeStyle> {
    @Override
    public void setupAnim(JoustStrikeStyle style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        if (hand != InteractionHand.MAIN_HAND) return;

        JoustData data = player.getData(CMAttachments.JOUST);
        JoustData.Stage stage = data.getStage();

        if (!data.hasQueuedAnimation()) {
            return;
        }

        ItemAnimationState.LoopType loopType = switch (stage) {
            case NONE, DISENGAGED -> ItemAnimationState.LoopType.PLAY_ONCE;
            default -> ItemAnimationState.LoopType.HOLD_ON_LAST_FRAME;
        };

        ItemAnimationDefinition definition = defaultJoustAnimation(stage);

        if (definition != null) {
            JoustData.consumeAnimation(player);
            state.setActiveDefinition(definition);
            state.start(player.tickCount, loopType);
        }
    }

    @Override
    public void onAnimStop(JoustStrikeStyle style, ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, int tickCount) {
        state.stop();
    }

    @Override
    public @Nullable StrikeIndicator getCustomStrikeIndicator(JoustStrikeStyle style) {
        return StrikeIndicator.JOUST;
    }

    private ItemAnimationDefinition defaultJoustAnimation(JoustData.Stage stage) {
        return switch (stage) {
            case NONE -> null;
            case ENGAGED -> SpearAnimations.joustEngaged();
            case TIRED -> SpearAnimations.joustTired();
            case DISENGAGED -> SpearAnimations.joustDisengaged();
        };
    }
}
