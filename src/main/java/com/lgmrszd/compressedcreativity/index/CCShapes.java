package com.lgmrszd.compressedcreativity.index;


import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;


public class CCShapes {

    private static VoxelShape rotationalCompressorShape() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 1, 0.375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.375, 0.875, 0.875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.3125, 0.375, 0.125, 0.6875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.3125, 0.375, 1, 0.6875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.375, 1, 0.125, 1), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape airBlowerShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 1, 1, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.125, 0.125, 0.875, 0.875, 0.875), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.125), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.875, 0.3125, 0.3125, 1, 0.6875, 0.6875), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0, 0.3125, 0.3125, 0.125, 0.6875, 0.6875), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.3125, 0.875, 0.3125, 0.6875, 1, 0.6875), BooleanOp.OR);
//        shape = Shapes.join(shape, Shapes.box(0.3125, 0, 0.3125, 0.6875, 0.125, 0.6875), BooleanOp.OR);


        return shape;
    }

    private static VoxelShape compressedAirEngineShapeCore() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.25, 0.875, 0.8125, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0.125, 0.8125, 0.875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.25, 0.8125, 0.1875, 0.6875), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape compressedAirEngineShapeBackEnd() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.6875, 0.875, 0.8125, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0.6875, 0.8125, 0.875, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.6875, 0.8125, 0.1875, 0.875), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape compressedAirEngineShapeBackOpen() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0.6875, 0.8125, 0.875, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.6875, 0.875, 0.8125, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.6875, 0.8125, 0.1875, 1), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape compressedAirEngineShapeFrontEnd() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.1875, 0.0625, 0.8125, 0.8125, 0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0.125, 0.875, 0.8125, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0.125, 0.8125, 0.1875, 0.25), BooleanOp.OR);

        return shape;
    }

    private static VoxelShape compressedAirEngineShapeFrontOpen() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.1875, 0, 0.875, 0.8125, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.125, 0, 0.8125, 0.1875, 0.25), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.1875, 0.8125, 0, 0.8125, 0.875, 0.125), BooleanOp.OR);

        return shape;
    }

    public static final VoxelShaper
        ROTATIONAL_COMPRESSOR = new AllShapes.Builder(rotationalCompressorShape()).forHorizontal(Direction.NORTH),
        AIR_BLOWER = new AllShapes.Builder(airBlowerShape()).forDirectional(Direction.SOUTH),
        COMPRESSED_AIR_ENGINE_CORE = new AllShapes.Builder(compressedAirEngineShapeCore()).forDirectional(Direction.NORTH),
        COMPRESSED_AIR_ENGINE_BACK_END = new AllShapes.Builder(compressedAirEngineShapeBackEnd()).forDirectional(Direction.NORTH),
        COMPRESSED_AIR_ENGINE_BACK_OPEN = new AllShapes.Builder(compressedAirEngineShapeBackOpen()).forDirectional(Direction.NORTH),
        COMPRESSED_AIR_ENGINE_FRONT_END = new AllShapes.Builder(compressedAirEngineShapeFrontEnd()).forDirectional(Direction.NORTH),
        COMPRESSED_AIR_ENGINE_FRONT_OPEN = new AllShapes.Builder(compressedAirEngineShapeFrontOpen()).forDirectional(Direction.NORTH);
}
