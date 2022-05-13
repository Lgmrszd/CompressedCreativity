package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.item.IUpgradeRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import me.desht.pneumaticcraft.common.util.UpgradableItemUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import static com.lgmrszd.compressedcreativity.index.CCUpgrades.ENGINEER_GOGGLES;


public class GoggledChecker {
//    private static final IUpgradeRegistry upgradeRegistry = PneumaticRegistry.getInstance().getItemRegistry().getUpgradeRegistry();

//    @OnlyIn(Dist.CLIENT)
    public static boolean hasBlockTrackerUpgrade(Player player) {
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        ICommonArmorHandler handler = reg.getCommonArmorHandler(player);
        return handler.upgradeUsable(CCCommonUpgradeHandlers.engineersGogglesHandler, true);
    }
}
