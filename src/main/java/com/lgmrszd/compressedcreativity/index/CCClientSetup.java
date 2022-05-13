package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.EngineersGogglesClientHandler;
// TODO use API
import me.desht.pneumaticcraft.client.pneumatic_armor.ArmorUpgradeClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CCClientSetup {
    private static void registerArmorClientUpgradeHandlers() {
        ArmorUpgradeClientRegistry cr = ArmorUpgradeClientRegistry.getInstance();
        cr.registerHandler(CCCommonUpgradeHandlers.engineersGogglesHandler, new EngineersGogglesClientHandler());
    }

    public static void initEarly() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CCClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        event.enqueueWork(CCClientSetup::initLate);
    }

    private static void initLate() {
        registerArmorClientUpgradeHandlers();
    }
}
