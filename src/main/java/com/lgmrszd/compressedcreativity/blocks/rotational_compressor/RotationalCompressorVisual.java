package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

/*
    Original code is taken from EncasedFanTileEntity.java, from Create mod.
    Create is redistributed under the MIT License.
    Source code: https://github.com/Creators-of-Create/Create
*/

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class RotationalCompressorVisual extends KineticBlockEntityVisual<RotationalCompressorBlockEntity> {

    protected final RotatingInstance shaft;
    protected final RotatingInstance fan;
    final Direction direction;
    private final Direction opposite;

    public RotationalCompressorVisual(VisualizationContext context, RotationalCompressorBlockEntity tile, float partialTick) {
        super(context, tile, partialTick);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        opposite = direction.getOpposite();
        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF)).createInstance();
        fan = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.ENCASED_FAN_INNER)).createInstance();

        shaft.setup(tile)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();

        fan.setup(tile, getFanSpeed())
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();
    }

    private float getFanSpeed() {
        float speed = this.blockEntity.getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update(float partialTick) {
        shaft.setup(blockEntity).setChanged();
        fan.setup(blockEntity, getFanSpeed()).setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, fan);
    }

    @Override
    protected void _delete() {
        shaft.delete();
        fan.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(fan);
    }
}
