package com.vidal1shere.combatmania.api.extension.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class ClientStrikeWeaponExtension implements IClientItemExtensions {

    @Override
    public HumanoidModel.@Nullable ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack stack) {
        if (!IStrikeWeapon.isStrikeWeapon(stack))
            return null;
            
        return ((IStrikeWeapon) stack.getItem()).getArmPose(entityLiving, hand, stack);
    }
}
