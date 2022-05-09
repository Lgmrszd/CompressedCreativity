package com.lgmrszd.compressedcreativity;

import me.desht.pneumaticcraft.common.util.UpgradableItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import static me.desht.pneumaticcraft.common.core.ModUpgrades.BLOCK_TRACKER;

public class GoggledChecker {

//    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade(Player player) {
        ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
        return UpgradableItemUtils.getUpgradeCount(helm, BLOCK_TRACKER.get()) > 0;
    }
}
