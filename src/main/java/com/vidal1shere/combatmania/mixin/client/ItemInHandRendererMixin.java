package com.vidal1shere.combatmania.mixin.client;

import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.api.IVanillaStrikeWeapon;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Shadow
    private ItemStack mainHandItem;

    @ModifyVariable(
            method = "tick",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"),
            ordinal = 0
    )
    private float suppressAttackDip(float attackStrength) {
        if (IVanillaStrikeWeapon.isVanillaDisabled(mainHandItem)) {
            return attackStrength;
        }
        
        if (mainHandItem.getItem() instanceof IAnimatedItem) {
            return 1.0F;
        }
        
        return attackStrength;
    }
}