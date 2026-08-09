package com.vidal1shere.combatmania.data.attachment;

import com.vidal1shere.combatmania.registry.data.CMAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoustData {

    public static final StreamCodec<ByteBuf, JoustData> STREAM_CODEC =
            StreamCodec.of(
                    (buf, data) -> {
                        buf.writeInt(data.stage.ordinal());
                        buf.writeBoolean(data.queuedAnimation);
                    },
                    buf -> {
                        JoustData data = new JoustData();
                        data.stage = Stage.byOrdinal(buf.readInt());
                        data.queuedAnimation = buf.readBoolean();
                        data.hitEntities.clear();
                        return data;
                    }
            );

    private final List<UUID> hitEntities = new ArrayList<>();
    private Stage stage = Stage.NONE;
    private boolean queuedAnimation = false;

    public JoustData() {}

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
    
    public boolean hasHitEntity(Entity entity) {
        if (entity == null) return false;
        return hitEntities.contains(entity.getUUID());
    }
    
    public static JoustData get(Player player) {
        return player.getData(CMAttachments.JOUST);
    }
    
    public static void start(Player player) {
        get(player).setStage(Stage.ENGAGED);
        get(player).queuedAnimation = true;
        player.syncData(CMAttachments.JOUST);
    }

    public static void increaseStage(Player player) {
        JoustData data = get(player);
        int ordinal = data.getStage().ordinal();
        if (ordinal >= Stage.values().length) {
            ordinal = Stage.values().length - 1;
        } else {
            ordinal++;
        }
        data.setStage(Stage.byOrdinal(ordinal));
        data.queuedAnimation = true;
        player.syncData(CMAttachments.JOUST);
    }

    public static void reset(Player player) {
        JoustData data = get(player);
        data.setStage(Stage.NONE);
        data.queuedAnimation = false;
        data.hitEntities.clear();
        player.syncData(CMAttachments.JOUST);
    }
    
    public static void registerEntity(Player player, Entity entity) {
        if (entity == null) return;
        get(player).hitEntities.add(entity.getUUID());
    }

    public static void consumeAnimation(Player player) {
        get(player).queuedAnimation = false;
    }
    
    public boolean hasQueuedAnimation() {
        return queuedAnimation;
    }

    public enum Stage {
        NONE(List.of()),
        ENGAGED(List.of(Action.DAMAGE, Action.KNOCKBACK, Action.DISMOUNT)),
        TIRED(List.of(Action.DAMAGE, Action.KNOCKBACK)),
        DISENGAGED(List.of(Action.DAMAGE));

        private final List<Action> actions;

        Stage(List<Action> actions) {
            this.actions = actions;
        }

        public List<Action> getActions() {
            return actions;
        }

        public boolean canPerformAction(Action action) {
            return actions.contains(action);
        }

        public static Stage byOrdinal(int ordinal) {
            for (Stage s : values()) {
                if (s.ordinal() == ordinal) return s;
            }
            return NONE;
        }
    }

    public enum Action {
        DAMAGE,
        KNOCKBACK,
        DISMOUNT;
    }
}