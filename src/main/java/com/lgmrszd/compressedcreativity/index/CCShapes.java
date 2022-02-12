package com.lgmrszd.compressedcreativity.index;


import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;


public class CCShapes {

    private static VoxelShape rotationalCompressorShape() {
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 1, 0.375), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.125, 0.125, 0.375, 0.875, 0.875, 0.875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.3125, 0.375, 0.125, 0.6875, 0.6875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.875, 0.3125, 0.375, 1, 0.6875, 0.6875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0.375, 1, 0.125, 1), IBooleanFunction.OR);

        return shape;
    }

    private static VoxelShape airBlowerShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0, 0, 1, 1, 0.125), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.125, 0.125, 0.125, 0.875, 0.875, 0.875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.3125, 0.3125, 0.875, 0.6875, 0.6875, 1), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.875, 0.3125, 0.3125, 1, 0.6875, 0.6875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0, 0.3125, 0.3125, 0.125, 0.6875, 0.6875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875), IBooleanFunction.OR);
        shape = VoxelShapes.join(shape, VoxelShapes.box(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875), IBooleanFunction.OR);


        return shape;
    }

    public static final VoxelShaper
        ROTATIONAL_COMPRESSOR = new AllShapes.Builder(rotationalCompressorShape()).forHorizontal(Direction.NORTH),
        AIR_BLOWER = new AllShapes.Builder(airBlowerShape()).forDirectional(Direction.NORTH);
}
