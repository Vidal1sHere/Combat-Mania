package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.api.extension.entity.JoustMotionAware;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements JoustMotionAware {

    @Shadow
    public abstract Vec3 position();

    @Shadow
    public abstract LivingEntity getControllingPassenger();

    @Shadow
    public abstract boolean isAlive();

    @Unique
    private Vec3 cm$joustSpeed = Vec3.ZERO;

    @Unique
    @Nullable
    private Vec3 cm$joustPreviousPosition;

    @Inject(method = "reapplyPosition", at = @At("TAIL"))
    private void cm$resetJoustPosition(CallbackInfo ci) {
        this.cm$joustPreviousPosition = null;
    }

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void cm$updateJoustSpeed(CallbackInfo ci) {
        Vec3 currentPosition = this.position();

        if (this.cm$joustPreviousPosition == null) {
            this.cm$joustPreviousPosition = currentPosition;
        }

        this.cm$joustSpeed = currentPosition.subtract(this.cm$joustPreviousPosition);
        this.cm$joustPreviousPosition = currentPosition;
    }

    @Override
    public Vec3 getJoustMovement() {
        return this.getControllingPassenger() instanceof Player player && this.isAlive()
                ? ((JoustMotionAware) player).getJoustMovement()
                : this.cm$joustSpeed;
    }
}