package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.MechanicalVisorClientHandler;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IPneumaticHelmetRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CCClientSetup {
    private static void registerArmorClientUpgradeHandlers() {
        IPneumaticHelmetRegistry clientRegistry = PneumaticRegistry.getInstance().getHelmetRegistry();
        clientRegistry.registerRenderHandler(CCCommonUpgradeHandlers.mechanicalVisorHandler, new MechanicalVisorClientHandler());
    }

    public static void initEarly() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(CCClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        registerArmorClientUpgradeHandlers();
        event.enqueueWork(CCClientSetup::initLate);
    }

    private static void initLate() {
    }
}
