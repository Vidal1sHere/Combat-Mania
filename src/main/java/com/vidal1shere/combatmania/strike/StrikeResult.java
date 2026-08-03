package com.vidal1shere.combatmania.strike;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class StrikeResult {

    public enum Type {
        HIT_ENTITY,
        MISS
    }

    private Type type = Type.MISS;

    @Nullable
    private Entity hitEntity;

    public void setHitEntity(Entity entity) {
        this.type = Type.HIT_ENTITY;
        this.hitEntity = entity;
    }

    public Type getType() { return type; }
    @Nullable public Entity getHitEntity()  { return hitEntity; }
}