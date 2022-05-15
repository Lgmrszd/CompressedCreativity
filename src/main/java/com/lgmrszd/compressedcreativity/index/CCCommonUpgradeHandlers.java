package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.upgrades.MechanicalVisorHandler;
// TODO use API
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;

public class CCCommonUpgradeHandlers {
    public static MechanicalVisorHandler mechanicalVisorHandler;

    public static void init() {
//        ArmorUpgradeRegistry r = ArmorUpgradeRegistry.getInstance();
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        mechanicalVisorHandler = reg.registerUpgradeHandler(new MechanicalVisorHandler());
    }
}
