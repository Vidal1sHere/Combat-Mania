package com.vidal1shere.combatmania.item.armor;

import com.vidal1shere.combatmania.client.renderer.armor.FlamingQuiverRenderer;
import com.vidal1shere.combatmania.data.attachment.QuiverData;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.entity.FlamingArrow;
import com.vidal1shere.combatmania.event.game.bow.BowChargeEvent;
import com.vidal1shere.combatmania.event.game.bow.BowShootEvent;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.item.CMArmorMaterials;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Set;
import java.util.function.Predicate;

public class FlamingQuiverItem extends QuiverItem {

    private static final Set<EquipmentSlot> DEFAULT_SLOTS = Set.of(
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS
    );

    private static final float CHARGE_REQUIREMENT = 1.5F;

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
    public void onBowShoot(BowShootEvent event) {
        if (!(event.getEntity() instanceof Player player)|| !event.getAmmo().is(Items.ARROW)) return;

        QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
        if (equippedQuiver == null) return;

        ChargeData chargeData = equippedQuiver.stack().getOrDefault(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);

        equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);
        if (!chargeData.charged()) return;

        FlamingArrow flamingArrow = new FlamingArrow(
                player.level(), player,
                event.getAmmo().copyWithCount(1), event.getWeapon()
        );

        event.setProjectile(flamingArrow);
        flamingArrow.playSound(CMSounds.FLAMING_ARROW_SHOOT.get(), 1.0F, 1.0F);

        if (player.level() instanceof ServerLevel level) {
            spawnShootParticles(player, level);
        }
    }

    @Override
    public void onBowCharge(BowChargeEvent event) {
        Player player = event.getEntity();

        QuiverData.EquippedQuiver equippedQuiver = QuiverData.findFirstEquippedQuiver(player);
        if (equippedQuiver == null) return;

        QuiverContents contents = equippedQuiver.stack().getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
        if (contents.isEmpty()) return;

        QuiverData data = player.getData(CMAttachments.QUIVER);
        ItemStack selected = data.getSelectedStack(contents);

        if (selected == null || !selected.is(Items.ARROW)) {
            equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);
            return;
        }

        ItemStack weapon = event.getWeapon();
        Predicate<ItemStack> predicate = ((ProjectileWeaponItem)weapon.getItem()).getSupportedHeldProjectiles(weapon);
        ItemStack heldProjectile = ProjectileWeaponItem.getHeldProjectile(player, predicate);

        if (!heldProjectile.isEmpty()) {
            equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);
            return;
        }

        String currentArrow = BuiltInRegistries.ITEM.getKey(selected.getItem()).toString();
        long currentTick = player.level().getGameTime();

        ChargeData chargeData = equippedQuiver.stack().getOrDefault(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);

        if (chargeData.isCharging() && !chargeData.selectedArrow().equals(currentArrow)) {
            equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, ChargeData.EMPTY);
            return;
        }

        if (!chargeData.isCharging()) {
            equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, chargeData.startCharging(currentTick, currentArrow));
            return;
        }

        long elapsed = currentTick - chargeData.chargeStartTick();
        float power;
        boolean isCrossbow = event.isCrossbow();

        if (isCrossbow) {
            int chargeDuration = CrossbowItem.getChargeDuration(event.getWeapon(), player);
            power = (float) event.getCharge() / chargeDuration;
        } else {
            float f = (float) elapsed / 20.0F;
            power = (f * f + f * 2.0F) / 3.0F;
        }

        if (!isCrossbow && power < CHARGE_REQUIREMENT) return;
        if (power < 1.0F) return;
        if (chargeData.charged()) return;

        equippedQuiver.stack().set(CMDataComponents.FLAMING_QUIVER, chargeData.withCharged(true));

        Level level = event.getLevel();
        level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, player.getSoundSource(), 0.5F, 1.0F);

        if (level instanceof ServerLevel serverLevel) {
            spawnChargeParticles(player, serverLevel, 1.5);
            spawnChargeParticles(player, serverLevel, 3.0);
        }
    }
    
    private void spawnShootParticles(Player player, ServerLevel level) {
        Vec3 look = player.getViewVector(1.0F).normalize();
        double distance = 2.0;
        double cx = player.getX() + look.x * distance;
        double cy = player.getEyeY() + look.y * distance;
        double cz = player.getZ() + look.z * distance;

        Vec3 up = Math.abs(look.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 right = look.cross(up).normalize();
        Vec3 realUp = look.cross(right).normalize();

        int count = 24;
        double radius = 1.0;

        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI / count) * i;
            double cos = Math.cos(angle) * radius;
            double sin = Math.sin(angle) * radius;

            double px = cx + right.x * cos + realUp.x * sin;
            double py = cy + right.y * cos + realUp.y * sin;
            double pz = cz + right.z * cos + realUp.z * sin;

            level.sendParticles(
                    ParticleTypes.FLAME,
                    px, py, pz,
                    1, 0, 0, 0, 0.05
            );
        }
    }

    private void spawnChargeParticles(Player player, ServerLevel level, double radius) {
        int count = 32;
        double centerY = player.getY() + (player.onGround() ? 0.2 : player.getBbHeight() / 2.0);

        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI / count) * i;

            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            level.sendParticles(
                    ParticleTypes.FLAME,
                    player.getX() + offsetX,
                    centerY,
                    player.getZ() + offsetZ,
                    1, 0, 0, 0, 0.02
            );
        }
    }
}
