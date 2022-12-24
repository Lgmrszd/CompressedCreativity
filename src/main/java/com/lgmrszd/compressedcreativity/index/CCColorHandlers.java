package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerTileEntity;
import me.desht.pneumaticcraft.client.util.TintColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

import javax.annotation.Nullable;

public class CCColorHandlers {
    @OnlyIn(Dist.CLIENT)
    public static void registerBlockColorHandlers(ColorHandlerEvent.Block event) {
        event.getBlockColors().register(CCColorHandlers::getTintColor, CCBlocks.INDUSTRIAL_AIR_BLOWER.get());
    }

    public static int getTintColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            BlockEntity te = world.getBlockEntity(pos);
            return te instanceof AdvancedAirBlowerTileEntity ? ((AdvancedAirBlowerTileEntity) te).getTintColor(tintIndex) : TintColor.WHITE.getRGB();
        } else {
            return -1;
        }
    }
}
