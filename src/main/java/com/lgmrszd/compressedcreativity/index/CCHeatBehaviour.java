package com.lgmrszd.compressedcreativity.index;

import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

public class CCHeatBehaviour {
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(CCHeatBehaviour::registerHeatBehaviour);
    }

    public static void registerHeatBehaviour() {
//        IHeatRegistry heatRegistry = PneumaticRegistry.getInstance().getHeatRegistry();
    }
}
