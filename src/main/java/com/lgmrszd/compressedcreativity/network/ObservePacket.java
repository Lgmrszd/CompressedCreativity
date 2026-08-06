package com.lgmrszd.compressedcreativity.network;

/*
    Code is taken from https://github.com/mrh0/createaddition, made by MRH,
    taken with the permission of the original creator
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ObservePacket(BlockPos pos, int node) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ObservePacket> TYPE = 
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("compressedcreativity", "observe"));
    
    public static final StreamCodec<ByteBuf, ObservePacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        ObservePacket::pos,
        ByteBufCodecs.VAR_INT,
        ObservePacket::node,
        ObservePacket::new
    );

    private static int cooldown = 0;

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ObservePacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            try {
                ServerPlayer player = (ServerPlayer) ctx.player();
                if (player != null) {
                    sendUpdate(pkt, player);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        });
    }

    private static void sendUpdate(ObservePacket pkt, ServerPlayer player) {
        BlockEntity te = player.level().getBlockEntity(pkt.pos());
        if (te instanceof IObserveTileEntity) {
            ((IObserveTileEntity)te).onObserved(player, pkt);
            Packet<ClientGamePacketListener> supdatetileentitypacket = te.getUpdatePacket();
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
            net.neoforged.neoforge.network.PacketDistributor.sendToServer(new ObservePacket(pos, node));
        }
    }
}
