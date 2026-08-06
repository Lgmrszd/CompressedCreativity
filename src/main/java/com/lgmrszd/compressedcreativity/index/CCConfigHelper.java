package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.config.ClientConfig;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import me.desht.pneumaticcraft.client.pneumatic_armor.ClientArmorRegistry;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.bus.api.IEventBus;

public class CCConfigHelper {

    public static void init() {
        ModContainer container = ModList.get().getModContainerById(CompressedCreativity.MOD_ID).orElseThrow();
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);
    }

    public static void registerConfigListener(IEventBus modEventBus) {
        modEventBus.addListener(CCConfigHelper::onConfigChanged);
    }

    private static void onConfigChanged(final ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ClientConfig.CLIENT_SPEC) {
            refreshClient();
        }
    }

    static void refreshClient() {
        ClientArmorRegistry.getInstance().refreshConfig();
    }
}
