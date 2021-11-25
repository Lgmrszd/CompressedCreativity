package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

/*
    Original code is taken from EncasedFanTileEntity.java, from Create mod.
    Create is redistributed under the MIT License.
    Source code: https://github.com/Creators-of-Create/Create
*/

import com.jozufozu.flywheel.backend.material.MaterialManager;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class RotationalCompressorInstance extends KineticTileInstance<RotationalCompressorTileEntity> {

    protected final RotatingData shaft;
    protected final RotatingData fan;
    final Direction direction;
    private final Direction opposite;

    public RotationalCompressorInstance(MaterialManager<?> modelManager, RotationalCompressorTileEntity tile) {
        super(modelManager, tile);

        direction = blockState.getValue(HORIZONTAL_FACING);

        opposite = direction.getOpposite();
        shaft = getRotatingMaterial().getModel(AllBlockPartials.SHAFT_HALF, blockState, opposite).createInstance();
        fan = modelManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllBlockPartials.ENCASED_FAN_INNER, blockState, opposite)
                .createInstance();

        setup(shaft);
        setup(fan, getFanSpeed());
    }

    private float getFanSpeed() {
        float speed = tile.getSpeed() * 5;
        if (speed > 0)
            speed = MathHelper.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = MathHelper.clamp(speed, -64 * 20, -80);
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
