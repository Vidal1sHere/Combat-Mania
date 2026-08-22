package com.vidal1shere.combatmania.setup;

import com.ibm.icu.impl.Utility;
import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.api.extension.item.ClientHammerExtension;
import com.vidal1shere.combatmania.api.extension.item.ClientMaceExtensions;
import com.vidal1shere.combatmania.client.hud.overlay.StrikeIndicatorOverlay;
import com.vidal1shere.combatmania.client.model.TieredHammerHeadModel;
import com.vidal1shere.combatmania.client.model.TieredMaceHeadModel;
import com.vidal1shere.combatmania.client.model.VanillaMaceHeadModel;
import com.vidal1shere.combatmania.client.renderer.item.VanillaMaceRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.FlamingArrowRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownJavelinRenderer;
import com.vidal1shere.combatmania.client.renderer.projectile.ThrownTrident2dRenderer;
import com.vidal1shere.combatmania.config.CMClientConfig;
import com.vidal1shere.combatmania.constants.HammerTransforms;
import com.vidal1shere.combatmania.constants.MaceTransforms;
import com.vidal1shere.combatmania.data.component.QuiverContents;
import com.vidal1shere.combatmania.item.armor.QuiverItem;
import com.vidal1shere.combatmania.item.weapon.HammerItem;
import com.vidal1shere.combatmania.item.weapon.StrikeMaceItem;
import com.vidal1shere.combatmania.particle.CustomParticle;
import com.vidal1shere.combatmania.registry.data.CMAttachments;
import com.vidal1shere.combatmania.registry.data.CMDataComponents;
import com.vidal1shere.combatmania.registry.entity.CMEntities;
import com.vidal1shere.combatmania.registry.item.CMItemProperties;
import com.vidal1shere.combatmania.registry.item.CMItems;
import com.vidal1shere.combatmania.registry.particle.CMParticles;
import com.vidal1shere.combatmania.util.CMUtil;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = CombatMania.MOD_ID, value = Dist.CLIENT)
public class CMClientSetup {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CMItemProperties.addCustomItemsProperties();
        });

    }

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new ClientMaceExtensions.Tiered(), CMItems.getItems()
                .stream()
                .filter(item -> item.get() instanceof StrikeMaceItem)
                .map(holder -> (Item) holder.get())
                .toArray(Item[]::new));
        
        event.registerItem(new ClientMaceExtensions.Vanilla(), Items.MACE);

        event.registerItem(new ClientHammerExtension(), CMItems.getItems()
                .stream()
                .filter(item -> item.get() instanceof HammerItem)
                .map(holder -> (Item) holder.get())
                .toArray(Item[]::new));
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TieredMaceHeadModel.LAYER_LOCATION, TieredMaceHeadModel::createBodyLayer);
        event.registerLayerDefinition(VanillaMaceHeadModel.LAYER_LOCATION, VanillaMaceHeadModel::createBodyLayer);
        event.registerLayerDefinition(TieredHammerHeadModel.LAYER_LOCATION, TieredHammerHeadModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (MaceTransforms tier : MaceTransforms.values()) {
            event.register(ModelResourceLocation.standalone(tier.getFullModel()));
            event.register(ModelResourceLocation.standalone(tier.getHandleModel()));
        }

        for (HammerTransforms tier : HammerTransforms.values()) {
            event.register(ModelResourceLocation.standalone(tier.getFullModel()));
            event.register(ModelResourceLocation.standalone(tier.getHandleModel()));
        }

        event.register(ModelResourceLocation.standalone(VanillaMaceRenderer.MACE_MODELS.getFullModel()));
        event.register(ModelResourceLocation.standalone(VanillaMaceRenderer.MACE_MODELS.getHandleModel()));
    }
    
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CMEntities.THROWN_JAVELIN.get(), ThrownJavelinRenderer::new);
        event.registerEntityRenderer(CMEntities.FLAMING_ARROW.get(), FlamingArrowRenderer::new);

        if (CMClientConfig.TWO_DIMENSIONAL_TRIDENT.get()) {
            event.registerEntityRenderer(EntityType.TRIDENT, ThrownTrident2dRenderer::new);
        }
    }
    
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(CMParticles.LEFT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.LEFT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.RIGHT_SHORT_SWEEP_ATTACK.get(), CustomParticle.Provider::new);
        event.registerSpriteSet(CMParticles.SLASH_ATTACK.get(), CustomParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, index) -> {
            if (!(stack.getItem() instanceof QuiverItem) || index < 1 || index > 3) return 0xFFFFFFFF;

            QuiverContents contents = stack.getOrDefault(
                    CMDataComponents.QUIVER_CONTENTS, QuiverContents.EMPTY
            );

            int count = contents.count();
            int max = QuiverContents.MAX_ARROWS;

            boolean show = switch (index) {
                case 1 -> count > 0 && count < max / 2;
                case 2 -> count >= max / 2 && count < max;
                case 3 -> count >= max;
                default -> false;
            };

            return show ? 0xFFFFFFFF : 0x00FFFFFF;
        }, CMItems.LEATHER_QUIVER.get());
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.CROSSHAIR,
                CMUtil.modResLoc("item_gauge_overlay"),
                new StrikeIndicatorOverlay()
        );
    }

    @SubscribeEvent
    public static void stopStates(PlayerEvent.PlayerLoggedOutEvent event) {
        event.getEntity().getData(CMAttachments.ITEM_ANIMATIONS).stopAll();
    }
}
