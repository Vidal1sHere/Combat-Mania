package com.vidal1shere.combatmania.api.extension.client;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface IMinecraftInstance {
    @Nullable
    Player player();
}
