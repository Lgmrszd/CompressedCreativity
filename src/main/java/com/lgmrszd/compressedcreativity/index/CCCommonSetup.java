package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.BlockTrackerEntryKinetic;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IPneumaticHelmetRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CCCommonSetup {
    public static void init(FMLCommonSetupEvent event) {

        CCCommonUpgradeHandlers.init();
        CCUpgradesDBSetup.init();
    }
}
