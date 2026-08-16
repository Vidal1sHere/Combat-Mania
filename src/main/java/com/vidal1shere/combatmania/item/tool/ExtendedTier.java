package com.vidal1shere.combatmania.item.tool;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class ExtendedTier implements IExtendedTier {
    private final ToolProperties toolProperties;
    private final CustomProperties customProperties;

    public ExtendedTier(ToolProperties toolProperties, CustomProperties.PropertySet<?>... customProperties) {
        this.toolProperties = toolProperties;
        this.customProperties = CustomProperties.ofImmutable(customProperties);
    }

    public int getUses() {
        return this.toolProperties.getUses();
    }

    public float getSpeed() {
        return this.toolProperties.getSpeed();
    }

    public float getAttackDamageBonus() {
        return this.toolProperties.getAttackDamageBonus();
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.toolProperties.getIncorrectBlocksForDrops();
    }

    public int getEnchantmentValue() {
        return this.toolProperties.getEnchantmentValue();
    }

    public Ingredient getRepairIngredient() {
        return this.toolProperties.getRepairIngredient();
    }

    @Override
    public CustomProperties getCustomProperties() {
        return customProperties;
    }
}
