package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.blocks.common.PneumaticHorizontalKineticBlock;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CompressedAirEngineBlock extends PneumaticHorizontalKineticBlock<CompressedAirEngineBlockEntity> {

    public static final BooleanProperty BACK = BooleanProperty.create("back");
    public static final BooleanProperty FRONT = BooleanProperty.create("front");

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public CompressedAirEngineBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(BACK, true)
                        .setValue(FRONT, true)
                        .setValue(UP, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
        Direction dir = state.getValue(HORIZONTAL_FACING);
        return Shapes.join(
                CCShapes.COMPRESSED_AIR_ENGINE_CORE.get(dir),
                Shapes.join(
                        state.getValue(FRONT) ? CCShapes.COMPRESSED_AIR_ENGINE_FRONT_END.get(dir) : CCShapes.COMPRESSED_AIR_ENGINE_FRONT_OPEN.get(dir),
                        state.getValue(BACK) ? CCShapes.COMPRESSED_AIR_ENGINE_BACK_END.get(dir) : CCShapes.COMPRESSED_AIR_ENGINE_BACK_OPEN.get(dir),
                        BooleanOp.OR
                ),
                BooleanOp.OR
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BACK);
        builder.add(FRONT);
        builder.add(UP);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (context.getClickedFace() == state.getValue(HORIZONTAL_FACING) && state.getValue(FRONT)) {
            BlockPos relative_pos = context.getClickedPos().relative(context.getClickedFace());
            BlockState relative = context.getLevel().getBlockState(relative_pos);
            if (relative.getBlock() instanceof CompressedAirEngineBlock &&
                    relative.getValue(HORIZONTAL_FACING) == state.getValue(HORIZONTAL_FACING)) {
                context.getLevel().setBlockAndUpdate(relative_pos, relative.setValue(BACK, false));
            }
        }
        if (context.getClickedFace() == state.getValue(HORIZONTAL_FACING).getOpposite() && state.getValue(BACK)) {
            BlockPos relative_pos = context.getClickedPos().relative(context.getClickedFace());
            BlockState relative = context.getLevel().getBlockState(relative_pos);
            if (relative.getBlock() instanceof CompressedAirEngineBlock &&
                    relative.getValue(HORIZONTAL_FACING) == state.getValue(HORIZONTAL_FACING)) {
                context.getLevel().setBlockAndUpdate(relative_pos, relative.setValue(FRONT, false));
            }
        }
        InteractionResult result = super.onWrenched(state, context);
        if (!context.getLevel().isClientSide()) {
            BlockEntity te = state.hasBlockEntity() ? context.getLevel().getBlockEntity(context.getClickedPos()) : null;
            if (te instanceof CompressedAirEngineBlockEntity caete) {
                caete.updateAirHandler();
            }
        }
        return result;
    }

    // For some reason, it works without overriding this, will comment out for now
//    @Override
//    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
//        if (originalState.getValue(HORIZONTAL_FACING) == targetedFace) {
//            return originalState.setValue(FRONT, false);
//        }
//            return super.getRotatedBlockState(originalState, targetedFace);
//    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction face = context.getClickedFace();

        BlockState placedOn = world.getBlockState(pos.relative(face.getOpposite()));

//        BlockState state = this.defaultBlockState();

        Direction dir = context.getHorizontalDirection();


        if (placedOn.getBlock() instanceof CompressedAirEngineBlock) {
            if(placedOn.getValue(HORIZONTAL_FACING) == face) {
                return this.defaultBlockState()
                        .setValue(BACK, false)
                        .setValue(HORIZONTAL_FACING, placedOn.getValue(HORIZONTAL_FACING));
            }
            if(placedOn.getValue(HORIZONTAL_FACING) == face.getOpposite()) {
                return this.defaultBlockState()
                        .setValue(FRONT, false)
                        .setValue(HORIZONTAL_FACING, placedOn.getValue(HORIZONTAL_FACING));
            }
        }

        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, dir);
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, dir.getOpposite());
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.UP) {
            BlockEntity other_te = worldIn.getBlockEntity(currentPos.relative(facing));
            boolean has_connection = other_te != null && other_te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, facing.getOpposite()).isPresent();
            stateIn = stateIn.setValue(UP, has_connection);
        }
        if (facing.getAxis().isHorizontal() && facingState.getBlock() instanceof CompressedAirEngineBlock &&
                facingState.getValue(HORIZONTAL_FACING) == stateIn.getValue(HORIZONTAL_FACING)) {
            if (stateIn.getValue(HORIZONTAL_FACING) == facing && !facingState.getValue(BACK))
                return stateIn.setValue(FRONT, false);
            if (stateIn.getValue(HORIZONTAL_FACING) == facing.getOpposite() && !facingState.getValue(FRONT))
                return stateIn.setValue(BACK, false);
        }
        if (stateIn.getValue(HORIZONTAL_FACING) == facing) {
            return stateIn.setValue(FRONT, true);
        }
        if (stateIn.getValue(HORIZONTAL_FACING) == facing.getOpposite()) {
            return stateIn.setValue(BACK, true);
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING) || face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(HORIZONTAL_FACING).getAxis();
    }

    @Override
    public Class<CompressedAirEngineBlockEntity> getBlockEntityClass() {
        return CompressedAirEngineBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CompressedAirEngineBlockEntity> getBlockEntityType() {
        return CCBlockEntities.COMPRESSED_AIR_ENGINE.get();
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        if (te instanceof CompressedAirEngineBlockEntity caete) {
            caete.updateAirHandler();
        }
    }
}
