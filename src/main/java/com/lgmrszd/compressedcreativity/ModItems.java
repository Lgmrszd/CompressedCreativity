package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCUpgradeItem;
import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import com.lgmrszd.compressedcreativity.items.EngineerGogglesUpgradeItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import me.desht.pneumaticcraft.common.core.ModUpgrades;
import me.desht.pneumaticcraft.common.item.UpgradeItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.IntStream;

public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);


    public static void register(IEventBus eventBus) {
    }


    static {
        REGISTRATE.item("engineer_goggles_upgrade", EngineerGogglesUpgradeItem::new).register();
    }



//    private static void registerUpgrade(RegistryObject<PNCUpgrade> upgrade, ModUpgrades.BuiltinUpgrade upgradeDetails) {
//        IntStream.range(1, upgradeDetails.getMaxTier() + 1).forEach(
//                tier -> {
//                    String baseName = upgradeDetails.getName() + "_upgrade";
//                    String itemName = upgradeDetails.getMaxTier() > 1 ? baseName + "_" + tier : baseName;
//                    register(itemName, () -> new UpgradeItem(upgrade, tier));
//                }
//        );
//    }
}
