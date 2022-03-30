package com.lgmrszd.compressedcreativity.network;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import net.minecraft.server.level.ServerPlayer;

public interface IObserveTileEntity {
    void onObserved(ServerPlayer var1, ObservePacket var2);
}
