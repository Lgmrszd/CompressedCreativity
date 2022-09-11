package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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

    public static void setBlockAndUpdateKeepAir(Level world, BlockPos blockPos, BlockState newState) {
        AtomicInteger oldAir = new AtomicInteger();
        BlockEntity oldBE = world.getBlockEntity(blockPos);
        if (oldBE != null) {
            oldBE.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent((cap) -> {
                oldAir.set(cap.getAir());
            });
        }
        world.setBlockAndUpdate(blockPos, newState);
        BlockEntity newBE = world.getBlockEntity(blockPos);
        if (newBE != null) {
            newBE.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent((cap) -> {
                cap.addAir(oldAir.get() - cap.getAir());
            });
        }
    }

    public static ResourceLocation CCRL(String path) {
        return new ResourceLocation(CompressedCreativity.MOD_ID, path);
    }
}
