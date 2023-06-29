package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.ITintedBlockEntity;
import me.desht.pneumaticcraft.client.util.TintColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = CompressedCreativity.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CCColorHandlers {
    @SubscribeEvent
    public static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(CCColorHandlers::getTintColor, CCBlocks.INDUSTRIAL_AIR_BLOWER.get());
        event.register(CCColorHandlers::getTintColor, CCBlocks.HEATER.get());
    }

    public static int getTintColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        if (world != null && pos != null) {
            BlockEntity te = world.getBlockEntity(pos);
            return te instanceof ITintedBlockEntity ? ((ITintedBlockEntity) te).getTintColor(tintIndex) : TintColor.WHITE.getRGB();
        } else {
            return -1;
        }
    }
}
