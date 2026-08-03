package com.vidal1shere.combatmania.mixin;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.strike.StrikeCooldownData;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    // Tick cooldown timers every player tick
    @Inject(method = "tick", at = @At("TAIL"))
    private void combatmania_tickCooldowns(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        self.getData(CMAttachments.STRIKE_COOLDOWN).tick();
    }

    @Inject(method = "getCurrentItemAttackStrengthDelay", at = @At("HEAD"), cancellable = true)
    private void combatmania_attackSpeedOverride(CallbackInfoReturnable<Float> cir) {
        Player self = (Player) (Object) this;
        ItemStack stack = self.getMainHandItem();

        if (!(stack.getItem() instanceof IStrikeWeapon weapon)) return;

        StrikeCooldownData cooldown = self.getData(CMAttachments.STRIKE_COOLDOWN);

        boolean primaryExhausted  = cooldown.getPrimaryScale() < 1.0F;
        boolean secondaryExhausted = cooldown.getSecondaryScale() < 1.0F;

        if (!primaryExhausted && !secondaryExhausted) return;

        StrikeStyle style = primaryExhausted
                ? weapon.getPrimaryStyle()
                : weapon.getSecondaryStyle();

        if (style == null) return;

        double speed = self.getAttributeValue(style.getSpeedAttribute());
        if (speed <= 0) return;

        cir.setReturnValue((float) (1.0 / speed * 20.0));
    }
}