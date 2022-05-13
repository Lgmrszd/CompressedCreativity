package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IArmorUpgradeClientHandler;
import me.desht.pneumaticcraft.common.pneumatic_armor.handlers.EnderVisorHandler;

public class EngineersGogglesClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<EngineersGogglesHandler>{
    public EngineersGogglesClientHandler() {
        super(CCCommonUpgradeHandlers.engineersGogglesHandler);
    }
}
