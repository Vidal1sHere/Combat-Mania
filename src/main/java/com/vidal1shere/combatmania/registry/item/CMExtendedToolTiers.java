package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.api.IExtendedTier;
import com.vidal1shere.combatmania.data.component.JoustProperties;
import com.vidal1shere.combatmania.data.component.SmashProperties;
import com.vidal1shere.combatmania.item.tool.ToolProperties;
import com.vidal1shere.combatmania.util.CustomProperties;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import static com.vidal1shere.combatmania.util.CustomProperties.*;

public enum CMExtendedToolTiers implements IExtendedTier {
    WOOD(Tiers.WOOD,
            JoustProperties.of(0.7F, 20.0F, 14.0F, 0.666667F, 0.600000F, 0.600000F, 0.700000F),
            SmashProperties.of(4.0F, 10.0F, 1.5F, 2.0F, 5.0F, 40.0F, 2.0F, 0.2F)),

    STONE(Tiers.STONE,
            JoustProperties.of(0.82F, 25.0F, 13.0F, 0.714286F, 0.666667F, 0.666667F, 0.736842F),
            SmashProperties.of(3.5F, 9.0F, 2.0F, 2.0F, 4.0F, 50.0F, 2.5F, 0.3F)),

    IRON(Tiers.IRON,
            JoustProperties.of(0.95F, 30.0F, 11.0F, 0.833333F, 1.200000F, 0.705882F, 0.777778F),
            SmashProperties.of(3.0F, 8.0F, 2.5F, 2.0F, 3.0F, 60.0F, 3.0F, 0.4F)),

    DIAMOND(Tiers.DIAMOND,
            JoustProperties.of(1.075F, 35.0F, 10.0F, 1.000000F, 1.000000F, 1.000000F, 1.000000F),
            SmashProperties.of(2.5F, 7.0F, 3.5F, 2.0F, 2.0F, 70.0F, 3.5F, 0.5F)),

    GOLD(Tiers.GOLD,
            JoustProperties.of(0.7F, 25.0F, 13.0F, 0.714286F, 0.857143F, 0.600000F, 0.666667F),
            SmashProperties.of(4.0F, 10.0F, 1.5F, 2.0F, 5.0F, 80.0F, 3.5F, 0.7F)),

    NETHERITE(Tiers.NETHERITE,
            JoustProperties.of(1.2F, 40.0F, 9.0F, 1.250000F, 1.200000F, 1.000000F, 1.076923F),
            SmashProperties.of(1.5F, 5.0F, 5.0F, 2.0F, 1.0F, 80.0F, 4.0F, 0.8F));

    private final ToolProperties toolProperties;
    private final CustomProperties customProperties;
    
    CMExtendedToolTiers(Tier tier, PropertySet<?>... customProperties) {
        this.toolProperties = new ToolProperties(tier);
        this.customProperties = CustomProperties.ofImmutable(customProperties);
    }

    CMExtendedToolTiers(ToolProperties toolProperties, PropertySet<?>... customProperties) {
        this.toolProperties = toolProperties;
        this.customProperties = CustomProperties.ofImmutable(customProperties);
    }

    @Override
    public CustomProperties getCustomProperties() {
        return customProperties;
    }

    @Override
    public ToolProperties getToolProperties() {
        return toolProperties;
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
    
    public static IExtendedTier getVanillaTier(Tier tier) {
        return switch (tier) {
            case Tiers.WOOD -> WOOD;
            case Tiers.STONE -> STONE;
            case Tiers.IRON -> IRON;
            case Tiers.GOLD -> GOLD;
            case Tiers.DIAMOND -> DIAMOND;
            case Tiers.NETHERITE -> NETHERITE;
            default -> null;
        };
    }
}
