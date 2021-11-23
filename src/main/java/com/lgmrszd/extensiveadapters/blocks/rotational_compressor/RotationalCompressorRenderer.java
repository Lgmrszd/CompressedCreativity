package com.lgmrszd.extensiveadapters.blocks.rotational_compressor;

import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.PartialBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;

public class RotationalCompressorRenderer extends KineticTileEntityRenderer {
    public RotationalCompressorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    protected SuperByteBuffer getRotatedModel(KineticTileEntity te) {
//        return PartialBufferer.getFacing(AllBlockPartials.SHAFT_HALF, te.getBlockState());
        return PartialBufferer.getFacing(AllBlockPartials.SHAFT_HALF, te.getBlockState(), ((Direction)te.getBlockState().getValue(RotationalCompressorBlock.HORIZONTAL_FACING)).getOpposite());
    }
}
