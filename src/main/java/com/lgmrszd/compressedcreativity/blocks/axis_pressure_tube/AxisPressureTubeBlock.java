package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlockItem;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.content.contraptions.wrench.IWrenchableWithBracket;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import me.desht.pneumaticcraft.common.block.PressureTubeBlock;
import me.desht.pneumaticcraft.common.core.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.LOGGER;

@Mod.EventBusSubscriber
public class AxisPressureTubeBlock extends RotatedPillarBlock implements ITE<AxisPressureTubeTileEntity>, IWrenchableWithBracket, SimpleWaterloggedBlock {
    public AxisPressureTubeBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        super.createBlockStateDefinition(definition.add(BlockStateProperties.WATERLOGGED));
    }

//    private static final EnumProperty<PressureTubeBlock.ConnectionType>[] connectionProperties;
//
//    static {
//        try {
//            Field connProperties = ModBlocks.PRESSURE_TUBE.get().getClass().getDeclaredField("CONNECTION_PROPERTIES_3");
//            connProperties.setAccessible(true);
//            connectionProperties =
//                    (EnumProperty<PressureTubeBlock.ConnectionType>[]) connProperties.get(ModBlocks.PRESSURE_TUBE.get());
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.FOUR_VOXEL_POLE.get(state.getValue(AXIS));
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        boolean blockTypeChanged = state.getBlock() != newState.getBlock();
        if (state != newState && !isMoving)
            removeBracket(world, pos, true).ifPresent(stack -> Block.popResource(world, pos, stack));
        if (state.hasBlockEntity() && (blockTypeChanged || !newState.hasBlockEntity()))
            world.removeBlockEntity(pos);
        IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
        miscHelpers.forceClientShapeRecalculation(world, pos);
    }

    @Override
    public boolean tryRemoveBracket(UseOnContext context) {
        boolean removed = IWrenchableWithBracket.super.tryRemoveBracket(context);
        if(removed) {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState oldBlockState = world.getBlockState(pos);
            BlockState newBlockState = ModBlocks.PRESSURE_TUBE.get().defaultBlockState()
                    .setValue(BlockStateProperties.WATERLOGGED,
                            oldBlockState.getValue(BlockStateProperties.WATERLOGGED));
            switch (oldBlockState.getValue(AXIS)) {
                case X -> newBlockState = newBlockState
                        .setValue(PressureTubeBlock.WEST_3, PressureTubeBlock.ConnectionType.CONNECTED)
                        .setValue(PressureTubeBlock.EAST_3, PressureTubeBlock.ConnectionType.CONNECTED);
                case Y -> newBlockState = newBlockState
                        .setValue(PressureTubeBlock.UP_3, PressureTubeBlock.ConnectionType.CONNECTED)
                        .setValue(PressureTubeBlock.DOWN_3, PressureTubeBlock.ConnectionType.CONNECTED);
                case Z -> newBlockState = newBlockState
                        .setValue(PressureTubeBlock.NORTH_3, PressureTubeBlock.ConnectionType.CONNECTED)
                        .setValue(PressureTubeBlock.SOUTH_3, PressureTubeBlock.ConnectionType.CONNECTED);
            }

            world.setBlockAndUpdate(context.getClickedPos(), newBlockState);
//            world.getBlockState(context.getClickedPos()).updateNeighbourShapes(world, context.getClickedPos(), 3);
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.forceClientShapeRecalculation(context.getLevel(), context.getClickedPos());
        }
        return removed;
    }

    @Override
    public Optional<ItemStack> removeBracket(BlockGetter world, BlockPos pos, boolean inOnReplacedContext) {
        BracketedTileEntityBehaviour behaviour = TileEntityBehaviour.get(world, pos, BracketedTileEntityBehaviour.TYPE);
        if (behaviour == null)
            return Optional.empty();
        BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
        if (bracket == null)
            return Optional.empty();
        return Optional.of(new ItemStack(bracket.getBlock()));
    }

    @Override
    public Class<AxisPressureTubeTileEntity> getTileEntityClass() {
        return AxisPressureTubeTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends AxisPressureTubeTileEntity> getTileEntityType() {
        return CCTileEntities.AXIS_PRESSURE_TUBE.get();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }


    @SubscribeEvent
    public static void usingBracketOnPressureTube(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        Level world = event.getWorld();
        BlockPos blockPos = event.getPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!(item.getItem() instanceof BracketBlockItem)) return;
        if (!(blockState.getBlock() instanceof PressureTubeBlock)) return;
//        if (!(world.getBlockEntity(blockPos) instanceof PressureTubeBlockEntity ptbe)) return;
        Direction.Axis axis = null;
        int state = 0;
        for(Direction dir : Direction.values()) {
//            if (blockState.getValue(connectionProperties[dir.get3DDataValue()]) == PressureTubeBlock.ConnectionType.CONNECTED) {
//                state += 1 << dir.get3DDataValue();
//            }
            if (blockState.getValue(PressureTubeBlock.CONNECTION_PROPERTIES_3[dir.get3DDataValue()]) == PressureTubeBlock.ConnectionType.CONNECTED) {
                state += 1 << dir.get3DDataValue();
            }

//            if (ptbe.canConnectPneumatic(dir)) {
//                state += 1 << dir.get3DDataValue();
//            }
        }
        LOGGER.debug(state);
        switch (state) {
            case 48 -> {
                axis = Direction.Axis.X;
            }
            case 3 -> {
                axis = Direction.Axis.Y;
            }
            case 12 -> {
                axis = Direction.Axis.Z;
            }
            default -> {
            }
        }
        if (axis != null) {
            BlockState newState = CCBlocks.AXIS_PRESSURE_TUBE.getDefaultState()
                    .setValue(AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED,
                            blockState.getValue(BlockStateProperties.WATERLOGGED));
            world.setBlockAndUpdate(blockPos, newState);
        }
    }
}
