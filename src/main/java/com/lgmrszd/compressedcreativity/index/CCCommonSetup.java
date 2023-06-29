package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.heater.HeaterBlock;
import com.lgmrszd.compressedcreativity.network.CCNetwork;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CCCommonSetup {
    public static void init(FMLCommonSetupEvent event) {
        CCCommonUpgradeHandlers.init();
        CCUpgradesDBSetup.init();
        CCNetwork.init();
        event.enqueueWork(HeaterBlock::registerHeater);
    }
}
