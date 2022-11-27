package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

public class BracketedPressureTubeBlockStateGenerator {

    public static ModelFile tubeCore(RegistrateBlockstateProvider prov) {
        return prov.models()
                .getExistingFile(prov.modLoc("block/pressure_tube_core"));
    }


    public static void blockState(DataGenContext<Block, ? extends BracketedPressureTubeBlock> c, RegistrateBlockstateProvider p) {
        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

        String pnc_path = "pneumaticcraft:block/" + c.getName().substring(10) + "_connected"; // cut off "bracketed_"

        builder.part()
                .modelFile(tubeCore(p))
                .addModel()
                .end();

        for (Direction.Axis axis : Direction.Axis.VALUES) {
            builder.part()
                    .modelFile(new ModelFile.UncheckedModelFile(pnc_path))
                    .rotationX(
                            axis == Direction.Axis.Y ? 180 : 90
                    )
                    .rotationY(
                            axis == Direction.Axis.X ? 270 : 0
                    )
                    .addModel()
                    .condition(BracketedPressureTubeBlock.AXIS, axis)
                    .end();

            builder.part()
                    .modelFile(new ModelFile.UncheckedModelFile(pnc_path))
                    .rotationX(
                            axis == Direction.Axis.Y ? 0 : 90
                    )
                    .rotationY(
                            axis == Direction.Axis.X ? 90 :
                                    axis == Direction.Axis.Y ? 0 : 180
                    )
                    .addModel()
                    .condition(BracketedPressureTubeBlock.AXIS, axis)
                    .end();
        }
    }
}