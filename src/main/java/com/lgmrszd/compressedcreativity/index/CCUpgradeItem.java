package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.ModGroup;
import me.desht.pneumaticcraft.api.item.IUpgradeItem;
import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CCUpgradeItem extends Item implements IUpgradeItem {

    private final Supplier<PNCUpgrade> upgrade;
    private final int tier;
//
//    public CCUpgradeItem(Supplier<PNCUpgrade> upgrade, int tier) {
//        super(new Properties().tab(ModGroup.MAIN));
//        this.upgrade = upgrade;
//        this.tier = tier;
//    }

    public CCUpgradeItem() {
        super(new Properties().tab(ModGroup.MAIN));
        this.upgrade = null;
        this.tier = 1;
    }

    @Override
    public PNCUpgrade getUpgradeType() {
        return upgrade.get();
    }

    @Override
    public int getUpgradeTier() {
        return tier;
    }
}
