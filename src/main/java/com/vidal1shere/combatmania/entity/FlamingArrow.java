package com.vidal1shere.combatmania.entity;

import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class FlamingArrow extends AbstractArrow {
    private static final int FLAME_DURATION = 50;
    private static final int MAX_FLAME_DURATION = 300;
    private static final float BASE_DAMAGE = 3.0F;
    
    public FlamingArrow(EntityType<? extends FlamingArrow> entityType, Level level) {
        super(entityType, level);
        this.setBaseDamage(BASE_DAMAGE);
    }

    public FlamingArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(CMEntities.FLAMING_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(BASE_DAMAGE);
    }

    public FlamingArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(CMEntities.FLAMING_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
        this.setBaseDamage(BASE_DAMAGE);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide() && !this.inGround) {
            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    protected void doPostHurtEffects(LivingEntity target) {
//        target.setRemainingFireTicks(Math.min(MAX_FLAME_DURATION, target.getRemainingFireTicks() + FLAME_DURATION));
        
        if (level() instanceof ServerLevel level) {
            level.sendParticles(
                    ParticleTypes.FLAME,
                    getX(),
                    getEyeY(),
                    getZ(),
                    32, 0, 0, 0, 0.5
            );
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        BlockPos pos = result.getBlockPos().relative(result.getDirection());

        if (this.level().isClientSide()) return;

        if (BaseFireBlock.canBePlacedAt(this.level(), pos, this.getDirection())) {
            this.level().setBlockAndUpdate(pos, BaseFireBlock.getState(this.level(), pos));
        }

        if (level() instanceof ServerLevel level) {
            level.sendParticles(
                    ParticleTypes.FLAME,
                    getX(),
                    getEyeY(),
                    getZ(),
                    32, 0, 0, 0, 0.5
            );
        }

        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return CMSounds.FLAMING_ARROW_IMPACT.get();
    }

    @Override
    public boolean isOnFire() {
        if (level().isClientSide()) return false;
        return super.isOnFire();
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }
}
