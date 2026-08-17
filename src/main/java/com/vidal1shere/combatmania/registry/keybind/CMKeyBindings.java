package com.vidal1shere.combatmania.registry.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public class CMKeyBindings {
    public static final KeyMapping QUIVER_ARROW_CYCLE = registerKey("gun_reload", InputConstants.KEY_R);
    
    private static String getKey(String key) {
        return "key." + CombatMania.MOD_ID + "." + key;
    }
    
    private static String getCategory() {
        return "key.categories." + CombatMania.MOD_ID;
    }

    private static KeyMapping registerKey(String name, int keyCode) {
        return new KeyMapping(getKey(name), InputConstants.Type.KEYSYM, keyCode, getCategory());
    }
    
    private static KeyMapping registerKey(String name, InputConstants.Type type, int keyCode) {
        return new KeyMapping(getKey(name), type, keyCode, getCategory());
    }
    
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(QUIVER_ARROW_CYCLE);
    }
}
