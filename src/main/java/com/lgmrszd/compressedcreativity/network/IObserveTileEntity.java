package com.lgmrszd.compressedcreativity.network;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import net.minecraft.entity.player.ServerPlayerEntity;

public interface IObserveTileEntity {
    void onObserved(ServerPlayerEntity var1, ObservePacket var2);
}
