package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.data.attachment.JoustData;
import com.vidal1shere.combatmania.data.attachment.StaggerData;
import com.vidal1shere.combatmania.data.attachment.StrikeData;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationStates;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CMAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CombatMania.MOD_ID);
    
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<StrikeData>> STRIKE =
            ATTACHMENT_TYPES.register("strike",
                    () -> AttachmentType.builder(StrikeData::new)
                            .serialize(StrikeData.CODEC).sync(StrikeData.STREAM_CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemAnimationStates>> ITEM_ANIMATIONS =
            ATTACHMENT_TYPES.register("item_animations",
                    () -> AttachmentType.builder(ItemAnimationStates::new).serialize(ItemAnimationStates.CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<StaggerData>> STAGGER =
            ATTACHMENT_TYPES.register("stagger",
                    () -> AttachmentType.builder(StaggerData::new).serialize(StaggerData.CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<JoustData>> JOUST =
            ATTACHMENT_TYPES.register("joust",
                    () -> AttachmentType.builder(() -> new JoustData())
                            .sync(JoustData.STREAM_CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> BACKSTAB =
            ATTACHMENT_TYPES.register("backstab",
                    () -> AttachmentType.builder(() -> false)
                            .sync(ByteBufCodecs.BOOL).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}