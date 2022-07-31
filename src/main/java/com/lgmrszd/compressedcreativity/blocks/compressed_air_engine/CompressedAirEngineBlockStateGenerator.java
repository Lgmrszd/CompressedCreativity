package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

public class CompressedAirEngineBlockStateGenerator {
    public static void blockState(DataGenContext<Block, CompressedAirEngineBlock> c, RegistrateBlockstateProvider p) {

        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

        for (Direction dir : Iterate.horizontalDirections) {
            int rotY = (((int) dir.toYRot()) + 180) % 360;
            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p))
                    .rotationY(rotY)
                    .addModel()
                    .condition(CompressedAirEngineBlock.HORIZONTAL_FACING, dir)
                    .end();

            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p, "back_end"))
                    .rotationY(rotY)
                    .addModel()
                    .condition(CompressedAirEngineBlock.HORIZONTAL_FACING, dir)
                    .condition(CompressedAirEngineBlock.BACK, true)
                    .end();

            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p, "back_open"))
                    .rotationY(rotY)
                    .addModel()
                    .condition(CompressedAirEngineBlock.HORIZONTAL_FACING, dir)
                    .condition(CompressedAirEngineBlock.BACK, false)
                    .end();

            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p, "front_end"))
                    .rotationY(rotY)
                    .addModel()
                    .condition(CompressedAirEngineBlock.HORIZONTAL_FACING, dir)
                    .condition(CompressedAirEngineBlock.FRONT, true)
                    .end();

            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p, "front_open"))
                    .rotationY(rotY)
                    .addModel()
                    .condition(CompressedAirEngineBlock.HORIZONTAL_FACING, dir)
                    .condition(CompressedAirEngineBlock.FRONT, false)
                    .end();
        }

        builder.part()
                .modelFile(AssetLookup.partialBaseModel(c, p, "up_closed"))
                .addModel()
                .condition(CompressedAirEngineBlock.UP, false)
                .end();
        builder.part()
                .modelFile(AssetLookup.partialBaseModel(c, p, "up_connected"))
                .addModel()
                .condition(CompressedAirEngineBlock.UP, true)
                .end();

    }
}
