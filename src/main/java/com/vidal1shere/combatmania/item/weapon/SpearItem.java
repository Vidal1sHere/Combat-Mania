package com.vidal1shere.combatmania.item.weapon;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SpearItem extends TieredItem implements IStrikeWeapon {
    
    public static final float RANGE_BONUS = 1.5F;
    
    public SpearItem(IExtendedTier tier, Properties properties, JoustProperties joustProperties) {
        super(tier, properties.component(CMDataComponents.JOUST, joustProperties));
    }

    public SpearItem(IExtendedTier tier, Properties properties) {
        this(tier, properties, tier.getJoustProperties());
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.SPEAR_JAB;
        else return CMStrikeStyles.JOUST;
    }

    @Override
    public IExtendedTier getTier() {
        return (IExtendedTier) super.getTier();
    }

    public static ItemAttributeModifiers createAttributes(IExtendedTier tier, double damage, double jabSpeed, double joustSpeed) {
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
                .add(
                        CMAttributes.JOUST_DAMAGE_MULTIPLIER,
                        new AttributeModifier(
                                CMAttributes.BASE_JOUST_DAMAGE_MULTIPLIER_ID,
                                tier.getJoustProperties().damageMultiplier(),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        CMAttributes.MAX_JOUST_DAMAGE,
                        new AttributeModifier(
                                CMAttributes.BASE_MAX_JOUST_DAMAGE_ID,
                                tier.getJoustProperties().maxDamage(),
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        CMAttributes.JOUST_SPEED,
                        new AttributeModifier(
                                CMAttributes.BASE_JOUST_SPEED_ID,
                                joustSpeed,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(
                                CMAttributes.BASE_ENTITY_INTERACTION_RANGE_ID,
                                RANGE_BONUS,
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

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static ItemAnimationDefinition spearAnimation(ItemStack stack, InteractionHand hand,
                                                          Player player, ItemAnimationState state) {
        return stack.is(Items.TRIDENT) ? SpearAnimations.tridentJab() : SpearAnimations.jab();
    }
}
