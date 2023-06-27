package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerRenderer;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedAdvancedPressureTubeBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedPressureTubeBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedReinforcedPressureTubeBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineInstance;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineRenderer;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlockEntity;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorInstance;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorRenderer;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.REGISTRATE;

public class CCBlockEntities {

    public static final BlockEntityEntry<RotationalCompressorBlockEntity> ROTATIONAL_COMPRESSOR = REGISTRATE
            .blockEntity("rotational_compressor", RotationalCompressorBlockEntity::new)
            .instance(() -> RotationalCompressorInstance::new, false)
            .validBlock(CCBlocks.ROTATIONAL_COMPRESSOR)
            .renderer(() -> RotationalCompressorRenderer::new)
            .register();

    public static final BlockEntityEntry<CompressedAirEngineBlockEntity> COMPRESSED_AIR_ENGINE = REGISTRATE
            .blockEntity("compressed_air_engine", CompressedAirEngineBlockEntity::new)
            .instance(() -> CompressedAirEngineInstance::new, false)
            .validBlock(CCBlocks.COMPRESSED_AIR_ENGINE)
            .renderer(() -> CompressedAirEngineRenderer::new)
            .register();

    public static final BlockEntityEntry<AirBlowerBlockEntity> AIR_BLOWER = REGISTRATE
            .blockEntity("air_blower", AirBlowerBlockEntity::new)
            .validBlock(CCBlocks.AIR_BLOWER)
            .register();


    // TODO: fix Flywheel Instance
    public static final BlockEntityEntry<AdvancedAirBlowerBlockEntity> INDUSTRIAL_AIR_BLOWER = REGISTRATE
            .blockEntity("advanced_air_blower", AdvancedAirBlowerBlockEntity::new)
//            .instance(() -> AdvancedAirBlowerInstance::new, false)
            .validBlock(CCBlocks.INDUSTRIAL_AIR_BLOWER)
            .renderer(() -> AdvancedAirBlowerRenderer::new)
            .register();

    public static final BlockEntityEntry<BracketedPressureTubeBlockEntity> BRACKETED_PRESSURE_TUBE = REGISTRATE
            .blockEntity("bracketed_pressure_tube", BracketedPressureTubeBlockEntity::new)
            .validBlock(CCBlocks.BRACKETED_PRESSURE_TUBE)
            .register();

    public static final BlockEntityEntry<BracketedReinforcedPressureTubeBlockEntity> BRACKETED_REINFORCED_PRESSURE_TUBE = REGISTRATE
            .blockEntity("bracketed_reinforced_pressure_tube", BracketedReinforcedPressureTubeBlockEntity::new)
            .validBlock(CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE)
            .register();

    public static final BlockEntityEntry<BracketedAdvancedPressureTubeBlockEntity> BRACKETED_ADVANCED_PRESSURE_TUBE = REGISTRATE
            .blockEntity("bracketed_advanced_pressure_tube", BracketedAdvancedPressureTubeBlockEntity::new)
            .validBlock(CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE)
            .register();

    public static void register() {
    }
}
