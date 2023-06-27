package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import me.desht.pneumaticcraft.api.pressure.PressureTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BracketedAdvancedPressureTubeBlockEntity extends BracketedPressureTubeBlockEntity {
    public BracketedAdvancedPressureTubeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, PressureTier.TIER_TWO, 4000);
    }
}
