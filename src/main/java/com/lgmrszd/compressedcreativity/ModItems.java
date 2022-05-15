package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import com.simibubi.create.foundation.data.CreateRegistrate;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static void register(IEventBus eventBus) {
    }


    static {
        REGISTRATE.item("mechanical_visor_upgrade", (properties) -> {
            IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getUpgradeRegistry();
            return upgradeRegistry.makeUpgradeItem(CCUpgrades.MECHANICAL_VISOR::get, 1);
        }).register();
    }

}
