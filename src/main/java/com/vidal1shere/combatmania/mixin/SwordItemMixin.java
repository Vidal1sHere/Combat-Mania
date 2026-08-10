package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.config.CMCommonConfig;
import com.vidal1shere.combatmania.api.IVanillaStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.item.SwordItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SwordItem.class)
public class SwordItemMixin implements IVanillaStrikeWeapon {
    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.SWEEP;
        return null;
    }

    @Override
    public boolean isEnabled() {
        return CMCommonConfig.MODIFY_SWORDS.get();
    }
}
