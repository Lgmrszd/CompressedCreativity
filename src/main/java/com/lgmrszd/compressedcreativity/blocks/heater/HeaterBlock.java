package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.content.contraptions.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HeaterBlock extends Block implements ITE<HeaterTileEntity> {
    public HeaterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<HeaterTileEntity> getTileEntityClass() {
        return HeaterTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeaterTileEntity> getTileEntityType() {
        return CCTileEntities.HEATER.get();
    }

    public static void registerHeater() {
//        BoilerHeaters.registerHeaterProvider((level, pos, state) -> {
//            BlockEntity be = level.getBlockEntity(pos);
//
//            return null;
//        });
        BoilerHeaters.registerHeater(CCBlocks.HEATER.get(), (level, pos, state) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof HeaterTileEntity hbe) return hbe.getHeatLevel();
            return -1;
        });
    }
}
