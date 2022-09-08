package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AxisAdvancedPressureTubeBlock extends AxisPressureTubeBlock {
    public AxisAdvancedPressureTubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends AxisPressureTubeTileEntity> getTileEntityType() {
        return CCTileEntities.AXIS_ADVANCED_PRESSURE_TUBE.get();
    }
}
