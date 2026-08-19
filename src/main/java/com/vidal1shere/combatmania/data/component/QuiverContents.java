package com.vidal1shere.combatmania.data.component;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class QuiverContents {

    public static final QuiverContents EMPTY = new QuiverContents(List.of());
    public static final int MAX_ARROWS = 64;

    public static final Codec<QuiverContents> CODEC =
        ItemStack.CODEC.listOf().xmap(QuiverContents::new, c -> c.items);

    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverContents> STREAM_CODEC =
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(QuiverContents::new, c -> c.items);

    private final List<ItemStack> items;
    private final int count;

    QuiverContents(List<ItemStack> items, int count) {
        this.items = items;
        this.count = count;
    }

    public QuiverContents(List<ItemStack> items) {
        this(items, computeCount(items));
    }

    private static int computeCount(List<ItemStack> items) {
        return items.stream().mapToInt(ItemStack::getCount).sum();
    }

    private static boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ArrowItem;
    }

    public List<ItemStack> items() {
        return this.items;
    }

    public Iterable<ItemStack> itemsCopy() {
        return Lists.transform(this.items, ItemStack::copy);
    }

    public int count() {
        return this.count;
    }

    public int size() {
        return this.items.size();
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public boolean isFull() {
        return this.count >= MAX_ARROWS;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof QuiverContents q)
            return this.count == q.count && ItemStack.listMatches(this.items, q.items);
        return false;
    }

    @Override
    public int hashCode() {
        return ItemStack.hashStackList(this.items);
    }

    @Override
    public String toString() {
        return "QuiverContents" + this.items;
    }
    
    public static class Mutable {
        private final List<ItemStack> items;
        private int count;

        public Mutable(QuiverContents contents) {
            this.items = new ArrayList<>(contents.items);
            this.count = contents.count;
        }

        private int findStackIndex(ItemStack stack) {
            if (!stack.isStackable()) return -1;
            for (int i = 0; i < items.size(); i++) {
                if (ItemStack.isSameItemSameComponents(items.get(i), stack))
                    return i;
            }
            return -1;
        }

        public int tryInsert(ItemStack stack) {
            if (stack.isEmpty() || !isArrow(stack)) return 0;

            int space = MAX_ARROWS - this.count;
            int toInsert = Math.min(stack.getCount(), space);
            if (toInsert <= 0) return 0;

            int idx = findStackIndex(stack);
            if (idx != -1) {
                ItemStack existing = items.get(idx);
                int canAdd = Math.min(toInsert, existing.getMaxStackSize() - existing.getCount());
                if (canAdd <= 0) {
                    items.add(0, stack.split(toInsert));
                } else {
                    items.remove(idx);
                    items.add(0, existing.copyWithCount(existing.getCount() + canAdd));
                    stack.shrink(canAdd);
                    toInsert = canAdd;
                }
            } else {
                items.add(0, stack.split(toInsert));
            }

            this.count += toInsert;
            return toInsert;
        }

        public int tryTransfer(Slot slot, Player player) {
            ItemStack stack = slot.getItem();
            if (stack.isEmpty() || !isArrow(stack)) return 0;
            int space = MAX_ARROWS - this.count;
            return tryInsert(slot.safeTake(stack.getCount(), space, player));
        }

        @Nullable
        public ItemStack remove(int index) {
            if (index < 0 || index >= items.size()) return null;

            ItemStack removed = items.remove(index);
            this.count -= removed.getCount();
            return removed;
        }

        @Nullable
        public ItemStack removeOne(int index) {
            if (index < 0 || index >= items.size()) return null;

            ItemStack stack = items.get(index);
            ItemStack removed = stack.copyWithCount(1);

            stack.shrink(1);
            count--;

            if (stack.isEmpty()) items.remove(index);

            return removed;
        }

        @Nullable
        public ItemStack removeOne() {
            return removeOne(0);
        }

        public int count() {
            return this.count;
        }

        public int size() {
            return this.items.size();
        }

        public QuiverContents toImmutable() {
            return new QuiverContents(List.copyOf(items), count);
        }
    }
}