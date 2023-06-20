package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

/*
    Original code is taken from EncasedFanTileEntity.java, from Create mod.
    Create is redistributed under the MIT License.
    Source code: https://github.com/Creators-of-Create/Create
*/

import com.jozufozu.flywheel.api.MaterialManager;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

//import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class RotationalCompressorInstance extends KineticBlockEntityInstance<RotationalCompressorBlockEntity> {

    protected final RotatingData shaft;
    protected final RotatingData fan;
    final Direction direction;
    private final Direction opposite;

    public RotationalCompressorInstance(MaterialManager modelManager, RotationalCompressorBlockEntity tile) {
        super(modelManager, tile);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, opposite).createInstance();
        fan = modelManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.ENCASED_FAN_INNER, blockState, opposite)
                .createInstance();

        setup(shaft);
        setup(fan, getFanSpeed());
    }

    private float getFanSpeed() {
        float speed = ((RotationalCompressorBlockEntity)this.blockEntity).getSpeed() * 5;
        if (speed > 0)
            speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = Mth.clamp(speed, -64 * 20, -80);
        return speed;
    }

    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(fan, getFanSpeed());
    }

    @Override
    public void updateLight() {
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, fan);
    }

    @Override
    public void remove() {
        shaft.delete();
        fan.delete();
    }
}
