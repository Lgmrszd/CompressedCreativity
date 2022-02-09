package com.lgmrszd.compressedcreativity.multiblock_helper;

import java.util.Arrays;

public class ShapeHelper {

    public static class Shape {

        private boolean[][][] shape;
        private final int dim_x, dim_y, dim_z;

        public Shape(int x, int y, int z) {
            shape = new boolean[x][y][z];
            dim_x = x;
            dim_y = y;
            dim_z = z;
            for (int in_x = 0; in_x < x; in_x++)
                for (int in_y = 0; in_y < y; in_y++)
                    for (int in_z = 0; in_z < z; in_z++)
                        shape[in_x][in_y][in_z] = true;
        }

        public int getDim_x() {
            return dim_x;
        }

        public int getDim_y() {
            return dim_y;
        }

        public int getDim_z() {
            return dim_z;
        }

        public boolean getAt(int x, int y, int z) {
            return shape[x][y][z];
        }

        public void setAt(int x, int y, int z, boolean val){
            shape[x][y][z] = val;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (!(o instanceof Shape))
                return false;
            Shape s = (Shape) o;
            return Arrays.deepEquals(shape, s.shape);
        }
    }

//    public static
}
