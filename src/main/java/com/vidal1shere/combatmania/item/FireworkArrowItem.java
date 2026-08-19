package com.vidal1shere.combatmania.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class FireworkArrowItem extends ArrowItem {
    public static final int COOLDOWN = 100;
    
    public FireworkArrowItem(Properties properties) {
        super(properties);
    }
    
    public FireworkRocketEntity createFirework(Level level, ItemStack ammo, LivingEntity shooter) {
        ItemStack firework = new ItemStack(Items.FIREWORK_ROCKET);
        firework.set(DataComponents.FIREWORKS, ammo.get(DataComponents.FIREWORKS));
        firework.set(DataComponents.FIREWORK_EXPLOSION, ammo.get(DataComponents.FIREWORK_EXPLOSION));
        return new FireworkRocketEntity(level, firework, shooter, shooter.getX(), shooter.getEyeY() - (double)0.15F, shooter.getZ(), true);
    }

    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new FireworkRocketEntity(level, stack, pos.x(), pos.y(), pos.z(), true);
    }

    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return false;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Fireworks fireworks = stack.get(DataComponents.FIREWORKS);
        if (fireworks != null) {
            Objects.requireNonNull(tooltipComponents);
            fireworks.addToTooltip(context, tooltipComponents::add, tooltipFlag);
        }

    }
}
