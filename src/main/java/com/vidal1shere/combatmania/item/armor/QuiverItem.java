package com.vidal1shere.combatmania.item.armor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.event.game.BowChargeEvent;
import com.vidal1shere.combatmania.event.game.ProjectileCreateEvent;
import com.vidal1shere.combatmania.event.game.ProjectileShootEvent;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.sound.CMSounds;
import com.vidal1shere.combatmania.util.Color;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class QuiverItem extends MultiSlotArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public QuiverItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties.stacksTo(1).component(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY));
    }

    public QuiverItem(Holder<ArmorMaterial> material, Properties properties) {
        super(material, Type.CHESTPLATE, properties.stacksTo(1).component(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY));
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract GeoArmorRenderer<?> supplyRenderer();
    
    public void onArrowLoose(ArrowLooseEvent event) {}

    public void onArrowNock(ArrowNockEvent event) {}
    
    public void onArrowCreate(ProjectileCreateEvent event) {}
    
    public void onArrowShoot(ProjectileShootEvent event) {}

    public void onBowCharge(BowChargeEvent event) {}

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return CMSounds.wrapAsHolder(CMSounds.QUIVER_EQUIP);
    }
    
    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        if (stack.getCount() != 1 || action != ClickAction.SECONDARY) return false;

        QuiverContents contents = stack.get(CMDataComponents.QUIVER_CONTENTS);
        if (contents == null) return false;

        ItemStack slotItem = slot.getItem();
        QuiverContents.Mutable mutable = new QuiverContents.Mutable(contents);

        if (slotItem.isEmpty()) {
            ItemStack removed = mutable.remove(0);
            if (removed != null) {
                playRemoveOneSound(player);
                ItemStack leftover = slot.safeInsert(removed);
                mutable.tryInsert(leftover);
            }
        } else {
            int inserted = mutable.tryTransfer(slot, player);
            if (inserted > 0) {
                playInsertSound(player);
            }
        }

        stack.set(CMDataComponents.QUIVER_CONTENTS, mutable.toImmutable());
        return true;
    }
    
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot,
                                            ClickAction action, Player player, SlotAccess access) {
        if (stack.getCount() != 1) 
            return false;
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) 
            return false;

        QuiverContents contents = stack.get(CMDataComponents.QUIVER_CONTENTS);
        if (contents == null) 
            return false;

        QuiverContents.Mutable mutable = new QuiverContents.Mutable(contents);

        if (other.isEmpty()) {
            ItemStack removed = mutable.removeOne();
            if (removed != null) {
                playRemoveOneSound(player);
                access.set(removed);
            }
        } else {
            int inserted = mutable.tryInsert(other);
            if (inserted > 0) {
                playInsertSound(player);
            }
        }

        stack.set(CMDataComponents.QUIVER_CONTENTS, mutable.toImmutable());
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (dropContents(stack, player)) {
            playDropContentsSound(player);
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
        }
        return InteractionResultHolder.fail(stack);
    }

    private static boolean dropContents(ItemStack stack, Player player) {
        QuiverContents contents = stack.get(CMDataComponents.QUIVER_CONTENTS);
        if (contents == null || contents.isEmpty()) return false;

        stack.set(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
        if (player instanceof ServerPlayer) {
            contents.itemsCopy().forEach(arrow -> player.drop(arrow, true));
        }
        return true;
    }
    
    @Override
    public boolean isBarVisible(ItemStack stack) {
        QuiverContents contents = stack.getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
        return contents.count() > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        QuiverContents contents = stack.getOrDefault(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
        return Math.min(1 + Mth.floor(contents.count() * 12.0F / QuiverContents.MAX_ARROWS), 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return new Color(0.4F, 0.4F, 1.0F, 1.0F).color;
    }

    public static void modifyTooltip(ItemTooltipEvent event) {
        if (!(event.getItemStack().getItem() instanceof QuiverItem)) return;

        if (event.getToolTip().size() > 1) {
            event.getToolTip()
                    .subList(1, event.getToolTip().size())
                    .clear();
        }
    }
    
    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        QuiverContents contents = itemEntity.getItem().get(CMDataComponents.QUIVER_CONTENTS);
        if (contents != null) {
            itemEntity.getItem().set(CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY);
            net.minecraft.world.item.ItemUtils.onContainerDestroyed(itemEntity, contents.itemsCopy());
        }
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT,
                0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE,
                0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS,
                0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }
    
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T livingEntity, ItemStack itemStack,
                    @Nullable EquipmentSlot equipmentSlot,
                    @Nullable HumanoidModel<T> original) {
                if (this.renderer == null) this.renderer = supplyRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public record ChargeData(boolean charged, long chargeStartTick, String selectedArrow) {

        public static final ChargeData EMPTY = new ChargeData(false, -1L, "");

        public static final Codec<ChargeData> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.BOOL.fieldOf("charged").forGetter(ChargeData::charged),
                        Codec.LONG.fieldOf("charge_start_tick").forGetter(ChargeData::chargeStartTick),
                        Codec.STRING.fieldOf("selected_arrow").forGetter(ChargeData::selectedArrow)
                ).apply(instance, ChargeData::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ChargeData> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.BOOL, ChargeData::charged,
                        ByteBufCodecs.VAR_LONG, ChargeData::chargeStartTick,
                        ByteBufCodecs.STRING_UTF8, ChargeData::selectedArrow,
                        ChargeData::new
                );

        public boolean isCharging() {
            return chargeStartTick >= 0;
        }

        public ChargeData withCharged(boolean charged) {
            return new ChargeData(charged, chargeStartTick, selectedArrow);
        }

        public ChargeData startCharging(long tick, String arrow) {
            return new ChargeData(false, tick, arrow);
        }

        public ChargeData reset() {
            return EMPTY;
        }
    }
}