package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.blocks.common.PneumaticHorizontalKineticBlock;
import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.kinetics.base.IRotate;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;


public class RotationalCompressorBlock extends PneumaticHorizontalKineticBlock<RotationalCompressorBlockEntity> implements IRotate {

//    public static final VoxelShape shape = Block.box(0, 0, 0, 16, 10, 16);

    public RotationalCompressorBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
        return CCShapes.ROTATIONAL_COMPRESSOR.get(state.getValue(HORIZONTAL_FACING)) ;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }


    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    // TODO: check docs for diff in onNeighborChange / neighborChanged

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof RotationalCompressorBlockEntity rcte) {
            rcte.updateAirHandler();
        }
    }


    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    @Override
    public Class<RotationalCompressorBlockEntity> getBlockEntityClass() {
        return RotationalCompressorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends RotationalCompressorBlockEntity> getBlockEntityType() {
        return CCBlockEntities.ROTATIONAL_COMPRESSOR.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = super.onWrenched(state, context);
        if (result == InteractionResult.SUCCESS) {
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.forceClientShapeRecalculation(context.getLevel(), context.getClickedPos());
            if(!context.getLevel().isClientSide()){
                if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof RotationalCompressorBlockEntity rcte) {
                    rcte.updateAirHandler();
                }
            }
        }
        return result;
    }

}