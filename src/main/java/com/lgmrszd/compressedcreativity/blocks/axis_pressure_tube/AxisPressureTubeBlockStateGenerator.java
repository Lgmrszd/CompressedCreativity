package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import java.util.function.Function;

public class AxisPressureTubeBlockStateGenerator {

    public static ModelFile tubeCore(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov) {
        return prov.models()
                .getExistingFile(prov.modLoc("block/" + ctx.getName() + "_core"));
    }


    public static <T extends AxisPressureTubeBlock> void blockState(DataGenContext<Block, T> c, RegistrateBlockstateProvider p) {
        MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

        String pnc_path = "pneumaticcraft:block/" + c.getName().substring(5) + "_connected";

        builder.part()
                .modelFile(tubeCore(c, p))
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
                    .condition(AxisPressureTubeBlock.AXIS, axis)
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
                    .condition(AxisPressureTubeBlock.AXIS, axis)
                    .end();
        }
    }
}