package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class AxisReinforcedPressureTubeBlock extends AxisPressureTubeBlock {
    public AxisReinforcedPressureTubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends AxisPressureTubeTileEntity> getTileEntityType() {
        return CCTileEntities.AXIS_REINFORCED_PRESSURE_TUBE.get();
    }
}
