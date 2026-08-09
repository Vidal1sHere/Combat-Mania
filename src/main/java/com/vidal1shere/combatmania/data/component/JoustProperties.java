package com.vidal1shere.combatmania.data.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.util.CMUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

import static com.vidal1shere.combatmania.util.CustomProperties.*;

public class JoustProperties {
    
    public static final Key<JoustProperties> KEY = new Key<>(new JoustProperties());

    public static final float EQUIP_DURATION = 0.5F;
    public static final float ENGAGED_DURATION = 3.0F;
    public static final float TIRED_DURATION = 3.5F;
    public static final float DISENGAGED_DURATION = 3.5F;

    public static final Codec<JoustProperties> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("damage_multiplier")
                            .forGetter(JoustProperties::getDamageMultiplier),
                    Codec.FLOAT.fieldOf("min_dismount_speed")
                            .forGetter(JoustProperties::getMinDismountSpeed),
                    Codec.FLOAT.fieldOf("equip_speed")
                            .forGetter(properties -> properties.equipSpeed),
                    Codec.FLOAT.fieldOf("engaged_speed")
                            .forGetter(properties -> properties.engagedSpeed),
                    Codec.FLOAT.fieldOf("tired_speed")
                            .forGetter(properties -> properties.tiredSpeed),
                    Codec.FLOAT.fieldOf("disengaged_speed")
                            .forGetter(properties -> properties.disengagedSpeed)
            ).apply(instance, JoustProperties::new)
    );

    public static final StreamCodec<ByteBuf, JoustProperties> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT, JoustProperties::getDamageMultiplier,
                    ByteBufCodecs.FLOAT, JoustProperties::getMinDismountSpeed,
                    ByteBufCodecs.FLOAT, JoustProperties::getEquipSpeed,
                    ByteBufCodecs.FLOAT, JoustProperties::getEngagedSpeed,
                    ByteBufCodecs.FLOAT, JoustProperties::getTiredSpeed,
                    ByteBufCodecs.FLOAT, JoustProperties::getDisengagedSpeed,
                    JoustProperties::new
            );

    private final float damageMultiplier;
    private final float minDismountSpeed;
    private final float equipSpeed;
    private final float engagedSpeed;
    private final float tiredSpeed;
    private final float disengagedSpeed;

    public JoustProperties(float damageMultiplier, float minDismountSpeed, float equipSpeed, 
                           float engagedSpeed, float tiredSpeed, float disengagedSpeed) {
        this.damageMultiplier = damageMultiplier;
        this.minDismountSpeed = minDismountSpeed;
        this.equipSpeed = equipSpeed;
        this.engagedSpeed = engagedSpeed;
        this.tiredSpeed = tiredSpeed;
        this.disengagedSpeed = disengagedSpeed;
    }

    public JoustProperties() {
        this(0.7F, 10.0F, 1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    public static Set<JoustProperties> of(float damageMultiplier, float minDismountSpeed, float equipSpeed,
                                   float engagedSpeed, float tiredSpeed, float disengagedSpeed) {
        return new Set<>(KEY, new JoustProperties(
                damageMultiplier, minDismountSpeed, equipSpeed, 
                engagedSpeed, tiredSpeed, disengagedSpeed)
        );
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getMinDismountSpeed() {
        return minDismountSpeed;
    }

    public float getEquipSpeed() {
        return equipSpeed;
    }

    public float getEngagedSpeed() {
        return engagedSpeed;
    }

    public float getTiredSpeed() {
        return tiredSpeed;
    }

    public float getDisengagedSpeed() {
        return disengagedSpeed;
    }

    public float getSpeedForStage(JoustData.Stage stage) {
        return switch (stage) {
            case NONE -> 1.0F;
            case ENGAGED -> engagedSpeed;
            case TIRED -> tiredSpeed;
            case DISENGAGED -> disengagedSpeed;
        };
    }

    public float getDurationSeconds(JoustData.Stage stage) {
        return switch (stage) {
            case NONE -> 1.0F;
            case ENGAGED -> ENGAGED_DURATION / engagedSpeed;
            case TIRED -> TIRED_DURATION / tiredSpeed;
            case DISENGAGED -> DISENGAGED_DURATION / disengagedSpeed;
        };
    }
    
    public int getEquipDurationTicks() {
        return CMUtil.secondsToTicks(EQUIP_DURATION / equipSpeed);
    }

    public int getDurationTicks(JoustData.Stage stage) {
        return CMUtil.secondsToTicks(getDurationSeconds(stage));
    }

    public int getTotalDuration() {
        return CMUtil.secondsToTicks(getDurationSeconds(JoustData.Stage.ENGAGED)) +
                CMUtil.secondsToTicks(getDurationSeconds(JoustData.Stage.TIRED)) +
                CMUtil.secondsToTicks(getDurationSeconds(JoustData.Stage.DISENGAGED));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JoustProperties other)) return false;

        return Float.compare(damageMultiplier, other.damageMultiplier) == 0
                && Float.compare(minDismountSpeed, other.minDismountSpeed) == 0
                && Float.compare(equipSpeed, other.equipSpeed) == 0
                && Float.compare(engagedSpeed, other.engagedSpeed) == 0
                && Float.compare(tiredSpeed, other.tiredSpeed) == 0
                && Float.compare(disengagedSpeed, other.disengagedSpeed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                damageMultiplier,
                minDismountSpeed,
                equipSpeed,
                engagedSpeed,
                tiredSpeed,
                disengagedSpeed
        );
    }
}