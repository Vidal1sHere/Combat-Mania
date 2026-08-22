package com.vidal1shere.combatmania.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;

public final class RaycastUtil {

    public static HitResult pick(Entity entity, double blockInteractionRange, double entityInteractionRange) {
        double maxInteractionRange = Math.max(blockInteractionRange, entityInteractionRange);
        double maxDistanceSqr = Mth.square(maxInteractionRange);
        Vec3 eyePosition = entity.getEyePosition(1.0f);
        HitResult blockHit = entity.pick(maxInteractionRange, 1.0f, false);
        double blockHitDistanceSqr = blockHit.getLocation().distanceToSqr(eyePosition);

        if (blockHit.getType() != HitResult.Type.MISS) {
            maxDistanceSqr = blockHitDistanceSqr;
            maxInteractionRange = Math.sqrt(blockHitDistanceSqr);
        }

        Vec3 viewVector = entity.getViewVector(1.0f);
        Vec3 rayEnd = eyePosition.add(viewVector.x * maxInteractionRange, viewVector.y * maxInteractionRange, viewVector.z * maxInteractionRange);
        AABB searchBox = entity.getBoundingBox().expandTowards(viewVector.scale(maxInteractionRange)).inflate(1.0D);
        EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(entity, eyePosition, rayEnd, searchBox, (target) -> !target.isSpectator() && target.isPickable(), maxDistanceSqr);

        return entityHit != null && entityHit.getLocation().distanceToSqr(eyePosition) < blockHitDistanceSqr ? filterHitResult(entityHit, eyePosition, entityInteractionRange) : filterHitResult(blockHit, eyePosition, blockInteractionRange);
    }

    public static HitResult filterHitResult(HitResult hitResult, Vec3 position, double interactionRange) {
        Vec3 hitPosition = hitResult.getLocation();

        if (!hitPosition.closerThan(position, interactionRange)) {
            Direction missDirection = Direction.getNearest(hitPosition.x - position.x, hitPosition.y - position.y, hitPosition.z - position.z);
            return BlockHitResult.miss(hitPosition, missDirection, BlockPos.containing(hitPosition));
        }

        return hitResult;
    }
}
