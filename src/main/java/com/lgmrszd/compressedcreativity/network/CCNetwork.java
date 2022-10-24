package com.lgmrszd.compressedcreativity.network;

import com.lgmrszd.compressedcreativity.index.CCMisc;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class CCNetwork {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.ChannelBuilder.named(CCMisc.CCRL("main"))
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .networkProtocolVersion(() -> PROTOCOL)
            .simpleChannel();


    public static void init() {
        int i = 0;
        NETWORK.registerMessage(i++, ObservePacket.class, ObservePacket::encode, ObservePacket::decode, ObservePacket::handle);
        NETWORK.registerMessage(i++, ForceUpdatePacket.class, ForceUpdatePacket::encode, ForceUpdatePacket::decode, ForceUpdatePacket::handle);
    }
}
