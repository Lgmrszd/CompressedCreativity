package com.lgmrszd.compressedcreativity.event;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import com.lgmrszd.compressedcreativity.network.ObservePacket;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class GameEvents {
    @SubscribeEvent
    public static void clientTickEvent(ClientTickEvent.Pre evt) {
        ObservePacket.tick();
    }
}
