package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.content.contraptions.fluids.tank.BoilerHeaters;
import com.simibubi.create.foundation.block.ITE;
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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CCShapes.HEATER_SHAPE;
    }

    public static void registerHeater() {
        BoilerHeaters.registerHeater(CCBlocks.HEATER.get(), (level, pos, state) -> {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof HeaterTileEntity hbe) return hbe.getHeatLevel();
            return -1;
        });
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof HeaterTileEntity hte) {
            hte.updateHeatExchanger();
        }
    }
}
