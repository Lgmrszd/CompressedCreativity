package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import com.lgmrszd.compressedcreativity.index.CCBlockPartials;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CompressedAirEngineVisual extends KineticBlockEntityVisual<CompressedAirEngineBlockEntity> {

    protected final RotatingInstance shaft;
    protected final RotatingInstance rotor;
    final Direction direction;

    public CompressedAirEngineVisual(VisualizationContext context, CompressedAirEngineBlockEntity tile, float partialTick) {
        super(context, tile, partialTick);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();

        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT, direction)).createInstance();
        rotor = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(CCBlockPartials.AIR_ENGINE_ROTOR)).createInstance();

        shaft.setup(tile)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.UP, direction.getAxis())
                .setChanged();

        rotor.setup(tile)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, direction)
                .setChanged();
    }

    @Override
    public void update(float partialTick) {
        shaft.setup(blockEntity).setChanged();
        rotor.setup(blockEntity).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(pos, shaft);
        relight(pos, rotor);
    }

    @Override
    public void _delete() {
        shaft.delete();
        rotor.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(rotor);
    }
}
