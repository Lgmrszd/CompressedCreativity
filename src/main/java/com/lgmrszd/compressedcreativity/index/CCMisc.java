package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber
public class CCMisc {
    public static void appendPneumaticHoverText(Supplier<BlockEntity> BEProvider, List<Component> infoList) {
        if (Screen.hasShiftDown()) {
//            infoList.add(new TranslatableComponent("compressedcreativity.gui.").withStyle(ChatFormatting.GOLD));
            BlockEntity te = BEProvider.get();
            if (te instanceof IPneumaticTileEntity pte) {
                infoList.add(new TranslatableComponent("pneumaticcraft.gui.tooltip.maxPressure", pte.getDangerPressure()).withStyle(ChatFormatting.GOLD));
            }
//            PneumaticRegistry.getInstance().getUpgradeRegistry().addUpgradeTooltip(upgrade.get(), infoList);
        } else {
            infoList.add(new TranslatableComponent("compressedcreativity.gui.tooltip.expand").withStyle(ChatFormatting.GOLD));
        }
    }
}
