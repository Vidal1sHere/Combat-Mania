package com.vidal1shere.combatmania.item.armor;

import com.vidal1shere.combatmania.client.renderer.armor.FlamingQuiverRenderer;
import com.vidal1shere.combatmania.event.game.ProjectileCreateEvent;
import com.vidal1shere.combatmania.registry.item.CMArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Arrow;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Set;

public class FlamingQuiverItem extends QuiverItem {

    private static final Set<EquipmentSlot> DEFAULT_SLOTS = Set.of(
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS
    );

    public FlamingQuiverItem(Properties properties) {
        super(CMArmorMaterials.QUIVER_MATERIAL, properties);
    }

    @Override
    public Set<EquipmentSlot> getAllowedSlots() {
        return DEFAULT_SLOTS;
    }

    @Override
    public GeoArmorRenderer<?> supplyRenderer() {
        return new FlamingQuiverRenderer();
    }

    @Override
    public void onArrowCreate(ProjectileCreateEvent event) {
        if (event.getProjectile() instanceof Arrow arrow) {
            arrow.setRemainingFireTicks(100);
        }
    }
}
