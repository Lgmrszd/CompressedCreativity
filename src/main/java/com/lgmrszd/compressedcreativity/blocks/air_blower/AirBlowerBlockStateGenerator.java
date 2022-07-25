package com.lgmrszd.compressedcreativity.blocks.air_blower;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.repack.registrate.providers.DataGenContext;
import com.simibubi.create.repack.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import java.util.Map;

public class AirBlowerBlockStateGenerator {

    private static ModelFile standartPartialModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String name) {
        final String location = "block/partials/" + name;
		return prov.models()
			.getExistingFile(prov.modLoc(location));
    }

    public static void blockState(DataGenContext<Block, AirBlowerBlock> c, RegistrateBlockstateProvider p) {

        Map<Direction, Integer> xRot = Map.of(
                Direction.UP, 270,
                Direction.DOWN, 90
        );

        Map<Direction, Integer> yRot = Map.of(
                Direction.EAST, 90,
                Direction.SOUTH, 180,
                Direction.WEST, 270
        );

        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

        for (Direction dir : Iterate.directions) {
            ConfiguredModel.Builder<MultiPartBlockStateBuilder.PartBuilder> part = builder.part()
                    .modelFile(AssetLookup.standardModel(c, p));
            if (xRot.containsKey(dir))
                part = part.rotationX(xRot.get(dir));
            if (yRot.containsKey(dir))
                part = part.rotationY(yRot.get(dir));
            part.addModel()
                    .condition(AirBlowerBlock.FACING, dir)
                    .end();
        }

        builder.part()
            .modelFile(standartPartialModel(c, p, "air_blower_tube"))
            .addModel()
            .condition(AirBlowerBlock.UP, true)
            .end();

        builder.part()
                .modelFile(standartPartialModel(c, p, "air_blower_tube"))
                .rotationX(180)
                .addModel()
                .condition(AirBlowerBlock.DOWN, true)
                .end();

        builder.part()
                .modelFile(standartPartialModel(c, p, "air_blower_tube"))
                .rotationX(90)
                .addModel()
                .condition(AirBlowerBlock.NORTH, true)
                .end();

        builder.part()
                .modelFile(standartPartialModel(c, p, "air_blower_tube"))
                .rotationX(90)
                .rotationY(90)
                .addModel()
                .condition(AirBlowerBlock.EAST, true)
                .end();

        builder.part()
                .modelFile(standartPartialModel(c, p, "air_blower_tube"))
                .rotationX(90)
                .rotationY(180)
                .addModel()
                .condition(AirBlowerBlock.SOUTH, true)
                .end();

        builder.part()
                .modelFile(standartPartialModel(c, p, "air_blower_tube"))
                .rotationX(90)
                .rotationY(270)
                .addModel()
                .condition(AirBlowerBlock.WEST, true)
                .end();
    }
}
