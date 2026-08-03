package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.CMUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class CMTags {
    
    public static class Blocks {
        
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(CMUtil.modResLoc(name));
        }
    }
    
    public static class Items {
        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(CMUtil.modResLoc(name));
        }
    }
    
    public static class EntityTypes {
        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(
                    Registries.ENTITY_TYPE,
                    CMUtil.modResLoc(name)
            );
        }
    }
    
    public static class DamageTypes {
        private static TagKey<DamageType> createTag(String name) {
            return TagKey.create(
                    Registries.DAMAGE_TYPE,
                    CMUtil.modResLoc(name)
            );
        }
    }
}
