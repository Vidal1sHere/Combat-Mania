package com.vidal1shere.combatmania.item.tier;

import com.google.common.base.Suppliers;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.Objects;
import java.util.function.Supplier;

public class ToolProperties {

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int uses;
    private final float speed;
    private final float damage;
    private final float stagger;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    public ToolProperties(Tier tier) {
        this.incorrectBlocksForDrops = tier.getIncorrectBlocksForDrops();
        this.uses = tier.getUses();
        this.speed = tier.getSpeed();
        this.damage = tier.getAttackDamageBonus();
        this.stagger = tier.getAttackDamageBonus() > 0 ? tier.getAttackDamageBonus() / 2 : 0.0F;
        this.enchantmentValue = tier.getEnchantmentValue();
        Objects.requireNonNull(tier.getRepairIngredient());
        this.repairIngredient = Suppliers.memoize(tier::getRepairIngredient);
    }

    public ToolProperties(TagKey<Block> incorrectBlockForDrops, int uses, 
                          float speed, float damage, float stagger, int enchantmentValue,
                          Supplier<Ingredient> repairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlockForDrops;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.stagger = stagger;
        this.enchantmentValue = enchantmentValue;
        Objects.requireNonNull(repairIngredient);
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }
    
    public float getAttackStagger() {
        return this.stagger;
    }

    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
