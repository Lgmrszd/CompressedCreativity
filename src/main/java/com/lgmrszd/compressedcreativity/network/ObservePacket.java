package com.lgmrszd.compressedcreativity.network;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import java.util.function.Supplier;

import com.lgmrszd.compressedcreativity.CompressedCreativity;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class ObservePacket {
    private BlockPos pos;
    private int node;
    private static int cooldown = 0;

    public ObservePacket(BlockPos pos, int node) {
        this.pos = pos;
        this.node = node;
    }

    public static void encode(ObservePacket packet, PacketBuffer tag) {
        tag.writeBlockPos(packet.pos);
        tag.writeInt(packet.node);
    }

    public static ObservePacket decode(PacketBuffer buf) {
        ObservePacket scp = new ObservePacket(buf.readBlockPos(), buf.readInt());
        return scp;
    }

    public static void handle(ObservePacket pkt, Supplier<Context> ctx) {
        ((Context)ctx.get()).enqueueWork(() -> {
            try {
                ServerPlayerEntity player = ((Context)ctx.get()).getSender();
                if (player != null) {
                    sendUpdate(pkt, player);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        });
        ((Context)ctx.get()).setPacketHandled(true);
    }

    private static void sendUpdate(ObservePacket pkt, ServerPlayerEntity player) {
        TileEntity te = player.level.getBlockEntity(pkt.pos);
        if (te instanceof IObserveTileEntity) {
            ((IObserveTileEntity)te).onObserved(player, pkt);
            SUpdateTileEntityPacket supdatetileentitypacket = te.getUpdatePacket();
            if (supdatetileentitypacket != null) {
                player.connection.send(supdatetileentitypacket);
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
            CompressedCreativity.Network.sendToServer(new ObservePacket(pos, node));
        }
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getNode() {
        return this.node;
    }
}