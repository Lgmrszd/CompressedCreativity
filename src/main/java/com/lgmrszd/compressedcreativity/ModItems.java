package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import com.simibubi.create.foundation.data.CreateRegistrate;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import me.desht.pneumaticcraft.common.item.ItemRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.stream.IntStream;

import static me.desht.pneumaticcraft.common.core.ModItems.PNEUMATIC_HELMET;

public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static void register(IEventBus eventBus) {
    }


    static {
        REGISTRATE.item("engineer_goggles_upgrade", (properties) -> {
            IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getItemRegistry().getUpgradeRegistry();
            return upgradeRegistry.makeUpgradeItem(CCUpgrades.ENGINEER_GOGGLES::get, 1);
        }).register();
    }

}
