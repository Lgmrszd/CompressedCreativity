package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HeaterBlock extends Block implements IBE<HeaterBlockEntity> {
    public HeaterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<HeaterBlockEntity> getBlockEntityClass() {
        return HeaterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeaterBlockEntity> getBlockEntityType() {
        return CCBlockEntities.HEATER.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CCShapes.HEATER_SHAPE;
    }

    public static void registerHeater() {
        BoilerHeaters.registerHeater(CCBlocks.HEATER.get(), (level, pos, state) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof HeaterBlockEntity hbe) return hbe.getHeatLevel();
            return -1;
        });
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof HeaterBlockEntity hte) {
            hte.updateHeatExchanger();
        }
    }
}
