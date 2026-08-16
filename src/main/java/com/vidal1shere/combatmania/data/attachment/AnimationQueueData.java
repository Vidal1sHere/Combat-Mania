package com.vidal1shere.combatmania.data.attachment;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class AnimationQueueData {

    public static final StreamCodec<ByteBuf, AnimationQueueData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            data -> data.queuedAnimation,
            AnimationQueueData::new
    );
    
    private String queuedAnimation = "";

    public AnimationQueueData(String anim) {
        this.queuedAnimation = anim;
    }
    
    public AnimationQueueData() {}
    
    public void queueAnimation(String animation) {
        this.queuedAnimation = animation;
    }
    
    public boolean hasQueuedAnimation() {
        return !this.queuedAnimation.isEmpty();
    }

    public boolean hasQueuedAnimation(String targetAnimation) {
        return !targetAnimation.isEmpty() && this.queuedAnimation.equals(targetAnimation);
    }
    
    public void clearAnimation() {
        this.queuedAnimation = "";
    }
}
