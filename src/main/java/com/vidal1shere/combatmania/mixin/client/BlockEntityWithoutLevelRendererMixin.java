package com.vidal1shere.combatmania.mixin.client;

import com.vidal1shere.combatmania.config.CMClientConfig;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

    @Redirect(
        method = "renderByItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean cm$disableTridentCustomRenderer(ItemStack stack, Item item) {
        if (item == Items.TRIDENT) {
            return !CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get() && stack.is(item);
        }

        return stack.is(item);
    }
}