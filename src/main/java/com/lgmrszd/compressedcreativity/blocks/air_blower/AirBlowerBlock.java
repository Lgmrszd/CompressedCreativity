package com.lgmrszd.compressedcreativity.blocks.air_blower;


import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

import static com.lgmrszd.compressedcreativity.index.CCMisc.appendPneumaticHoverText;


public class AirBlowerBlock extends Block implements IPneumaticWrenchable, IWrenchable, IBE<AirBlowerBlockEntity> {

    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;

    public static final BooleanProperty[] CONNECTION_PROPERTIES = new BooleanProperty[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};

    public AirBlowerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(UP, false)
                        .setValue(DOWN, false)
                        .setValue(NORTH, false)
                        .setValue(SOUTH, false)
                        .setValue(EAST, false)
                        .setValue(WEST, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
                .add(FACING)
                .add(UP)
                .add(DOWN)
                .add(NORTH)
                .add(SOUTH)
                .add(EAST)
                .add(WEST);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getPlayer() != null && context.getPlayer().isShiftKeyDown() ?
                context.getNearestLookingDirection():
                context.getNearestLookingDirection().getOpposite();
        BlockState state = defaultBlockState().setValue(FACING, facing);
        for (Direction dir : Direction.values()) {
            if (dir == facing) continue;
            BlockEntity te = context.getLevel().getBlockEntity(context.getClickedPos().relative(dir));
            if (te != null && te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, dir.getOpposite()).isPresent()) {
                state = state.setValue(CONNECTION_PROPERTIES[dir.get3DDataValue()], true);
            }
        }
        return state;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof AirBlowerBlockEntity abte) {
            abte.updateAirHandler();
        }
    }

    @Nonnull
    @Override
    public BlockState updateShape(BlockState stateIn, @Nonnull Direction facing, @Nonnull BlockState facingState, @Nonnull LevelAccessor worldIn, @Nonnull BlockPos currentPos, @Nonnull BlockPos facingPos) {
//        BlockEntity te = worldIn.getBlockEntity(currentPos);
//        if (te != null && te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, facing).isPresent()) {
        if (facing != stateIn.getValue(FACING)) {
            BlockEntity other_te = worldIn.getBlockEntity(currentPos.relative(facing));
            boolean has_connection = canConnect(facing, other_te);
            stateIn = stateIn.setValue(CONNECTION_PROPERTIES[facing.get3DDataValue()], has_connection);
        } else {
            stateIn = stateIn.setValue(CONNECTION_PROPERTIES[facing.get3DDataValue()], false);
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean canConnect(Direction facing, BlockEntity other_te) {
        return other_te != null && other_te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, facing.getOpposite()).isPresent();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        InteractionResult result = IWrenchable.super.onWrenched(state, context);
        if (result == InteractionResult.SUCCESS) {
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.forceClientShapeRecalculation(context.getLevel(), context.getClickedPos());
            if(!context.getLevel().isClientSide()){
                if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof AirBlowerBlockEntity abte) {
                    abte.updateAirHandler();
                }
            }
        }
        return result;
    }

    @Override
    public void onRemove(BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof AirBlowerBlockEntity abte) {
                IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
                miscHelpers.playMachineBreakEffect(abte);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Nonnull
    @Override
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return CCShapes.AIR_BLOWER.get(state.getValue(FACING));
    }

    @Override
    public Class<AirBlowerBlockEntity> getBlockEntityClass() {
        return AirBlowerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AirBlowerBlockEntity> getBlockEntityType() {
        return CCBlockEntities.AIR_BLOWER.get();
    }

    @Override
    public boolean onWrenched(Level world, Player player, BlockPos pos, Direction side, InteractionHand hand) {
        UseOnContext ctx = new UseOnContext(player, hand, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getX()), side, pos, false));
        return ctx.getPlayer() != null && (
                ctx.getPlayer().isCrouching() ?
                        onSneakWrenched(world.getBlockState(pos), ctx) :
                        onWrenched(world.getBlockState(pos), ctx)
        )  == InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@Nonnull ItemStack stack, BlockGetter world, @Nonnull List<Component> infoList, @Nonnull TooltipFlag par4) {
        appendPneumaticHoverText(
                () -> newBlockEntity(BlockPos.ZERO, defaultBlockState()),
                infoList);
    }
}
