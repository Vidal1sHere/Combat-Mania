package com.vidal1shere.combatmania.client.hud.config;

import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.resources.ResourceLocation;

public record StrikeIndicator(String backgroundSprite, String progressSprite, int width, int height) {
    
    public static final StrikeIndicator CLEAVE = new StrikeIndicator("cleave_indicator_background", "cleave_indicator_progress", 16, 7);
    public static final StrikeIndicator JAB = new StrikeIndicator("jab_indicator_background", "jab_indicator_progress", 18, 6);
    public static final StrikeIndicator JOUST = new StrikeIndicator("joust_indicator_background", "joust_indicator_progress", 18, 6);
    public static final StrikeIndicator SMASH = new StrikeIndicator("smash_indicator_background", "smash_indicator_progress", 16, 8);
    public static final StrikeIndicator SHOCKWAVE = new StrikeIndicator("shockwave_indicator_background", "shockwave_indicator_progress", 16, 10);
    
    public ResourceLocation getBackgroundSprite() {
        return CMUtil.modResLoc("hud/" + backgroundSprite);
    }

    public ResourceLocation getProgressSprite() {
        return CMUtil.modResLoc("hud/" + progressSprite);
    }
}
