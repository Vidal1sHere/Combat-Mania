package com.vidal1shere.combatmania.event.client;

import com.vidal1shere.combatmania.client.renderer.strike.StrikeRenderers;
import com.vidal1shere.combatmania.client.renderer.strike.StrikeStyleRenderer;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;

public class RegisterStrikeRenderersEvent extends Event implements IModBusEvent {

    public RegisterStrikeRenderersEvent() {
    }
    
    public <T extends StrikeStyle> void registerStrikeRenderer(Class<T> strikeStyle, StrikeStyleRenderer<T> renderer) {
        StrikeRenderers.register(strikeStyle, renderer);
    }
}
