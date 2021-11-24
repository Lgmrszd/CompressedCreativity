package com.lgmrszd.compressedcreativity.event;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import com.lgmrszd.compressedcreativity.network.ObservePacket;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GameEvents {
    @SubscribeEvent
    public static void clientTickEvent(TickEvent.ClientTickEvent evt) {
        if(evt.phase == TickEvent.Phase.START)
            return;
        ObservePacket.tick();
    }
}
