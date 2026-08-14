package com.vidal1shere.combatmania.entity;

import javax.annotation.Nullable;

import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ThrownJavelin extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Float> DATA_DAMAGE =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> DATA_LOYALTY =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_FOIL =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_TIER =
            SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.STRING);

    private static final float DEFAULT_DAMAGE = 8.0F;

    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ThrownJavelin(EntityType<? extends ThrownJavelin> entityType, Level level) {
        super(entityType, level);
    }

    protected ThrownJavelin(EntityType<? extends ThrownJavelin> entityType, Level level, LivingEntity shooter, ItemStack pickupItemStack, Tier tier) {
        super(entityType, shooter, level, pickupItemStack, null);
        this.entityData.set(DATA_TIER, CMUtil.tierName(tier));
        this.entityData.set(DATA_DAMAGE, this.getDamageFromShooter(shooter));
        this.entityData.set(DATA_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
        this.entityData.set(DATA_FOIL, pickupItemStack.hasFoil());
    }

    protected ThrownJavelin(EntityType<? extends ThrownJavelin> entityType, Level level, double x, double y, double z, ItemStack pickupItemStack, Tier tier) {
        super(entityType, x, y, z, level, pickupItemStack, pickupItemStack);
        this.entityData.set(DATA_TIER, CMUtil.tierName(tier));
        this.entityData.set(DATA_DAMAGE, this.getDamageFromShooter(null));
        this.entityData.set(DATA_LOYALTY, this.getLoyaltyFromItem(pickupItemStack));
        this.entityData.set(DATA_FOIL, pickupItemStack.hasFoil());
    }

    public ThrownJavelin(Level level, LivingEntity shooter, ItemStack pickupItemStack) {
        this(CMEntities.THROWN_JAVELIN.get(), level, shooter, pickupItemStack,
                pickupItemStack.getItem() instanceof TieredItem tiered ? tiered.getTier() : Tiers.IRON);
    }

    public ThrownJavelin(Level level, double x, double y, double z, ItemStack pickupItemStack) {
        this(CMEntities.THROWN_JAVELIN.get(), level, x, y, z, pickupItemStack,
                pickupItemStack.getItem() instanceof TieredItem tiered ? tiered.getTier() : Tiers.IRON);
    }

    public Tier getTier() {
        return CMUtil.tierFromName(this.entityData.get(DATA_TIER));
    }

    public ResourceLocation getModelResource() {
        return CMUtil.modResLoc("geo/projectile/" + (CMUtil.isWoodTier(getTier()) ? "wooden_javelin" : "javelin") + ".geo.json");
    }

    public ResourceLocation getTextureResource() {
        return CMUtil.modResLoc("textures/item/" + this.entityData.get(DATA_TIER) + "_javelin_in_hand.png");
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_DAMAGE, DEFAULT_DAMAGE);
        builder.define(DATA_LOYALTY, (byte) 0);
        builder.define(DATA_FOIL, false);
        builder.define(DATA_TIER, "iron");
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity owner = this.getOwner();
        int loyalty = this.entityData.get(DATA_LOYALTY);

        if (loyalty > 0 && (this.dealtDamage || this.isNoPhysics()) && owner != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide() && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }
                this.discard();
            } else {
                this.setNoPhysics(true);

                Vec3 returnDirection = owner.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + returnDirection.y * 0.015 * loyalty, this.getZ());

                if (this.level().isClientSide()) {
                    this.yOld = this.getY();
                }

                double returnSpeed = 0.05 * loyalty;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(returnDirection.normalize().scale(returnSpeed)));

                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    public boolean isFoil() {
        return this.entityData.get(DATA_FOIL);
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity hitEntity = result.getEntity();
        Entity shooter = this.getOwner();
        float damage = this.entityData.get(DATA_DAMAGE);
        DamageSource damageSource = this.damageSources().trident(this, shooter == null ? this : shooter);
        Level level = this.level();

        if (level instanceof ServerLevel serverlevel) {
            damage = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), hitEntity, damageSource, damage);
        }

        this.dealtDamage = true;

        if (hitEntity.hurt(damageSource, damage)) {
            if (hitEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            level = this.level();
            if (level instanceof ServerLevel serverLevel) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, hitEntity, damageSource, this.getWeaponItem());
            }

            if (hitEntity instanceof LivingEntity livingentity) {
                this.doKnockback(livingentity, damageSource);
                this.doPostHurtEffects(livingentity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    protected void hitBlockEnchantmentEffects(ServerLevel level, BlockHitResult hitResult, ItemStack stack) {
        Vec3 hitLocation = hitResult.getBlockPos().clampLocationWithin(hitResult.getLocation());
        Entity owner = this.getOwner();
        LivingEntity livingEntity = owner instanceof LivingEntity entity ? entity : null;

        EnchantmentHelper.onHitBlock(level, stack, livingEntity,
                this, null, hitLocation,
                level.getBlockState(hitResult.getBlockPos()), callback -> this.kill());
    }

    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    @Override
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.TRIDENT);
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player entity) {
        if (this.ownedBy(entity) || this.getOwner() == null) {
            super.playerTouch(entity);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.entityData.set(DATA_DAMAGE, compound.contains("Damage", CompoundTag.TAG_FLOAT) ? compound.getFloat("Damage") : DEFAULT_DAMAGE);
        this.entityData.set(DATA_LOYALTY, this.getLoyaltyFromItem(this.getPickupItemStackOrigin()));
        deserializeTier(compound);
    }

    protected void deserializeTier(CompoundTag tag) {
        this.entityData.set(DATA_TIER, tag.getString("Tier"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Damage", this.entityData.get(DATA_DAMAGE));
        compound.putBoolean("DealtDamage", this.dealtDamage);
        serializeTier(compound);
    }

    protected void serializeTier(CompoundTag tag) {
        tag.putString("Tier", this.entityData.get(DATA_TIER));
    }

    private float getDamageFromShooter(@Nullable LivingEntity entity) {
        if (entity == null) {
            return DEFAULT_DAMAGE;
        }
        return (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        Level level = this.level();
        if (level instanceof ServerLevel serverlevel) {
            return (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverlevel, stack, this), 0, 127);
        }
        return 0;
    }

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(DATA_LOYALTY);
        if (this.pickup != Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}