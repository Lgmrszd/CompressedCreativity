package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.lgmrszd.compressedcreativity.index.CCBlockEntities;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HEAT_LEVEL;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HeaterBlock extends Block implements IBE<HeaterBlockEntity> {
    public HeaterBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.NONE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HEAT_LEVEL);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public Class<HeaterBlockEntity> getBlockEntityClass() {
        return HeaterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HeaterBlockEntity> getBlockEntityType() {
        return CCBlockEntities.HEATER.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return CCShapes.HEATER_SHAPE;
    }

    public static void registerHeater() {
        BoilerHeaters.registerHeater(CCBlocks.HEATER.get(), (level, pos, state) -> {
            BlazeBurnerBlock.HeatLevel value = state.getOptionalValue(HEAT_LEVEL).orElse(BlazeBurnerBlock.HeatLevel.NONE);
            if (value == BlazeBurnerBlock.HeatLevel.NONE) {
                return -1;
            }
            if (value == BlazeBurnerBlock.HeatLevel.SEETHING) {
                return 2;
            }
            if (value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
                return 1;
            }
            return 0;
        });
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof HeaterBlockEntity hte) {
            hte.updateHeatExchanger();
        }
    }
}
