package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.constants.TieredMaceTextures;
import com.vidal1shere.combatmania.registry.item.CMExtendedToolTiers;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StrikeMaceItem extends TieredItem implements IStrikeWeapon {
    public StrikeMaceItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        return null;
    }
    
    public TieredMaceTextures getTierTexture() {
        Tier tier = this.getTier() instanceof CMExtendedToolTiers ext ? ext.getVanillaTier() : getTier();
        
        return switch (tier) {
            case Tiers.WOOD -> TieredMaceTextures.WOOD;
            case Tiers.STONE -> TieredMaceTextures.STONE;
            case Tiers.GOLD -> TieredMaceTextures.GOLD;
            case Tiers.DIAMOND -> TieredMaceTextures.DIAMOND;
            default -> TieredMaceTextures.IRON;
        };
    }
}
