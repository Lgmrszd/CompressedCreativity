package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerRenderer;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedAdvancedPressureTubeTileEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedPressureTubeTileEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedReinforcedPressureTubeTileEntity;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineInstance;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineRenderer;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineTileEntity;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorInstance;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorRenderer;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorTileEntity;
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


    // TODO: fix Flywheel Instance
    public static final BlockEntityEntry<AdvancedAirBlowerTileEntity> INDUSTRIAL_AIR_BLOWER = REGISTRATE
            .tileEntity("advanced_air_blower", AdvancedAirBlowerTileEntity::new)
//            .instance(() -> AdvancedAirBlowerInstance::new, false)
            .validBlock(CCBlocks.INDUSTRIAL_AIR_BLOWER)
            .renderer(() -> AdvancedAirBlowerRenderer::new)
            .register();

    public static final BlockEntityEntry<BracketedPressureTubeTileEntity> BRACKETED_PRESSURE_TUBE = REGISTRATE
            .tileEntity("bracketed_pressure_tube", BracketedPressureTubeTileEntity::new)
            .validBlock(CCBlocks.BRACKETED_PRESSURE_TUBE)
            .register();

    public static final BlockEntityEntry<BracketedReinforcedPressureTubeTileEntity> BRACKETED_REINFORCED_PRESSURE_TUBE = REGISTRATE
            .tileEntity("bracketed_reinforced_pressure_tube", BracketedReinforcedPressureTubeTileEntity::new)
            .validBlock(CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE)
            .register();

    public static final BlockEntityEntry<BracketedAdvancedPressureTubeTileEntity> BRACKETED_ADVANCED_PRESSURE_TUBE = REGISTRATE
            .tileEntity("bracketed_advanced_pressure_tube", BracketedAdvancedPressureTubeTileEntity::new)
            .validBlock(CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE)
            .register();

    public static void register() {
    }
}
