package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IArmorUpgradeClientHandler;

public class MechanicalVisorClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<MechanicalVisorHandler>{
    public MechanicalVisorClientHandler() {
        super(CCCommonUpgradeHandlers.mechanicalVisorHandler);
    }
}
