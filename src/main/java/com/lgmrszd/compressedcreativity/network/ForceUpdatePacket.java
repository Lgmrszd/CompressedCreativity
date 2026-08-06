package com.lgmrszd.compressedcreativity.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ForceUpdatePacket(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ForceUpdatePacket> TYPE =
        new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("compressedcreativity", "force_update"));

    public static final StreamCodec<ByteBuf, ForceUpdatePacket> STREAM_CODEC = StreamCodec.composite(
        BlockPos.STREAM_CODEC,
        ForceUpdatePacket::pos,
        ForceUpdatePacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ForceUpdatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            handlePacket(packet, context);
        });
    }

    private static void handlePacket(ForceUpdatePacket packet, IPayloadContext context) {
        BlockPos pos = packet.pos();
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.isLoaded(pos)) return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof IUpdateBlockEntity ube)) return;
//        CompressedCreativity.LOGGER.debug("Updating! {}", pos);
        ube.forceUpdate();
    }

    public static void send(Level world, BlockPos pos) {
        PacketDistributor.sendToPlayersTrackingChunk((net.minecraft.server.level.ServerLevel) world, 
            new net.minecraft.world.level.ChunkPos(pos), 
            new ForceUpdatePacket(pos));
    }
}
