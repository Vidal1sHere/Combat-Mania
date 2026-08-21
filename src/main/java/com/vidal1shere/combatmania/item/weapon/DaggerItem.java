package com.vidal1shere.combatmania.item.weapon;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.DaggerAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class DaggerItem extends TieredItem implements IStrikeWeapon {

    public static final float BASE_ATTACK_SPEED = -2.4F;
    public static final float BASE_STAB_SPEED = -3.0F;
    public static final float TIER_SPEED_MULTIPLIER = 0.1F;
    public static final float REACH_MODIFIER = -0.25F;
    
    public DaggerItem(Tier tier, Item.Properties properties) {
        super(tier, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    public DaggerItem(Tier tier, Item.Properties properties, Tool toolComponentData) {
        super(tier, properties.component(DataComponents.TOOL, toolComponentData));
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.DAGGER_SWEEP;
        else return CMStrikeStyles.STAB;
    }

    public static Tool createToolProperties() {
        return new Tool(List.of(Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F), Tool.Rule.overrideSpeed(BlockTags.SWORD_EFFICIENT, 1.5F)), 1.0F, 2);
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, float attackDamage) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                                attackDamage + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                                BASE_ATTACK_SPEED + (tier.getSpeed() * TIER_SPEED_MULTIPLIER), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(CMAttributes.STAB_SPEED,
                        new AttributeModifier(CMAttributes.BASE_STAB_SPEED_ID,
                                BASE_STAB_SPEED, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ENTITY_INTERACTION_RANGE,
                        new AttributeModifier(CMAttributes.BASE_ENTITY_INTERACTION_RANGE_ID,
                                REACH_MODIFIER, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
    
    public static boolean backStabQueued(Player player) {
        return player.getData(CMAttachments.BACKSTAB);
    }

    public static void queueBackStab(Player player) {
        player.setData(CMAttachments.BACKSTAB, true);
        player.syncData(CMAttachments.BACKSTAB);
    }
    
    public static void clearBackStab(Player player) {
        player.setData(CMAttachments.BACKSTAB, false);
        player.syncData(CMAttachments.BACKSTAB);
    }
    
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static void sweepParticle(Player player, LivingEntity target) {
        double d0 = (-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)));
        double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleUtil.sweep(false, true, !player.onGround(), false),
                    target.getX() - d0, target.getY() + 1, target.getZ() - d1, 0, d0, 0.0F, d1, 0.0F);
        }
    }

    public static ItemAnimationDefinition stabAnimation(ItemStack stack, InteractionHand hand,
                                                         Player player, ItemAnimationState state) {
        return DaggerAnimations.stab();
    }

    public static ItemAnimationDefinition backstabAnimation(ItemStack stack, InteractionHand hand,
                                                        Player player, ItemAnimationState state) {
        return DaggerAnimations.backStab();
    }
}
