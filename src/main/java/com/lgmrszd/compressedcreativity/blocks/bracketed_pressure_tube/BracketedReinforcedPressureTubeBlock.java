package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BracketedReinforcedPressureTubeBlock extends BracketedPressureTubeBlock {
    public BracketedReinforcedPressureTubeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends BracketedPressureTubeBlockEntity> getBlockEntityType() {
        return CCBlockEntities.BRACKETED_REINFORCED_PRESSURE_TUBE.get();
    }
}
