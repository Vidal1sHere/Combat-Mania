package com.vidal1shere.combatmania.registry.data;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.client.animation.state.ItemAnimationStates;
import com.vidal1shere.combatmania.attachment.StrikeActiveData;
import com.vidal1shere.combatmania.attachment.StrikeCooldownData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class CMAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CombatMania.MOD_ID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<StrikeCooldownData>> STRIKE_COOLDOWN =
            ATTACHMENT_TYPES.register("strike_cooldown",
                    () -> AttachmentType.builder(StrikeCooldownData::new).serialize(StrikeCooldownData.CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<StrikeActiveData>> STRIKE_ACTIVE =
            ATTACHMENT_TYPES.register("strike_active",
                    () -> AttachmentType.builder(StrikeActiveData::new).serialize(StrikeActiveData.CODEC).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<ItemAnimationStates>> ITEM_ANIMATIONS =
            ATTACHMENT_TYPES.register("item_animations",
                    () -> AttachmentType.builder(ItemAnimationStates::new).serialize(ItemAnimationStates.CODEC).build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}