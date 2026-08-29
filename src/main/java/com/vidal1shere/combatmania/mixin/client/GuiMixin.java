package com.vidal1shere.combatmania.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(
            method = "renderCrosshair",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void combatmania_renderAttackIndicator(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Player player = this.minecraft.player;
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return;
        
        IStrikeWeapon weapon = (IStrikeWeapon) stack.getItem();
        boolean hasPrimary = weapon.getPrimaryStyle() != null;
        boolean hasSecondary = weapon.getSecondaryStyle() != null;
        
        if (!hasPrimary && !hasSecondary) {
            return;
        }
        
        ci.cancel();

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}