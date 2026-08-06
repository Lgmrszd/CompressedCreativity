package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.heater.HeaterBlock;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class CCCommonSetup {
    public static void init(FMLCommonSetupEvent event) {
        CCCommonUpgradeHandlers.init();
        CCUpgradesDBSetup.init();
        // Network registration is now handled by RegisterPayloadHandlersEvent in CCNetwork
        event.enqueueWork(HeaterBlock::registerHeater);
    }
}
