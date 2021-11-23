package com.lgmrszd.compressedcreativity.config;

import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MACHINES = "machines";
    public static final String CATEGORY_ROTATIONAL_COMPRESSOR = "rotational_compressor";

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_STRESS;
    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_VOLUME;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_DANGER_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_BASE_PRODUCTION;

    static {
        COMMON_BUILDER.comment("General Configuration").push(CATEGORY_GENERAL);


        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Machines Configuration").push(CATEGORY_MACHINES);

        COMMON_BUILDER.comment("Rotational Compressor").push(CATEGORY_ROTATIONAL_COMPRESSOR);

        ROTATIONAL_COMPRESSOR_STRESS = COMMON_BUILDER.comment("Max stress Impact of the machine (at 256 rpm)\nDefault value: 2048").defineInRange("stress", 2048, 0, Integer.MAX_VALUE);
        ROTATIONAL_COMPRESSOR_VOLUME = COMMON_BUILDER.comment("Air Volume of the machine\nDefault value: 5000").defineInRange("volume", 5000, 0, Integer.MAX_VALUE);
        ROTATIONAL_COMPRESSOR_DANGER_PRESSURE = COMMON_BUILDER.comment("Danger Pressure of the machine\nDefault value: 5.0").defineInRange("danger_pressure", 5d, 0d, 20d);
        ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE = COMMON_BUILDER.comment("Additional Critical Pressure of the machine.\nActual Critical Pressure is the sum of this value and Danger Pressure\nDefault value: 2.0").defineInRange("critical_pressure", 2d, 0d, 20d);
        ROTATIONAL_COMPRESSOR_BASE_PRODUCTION = COMMON_BUILDER.comment("How much air this machine produces per tick if it is running at 128 rpm?\nDefault value: 10.0").defineInRange("base_production", 10d, 0d, 999999d);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();



        COMMON_SPEC = COMMON_BUILDER.build();
    }
}
