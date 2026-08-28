package com.vidal1shere.combatmania.api.extension.item;

import com.vidal1shere.combatmania.client.renderer.item.TieredHammerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class ClientHammerExtension extends ClientStrikeWeaponExtension {

    @Override
    public BlockEntityWithoutLevelRenderer getCustomRenderer() {
        return new TieredHammerRenderer(Minecraft.getInstance().getItemRenderer(),
                Minecraft.getInstance().getEntityModels());
    }
}
