package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.old_animation.data.AnimationTrack;
import com.vidal1shere.combatmania.registry.strike.CMStrikeStyles;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon, IAnimatedItem {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public DeferredHolder<StrikeStyle, ? extends StrikeStyle> getPrimaryStyle() {
        return CMStrikeStyles.SWEEP;
    }

    @Override
    public @Nullable DeferredHolder<StrikeStyle, ? extends StrikeStyle> getSecondaryStyle() {
        return null;
    }

    @Override
    public AnimationTrack getAnimation(LocalPlayer player, HumanoidArm arm, ItemStack stack) {
        return null;
    }
}
