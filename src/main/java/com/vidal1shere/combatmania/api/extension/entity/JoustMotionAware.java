package com.vidal1shere.combatmania.api.extension.entity;

import net.minecraft.world.phys.Vec3;

public interface JoustMotionAware {
    Vec3 getJoustMovement();

    void setJoustMovement(Vec3 movement);
}