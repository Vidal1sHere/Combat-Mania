package com.vidal1shere.combatmania.setup;

import com.vidal1shere.combatmania.CombatMania;
import com.vidal1shere.combatmania.network.QuiverArrowCyclePayload;
import com.vidal1shere.combatmania.network.ShockwaveParticlePayload;
import com.vidal1shere.combatmania.network.StrikePayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = CombatMania.MOD_ID)
public class CMPayloadSetup {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                StrikePayload.Request.TYPE,
                StrikePayload.Request.STREAM_CODEC,
                StrikePayload.Request::handle
        );

        registrar.playToClient(
                StrikePayload.Confirm.TYPE,
                StrikePayload.Confirm.STREAM_CODEC,
                StrikePayload.Confirm::handle
        );
        
        registrar.playToServer(
                QuiverArrowCyclePayload.Request.TYPE, 
                QuiverArrowCyclePayload.Request.STREAM_CODEC, 
                QuiverArrowCyclePayload.Request::handle
        );

        registrar.playToClient(
                QuiverArrowCyclePayload.Confirm.TYPE,
                QuiverArrowCyclePayload.Confirm.STREAM_CODEC,
                QuiverArrowCyclePayload.Confirm::handle
        );

        registrar.playToClient(
                ShockwaveParticlePayload.TYPE,
                ShockwaveParticlePayload.STREAM_CODEC,
                ShockwaveParticlePayload::handle
        );
    }
}
