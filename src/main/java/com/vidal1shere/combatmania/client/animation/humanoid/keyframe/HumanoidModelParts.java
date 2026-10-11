package com.vidal1shere.combatmania.client.animation.humanoid.keyframe;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class HumanoidModelParts {
    private HumanoidModelParts() {}

    public static Function<String, ModelPart> resolver(HumanoidModel<?> model) {
        Map<String, ModelPart> map = new HashMap<>();
        map.put("head", model.head);
        map.put("hat", model.hat);
        map.put("body", model.body);
        map.put("right_arm", model.rightArm);
        map.put("left_arm", model.leftArm);
        map.put("right_leg", model.rightLeg);
        map.put("left_leg", model.leftLeg);

        if (model instanceof PlayerModel<?> playerModel) {
            map.put("jacket", playerModel.jacket);
            map.put("left_sleeve", playerModel.leftSleeve);
            map.put("right_sleeve", playerModel.rightSleeve);
            map.put("left_pants", playerModel.leftPants);
            map.put("right_pants", playerModel.rightPants);
        }

        return map::get;
    }
}