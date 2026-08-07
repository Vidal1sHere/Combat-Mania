package com.vidal1shere.combatmania.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.attachment.StrikeData;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_FULL_SINGLE =
            CMUtil.modResLoc("hud/attack_indicator_full_single");
    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_FULL_DOUBLE =
            CMUtil.modResLoc("hud/attack_indicator_full_double");
    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_PRIMARY_BACKGROUND =
            CMUtil.modResLoc("hud/attack_indicator_primary_background");
    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_PRIMARY_PROGRESS =
            CMUtil.modResLoc("hud/attack_indicator_primary_progress");
    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_SECONDARY_BACKGROUND =
            CMUtil.modResLoc("hud/attack_indicator_secondary_background");
    @Unique
    private static final ResourceLocation ATTACK_INDICATOR_SECONDARY_PROGRESS =
            CMUtil.modResLoc("hud/attack_indicator_secondary_progress");

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
        if (!(stack.getItem() instanceof IStrikeWeapon weapon)) {
            return;
        }


        StrikeData strikeData = player.getData(CMAttachments.STRIKE);
        boolean hasPrimary = weapon.getPrimaryStyle() != null;
        boolean hasSecondary = weapon.getSecondaryStyle() != null;
        boolean hasBoth = hasPrimary && hasSecondary;
        
        if (!hasPrimary && !hasSecondary) {
            return;
        }
        
        ci.cancel();

        float primaryScale = hasPrimary ? strikeData.getScale(StrikeSlot.PRIMARY) : 1.0F;
        float secondaryScale = hasSecondary ? strikeData.getScale(StrikeSlot.SECONDARY) : 1.0F;

        boolean noCooldown = primaryScale >= 1.0F && secondaryScale >= 1.0F;
        boolean hasPickEntity = this.minecraft.crosshairPickEntity instanceof LivingEntity le && le.isAlive();

        int j = guiGraphics.guiHeight() / 2 - 7 + 16;
        int k = guiGraphics.guiWidth() / 2 - 8;

        if (noCooldown && hasPickEntity) {
            ResourceLocation fullSprite = hasBoth ? ATTACK_INDICATOR_FULL_DOUBLE : ATTACK_INDICATOR_FULL_SINGLE;
            guiGraphics.blitSprite(fullSprite, k, j, 16, 16);

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            return;
        }

        if (!hasBoth) {
            float scale = hasPrimary ? primaryScale : secondaryScale;
            
            if (scale < 1.0F) {
                int l = (int)(scale * 17.0F);
                guiGraphics.blitSprite(ATTACK_INDICATOR_PRIMARY_BACKGROUND, k, j, 16, 4);
                guiGraphics.blitSprite(ATTACK_INDICATOR_PRIMARY_PROGRESS, 16, 4, 0, 0, k, j, l, 4);
            }

            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            return;
        }

        boolean anyCooldown = primaryScale < 1.0F || secondaryScale < 1.0F;
        if (anyCooldown) {
            int primaryProgress = (int)(primaryScale * 17.0F);
            int secondaryProgress = (int)(secondaryScale * 17.0F);

            guiGraphics.blitSprite(ATTACK_INDICATOR_PRIMARY_BACKGROUND, k, j, 16, 4);
            guiGraphics.blitSprite(ATTACK_INDICATOR_PRIMARY_PROGRESS, 16, 4, 0, 0, k, j, primaryProgress, 4);

            guiGraphics.blitSprite(ATTACK_INDICATOR_SECONDARY_BACKGROUND, k, j, 16, 16);
            guiGraphics.blitSprite(ATTACK_INDICATOR_SECONDARY_PROGRESS, 16, 16, 0, 0, k, j, secondaryProgress, 16);
        }

        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}