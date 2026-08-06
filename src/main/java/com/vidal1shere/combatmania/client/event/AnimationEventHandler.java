package com.vidal1shere.combatmania.client.event;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.client.old_animation.FirstPersonAnimationManager;
import com.vidal1shere.combatmania.client.old_animation.data.Keyframe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.event.level.LevelEvent;


/**
 * Hooks into NeoForge events to tick and reset the animation manager.
 *
 * <p>Registered on the NeoForge (game) event bus, client-side only.
 */
@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public final class AnimationEventHandler {
    
    /**
     * Advance all animation states by one tick (1/20 second) on the client.
     * has already run.
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        // 1 tick = 1/20 second
        FirstPersonAnimationManager.INSTANCE.tick(1f / 20f);
    }

    /**
     * Reset all animation states when the player leaves a world/server,
     * avoiding stale state if they rejoin with a different item in hand.
     */
    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel().isClientSide()) {
            FirstPersonAnimationManager.INSTANCE.reset();
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof IAnimatedItem animated)) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        HumanoidArm arm = event.getHand() == InteractionHand.MAIN_HAND
                ? player.getMainArm()
                : player.getMainArm().getOpposite();

        Keyframe kf = FirstPersonAnimationManager.INSTANCE.evaluate(
                player, arm, stack, event.getPartialTick()
        );
        if (kf == null) return;

        boolean doMirror = arm == HumanoidArm.LEFT && animated.mirrorForLeftHand();
        float mirror = doMirror ? -1f : 1f;

        PoseStack poseStack = event.getPoseStack();
        
        poseStack.translate(
                mirror * kf.translateX(),
                kf.translateY(),
                kf.translateZ()
        );

        if (kf.rotateX() != 0f)
            poseStack.mulPose(Axis.XP.rotationDegrees(kf.rotateX()));
        if (kf.rotateY() != 0f)
            poseStack.mulPose(Axis.YP.rotationDegrees(mirror * kf.rotateY()));
        if (kf.rotateZ() != 0f)
            poseStack.mulPose(Axis.ZP.rotationDegrees(mirror * kf.rotateZ()));
        if (kf.scale() != 1f)
            poseStack.scale(kf.scale(), kf.scale(), kf.scale());
    }
}