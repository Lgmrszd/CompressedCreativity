package com.lgmrszd.compressedcreativity.blocks.air_blower;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.utility.Iterate;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import java.util.Map;

public class AirBlowerBlockStateGenerator {

//    private static ModelFile standartPartialModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String name) {
//        final String location = "block/partials/" + name;
//		return prov.models()
//			.getExistingFile(prov.modLoc(location));
//    }

    public static void blockState(DataGenContext<Block, ? extends AirBlowerBlock> c, RegistrateBlockstateProvider p) {

        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

        for (Direction dir : Iterate.directions) {
            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p))
                    .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .addModel()
                    .condition(AirBlowerBlock.FACING, dir)
                    .end();

            builder.part()
                    .modelFile(AssetLookup.partialBaseModel(c, p, "connector"))
                    .rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 90 : 0)
                    .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
                    .addModel()
                    .condition(AirBlowerBlock.CONNECTION_PROPERTIES[dir.get3DDataValue()], true)
                    .end();
        }
    }
}
