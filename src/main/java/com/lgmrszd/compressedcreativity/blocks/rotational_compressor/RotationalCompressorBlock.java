package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.index.EATileEntities;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.base.IRotate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;


//public class RotationalCompressorBlock extends HorizontalKineticBlock implements ITE<RotationalCompressorTileEntity>, IRotate {
public class RotationalCompressorBlock extends HorizontalKineticBlock implements IRotate {

    public static final VoxelShape shape = Block.box(0, 0, 0, 16, 12, 16);

    public RotationalCompressorBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return RotationalCompressorBlock.shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState blockState, IBlockReader iBlockReader) {
        return EATileEntities.ROTATIONAL_COMPRESSOR.create();
    }

    @Override
    public boolean hasShaftTowards(IWorldReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        TileEntity te = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof RotationalCompressorTileEntity) {
            RotationalCompressorTileEntity rcte = (RotationalCompressorTileEntity) te;
            rcte.updateAirHandler();
        }
    }

    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }



//    @Override
//    public BlockState updateAfterWrenched(BlockState newState, ItemUseContext context) {
//        BlockState updatedState = super.updateAfterWrenched(newState, context);
//        TileEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
//        if (te instanceof RotationalCompressorTileEntity) {
//            RotationalCompressorTileEntity rcte = (RotationalCompressorTileEntity) te;
//            rcte.updateAirHandler();
//        }
//        return updatedState;
//    }

    //    @Override
//    public Class<RotationalCompressorTileEntity> getTileEntityClass() {
//        return RotationalCompressorTileEntity.class;
//    }
}