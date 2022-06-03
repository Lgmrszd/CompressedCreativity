package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.config.MechanicalVisorConfig;
import com.lgmrszd.compressedcreativity.index.CCClientSetup;
import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorRegistry;
import net.minecraft.world.entity.player.Player;


public class GoggledChecker {

//    @OnlyIn(Dist.CLIENT)
    public static boolean hasMechanicalVisorUpgrade(Player player) {
        ICommonArmorRegistry reg = PneumaticRegistry.getInstance().getCommonArmorRegistry();
        ICommonArmorHandler handler = reg.getCommonArmorHandler(player);
        return CCClientSetup.mechanicalVisorClientHandler.tooltipMode.isGoggled() &&
                handler.upgradeUsable(CCCommonUpgradeHandlers.mechanicalVisorHandler, true);
    }
}
