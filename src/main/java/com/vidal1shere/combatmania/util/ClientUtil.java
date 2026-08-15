package com.vidal1shere.combatmania.util;

import com.vidal1shere.combatmania.api.extension.client.IMinecraftInstance;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public final class ClientUtil implements IMinecraftInstance {
    
    public static IMinecraftInstance INSTANCE = () -> null;

    @Nullable
    @Override
    public Player player() {
        return INSTANCE.player();
    }
}
