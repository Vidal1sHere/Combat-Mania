package com.vidal1shere.combatmania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.vidal1shere.combatmania.event.custom.ProjectileCreateEvent;
import com.vidal1shere.combatmania.event.custom.ProjectileShootEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ProjectileWeaponItem.class)
public class ProjectileWeaponItemMixin {

    @Unique
    private static final ThreadLocal<ProjectileCreateEvent> PENDING_CREATE =
            ThreadLocal.withInitial(() -> null);

    @WrapOperation(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ProjectileWeaponItem;createProjectile(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/projectile/Projectile;"
            )
    )
    private Projectile combatmania$onProjectileCreate(
            ProjectileWeaponItem instance, Level level, LivingEntity shooter,
            ItemStack weapon, ItemStack ammo, boolean isCrit,
            Operation<Projectile> original,
            @Local(name = "itemstack") ItemStack itemstack,
            @Local(argsOnly = true, ordinal = 0) float velocity
    ) {
        Projectile projectile = original.call(instance, level, shooter, weapon, ammo, isCrit);

        ProjectileCreateEvent event = new ProjectileCreateEvent(shooter, weapon, itemstack, projectile, velocity);

        if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
            projectile.discard();
            PENDING_CREATE.remove();
            return projectile;
        }

        PENDING_CREATE.set(event);
        return event.getProjectile();
    }

    @ModifyArg(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ProjectileWeaponItem;shootProjectile(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/projectile/Projectile;IFFFLnet/minecraft/world/entity/LivingEntity;)V"
            ),
            index = 3
    )
    private float combatmania$modifyVelocity(float original) {
        ProjectileCreateEvent event = PENDING_CREATE.get();
        if (event != null && event.getVelocity() != original) {
            return event.getVelocity();
        }
        return original;
    }

    @Inject(
            method = "shoot",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private void combatmania$onProjectileShoot(
            ServerLevel level, LivingEntity shooter, InteractionHand hand,
            ItemStack weapon, List<ItemStack> projectileItems, float velocity, float inaccuracy,
            boolean isCrit, @Nullable LivingEntity target,
            CallbackInfo ci,
            float f, float f1, float f2, float f3,
            int i, ItemStack itemstack, float f4,
            Projectile projectile
    ) {
        ProjectileCreateEvent createEvent = PENDING_CREATE.get();
        PENDING_CREATE.remove();

        boolean modified = createEvent != null && createEvent.isModified();

        ProjectileShootEvent event = new ProjectileShootEvent(
                shooter, weapon, itemstack, projectile, velocity, modified
        );

        if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
            projectile.discard();
            ci.cancel();
        }
    }
}