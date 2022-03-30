package com.lgmrszd.compressedcreativity.blocks.air_blower;


import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ITE;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;

import javax.annotation.Nullable;


import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class AirBlowerBlock extends Block implements IWrenchable, ITE<AirBlowerTileEntity> {

    public static final Property<Direction> FACING = BlockStateProperties.FACING;

//    public static final VoxelShape shape = Block.box(2, 2, 2, 12, 12, 12);

    public AirBlowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection());
        return this.defaultBlockState()
                .setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof AirBlowerTileEntity) {
            AirBlowerTileEntity abte = (AirBlowerTileEntity) te;
            abte.updateAirHandler();
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CCShapes.AIR_BLOWER.get(state.getValue(FACING));
    }

    @Override
    public Class<AirBlowerTileEntity> getTileEntityClass() {
        return AirBlowerTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends AirBlowerTileEntity> getTileEntityType() {
        return CCTileEntities.AIR_BLOWER.get();
    }
}
