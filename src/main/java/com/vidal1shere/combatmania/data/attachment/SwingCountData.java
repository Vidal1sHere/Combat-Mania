package com.vidal1shere.combatmania.data.attachment;

import com.vidal1shere.combatmania.registry.data.CMAttachments;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;

public final class SwingCountData {
    
    public static final StreamCodec<ByteBuf, SwingCountData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, d -> d.count,
            SwingCountData::new
    );
    
    private int count = 0;

    public SwingCountData() {
    }

    public SwingCountData(int count) {
        this.count = count;
    }
    
    public void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }
    
    public void reset() {
        count = 0;
    }
    
    public static InteractionHand getHandForDualWield(Player player) {
        SwingCountData data = player.getData(CMAttachments.SWING_COUNT);
        
        return data.getCount() % 2 == 0 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }
}
