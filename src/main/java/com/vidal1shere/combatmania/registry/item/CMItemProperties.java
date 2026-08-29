package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class CMItemProperties {
    public static void addCustomItemsProperties() {
//        makeCustomBow(ModItems.RHODONITE_BOW.get());
//        makeCustomBow(ModItems.BONE_BOW.get());
        
        ItemProperties.register(Items.TRIDENT, CMUtil.modResLoc("trident_2d"),
                (stack, level, entity, seed) -> 
                    CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get() ? 1 : 0
                );
        
        makeThrowingItem(CMItems.WOODEN_JAVELIN.get());
        makeThrowingItem(CMItems.STONE_JAVELIN.get());
        makeThrowingItem(CMItems.IRON_JAVELIN.get());
        makeThrowingItem(CMItems.GOLDEN_JAVELIN.get());
        makeThrowingItem(CMItems.DIAMOND_JAVELIN.get());
        
        makeMaceItem(CMItems.WOODEN_MACE.get());
        makeMaceItem(CMItems.STONE_MACE.get());
        makeMaceItem(CMItems.GOLDEN_MACE.get());
        makeMaceItem(CMItems.IRON_MACE.get());
        makeMaceItem(CMItems.DIAMOND_MACE.get());
        makeMaceItem(Items.MACE);
        
        makeHammerItem(CMItems.WOODEN_HAMMER.get());
        makeHammerItem(CMItems.STONE_HAMMER.get());
        makeHammerItem(CMItems.IRON_HAMMER.get());
        makeHammerItem(CMItems.GOLDEN_HAMMER.get());
        makeHammerItem(CMItems.DIAMOND_HAMMER.get());
    }
    
    private static void makeMaceItem(Item item) {
        ItemProperties.register(item, CMUtil.modResLoc("mace_3d"),
                (stack, level, entity, seed) ->
                        CMClientConfig.THREE_DIMENSIONAL_MACES.get() ? 1 : 0
        );
    }

    private static void makeHammerItem(Item item) {
        ItemProperties.register(item, CMUtil.modResLoc("hammer_3d"),
                (stack, level, entity, seed) ->
                        CMClientConfig.THREE_DIMENSIONAL_HAMMERS.get() ? 1 : 0
        );
    }
    
    private static void makeThrowingItem(Item item) {
        ItemProperties.register(item, CMUtil.mcResLoc("throwing"),
                (stack, level, entity, seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
                );
    }

    private static <E extends Enum<E>> void makeEnumBasedItem(Item item, String name, Function<ItemStack, E> getter) {
        ItemProperties.register(item,
                CMUtil.modResLoc(name),
                (stack, level, entity, seed) -> {
                    E value = getter.apply(stack);
                    return value != null ? value.ordinal() + 1 : 0;
                });
    }
    
    private static void makeCustomBow(Item item) {
        ItemProperties.register(item, CMUtil.mcResLoc("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(
                item,
                CMUtil.mcResLoc("pulling"),
                (stack, level, entity, seed) -> 
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );
    }
}
