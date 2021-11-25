package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorInstance;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorRenderer;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorTileEntity;
import com.simibubi.create.content.contraptions.base.HorizontalHalfShaftInstance;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.TileEntityEntry;

public class CCTileEntities {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate();

    public static final TileEntityEntry<RotationalCompressorTileEntity> ROTATIONAL_COMPRESSOR = REGISTRATE
            .tileEntity("rotational_compressor", RotationalCompressorTileEntity::new)
            .instance(() -> RotationalCompressorInstance::new)
            .validBlock(CCBlocks.ROTATIONAL_COMPRESSOR)
            .renderer(() -> RotationalCompressorRenderer::new)
            .register();

    public static void register() {
    }
}
