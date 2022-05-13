package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.EngineersGogglesHandler;
// TODO use API
import me.desht.pneumaticcraft.common.pneumatic_armor.ArmorUpgradeRegistry;

public class CCCommonUpgradeHandlers {
    public static EngineersGogglesHandler engineersGogglesHandler;

    public static void init() {
        ArmorUpgradeRegistry r = ArmorUpgradeRegistry.getInstance();
        engineersGogglesHandler = r.registerUpgradeHandler(new EngineersGogglesHandler());
    }
}
