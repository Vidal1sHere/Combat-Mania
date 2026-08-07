package com.vidal1shere.combatmania.mixin.client;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.attachment.StrikeData;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.attachment.StrikeActiveData;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "handleKeybinds", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;startUseItem()V"
    ), cancellable = true)
    private void combatmania_suppressUseItem(CallbackInfo ci) {
        Minecraft mc = (Minecraft)(Object)this;
        if (mc.player == null) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof IStrikeWeapon)) return;

        StrikeData strikeData = mc.player.getData(CMAttachments.STRIKE);
        if (strikeData.isActive(StrikeSlot.SECONDARY)) {
            ci.cancel();
        }
    }
}