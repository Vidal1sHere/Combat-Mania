package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.event.game.bow.BowChargeEvent;
import com.vidal1shere.combatmania.event.game.bow.BowShootEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(BowItem.class)
public abstract class BowItemMixin extends ProjectileWeaponItem {

    @Unique
    private BowShootEvent cm$event;
    @Unique
    private Projectile cm$pendingProjectile;

    public BowItemMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BowItem;draw(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/List;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void cm$beforeDraw(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft,
                                        CallbackInfo ci,
                                        Player player, ItemStack ammoStack, int i, float f) {
        if (!(level instanceof ServerLevel)) return;
        
        Projectile dummyProjectile = this.createProjectile(level, player, stack, ammoStack, f == 1.0F);
        cm$event = new BowShootEvent(player, stack, ammoStack, dummyProjectile, f * 3.0F);
        NeoForge.EVENT_BUS.post(cm$event);

        if (cm$event.isCanceled()) {
            ci.cancel();
        }
    }

    @Override
    public Projectile createProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit) {
        if (cm$pendingProjectile != null) {
            Projectile p = cm$pendingProjectile;
            cm$pendingProjectile = null;
            return p;
        }
        return super.createProjectile(level, shooter, weapon, ammo, isCrit);
    }

    @Override
    protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon,
                         List<ItemStack> projectileItems, float velocity, float inaccuracy, boolean isCrit,
                         @Nullable LivingEntity target) {

        if (cm$event == null) {
            super.shoot(level, shooter, hand, weapon, projectileItems, velocity, inaccuracy, isCrit, target);
            return;
        }
        
        cm$pendingProjectile = cm$event.getProjectile();
        super.shoot(level, shooter, hand, weapon, projectileItems, cm$event.getVelocity(), inaccuracy, isCrit, target);

        cm$event = null;
        cm$pendingProjectile = null;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseTicks) {
        if (!(entity instanceof Player player)) return;

        int charge = stack.getUseDuration(entity) - remainingUseTicks;

        BowChargeEvent event = new BowChargeEvent(level, player, stack, charge);
        NeoForge.EVENT_BUS.post(event);
    }
}