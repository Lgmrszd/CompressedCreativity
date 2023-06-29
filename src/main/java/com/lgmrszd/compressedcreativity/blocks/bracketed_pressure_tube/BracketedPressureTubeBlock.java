package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.lgmrszd.compressedcreativity.index.CCModsReference;
import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.decoration.bracket.BracketBlockItem;
import com.simibubi.create.content.decoration.bracket.BracketedBlockEntityBehaviour;
import com.simibubi.create.content.equipment.wrench.IWrenchableWithBracket;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.ITubeNetworkConnector;
import me.desht.pneumaticcraft.api.block.PNCBlockStateProperties;
import me.desht.pneumaticcraft.api.block.PressureTubeConnection;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class BracketedPressureTubeBlock extends RotatedPillarBlock implements
        IBE<BracketedPressureTubeBlockEntity>, IWrenchableWithBracket, SimpleWaterloggedBlock,
        ISpecialBlockItemRequirement, ITubeNetworkConnector {

    private final int tubeTier;

    public BracketedPressureTubeBlock(Properties properties, int tier) {
        super(properties);
        this.tubeTier = tier;
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        super.createBlockStateDefinition(definition.add(BlockStateProperties.WATERLOGGED));
    }

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
            BlockState newBlockState;
            CCModsReference.BracketedPressureTube bracketedPressureTube =
                    CCModsReference.BracketedPressureTube.getByBlock(oldBlockState.getBlock());
            if (bracketedPressureTube == null) return true;
            newBlockState = CCModsReference.PNCPressureTube
                    .getByTier(bracketedPressureTube.getTier())
                    .getBlock()
                    .defaultBlockState();
            newBlockState = newBlockState
                    .setValue(BlockStateProperties.WATERLOGGED,
                            oldBlockState.getValue(BlockStateProperties.WATERLOGGED));
            switch (oldBlockState.getValue(AXIS)) {
                case X -> newBlockState = newBlockState
                        .setValue(PNCBlockStateProperties.PressureTubes.WEST, PressureTubeConnection.CONNECTED)
                        .setValue(PNCBlockStateProperties.PressureTubes.EAST, PressureTubeConnection.CONNECTED);
                case Y -> newBlockState = newBlockState
                        .setValue(PNCBlockStateProperties.PressureTubes.UP, PressureTubeConnection.CONNECTED)
                        .setValue(PNCBlockStateProperties.PressureTubes.DOWN, PressureTubeConnection.CONNECTED);
                case Z -> newBlockState = newBlockState
                        .setValue(PNCBlockStateProperties.PressureTubes.NORTH, PressureTubeConnection.CONNECTED)
                        .setValue(PNCBlockStateProperties.PressureTubes.SOUTH, PressureTubeConnection.CONNECTED);
            }

            CCMisc.setBlockAndUpdateKeepAir(world, context.getClickedPos(), newBlockState);
//            world.getBlockState(context.getClickedPos()).updateNeighbourShapes(world, context.getClickedPos(), 3);
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.forceClientShapeRecalculation(context.getLevel(), context.getClickedPos());
        }
        return removed;
    }

    @Override
    public Optional<ItemStack> removeBracket(BlockGetter world, BlockPos pos, boolean inOnReplacedContext) {
        BracketedBlockEntityBehaviour behaviour = BlockEntityBehaviour.get(world, pos, BracketedBlockEntityBehaviour.TYPE);
        if (behaviour == null)
            return Optional.empty();
        BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
        if (bracket == null)
            return Optional.empty();
        return Optional.of(new ItemStack(bracket.getBlock()));
    }

    @Override
    public Class<BracketedPressureTubeBlockEntity> getBlockEntityClass() {
        return BracketedPressureTubeBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BracketedPressureTubeBlockEntity> getBlockEntityType() {
        if (tubeTier == 1) return CCBlockEntities.BRACKETED_REINFORCED_PRESSURE_TUBE.get();
        if (tubeTier == 2) return CCBlockEntities.BRACKETED_ADVANCED_PRESSURE_TUBE.get();
        return CCBlockEntities.BRACKETED_PRESSURE_TUBE.get();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        CCModsReference.BracketedPressureTube bracketedPressureTube =
                CCModsReference.BracketedPressureTube.getByBlock(state.getBlock());
        if (bracketedPressureTube == null) return super.getCloneItemStack(state, target, level, pos, player);
        return new ItemStack(
                CCModsReference.PNCPressureTube
                        .getByTier(bracketedPressureTube.getTier())
                        .getBlock()
        );
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        CCModsReference.BracketedPressureTube bracketedPressureTube =
                CCModsReference.BracketedPressureTube.getByBlock(state.getBlock());
        if (bracketedPressureTube == null) return ItemRequirement.INVALID;
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                List.of(
                        new ItemStack(
                                CCModsReference.PNCPressureTube.getByTier(bracketedPressureTube.getTier()).getBlock()
                        )
                )
        );
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        if ((state.hasBlockEntity() ? level.getBlockEntity(pos) : null) instanceof BracketedPressureTubeBlockEntity tte) {
            tte.updateAirHandler();
        }
    }

    @Override
    public boolean canConnectToNetwork(Level level, BlockPos pos, Direction dir, BlockState state) {
        return state.hasProperty(AXIS) && dir.getAxis() == state.getValue(AXIS);
    }

    @SubscribeEvent
    public static void usingBracketOnPressureTube(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        Level world = event.getLevel();
        BlockPos blockPos = event.getPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!(item.getItem() instanceof BracketBlockItem)) return;

        CCModsReference.PNCPressureTube pressureTube = CCModsReference.PNCPressureTube.getByBlock(blockState.getBlock());
        if (pressureTube == null) return;
        BlockState axisTubeState =
                CCModsReference.BracketedPressureTube.getByTier(pressureTube.getTier()).getBlock().defaultBlockState();
        boolean c_down = blockState.getValue(PNCBlockStateProperties.PressureTubes.DOWN) == PressureTubeConnection.CONNECTED;
        boolean c_up = blockState.getValue(PNCBlockStateProperties.PressureTubes.UP) == PressureTubeConnection.CONNECTED;
        boolean c_north = blockState.getValue(PNCBlockStateProperties.PressureTubes.NORTH) == PressureTubeConnection.CONNECTED;
        boolean c_south = blockState.getValue(PNCBlockStateProperties.PressureTubes.SOUTH) == PressureTubeConnection.CONNECTED;
        boolean c_west = blockState.getValue(PNCBlockStateProperties.PressureTubes.WEST) == PressureTubeConnection.CONNECTED;
        boolean c_east = blockState.getValue(PNCBlockStateProperties.PressureTubes.EAST) == PressureTubeConnection.CONNECTED;
        Direction.Axis axis = null;
        if(!c_down && !c_up && !c_north && !c_south && c_east && c_west) axis = Direction.Axis.X;
        if(c_down && c_up && !c_north && !c_south && !c_east && !c_west) axis = Direction.Axis.Y;
        if(!c_down && !c_up && c_north && c_south && !c_east && !c_west) axis = Direction.Axis.Z;
        if (axis != null) {
            BlockState newState = axisTubeState
                    .setValue(AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED,
                            blockState.getValue(BlockStateProperties.WATERLOGGED));
            CCMisc.setBlockAndUpdateKeepAir(world, blockPos, newState);
        }
    }
}
