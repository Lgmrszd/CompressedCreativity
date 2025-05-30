package com.lgmrszd.compressedcreativity.config;

import com.google.common.base.Supplier;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlock;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import net.minecraft.world.level.block.Block;

import java.util.function.DoubleSupplier;

public class CCStressProvider {

    public DoubleSupplier getImpact(Block block) {
        if (block instanceof RotationalCompressorBlock) {
            return (() -> CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get() / 256.0);
        }
        return (() -> 0.0);
    }

    public DoubleSupplier getCapacity(Block block) {
        if (block instanceof CompressedAirEngineBlock) {
            return (() -> CommonConfig.COMPRESSED_AIR_ENGINE_STRESS.get() / 256.0);
        }
        return (() -> 0.0);
    }
}
