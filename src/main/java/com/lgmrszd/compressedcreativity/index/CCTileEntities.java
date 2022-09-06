package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube.AxisPressureTubeTileEntity;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineInstance;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineRenderer;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineTileEntity;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorInstance;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorRenderer;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorTileEntity;
import com.simibubi.create.content.contraptions.base.HorizontalHalfShaftInstance;
import com.simibubi.create.content.contraptions.relays.encased.ShaftInstance;
import com.simibubi.create.content.contraptions.relays.encased.ShaftRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class CCTileEntities {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate();

    public static final BlockEntityEntry<RotationalCompressorTileEntity> ROTATIONAL_COMPRESSOR = REGISTRATE
            .tileEntity("rotational_compressor", RotationalCompressorTileEntity::new)
            .instance(() -> RotationalCompressorInstance::new, false)
            .validBlock(CCBlocks.ROTATIONAL_COMPRESSOR)
            .renderer(() -> RotationalCompressorRenderer::new)
            .register();

    public static final BlockEntityEntry<CompressedAirEngineTileEntity> COMPRESSED_AIR_ENGINE = REGISTRATE
            .tileEntity("compressed_air_engine", CompressedAirEngineTileEntity::new)
            .instance(() -> CompressedAirEngineInstance::new, false)
            .validBlock(CCBlocks.COMPRESSED_AIR_ENGINE)
            .renderer(() -> CompressedAirEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<AirBlowerTileEntity> AIR_BLOWER = REGISTRATE
            .tileEntity("air_blower", AirBlowerTileEntity::new)
            .validBlock(CCBlocks.AIR_BLOWER)
            .register();

    public static final BlockEntityEntry<AxisPressureTubeTileEntity> AXIS_PRESSURE_TUBE = REGISTRATE
            .tileEntity("axis_pressure_tube", AxisPressureTubeTileEntity::new)
            .validBlock(CCBlocks.AXIS_PRESSURE_TUBE)
            .register();

    public static void register() {
    }
}
