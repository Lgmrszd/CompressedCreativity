package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import me.desht.pneumaticcraft.api.pressure.PressureTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class AxisReinforcedPressureTubeTileEntity extends AxisPressureTubeTileEntity {
    public AxisReinforcedPressureTubeTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, PressureTier.TIER_ONE_HALF, 1000);
    }
}
