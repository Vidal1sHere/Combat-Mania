package com.vidal1shere.combatmania.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.vidal1shere.combatmania.event.game.bow.BowChargeEvent;
import com.vidal1shere.combatmania.event.game.bow.BowShootEvent;
import com.vidal1shere.combatmania.event.game.bow.CrossbowLoadEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {
    
    @Unique
    private BowShootEvent cm$event;
    @Unique
    private Projectile cm$pendingProjectile;

    public CrossbowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "performShooting",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;shoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/world/entity/LivingEntity;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void cm$beforeShoot(Level level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                                float velocity, float inaccuracy, LivingEntity target, CallbackInfo ci,
                                ServerLevel serverlevel, ChargedProjectiles chargedprojectiles) {
        if (!(shooter instanceof Player player)) return;

        cm$event = new BowShootEvent(player, weapon, chargedprojectiles.getItems().get(0), null, velocity);
        NeoForge.EVENT_BUS.post(cm$event);

        if (cm$event.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(
            method = "createProjectile",
            at = @At("RETURN"),
            cancellable = true
    )
    private void cm$createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit,
                                     CallbackInfoReturnable<Projectile> cir) {
        if (cm$pendingProjectile != null) {
            Projectile p = cm$pendingProjectile;
            cm$pendingProjectile = null;
            cir.setReturnValue(p);
        }
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                         List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                         @Nullable LivingEntity target) {

        if (cm$event == null) {
            super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
            return;
        }

        Projectile custom = cm$event.getProjectile();
        if (custom != null) {
            cm$pendingProjectile = custom;
        }

        super.shoot(level, shooter, hand, weapon, projectileItems, cm$event.getVelocity(), inaccuracy, isCrit, target);

        cm$pendingProjectile = null;
    }

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FFLnet/minecraft/world/entity/LivingEntity;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void cm$afterPerformShooting(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        if (cm$event != null && cm$event.isCanceled()) {
            cir.setReturnValue(InteractionResultHolder.fail(player.getItemInHand(hand)));
        }
        
        cm$event = null;
    }

    @WrapOperation(
            method = "tryLoadProjectiles",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/CrossbowItem;draw(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/List;"
            )
    )
    private static List<ItemStack> cm$wrapDraw(
            ItemStack weapon,
            ItemStack ammo,
            LivingEntity shooter,
            Operation<List<ItemStack>> original
    ) {
        if (!(shooter instanceof Player player)) return original.call(weapon, ammo, shooter);

        CrossbowLoadEvent event = new CrossbowLoadEvent(player, ammo, weapon);
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return List.of();

        List<ItemStack> result = original.call(weapon, ammo, shooter);
        if (result.isEmpty()) return result;

        if (event.getProjectiles() != null) {
            return event.getProjectiles();
        }

        return result;
    }
    
    @Inject(
            method = "onUseTick",
            at = @At("HEAD")
    )
    private void cm$onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
        if (!(stack.getItem() instanceof CrossbowItem)) return;
        if (!(entity instanceof Player player)) return;

        int charge = stack.getUseDuration(entity) - remainingUseTicks;

        BowChargeEvent event = new BowChargeEvent(level, player, stack, charge);
        NeoForge.EVENT_BUS.post(event);
    }
}
