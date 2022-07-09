package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CompressedAirEngineBlock extends HorizontalKineticBlock implements ITE<CompressedAirEngineTileEntity> {
    public CompressedAirEngineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public Class<CompressedAirEngineTileEntity> getTileEntityClass() {
        return CompressedAirEngineTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends CompressedAirEngineTileEntity> getTileEntityType() {
        return CCTileEntities.COMPRESSED_AIR_ENGINE.get();
    }
}
