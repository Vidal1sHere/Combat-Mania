package com.vidal1shere.combatmania.data.attachment;

import com.mojang.serialization.Codec;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public final class QuiverData {
    public static final Codec<QuiverData> CODEC = Codec.INT.xmap(QuiverData::new, QuiverData::selectedIndex);

    public static final StreamCodec<RegistryFriendlyByteBuf, QuiverData> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, QuiverData::selectedIndex, QuiverData::new);

    public static final QuiverData EMPTY = new QuiverData(null, 0);

    public static final EquipmentSlot[] QUIVER_SEARCH_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    @Nullable
    private EquippedQuiver equippedQuiver;
    private int selectedIndex;
    private boolean quiverAmmoUsed;

    public QuiverData(int selectedIndex) {
        this(null, selectedIndex);
    }

    public QuiverData(@Nullable EquippedQuiver equippedQuiver, int selectedIndex) {
        this.equippedQuiver = equippedQuiver;
        this.selectedIndex = Math.max(0, selectedIndex);
    }

    @Nullable
    public EquippedQuiver equippedQuiver() {
        return this.equippedQuiver;
    }

    public int selectedIndex() {
        return this.selectedIndex;
    }

    public boolean quiverAmmoUsed() {
        return this.quiverAmmoUsed;
    }

    public void setEquippedQuiver(@Nullable EquippedQuiver equippedQuiver) {
        this.equippedQuiver = equippedQuiver;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = Math.max(0, selectedIndex);
    }

    public void setQuiverAmmoUsed(boolean quiverAmmoUsed) {
        this.quiverAmmoUsed = quiverAmmoUsed;
    }

    public void reset(@Nullable EquippedQuiver equippedQuiver) {
        System.out.println("QuiverData reset chamado, stack: " + Thread.currentThread().getStackTrace()[2]);
        this.equippedQuiver = equippedQuiver;
        this.selectedIndex = 0;
        this.quiverAmmoUsed = false;
    }

    public void next(int size) {
        if (size <= 0) {
            this.selectedIndex = 0;
            return;
        }

        this.selectedIndex = (this.selectedIndex + 1) % size;
    }

    public void previous(int size) {
        if (size <= 0) {
            this.selectedIndex = 0;
            return;
        }

        this.selectedIndex = (this.selectedIndex - 1 + size) % size;
    }

    public int getSelectedIndex(QuiverContents contents) {
        if (contents.isEmpty()) {
            this.selectedIndex = 0;
            return 0;
        }

        if (this.selectedIndex >= contents.size()) {
            this.selectedIndex = 0;
        }

        return this.selectedIndex;
    }

    @Nullable
    public ItemStack getSelectedStack(QuiverContents contents) {
        if (contents.isEmpty()) return null;

        int index = getSelectedIndex(contents);
        return contents.items().get(index);
    }

    public record EquippedQuiver(EquipmentSlot slot, ItemStack stack) {}

    @Nullable
    public static EquippedQuiver findFirstEquippedQuiver(Player player) {
        EquippedQuiver fallback = null;

        for (EquipmentSlot slot : QUIVER_SEARCH_SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);

            if (!(stack.getItem() instanceof QuiverItem)) continue;

            QuiverContents contents = stack.getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);

            if (!contents.isEmpty()) return new EquippedQuiver(slot, stack);

            if (fallback == null) fallback = new EquippedQuiver(slot, stack);
        }

        return fallback;
    }
}