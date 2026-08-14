package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.constants.StrikeSlot;
import com.vidal1shere.combatmania.entity.ThrownJavelin;
import com.vidal1shere.combatmania.registry.data.CMAttributes;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

public class JavelinItem extends TieredItem implements IStrikeWeapon, ProjectileItem {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float RANGE_BONUS = 1.0F;
    
    public JavelinItem(Tier tier, Properties properties) {
        super(tier, properties.component(DataComponents.TOOL, createToolProperties()));
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getStyle(StrikeSlot slot) {
        if (slot == StrikeSlot.PRIMARY) return CMStrikeStyles.JAB;
        return null;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, (double)8.0F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, (double)-2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }

    public static ItemAttributeModifiers createAttributes(Tier tier, double damage, double jabSpeed, double shootPower) {
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
                        CMAttributes.SHOOT_POWER,
                        new AttributeModifier(
                                CMAttributes.BASE_SHOOT_POWER_ID,
                                shootPower,
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

    public static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    public void releaseUsing(ItemStack stack, Level level, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int useTime = this.getUseDuration(stack, entityLiving) - timeLeft;

            if (useTime >= THROW_THRESHOLD_TIME) {
                float spinAttackStrength = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);

                if ((!(spinAttackStrength > 0.0F) || player.isInWaterOrRain()) && !isTooDamagedToUse(stack)) {
                    Holder<SoundEvent> sound = EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);

                    if (!level.isClientSide()) {
                        if (spinAttackStrength == 0.0F) {
                            float shootPower = (float) player.getAttributeValue(CMAttributes.SHOOT_POWER);
                            ThrownJavelin thrownJavelin = new ThrownJavelin(level, player, stack);
                            thrownJavelin.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, shootPower, 1.0F);

                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entityLiving.getUsedItemHand()));

                            if (player.hasInfiniteMaterials()) {
                                thrownJavelin.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(thrownJavelin);
                            level.playSound(null, thrownJavelin, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

                            if (!player.hasInfiniteMaterials()) {
                                player.getInventory().removeItem(stack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));

                    if (spinAttackStrength > 0.0F) {
                        float playerYaw = player.getYRot();
                        float playerPitch = player.getXRot();

                        float directionX = -Mth.sin(playerYaw * ((float) Math.PI / 180F)) * Mth.cos(playerPitch * ((float) Math.PI / 180F));
                        float directionY = -Mth.sin(playerPitch * ((float) Math.PI / 180F));
                        float directionZ = Mth.cos(playerYaw * ((float) Math.PI / 180F)) * Mth.cos(playerPitch * ((float) Math.PI / 180F));

                        float directionLength = Mth.sqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);

                        directionX *= spinAttackStrength / directionLength;
                        directionY *= spinAttackStrength / directionLength;
                        directionZ *= spinAttackStrength / directionLength;

                        player.push(directionX, directionY, directionZ);
                        player.startAutoSpinAttack(20, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE), stack);

                        if (player.onGround()) {
                            float groundJumpStrength = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0D, groundJumpStrength, 0.0D));
                        }

                        level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (isTooDamagedToUse(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        } else if (EnchantmentHelper.getTridentSpinAttackStrength(itemstack, player) > 0.0F && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    private static boolean isTooDamagedToUse(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
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
        return ItemAbilities.DEFAULT_TRIDENT_ACTIONS.contains(itemAbility);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public Projectile asProjectile(Level level, Position position, ItemStack itemStack, Direction direction) {
        return null;
    }
}
