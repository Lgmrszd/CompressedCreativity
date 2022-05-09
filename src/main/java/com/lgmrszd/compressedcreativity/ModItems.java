package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCUpgradeItem;
import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import com.lgmrszd.compressedcreativity.items.EngineerGogglesUpgradeItem;
import com.simibubi.create.foundation.data.CreateRegistrate;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IPneumaticHelmetRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import me.desht.pneumaticcraft.common.item.ItemRegistry;
import me.desht.pneumaticcraft.common.item.PneumaticArmorItem;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.stream.IntStream;

import static me.desht.pneumaticcraft.common.core.ModItems.PNEUMATIC_HELMET;

public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    private static final IUpgradeRegistry upgradeRegistry = ItemRegistry.getInstance().getUpgradeRegistry();


    public static void register(IEventBus eventBus) {
    }


    static {
        REGISTRATE.item("engineer_goggles_upgrade", (properties) -> upgradeRegistry.makeUpgradeItem(CCUpgrades.ENGINEER_GOGGLES::get, 1)).register();
        upgradeRegistry.addApplicableUpgrades(PNEUMATIC_HELMET.get(), new IUpgradeRegistry.Builder().with(CCUpgrades.ENGINEER_GOGGLES.get(), 1));
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
