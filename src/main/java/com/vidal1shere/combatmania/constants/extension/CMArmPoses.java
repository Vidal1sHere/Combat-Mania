package com.vidal1shere.combatmania.constants.extension;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.neoforge.client.IArmPoseTransformer;

public class CMArmPoses {
    public static final EnumProxy<HumanoidModel.ArmPose> SPEAR_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            (IArmPoseTransformer) (model, entity, arm) -> {
                var armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
                float targetXRot = -Mth.clamp(
                        90.0F - model.head.xRot * Mth.RAD_TO_DEG,
                        10.0F,
                        160.0F
                ) * Mth.DEG_TO_RAD;

                armPart.xRot = Mth.lerp(.85F, armPart.xRot, targetXRot);
                armPart.yRot += model.head.yRot;
            }
    );

    public static final EnumProxy<HumanoidModel.ArmPose> HAMMER_PROXY = new EnumProxy<>(
            HumanoidModel.ArmPose.class,
            false,
            (IArmPoseTransformer) (model, entity, arm) -> {
                var armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
                var offHand = arm == HumanoidArm.RIGHT ? model.leftArm : model.rightArm;

                float side = arm == HumanoidArm.RIGHT ? 1F : -1F;
                
                armPart.xRot = Mth.lerp(
                        .85F,
                        armPart.xRot,
                        -Mth.DEG_TO_RAD * 100F
                );

                offHand.xRot = Mth.lerp(
                        .85F,
                        offHand.xRot,
                        -Mth.DEG_TO_RAD * 35F
                );
                
                armPart.yRot = Mth.lerp(
                        .85F,
                        armPart.xRot,
                        -Mth.DEG_TO_RAD * 30F
                );
                
                armPart.zRot = Mth.lerp(
                        .85F,
                        armPart.zRot,
                        side * Mth.DEG_TO_RAD * 100F
                );
            }
    );

    public static HumanoidModel.ArmPose SPEAR() {
        return SPEAR_PROXY.getValue();
    }
    public static HumanoidModel.ArmPose HAMMER() {
        return HAMMER_PROXY.getValue();
    }
}
