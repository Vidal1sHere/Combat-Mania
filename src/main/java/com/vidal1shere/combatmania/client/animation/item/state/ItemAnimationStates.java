package com.vidal1shere.combatmania.client.animation.item.state;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.InteractionHand;

public class ItemAnimationStates {

    public static final Codec<ItemAnimationStates> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemAnimationState.CODEC.fieldOf("main_hand")
                            .forGetter(s -> s.get(InteractionHand.MAIN_HAND)),
                    ItemAnimationState.CODEC.fieldOf("off_hand")
                            .forGetter(s -> s.get(InteractionHand.OFF_HAND))
            ).apply(instance, ItemAnimationStates::new)
    );

    private final ItemAnimationState mainHand;
    private final ItemAnimationState offHand;

    public ItemAnimationStates() {
        this(new ItemAnimationState(), new ItemAnimationState());
    }

    public ItemAnimationStates(ItemAnimationState mainHand, ItemAnimationState offHand) {
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    public ItemAnimationState get(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? mainHand : offHand;
    }

    public void stopAll() {
        mainHand.stop();
        offHand.stop();
    }
}