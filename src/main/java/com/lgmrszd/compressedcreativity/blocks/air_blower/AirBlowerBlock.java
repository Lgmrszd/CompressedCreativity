package com.lgmrszd.compressedcreativity.blocks.air_blower;


import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nullable;


public class AirBlowerBlock extends Block implements IWrenchable {

    public static final Property<Direction> FACING = BlockStateProperties.FACING;

//    public static final VoxelShape shape = Block.box(2, 2, 2, 12, 12, 12);

    public AirBlowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return CCTileEntities.AIR_BLOWER.create();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection());
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        TileEntity te = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof AirBlowerTileEntity) {
            AirBlowerTileEntity abte = (AirBlowerTileEntity) te;
            abte.updateAirHandler();
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return CCShapes.AIR_BLOWER.get(state.getValue(FACING));
    }
}
