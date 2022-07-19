package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CompressedAirEngineInstance extends KineticTileInstance<CompressedAirEngineTileEntity> {

    protected final RotatingData shaft;
    protected final RotatingData rotor;
    final Direction direction;
    private final Direction opposite;

    public CompressedAirEngineInstance(MaterialManager modelManager, CompressedAirEngineTileEntity tile) {
        super(modelManager, tile);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        opposite = direction.getOpposite();
//        shaft = getRotatingMaterial().getModel(AllBlockPartials.SHAFT_HALF, blockState, opposite).createInstance();
        shaft = setup(getRotatingMaterial().getModel(shaft()).createInstance());
        rotor = modelManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(BlockPartials.AIR_ENGINE_ROTOR, blockState, opposite)
                .createInstance();
        setup(shaft);
        setup(rotor);
    }

    @Override
    public void update() {
        updateRotation(shaft);
        updateRotation(rotor);
    }

    @Override
    public void updateLight() {
        super.updateLight();
        BlockPos behind = pos.relative(opposite);
        relight(behind, shaft);

        BlockPos inFront = pos.relative(direction);
        relight(inFront, rotor);
    }

    @Override
    public void remove() {
        shaft.delete();
        rotor.delete();
    }
}
