package com.lgmrszd.compressedcreativity;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import me.desht.pneumaticcraft.common.util.UpgradableItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import static com.lgmrszd.compressedcreativity.index.CCUpgrades.ENGINEER_GOGGLES;


public class GoggledChecker {
//    private static final IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getItemRegistry().getUpgradeRegistry();

//    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade(Player player) {
        ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
        return UpgradableItemUtils.getUpgradeCount(helm, ENGINEER_GOGGLES.get()) > 0;
    }
}
