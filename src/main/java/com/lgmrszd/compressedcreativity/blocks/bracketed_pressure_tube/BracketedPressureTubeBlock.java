package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlockItem;
import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.content.contraptions.wrench.IWrenchableWithBracket;
import com.simibubi.create.content.schematics.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.ItemRequirement;
import com.simibubi.create.foundation.block.ITE;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.tterrag.registrate.util.entry.BlockEntry;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.PNCBlockStateProperties;
import me.desht.pneumaticcraft.api.block.PressureTubeConnection;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import me.desht.pneumaticcraft.common.block.PressureTubeBlock;
import me.desht.pneumaticcraft.common.block.entity.PressureTubeBlockEntity;
import me.desht.pneumaticcraft.common.core.ModBlocks;
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
        ITE<BracketedPressureTubeTileEntity>, IWrenchableWithBracket, SimpleWaterloggedBlock, ISpecialBlockItemRequirement {

    public BracketedPressureTubeBlock(Properties properties) {
        super(properties);
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
            if (oldBlockState.getBlock() == CCBlocks.BRACKETED_PRESSURE_TUBE.get())
                newBlockState = ModBlocks.PRESSURE_TUBE.get().defaultBlockState();
            else if (oldBlockState.getBlock() == CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE.get())
                newBlockState = ModBlocks.REINFORCED_PRESSURE_TUBE.get().defaultBlockState();
            else if (oldBlockState.getBlock() == CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE.get())
                newBlockState = ModBlocks.ADVANCED_PRESSURE_TUBE.get().defaultBlockState();
            else return true;
            newBlockState = newBlockState
                    .setValue(BlockStateProperties.WATERLOGGED,
                            oldBlockState.getValue(BlockStateProperties.WATERLOGGED));
            Direction.Axis axis = oldBlockState.getValue(AXIS);
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
        BracketedTileEntityBehaviour behaviour = TileEntityBehaviour.get(world, pos, BracketedTileEntityBehaviour.TYPE);
        if (behaviour == null)
            return Optional.empty();
        BlockState bracket = behaviour.removeBracket(inOnReplacedContext);
        if (bracket == null)
            return Optional.empty();
        return Optional.of(new ItemStack(bracket.getBlock()));
    }

    @Override
    public Class<BracketedPressureTubeTileEntity> getTileEntityClass() {
        return BracketedPressureTubeTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends BracketedPressureTubeTileEntity> getTileEntityType() {
        return CCTileEntities.BRACKETED_PRESSURE_TUBE.get();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false)
                : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        if (state.getBlock() == CCBlocks.BRACKETED_PRESSURE_TUBE.get())
            return new ItemStack(ModBlocks.PRESSURE_TUBE.get());
        else if (state.getBlock() == CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE.get())
            return new ItemStack(ModBlocks.REINFORCED_PRESSURE_TUBE.get());
        else if (state.getBlock() == CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE.get())
            return new ItemStack(ModBlocks.ADVANCED_PRESSURE_TUBE.get());
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @SubscribeEvent
    public static void usingBracketOnPressureTube(PlayerInteractEvent.RightClickBlock event) {
        ItemStack item = event.getItemStack();
        Level world = event.getWorld();
        BlockPos blockPos = event.getPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!(item.getItem() instanceof BracketBlockItem)) return;
        if (!(blockState.getBlock() instanceof PressureTubeBlock)) return;
        BlockEntry<? extends BracketedPressureTubeBlock> axisTube =
                (blockState.getBlock() == ModBlocks.PRESSURE_TUBE.get()) ? CCBlocks.BRACKETED_PRESSURE_TUBE :
                        (blockState.getBlock() == ModBlocks.REINFORCED_PRESSURE_TUBE.get()) ? CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE : CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE;
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
            BlockState newState = axisTube.getDefaultState()
                    .setValue(AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED,
                            blockState.getValue(BlockStateProperties.WATERLOGGED));
            CCMisc.setBlockAndUpdateKeepAir(world, blockPos, newState);
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity te) {
        if (state.getBlock() == CCBlocks.BRACKETED_PRESSURE_TUBE.get())
            return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    List.of(new ItemStack(ModBlocks.PRESSURE_TUBE.get())));
        else if (state.getBlock() == CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE.get())
            return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    List.of(new ItemStack(ModBlocks.REINFORCED_PRESSURE_TUBE.get())));
        else if (state.getBlock() == CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE.get())
            return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME,
                    List.of(new ItemStack(ModBlocks.ADVANCED_PRESSURE_TUBE.get())));
        else return ItemRequirement.INVALID;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        if ((state.hasBlockEntity() ? level.getBlockEntity(pos) : null) instanceof BracketedPressureTubeTileEntity tte) {
            tte.updateAirHandler();
        }
    }
}
