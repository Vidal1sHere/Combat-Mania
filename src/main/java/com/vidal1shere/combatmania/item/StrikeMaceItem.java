package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.definition.ItemAnimationDefinition;
import com.vidal1shere.combatmania.client.animation.item.HammerAnimations;
import com.vidal1shere.combatmania.client.animation.item.SpearAnimations;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationState;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.constants.TieredMaceModels;
import com.vidal1shere.combatmania.data.component.SmashProperties;
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
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class StrikeMaceItem extends TieredItem implements IStrikeWeapon {
    
    public StrikeMaceItem(IExtendedTier tier, Properties properties, SmashProperties smashProperties) {
        super(tier, properties.component(CMDataComponents.SMASH, smashProperties));
    }

    public StrikeMaceItem(IExtendedTier tier, Properties properties) {
        super(tier, properties.component(CMDataComponents.SMASH, tier.getSmashProperties()));
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.SMASH;
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
    
    public TieredMaceModels getTierTexture() {
        Tier tier = this.getTier() instanceof CMExtendedToolTiers ext ? ext.getVanillaTier() : getTier();
        
        return switch (tier) {
            case Tiers.WOOD -> TieredMaceModels.WOOD;
            case Tiers.STONE -> TieredMaceModels.STONE;
            case Tiers.GOLD -> TieredMaceModels.GOLD;
            case Tiers.DIAMOND -> TieredMaceModels.DIAMOND;
            default -> TieredMaceModels.IRON;
        };
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    public static ItemAnimationDefinition maceAnimation(ItemStack stack, InteractionHand hand,
                                                         Player player, ItemAnimationState state) {
        return HammerAnimations.whack();
    }
}
