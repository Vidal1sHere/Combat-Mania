package com.vidal1shere.combatmania.api;

import net.minecraft.world.item.ItemStack;

public interface IVanillaStrikeWeapon extends IStrikeWeapon {
    boolean isEnabled();
    
    static boolean isVanillaDisabled(ItemStack stack) {
        if (stack.getItem() instanceof IVanillaStrikeWeapon weapon) {
            return !weapon.isEnabled();
        }
        
        return !(stack.getItem() instanceof IStrikeWeapon);
    }
}
