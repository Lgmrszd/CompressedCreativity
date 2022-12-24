package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.jozufozu.flywheel.api.MaterialManager;
import com.lgmrszd.compressedcreativity.index.CCBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileInstance;
import com.simibubi.create.content.contraptions.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class CompressedAirEngineInstance extends KineticTileInstance<CompressedAirEngineTileEntity> {

    protected final RotatingData shaft;
    protected final RotatingData rotor;
    final Direction direction;

    public CompressedAirEngineInstance(MaterialManager modelManager, CompressedAirEngineTileEntity tile) {
        super(modelManager, tile);

        direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();

//        shaft = getRotatingMaterial().getModel(AllBlockPartials.SHAFT_HALF, blockState, opposite).createInstance();
        shaft = setup(getRotatingMaterial().getModel(shaft()).createInstance());
        rotor = modelManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(CCBlockPartials.AIR_ENGINE_ROTOR, blockState, direction)
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
        relight(pos, shaft);
        relight(pos, rotor);
    }

    @Override
    public void remove() {
        shaft.delete();
        rotor.delete();
    }
}
