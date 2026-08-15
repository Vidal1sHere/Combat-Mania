package com.vidal1shere.combatmania.api.extension.item;

import com.vidal1shere.combatmania.client.renderer.item.TieredMaceRenderer;
import com.vidal1shere.combatmania.client.renderer.item.VanillaMaceRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public abstract class ClientMaceExtensions {
    
    public static class Tiered implements IClientItemExtensions {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new TieredMaceRenderer(Minecraft.getInstance().getItemRenderer(),
                    Minecraft.getInstance().getEntityModels());
        }
    }

    public static class Vanilla implements IClientItemExtensions {
        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return new VanillaMaceRenderer(Minecraft.getInstance().getItemRenderer(),
                    Minecraft.getInstance().getEntityModels());
        }
    }
}
