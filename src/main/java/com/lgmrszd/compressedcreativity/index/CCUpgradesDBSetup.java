package com.lgmrszd.compressedcreativity.index;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry.Builder;

import static me.desht.pneumaticcraft.common.core.ModItems.PNEUMATIC_HELMET;

public class CCUpgradesDBSetup {

    public static final Builder HelmetBuilder = new Builder()
    .with(CCUpgrades.ENGINEER_GOGGLES.get(), 1);

    public static void init() {
        IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getItemRegistry().getUpgradeRegistry();
        upgradeRegistry.addApplicableUpgrades(PNEUMATIC_HELMET.get(), HelmetBuilder);
    }
}
