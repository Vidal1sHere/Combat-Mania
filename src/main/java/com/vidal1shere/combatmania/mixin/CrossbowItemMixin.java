package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.event.game.BowChargeEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
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
