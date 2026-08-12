package com.vidal1shere.combatmania.data.attachment;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.event.custom.ItemInHandChangeEvent;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Objects;

public class ItemInHandData {

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemInHandData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        data.encodeHand(buf, data.mainHand);
                        data.encodeHand(buf, data.offHand);
                    },
                    buf -> {
                        ItemInHandData data = new ItemInHandData();
                        data.decodeHand(buf, data.mainHand);
                        data.decodeHand(buf, data.offHand);
                        return data;
                    }
            );

    private final Hand mainHand = new Hand(InteractionHand.MAIN_HAND);
    private final Hand offHand = new Hand(InteractionHand.OFF_HAND);

    public void capture(Player player) {
        boolean mainCaptured = mainHand.capture(player);
        boolean offCaptured = offHand.capture(player);

        if (mainCaptured || offCaptured) {
            player.syncData(CMAttachments.ITEM_IN_HAND);
        }
    }

    public ItemStack getCurrentItemStack(InteractionHand hand) {
        return getHand(hand).getCurrentStack();
    }

    public ItemStack getPreviousItemStack(InteractionHand hand) {
        return getHand(hand).getPreviousStack();
    }

    public int getCurrentSlot(InteractionHand hand) {
        return getHand(hand).getCurrentSlot();
    }

    public int getPreviousSlot(InteractionHand hand) {
        return getHand(hand).getPreviousSlot();
    }

    private Hand getHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? mainHand : offHand;
    }

    private void encodeHand(RegistryFriendlyByteBuf buf, Hand data) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, data.currentStack);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, data.previousStack);

        buf.writeInt(data.currentSlot);
        buf.writeInt(data.previousSlot);
    }

    private void decodeHand(RegistryFriendlyByteBuf buf, Hand data) {
        data.currentStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        data.previousStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);

        data.currentSlot = buf.readInt();
        data.previousSlot = buf.readInt();
    }

    private static class Hand {

        private final InteractionHand hand;

        private ItemStack currentStack = ItemStack.EMPTY;
        private ItemStack previousStack = ItemStack.EMPTY;

        private int currentSlot = -1;
        private int previousSlot = -1;

        private boolean initialized;

        private Hand(InteractionHand hand) {
            this.hand = hand;
        }

        private boolean capture(Player player) {
            ItemStack stack = player.getItemInHand(hand);
            int slot = getSlot(player);

            if (!initialized) {
                currentStack = stack.copy();
                currentSlot = slot;
                initialized = true;

                return false;
            }

            if (ItemStack.isSameItem(currentStack, stack) && currentSlot == slot) {
                return false;
            }

            ItemStack oldStack = currentStack.copy();
            int oldSlot = currentSlot;

            boolean sameComponents = Objects.equals(stack.getComponents(), currentStack.getComponents());

            previousStack = currentStack;
            previousSlot = currentSlot;

            currentStack = stack.copy();
            currentSlot = slot;

            NeoForge.EVENT_BUS.post(
                    new ItemInHandChangeEvent(
                            player, hand,
                            oldStack, currentStack.copy(),
                            oldSlot, currentSlot, sameComponents
                    )
            );

            return true;
        }

        private int getSlot(Player player) {
            return hand == InteractionHand.MAIN_HAND
                    ? player.getInventory().selected
                    : 40;
        }

        public ItemStack getCurrentStack() {
            return currentStack;
        }

        public ItemStack getPreviousStack() {
            return previousStack;
        }

        public int getCurrentSlot() {
            return currentSlot;
        }

        public int getPreviousSlot() {
            return previousSlot;
        }
    }

    @EventBusSubscriber(modid = CombatMania.MOD_ID)
    private static final class Handler {
        
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();

            if (player.level().isClientSide()) {
                return;
            }

            ItemInHandData data = player.getData(CMAttachments.ITEM_IN_HAND);

            data.capture(player);
        }
    }
}
