package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.EngineersGogglesHandler;
// TODO use API
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import me.desht.pneumaticcraft.common.pneumatic_armor.ArmorUpgradeRegistry;

public class CCCommonUpgradeHandlers {
    public static EngineersGogglesHandler engineersGogglesHandler;

    public static void init() {
//        ArmorUpgradeRegistry r = ArmorUpgradeRegistry.getInstance();
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        engineersGogglesHandler = reg.registerUpgradeHandler(new EngineersGogglesHandler());
    }
}
