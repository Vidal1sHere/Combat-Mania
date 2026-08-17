package com.vidal1shere.combatmania.item.armor;

import com.vidal1shere.combatmania.client.renderer.armor.FlamingQuiverRenderer;
import com.vidal1shere.combatmania.entity.FlamingArrow;
import com.vidal1shere.combatmania.event.game.ProjectileCreateEvent;
import com.vidal1shere.combatmania.event.game.ProjectileShootEvent;
import com.vidal1shere.combatmania.registry.item.CMArmorMaterials;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
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
        if (event.getShooter() == null || !event.getAmmo().is(Items.ARROW)) return;
        
        FlamingArrow flamingArrow = new FlamingArrow(
                event.getShooter().level(), event.getShooter(), event.getAmmo().copyWithCount(1), event.getWeapon());
        
        event.setProjectile(flamingArrow);
    }

    @Override
    public void onArrowShoot(ProjectileShootEvent event) {
        if (event.getShooter() == null || !event.getAmmo().is(Items.ARROW)) return;
        event.getProjectile().playSound(SoundEvents.FIRECHARGE_USE, 0.5F, 1.0F);
    }
}
