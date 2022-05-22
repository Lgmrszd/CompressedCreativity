package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.base.IRotate;
import com.simibubi.create.foundation.block.ITE;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;


//public class RotationalCompressorBlock extends HorizontalKineticBlock implements ITE<RotationalCompressorTileEntity>, IRotate {
import com.simibubi.create.content.contraptions.base.IRotate.SpeedLevel;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class RotationalCompressorBlock extends HorizontalKineticBlock implements IPneumaticWrenchable, IRotate, ITE<RotationalCompressorTileEntity> {

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
        if (te instanceof RotationalCompressorTileEntity rcte) {
            rcte.updateAirHandler();
        }
    }

//    @Override
//    public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, BlockPos p_60513_, boolean p_60514_) {
//        super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
//    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof RotationalCompressorTileEntity rcte) {
                IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
                miscHelpers.playMachineBreakEffect(rcte);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }

    @Override
    public Class<RotationalCompressorTileEntity> getTileEntityClass() {
        return RotationalCompressorTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends RotationalCompressorTileEntity> getTileEntityType() {
        return CCTileEntities.ROTATIONAL_COMPRESSOR.get();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = super.onWrenched(state, context);
        if (result == InteractionResult.SUCCESS) {
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.forceClientShapeRecalculation(context.getLevel(), context.getClickedPos());
        }
        return result;
    }

//    @Override
//    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
//        return super.updateAfterWrenched(newState, context);
//    }

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

    @Override
    public boolean onWrenched(Level world, Player player, BlockPos pos, Direction side, InteractionHand hand) {
        UseOnContext ctx = new UseOnContext(player, hand, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getX()), side, pos, false));
        return ctx.getPlayer() != null && (
                ctx.getPlayer().isCrouching() ?
                        onSneakWrenched(world.getBlockState(pos), ctx) :
                        onWrenched(world.getBlockState(pos), ctx)
        )  == InteractionResult.SUCCESS;
    }
}