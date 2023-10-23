package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.config.ClientConfig;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.client.pneumatic_armor.ClientArmorRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CCConfigHelper {

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CommonConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(CCConfigHelper::onConfigChanged);
    }

    private static void onConfigChanged(final ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ClientConfig.CLIENT_SPEC) {
            refreshClient();
        }
    }

    static void refreshClient() {
//        PneumaticRegistry.getInstance().getClientArmorRegistry().getInstance().refreshConfig();
        ClientArmorRegistry.getInstance().refreshConfig();
    }
}
