package com.lgmrszd.compressedcreativity.items;

import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import me.desht.pneumaticcraft.api.item.IUpgradeItem;
import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraft.world.item.Item;

public class EngineerGogglesUpgradeItem extends Item implements IUpgradeItem {

    public EngineerGogglesUpgradeItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public PNCUpgrade getUpgradeType() {
        return CCUpgrades.ENGINEER_GOGGLES.get();
    }
}
