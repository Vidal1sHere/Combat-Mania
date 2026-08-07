package com.vidal1shere.combatmania.registry.sound;

import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.combatmania.CombatMania;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CMSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(
            BuiltInRegistries.SOUND_EVENT,
            CombatMania.MOD_ID
    );

    public static final Supplier<SoundEvent> WOODEN_SPEAR_JAB = registerSoundEvent("item.spear_wood.attack");
    public static final Supplier<SoundEvent> WOODEN_SPEAR_HIT = registerSoundEvent("item.spear_wood.hit");
    public static final Supplier<SoundEvent> SPEAR_JAB = registerSoundEvent("item.spear.attack");
    public static final Supplier<SoundEvent> SPEAR_HIT = registerSoundEvent("item.spear.hit");
    
    private static ResourceKey<JukeboxSong> createSong(String name) {
        return ResourceKey.create(
                Registries.JUKEBOX_SONG,
                CMUtil.modResLoc(name)
        );
    }

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = CMUtil.modResLoc(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
