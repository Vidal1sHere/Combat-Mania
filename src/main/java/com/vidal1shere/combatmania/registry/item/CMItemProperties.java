package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.data.component.QuiverContents;
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
        
        makeQuiverItem(CMItems.LEATHER_QUIVER.get());
    }
    
    private static void makeQuiverItem(Item item) {
        ItemProperties.register(item, CMUtil.modResLoc("filled"),
                (stack, level, entity, seed) -> {
                    QuiverContents contents = stack.getOrDefault(
                            CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY
                    );

                    int count = contents.count();
                    int max = QuiverContents.MAX_ARROWS;
                    
                    if (count >= max) {
                        return 3.0F;
                    } else if (count >= max / 2) {
                        return 2.0F;
                    } else if (count > 0) {
                        return 1.0F;
                    } else {
                        return 0.0F;
                    }
                }
        );
    }
    
    private static void makeMaceItem(Item item) {
        ItemProperties.register(item, CMUtil.modResLoc("mace_3d"),
                (stack, level, entity, seed) ->
                        CMClientConfig.THREE_DIMENSIONAL_MACES.get() ? 1 : 0
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
