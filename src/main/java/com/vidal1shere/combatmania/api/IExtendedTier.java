package com.vidal1shere.combatmania.api;

import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.data.component.SmashProperties;
import com.vidal1shere.combatmania.item.tool.ToolProperties;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public interface IExtendedTier extends Tier {
    CustomProperties getCustomProperties();
    ToolProperties getToolProperties();
    
    default float getAttackStagger() {
        return getToolProperties().getAttackStagger();
    }

    default int getUses() {
        return getToolProperties().getUses();
    }

    default float getSpeed() {
        return getToolProperties().getSpeed();
    }

    default float getAttackDamageBonus() {
        return getToolProperties().getAttackDamageBonus();
    }

    default TagKey<Block> getIncorrectBlocksForDrops() {
        return getToolProperties().getIncorrectBlocksForDrops();
    }

    default int getEnchantmentValue() {
        return getToolProperties().getEnchantmentValue();
    }

    default Ingredient getRepairIngredient() {
        return getToolProperties().getRepairIngredient();
    }
    
    default JoustProperties getJoustProperties() {
        return getCustomProperties().get(JoustProperties.KEY);
    }

    default SmashProperties getSmashProperties() {
        return getCustomProperties().get(SmashProperties.KEY);
    }
}
