package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.item.tool.ToolProperties;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import static com.vidal1shere.combatmania.util.CustomProperties.*;

public enum CMExtendedToolTiers implements IExtendedTier {
    WOOD(Tiers.WOOD, JoustProperties.of(0.7F, 14.0F, 0.666667F, 0.600000F, 0.600000F, 0.700000F)),
    STONE(Tiers.STONE, JoustProperties.of(0.82F, 13.0F, 0.714286F, 0.666667F, 0.666667F, 0.736842F)),
    IRON(Tiers.IRON, JoustProperties.of(0.95F, 11.0F, 0.833333F, 1.200000F, 0.705882F, 0.777778F)),
    DIAMOND(Tiers.DIAMOND, JoustProperties.of(1.075F, 10.0F, 1.000000F, 1.000000F, 1.000000F, 1.000000F)),
    GOLD(Tiers.GOLD, JoustProperties.of(0.7F, 13.0F, 0.714286F, 0.857143F, 0.600000F, 0.666667F)),
    NETHERITE(Tiers.NETHERITE, JoustProperties.of(1.2F, 9.0F, 1.250000F, 1.200000F, 1.000000F, 1.076923F));

    private final ToolProperties toolProperties;
    private final CustomProperties customProperties;
    
    CMExtendedToolTiers(Tier tier, Set<?>... customProperties) {
        this.toolProperties = new ToolProperties(tier);
        this.customProperties = CustomProperties.ofImmutable(customProperties);
    }

    CMExtendedToolTiers(ToolProperties toolProperties, Set<?>... customProperties) {
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
    
    public Tier getVanillaTier() {
        return switch (this) {
            case WOOD -> Tiers.WOOD;
            case STONE -> Tiers.STONE;
            case IRON -> Tiers.IRON;
            case GOLD -> Tiers.GOLD;
            case DIAMOND -> Tiers.DIAMOND;
            case NETHERITE -> Tiers.NETHERITE;
        };
    }
}
