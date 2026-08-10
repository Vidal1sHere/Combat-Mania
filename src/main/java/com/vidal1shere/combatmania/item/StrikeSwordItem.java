package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.LanceAnimations;
import com.vidal1shere.combatmania.client.animation.item.SwordAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        return null;
    }

    @Override
    public void setupAnim(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state, float ageInTicks, float partialTick) {
        state.startIfStopped((int) ageInTicks, ItemAnimationState.LoopType.PLAY_ONCE);
    }

    @Override
    public float getAnimationSpeed(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return 1.0f;
    }

    @Override
    public ItemAnimationDefinition getAnimation(ItemStack stack, InteractionHand hand, Player player, ItemAnimationState state) {
        return ItemAnimationDefinition.debug(1);
    }
}
