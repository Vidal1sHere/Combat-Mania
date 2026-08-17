package com.vidal1shere.combatmania.client.renderer.armor;

import com.vidal1shere.combatmania.client.model.armor.LeatherQuiverModel;
import com.vidal1shere.combatmania.item.armor.LeatherQuiverItem;

public class LeatherQuiverRenderer extends BasicQuiverRenderer<LeatherQuiverItem> {
    public LeatherQuiverRenderer() {
        super(new LeatherQuiverModel());
    }
}