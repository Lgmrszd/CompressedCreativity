package com.lgmrszd.compressedcreativity.network;

/*
    Original code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import net.minecraft.server.level.ServerPlayer;

public interface IObserveBlockEntity {
    default void onObserved(ServerPlayer var1, ObservePacket var2) {}
}
