package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.api.IVanillaStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AxeItem.class)
public abstract class AxeItemMixin implements IVanillaStrikeWeapon, IItemExtension {
    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.CLEAVE;
        return null;
    }

    @Override
    public boolean isEnabled() {
        return CMCommonConfig.MODIFY_AXES.get();
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (isEnabled()) {
            return slotChanged;
        }

        return IItemExtension.super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }
}
