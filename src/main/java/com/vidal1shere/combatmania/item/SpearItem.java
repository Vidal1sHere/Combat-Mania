package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.data.AnimationTrack;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public class SpearItem extends TieredItem implements IStrikeWeapon, IAnimatedItem {
    public SpearItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getPrimaryStyle() {
        return CMStrikeStyles.JAB;
    }

    @Override
    public @Nullable DeferredHolder<StrikeStyle, ? extends StrikeStyle> getSecondaryStyle() {
        return null;
    }

    @Override
    public @Nullable AnimationTrack getAnimation(LocalPlayer player, HumanoidArm arm, ItemStack stack) {
        return null;
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, double damage, double jabSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_ID,
                                (double) (damage + tier.getAttackDamageBonus()),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        CMAttributes.JAB_SPEED,
                        new AttributeModifier(
                                CMAttributes.BASE_JAB_SPEED_ID,
                                jabSpeed,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && !player.isCreative())
            stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }
}
