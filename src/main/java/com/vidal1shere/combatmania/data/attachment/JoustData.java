package com.vidal1shere.combatmania.data.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;

import java.util.List;

public class JoustData {

    public static final Codec<JoustData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("stage").forGetter(d -> d.stage.ordinal())
    ).apply(instance, (o) -> new JoustData(Stage.byOrdinal(o))));
    
    private Stage stage;

    public JoustData() {
        this.stage = Stage.NONE;
    }

    public JoustData(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public boolean canPerformAction(Action action) {
        return getStage().canPerformAction(action);
    }
    
    public void increaseStage() {
        int ordinal = getStage().ordinal();
        
        if (ordinal >= Stage.values().length) {
            ordinal = Stage.values().length - 1;
        } else {
            ordinal++;
        }
        
        setStage(Stage.byOrdinal(ordinal));
    }

    public void decreaseStage() {
        int ordinal = getStage().ordinal();

        if (ordinal > 0) {
            ordinal--;
        }

        setStage(Stage.byOrdinal(ordinal));
    }
    
    public void reset() {
        setStage(Stage.NONE);
    }
    
    public ItemAnimationDefinition getAnimation() {
        return switch (getStage()) {
            case NONE -> null;
            case EQUIP -> SpearAnimations.joustEquip();
            case ENGAGED -> SpearAnimations.joustEngaged();
            case TIRED -> SpearAnimations.joustTired();
            case DISENGAGED -> SpearAnimations.joustDisengaged();
        };
    }

    public enum Stage {
        NONE(List.of()),
        EQUIP(List.of()),
        ENGAGED(List.of(Action.DAMAGE, Action.KNOCKBACK, Action.DISMOUNT)),
        TIRED(List.of(Action.DAMAGE, Action.KNOCKBACK)),
        DISENGAGED(List.of(Action.DAMAGE));
        
        private final List<Action> capabilities;

        Stage(List<Action> capabilities) {
            this.capabilities = capabilities;
        }

        public List<Action> getCapabilities() {
            return capabilities;
        }
        
        public boolean canPerformAction(Action action) {
            return capabilities.contains(action);
        }
        
        public static Stage byOrdinal(int ordinal) {
            for (Stage s : values()) {
                if (s.ordinal() == ordinal) return s;
            }
            return null;
        }
    }
    
    public enum Action {
        DAMAGE,
        KNOCKBACK,
        DISMOUNT;
    }
}
