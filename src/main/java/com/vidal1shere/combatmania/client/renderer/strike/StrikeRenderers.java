package com.vidal1shere.combatmania.client.renderer.strike;

import com.vidal1shere.combatmania.client.renderer.strike.type.JoustStrikeRenderer;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike.styles.JoustStrikeStyle;

import java.util.IdentityHashMap;
import java.util.Map;

public class StrikeRenderers {

    private static final Map<Class<? extends StrikeStyle>, StrikeStyleRenderer<?>> RENDERERS = new IdentityHashMap<>();

    public static void registerAll() {
        register(JoustStrikeStyle.class, new JoustStrikeRenderer());
    }

    public static <T extends StrikeStyle> void register(Class<T> clazz, StrikeStyleRenderer<T> renderer) {
        RENDERERS.put(clazz, renderer);
    }

    @SuppressWarnings("unchecked")
    public static <T extends StrikeStyle> StrikeStyleRenderer<T> getRenderer(T style) {
        return (StrikeStyleRenderer<T>) RENDERERS.get(style.getClass());
    }
}