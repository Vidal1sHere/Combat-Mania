package com.vidal1shere.combatmania.item;

import com.vidal1shere.combatmania.api.IAnimatedItem;
import com.vidal1shere.combatmania.api.IStrikeWeapon;
import com.vidal1shere.combatmania.client.animation.data.AnimationTrack;
import com.vidal1shere.combatmania.strike.StrikeStyle;
import com.vidal1shere.combatmania.strike_styles.SweepStrikeStyle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;

public class StrikeSwordItem extends SwordItem implements IStrikeWeapon, IAnimatedItem {
    public StrikeSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public StrikeStyle getPrimaryStyle() {
        return new SweepStrikeStyle();
    }

    @Override
    public @Nullable StrikeStyle getSecondaryStyle() {
        return null;
    }

    @Override
    public AnimationTrack getAnimation(LocalPlayer player, HumanoidArm arm, ItemStack stack) {
        return null;
    }
}
