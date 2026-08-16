package com.vidal1shere.combatmania.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class StaggerData {

    public static final StreamCodec<ByteBuf, StaggerData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, d -> d.strength,
            ByteBufCodecs.INT, d -> d.ticks,
            StaggerData::new
    );

    public static final Codec<StaggerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("strength").forGetter(d -> d.strength),
            Codec.INT.fieldOf("ticks").forGetter(d -> d.ticks)
    ).apply(instance, StaggerData::new));
    
    private float strength;
    private int ticks;

    public StaggerData() {
        this.strength = 0;
        this.ticks = 0;
    }

    public StaggerData(float strength, int ticks) {
        this.strength = strength;
        this.ticks = ticks;
    }
    
    public boolean isStaggered() {
        return ticks > 0;
    }

    public float getStrength() {
        return strength;
    }

    public void tick(LivingEntity entity) {
        if (ticks > 0) {
            ticks--;

            if (ticks == 0) {
                AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);

                if (movementSpeed != null) {
                    movementSpeed.removeModifier(CMAttributes.STAGGER_MOVEMENT_SPEED_MODIFIER);
                }
            }
            
            entity.syncData(CMAttachments.STAGGER);
        }
    }
    
    public static void applyFullStun(LivingEntity entity, int ticks) {
        applyStagger(entity, 5.0F, ticks);
    }

    public static void applyStagger(LivingEntity entity, float strength, int ticks) {
        StaggerData data = entity.getData(CMAttachments.STAGGER);
        data.strength = strength;
        data.ticks = ticks;

        AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);

        if (movementSpeed == null) {
            entity.syncData(CMAttachments.STAGGER);
            return;
        }

        movementSpeed.addOrUpdateTransientModifier(new AttributeModifier(
                CMAttributes.STAGGER_MOVEMENT_SPEED_MODIFIER,
                -(strength * 0.2F),
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        ));

        entity.syncData(CMAttachments.STAGGER);
    }
}
