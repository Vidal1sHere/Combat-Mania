package com.vidal1shere.combatmania.registry.item;

import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.util.CMUtil;
import com.vidal1shere.strikeapi.client.StrikeItemProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

import java.util.function.Function;

public class CMItemProperties {
    public static void addCustomItemsProperties() {
//        makeCustomBow(ModItems.RHODONITE_BOW.get());
//        makeCustomBow(ModItems.BONE_BOW.get());
        
        ItemProperties.register(Items.TRIDENT, CMUtil.modResLoc("trident_2d"),
                (stack, level, entity, seed) -> 
                    CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get() ? 1 : 0
                );
        
        
        makeMaceItem(CMItems.WOODEN_MACE.get());
        makeMaceItem(CMItems.STONE_MACE.get());
        makeMaceItem(CMItems.GOLDEN_MACE.get());
        makeMaceItem(CMItems.IRON_MACE.get());
        makeMaceItem(CMItems.DIAMOND_MACE.get());
        makeMaceItem(CMItems.NETHERITE_MACE.get());
        makeMaceItem(Items.MACE);
        
        makeHammerItem(CMItems.WOODEN_HAMMER.get());
        makeHammerItem(CMItems.STONE_HAMMER.get());
        makeHammerItem(CMItems.IRON_HAMMER.get());
        makeHammerItem(CMItems.GOLDEN_HAMMER.get());
        makeHammerItem(CMItems.DIAMOND_HAMMER.get());
        makeHammerItem(CMItems.NETHERITE_HAMMER.get());
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
    
    private static void makeFirstPersonItem(Item item) {
        ItemProperties.register(item, CMUtil.modResLoc("first_person"),
                (stack, level, entity, seed) -> 
                    Minecraft.getInstance().options.getCameraType().isFirstPerson() ? 1 : 0
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

    private static void makeStrikeBow(Item item) {
        ItemProperties.register(item, CMUtil.mcResLoc("pull"), (stack, level, entity, seed) -> Math.max(0.0F, StrikeItemProperties.charge(stack, entity)));
        ItemProperties.register(item, CMUtil.mcResLoc("pulling"), (stack, level, entity, seed) -> StrikeItemProperties.charge(stack, entity) >= 0.0F ? 1.0F : 0.0F);
    }

    private static void makeStrikeCrossbow(Item item) {
        makeStrikeBow(item);
        ItemProperties.register(item, CMUtil.mcResLoc("charged"), (stack, level, entity, seed) -> loaded(stack).isEmpty() ? 0.0F : 1.0F);
        ItemProperties.register(item, CMUtil.mcResLoc("firework"), (stack, level, entity, seed) -> loaded(stack).contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
    }

    private static ChargedProjectiles loaded(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
    }
}
