package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BracketedReinforcedPressureTubeBlock extends BracketedPressureTubeBlock {
    public BracketedReinforcedPressureTubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends BracketedPressureTubeTileEntity> getTileEntityType() {
        return CCTileEntities.BRACKETED_REINFORCED_PRESSURE_TUBE.get();
    }
}
