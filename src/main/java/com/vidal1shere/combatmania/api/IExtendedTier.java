package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.world.item.Tier;

public interface IExtendedTier extends Tier {
    CustomProperties getCustomProperties();
    
    default JoustProperties getJoustProperties() {
        return getCustomProperties().get(JoustProperties.KEY);
    }
}
