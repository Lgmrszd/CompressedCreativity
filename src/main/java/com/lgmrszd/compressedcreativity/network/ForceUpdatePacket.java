package com.lgmrszd.compressedcreativity.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent.Context;

import java.util.function.Supplier;

public class ForceUpdatePacket {

    private BlockPos pos;

    public ForceUpdatePacket(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ForceUpdatePacket packet, FriendlyByteBuf buf) {
        buf.writeBlockPos(packet.pos);
    }

    public static ForceUpdatePacket decode(FriendlyByteBuf buf) {
        return new ForceUpdatePacket(buf.readBlockPos());
    }

    public static void handle(ForceUpdatePacket packet, Supplier<Context> context) {
        context.get().enqueueWork(() -> {
            handlePacket(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    private static void handlePacket(ForceUpdatePacket packet, Supplier<Context> context) {
        BlockPos pos = packet.pos;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.isLoaded(pos)) return;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof IUpdateBlockEntity ube)) return;
//        CompressedCreativity.LOGGER.debug("Updating! {}", pos);
        ube.forceUpdate();
    }

    public static void send(Level world, BlockPos pos) {
        CCNetwork.NETWORK.send(
                PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 16, world.dimension())),
                new ForceUpdatePacket(pos)
        );
    }
}
