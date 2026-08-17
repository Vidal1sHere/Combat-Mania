package com.vidal1shere.combatmania.client.gui.access;

public final class QuiverHudState {

    private static long lastChangeTick = -1L;
    private static final int DISPLAY_TICKS = 60;
    private static final int FADE_TICKS = 20;

    private QuiverHudState() {}

    public static void onCycleConfirmed(long gameTick) {
        lastChangeTick = gameTick;
    }

    public static float getAlpha(long now) {
        if (lastChangeTick < 0) return 0F;

        long elapsed = now - lastChangeTick;
        if (elapsed > DISPLAY_TICKS + FADE_TICKS) return 0F;
        if (elapsed <= DISPLAY_TICKS) return 1F;

        float alpha = 1.0F - (float)(elapsed - DISPLAY_TICKS) / FADE_TICKS;
        return alpha < 0.01F ? 0F : alpha;
    }
}