package com.vidal1shere.combatmania.item.weapon;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.HammerAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.constants.HammerTransforms;
import com.vidal1shere.combatmania.data.component.ShockwaveProperties;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.item.CMExtendedToolTiers;
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
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class HammerItem extends TieredItem implements IStrikeWeapon {

    public HammerItem(IExtendedTier tier, Properties properties, ShockwaveProperties shockwaveProperties) {
        super(tier, properties.component(CMDataComponents.SHOCKWAVE, shockwaveProperties));
    }
    
    public HammerItem(IExtendedTier tier, Properties properties) {
        this(tier, properties, tier.getShockwaveProperties());
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.SHOCKWAVE;
        return null;
    }

    public static ItemAttributeModifiers createAttributes(IExtendedTier tier, double damage, double speed) {
        return createAttributes(tier, damage, speed, tier.getAttackStagger());
    }

    public static ItemAttributeModifiers createAttributes(IExtendedTier tier, double damage, double speed, double stagger) {
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
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(
                                BASE_ATTACK_SPEED_ID,
                                speed, AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        CMAttributes.ATTACK_STAGGER,
                        new AttributeModifier(
                                CMAttributes.BASE_ATTACK_STAGGER_ID,
                                stagger, AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    public HammerTransforms getTierTexture() {
        Tier tier = this.getTier() instanceof CMExtendedToolTiers ext ? ext.getVanillaTier() : getTier();

        return switch (tier) {
            case Tiers.WOOD -> HammerTransforms.WOOD;
            case Tiers.STONE -> HammerTransforms.STONE;
            case Tiers.GOLD -> HammerTransforms.GOLD;
            case Tiers.DIAMOND -> HammerTransforms.DIAMOND;
            default -> HammerTransforms.IRON;
        };
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    public static ItemAnimationDefinition maceAnimation(ItemStack stack, InteractionHand hand,
                                                        Player player, ItemAnimationState state) {
        return HammerAnimations.whack();
    }
}
