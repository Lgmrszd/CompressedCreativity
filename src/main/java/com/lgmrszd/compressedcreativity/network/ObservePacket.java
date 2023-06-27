package com.lgmrszd.compressedcreativity.network;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import java.util.function.Supplier;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkEvent.Context;

public class ObservePacket {
    private BlockPos pos;
    private int node;
    private static int cooldown = 0;

    public ObservePacket(BlockPos pos, int node) {
        this.pos = pos;
        this.node = node;
    }

    public static void encode(ObservePacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
        buf.writeInt(packet.node);
    }

    public static ObservePacket decode(FriendlyByteBuf buf) {
        return new ObservePacket(buf.readBlockPos(), buf.readInt());
    }

    public static void handle(ObservePacket pkt, Supplier<Context> ctx) {
        ctx.get().enqueueWork(() -> {
            try {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    sendUpdate(pkt, player);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });
        ((Context)ctx.get()).setPacketHandled(true);
    }

    private static void sendUpdate(ObservePacket pkt, ServerPlayer player) {
        BlockEntity te = player.level.getBlockEntity(pkt.pos);
        if (te instanceof IObserveBlockEntity) {
            ((IObserveBlockEntity)te).onObserved(player, pkt);
            Packet<ClientGamePacketListener> supdateblockentitypacket = te.getUpdatePacket();
            if (supdateblockentitypacket != null) {
                player.connection.send(supdateblockentitypacket);
            }
        }

    }

    public static void tick() {
        --cooldown;
        if (cooldown < 0) {
            cooldown = 0;
        }

    }

    public static void send(BlockPos pos, int node) {
        if (cooldown <= 0) {
            cooldown = 10;
            CCNetwork.NETWORK.sendToServer(new ObservePacket(pos, node));
        }
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getNode() {
        return this.node;
    }
}