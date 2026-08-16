package com.vidal1shere.combatmania.util;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.registry.item.CMExtendedToolTiers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class CMUtil {

    public static ResourceLocation modResLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(CombatMania.MOD_ID, path);
    }

    public static ResourceLocation mcResLoc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation neoforgeResLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }

    public static ResourceLocation resLoc(EntityType<?> entity) {
        return EntityType.getKey(entity);
    }

    public static int secondsToTicks(double seconds) {
        return Math.round((float) (seconds * 20.0));
    }

    public static float blocksPerSecond(float blocksPerTick) {
        return blocksPerTick * 20.0F;
    }

    public static double angleToDot(float angle) {
        return Math.cos(Math.toRadians(angle));
    }
    
    public static boolean isWoodTier(Tier tier) {
        return tier == CMExtendedToolTiers.WOOD || tier == Tiers.WOOD;
    }
    
    public static String tierName(Tier tier) {
        if (tier instanceof CMExtendedToolTiers) {
            tier = ((CMExtendedToolTiers) tier).getVanillaTier();
        }
        
        return switch (tier) {
            case Tiers.WOOD -> "wooden";
            case Tiers.STONE -> "stone";
            case Tiers.GOLD -> "golden";
            case Tiers.IRON -> "iron";
            case Tiers.DIAMOND -> "diamond";
            case Tiers.NETHERITE -> "netherite";
            default -> "";
        };
    }

    public static Tier tierFromName(String name) {
        return switch (name) {
            case "wooden" -> Tiers.WOOD;
            case "stone" -> Tiers.STONE;
            case "golden" -> Tiers.GOLD;
            case "diamond" -> Tiers.DIAMOND;
            case "netherite" -> Tiers.NETHERITE;
            default -> Tiers.IRON;
        };
    }

    public static CompoundTag getNbt(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void updateNbt(ItemStack stack, Consumer<CompoundTag> mutator) {
        CompoundTag tag = getNbt(stack);
        mutator.accept(tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Nullable
    public static EntityHitResult getEntityHitResult(Entity shooter, Vec3 startVec, Vec3 endVec,
                                                     AABB boundingBox, Predicate<Entity> filter,
                                                     double distance, float inflation) {
        Level level = shooter.level();
        double d0 = distance;
        Entity entity = null;
        Vec3 vec3 = null;

        for (Entity entity1 : level.getEntities(shooter, boundingBox, filter)) {
            AABB aabb = entity1.getBoundingBox().inflate(inflation);
            Optional<Vec3> optional = aabb.clip(startVec, endVec);
            if (aabb.contains(startVec)) {
                if (d0 >= 0.0) {
                    entity = entity1;
                    vec3 = optional.orElse(startVec);
                    d0 = 0.0;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = startVec.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0) {
                    if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0) {
                            entity = entity1;
                            vec3 = vec31;
                        }
                    } else {
                        entity = entity1;
                        vec3 = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }

    public static float getEnchantedDamage(Player player, ItemStack weapon, Entity entity, float damage, DamageSource damageSource) {
        if (!(player.level() instanceof ServerLevel serverLevel)) return damage;

        return EnchantmentHelper.modifyDamage(serverLevel, weapon, entity, damageSource, damage);
    }

    public static float getKnockbackStrength(Player player, ItemStack itemstack, Entity target, DamageSource damagesource) {
        return (float) player.getAttributeValue(Attributes.ATTACK_KNOCKBACK)
                + EnchantmentHelper.modifyKnockback((ServerLevel) player.level(), itemstack, target, damagesource, 0);
    }
}
