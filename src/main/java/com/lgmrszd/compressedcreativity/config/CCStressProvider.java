package com.lgmrszd.compressedcreativity.config;

import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlock;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import com.simibubi.create.foundation.block.BlockStressValues;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class CCStressProvider implements BlockStressValues.IStressValueProvider {
    @Override
    public double getImpact(Block block) {
        if (block instanceof RotationalCompressorBlock) {
            return CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get() / 256.0;
        }
        return 0;
    }

    @Override
    public double getCapacity(Block block) {
        if (block instanceof CompressedAirEngineBlock) {
            return CommonConfig.COMPRESSED_AIR_ENGINE_STRESS.get() / 256.0;
        }
        return 0;
    }

    @Override
    public boolean hasImpact(Block block) {
        return block instanceof RotationalCompressorBlock;
    }

    @Override
    public boolean hasCapacity(Block block) {
        return block instanceof CompressedAirEngineBlock;
    }

    @Nullable
    @Override
    public Couple<Integer> getGeneratedRPM(Block block) {
        return null;
    }
}
