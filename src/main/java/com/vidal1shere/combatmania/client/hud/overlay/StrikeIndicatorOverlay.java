package com.vidal1shere.combatmania.client.hud.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.hud.config.StrikeIndicator;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class StrikeIndicatorOverlay implements LayeredDraw.Layer {
    
    private static final ResourceLocation ATTACK_INDICATOR_FULL =
            CMUtil.modResLoc("hud/attack_indicator_full");
    
    private static final StrikeIndicator DEFAULT_INDICATOR = new StrikeIndicator(
            "sweep_indicator_background",
            "sweep_indicator_progress",
            16, 4
    );

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.options.hideGui) return;

        Player player = mc.player;
        ItemStack stack = player.getMainHandItem();
        if (!IStrikeWeapon.isStrikeWeapon(stack)) return;

        IStrikeWeapon weapon = (IStrikeWeapon) stack.getItem();
        StrikeData strikeData = player.getData(CMAttachments.STRIKE);
        boolean hasPrimary = weapon.getPrimaryStyle() != null;
        boolean hasSecondary = weapon.getSecondaryStyle() != null;
        boolean hasBoth = hasPrimary && hasSecondary;

        if (!hasPrimary && !hasSecondary) return;

        float primaryScale = hasPrimary ? strikeData.getScale(StrikeSlot.PRIMARY) : 1.0F;
        float secondaryScale = hasSecondary ? strikeData.getScale(StrikeSlot.SECONDARY) : 1.0F;
        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);

        StrikeIndicator primaryIndicator = hasPrimary ? weapon.getPrimaryStyle().get().getCustomStrikeIndicator() : null;
        StrikeIndicator secondaryIndicator = hasSecondary ? weapon.getSecondaryStyle().get().getCustomStrikeIndicator() : null;

        if (primaryIndicator == null && hasPrimary) primaryIndicator = DEFAULT_INDICATOR;
        if (secondaryIndicator == null && hasSecondary) secondaryIndicator = DEFAULT_INDICATOR;

        int j = guiGraphics.guiHeight() / 2 - 7 + 16;

        int primaryX = hasPrimary
                ? guiGraphics.guiWidth() / 2 - primaryIndicator.width() / 2
                : guiGraphics.guiWidth() / 2 - secondaryIndicator.width() / 2;

        int secondaryX = hasSecondary
                ? guiGraphics.guiWidth() / 2 - secondaryIndicator.width() / 2
                : primaryX;

        int secondaryY = hasBoth ? j + primaryIndicator.height() + 3 : j;

        if (hasPrimary) {
            weapon.getPrimaryStyle().get().preRenderStrikeIndicator(
                    guiGraphics, player, stack, primaryIndicator,
                    primaryX, j, false, partialTick
            );
        }

        if (hasSecondary) {
            weapon.getSecondaryStyle().get().preRenderStrikeIndicator(
                    guiGraphics, player, stack, secondaryIndicator,
                    secondaryX, secondaryY, true, partialTick
            );
        }

        boolean primaryCooldown = primaryScale < 1.0F;
        boolean secondaryCooldown = secondaryScale < 1.0F;
        boolean hasPickEntity = mc.crosshairPickEntity instanceof LivingEntity le && le.isAlive();

        if (!primaryCooldown && !secondaryCooldown && !hasPickEntity) return;

        setup();

        if (!hasBoth) {
            StrikeIndicator indicator = hasPrimary ? primaryIndicator : secondaryIndicator;
            float scale = hasPrimary ? primaryScale : secondaryScale;
            int x = hasPrimary ? primaryX : secondaryX;

            renderIndicator(guiGraphics, indicator, x, j, scale, false);

            if (hasPickEntity && !(hasPrimary ? primaryCooldown : secondaryCooldown)) {
                int fullX = x + (indicator.width() - 3) / 2;
                int fullY = j + indicator.height();
                guiGraphics.blitSprite(ATTACK_INDICATOR_FULL, fullX, fullY, 3, 3);
            }

            if (hasPrimary) {
                weapon.getPrimaryStyle().get().postRenderStrikeIndicator(
                        guiGraphics, player, stack, primaryIndicator,
                        primaryX, j, false, partialTick
                );
            }

            if (hasSecondary) {
                weapon.getSecondaryStyle().get().postRenderStrikeIndicator(
                        guiGraphics, player, stack, secondaryIndicator,
                        secondaryX, j, false, partialTick
                );
            }

            cleanup();
            return;
        }

        renderIndicator(guiGraphics, primaryIndicator, primaryX, j, primaryScale, false);
        renderIndicator(guiGraphics, secondaryIndicator, secondaryX, secondaryY, secondaryScale, true);

        if (hasPickEntity && !primaryCooldown && !secondaryCooldown) {
            double primaryCenter = primaryX + primaryIndicator.width() / 2.0;
            double secondaryCenter = secondaryX + secondaryIndicator.width() / 2.0;
            int fullX = (int)((primaryCenter + secondaryCenter) / 2.0 - 1.5);
            int fullY = j + primaryIndicator.height();
            guiGraphics.blitSprite(ATTACK_INDICATOR_FULL, fullX, fullY, 3, 3);
        }

        weapon.getPrimaryStyle().get().postRenderStrikeIndicator(
                guiGraphics, player, stack, primaryIndicator,
                primaryX, j, false, partialTick
        );

        weapon.getSecondaryStyle().get().postRenderStrikeIndicator(
                guiGraphics, player, stack, secondaryIndicator,
                secondaryX, secondaryY, true, partialTick
        );

        cleanup();
    }


    private void renderIndicator(GuiGraphics guiGraphics, StrikeIndicator indicator, int x, int y, float scale, boolean rotated) {
        ResourceLocation backgroundSprite = indicator.getBackgroundSprite();
        ResourceLocation progressSprite = indicator.getProgressSprite();
        int width = indicator.width();
        int height = indicator.height();
        int progress = Math.min(width, (int)(scale * width));

        guiGraphics.pose().pushPose();

        if (rotated) {
            guiGraphics.pose().translate(x + width / 2.0F, y + height / 2.0F, 0.0F);
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0F));
            x = -width / 2;
            y = -height / 2;
        }

        guiGraphics.blitSprite(backgroundSprite, x, y, width, height);
        guiGraphics.blitSprite(progressSprite, width, height, 0, 0, x, y, progress, height);

        guiGraphics.pose().popPose();
    }

    private void setup() {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
    }

    private void cleanup() {
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}