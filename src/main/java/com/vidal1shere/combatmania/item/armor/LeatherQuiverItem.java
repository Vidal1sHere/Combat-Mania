package com.vidal1shere.combatmania.item.armor;

import com.vidal1shere.combatmania.client.renderer.armor.LeatherQuiverRenderer;
import com.vidal1shere.combatmania.registry.item.CMArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Set;

public class LeatherQuiverItem extends QuiverItem {

    private static final Set<EquipmentSlot> DEFAULT_SLOTS = Set.of(
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS
    );
    
    public LeatherQuiverItem(Properties properties) {
        super(CMArmorMaterials.QUIVER_ARMOR_MATERIAL, Type.CHESTPLATE, properties);
    }

    @Override
    public Set<EquipmentSlot> getAllowedSlots() {
        return DEFAULT_SLOTS;
    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new LeatherQuiverRenderer();
    }
}
