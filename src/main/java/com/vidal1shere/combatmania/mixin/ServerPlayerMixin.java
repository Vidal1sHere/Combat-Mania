package com.vidal1shere.combatmania.mixin;

import com.mojang.authlib.GameProfile;
import com.vidal1shere.combatmania.api.extension.entity.JoustMotionAware;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements JoustMotionAware {
    @Shadow
    private Vec3 lastKnownClientMovement;

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Override
    public Vec3 getJoustMovement() {
        Entity vehicle = this.getVehicle();
        return vehicle != null && vehicle.getControllingPassenger() != this ? ((JoustMotionAware) vehicle).getJoustMovement() : this.lastKnownClientMovement;
    }
}
