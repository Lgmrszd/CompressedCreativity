package com.lgmrszd.compressedcreativity.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class CCNetwork {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        
        registrar.playToServer(
            ObservePacket.TYPE,
            ObservePacket.STREAM_CODEC,
            ObservePacket::handle
        );
        
        registrar.playToClient(
            ForceUpdatePacket.TYPE,
            ForceUpdatePacket.STREAM_CODEC,
            ForceUpdatePacket::handle
        );
    }
}
