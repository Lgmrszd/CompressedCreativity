package com.lgmrszd.compressedcreativity.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MACHINES = "machines";
    public static final String CATEGORY_ROTATIONAL_COMPRESSOR = "rotational_compressor";
    public static final String CATEGORY_AIR_BLOWER = "air_blower";

    public static final String CATEGORY_ENGINE = "compressed_air_engine";
    public static final String CATEGORY_CUSTOM_PRESSURE = "custom_pressure";

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_STRESS;
    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_VOLUME;
    public static final ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> ROTATIONAL_COMPRESSOR_PRESSURE_TIER;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_DANGER_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_BASE_PRODUCTION;

    public static final ForgeConfigSpec.IntValue AIR_BLOWER_VOLUME;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_WORK_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_OVERWORK_PRESSURE;
    public static final ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> AIR_BLOWER_PRESSURE_TIER;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_DANGER_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_CRITICAL_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_AIR_USAGE_PER_BAR;


    public static final ForgeConfigSpec.IntValue COMPRESSED_AIR_ENGINE_VOLUME;
    public static final ForgeConfigSpec.IntValue COMPRESSED_AIR_ENGINE_STRESS;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_WORK_PRESSURE;
    public static final ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> COMPRESSED_AIR_ENGINE_PRESSURE_TIER;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_DANGER_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_CRITICAL_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK;

    private static ForgeConfigSpec.IntValue makeVolumeField(int def) {
        return COMMON_BUILDER
                .comment("Air Volume of the machine\n" +
                        "Default value: " + def)
                .defineInRange("volume", def, 0, Integer.MAX_VALUE);
    }

    private static ForgeConfigSpec.DoubleValue makeDangerPressureField(double def) {
        return COMMON_BUILDER
                .comment("Danger Pressure of the machine\n" +
                        "Default value: " + def)
                .defineInRange("danger_pressure", def, 0d, 20d);
    }

    private static ForgeConfigSpec.DoubleValue makeCriticalPressureField(double def) {
        return COMMON_BUILDER
                .comment("""
                        Additional Critical Pressure of the machine.
                        Actual Critical Pressure is the sum of this value and Danger Pressure
                        Default value:\040""" + def)
                .defineInRange("critical_pressure", def, 0d, 20d);
    }

    private static ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> makePressureTierFieldAndPushCategory() {
        ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> pressure_tier = COMMON_BUILDER
                .comment("Pressure Tier of the machine\n" +
                        "All but CUSTOM one match Pressure Tiers from PNC:R")
                .defineEnum("pressure_tier", PressureTierConfig.PressureTierEnum.TIER_ONE);
        COMMON_BUILDER
                .comment("Values from Custom Air Pressure for this machine").push(CATEGORY_CUSTOM_PRESSURE);
        return pressure_tier;
    }

    static {
        COMMON_BUILDER.comment("General Configuration").push(CATEGORY_GENERAL);


        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Machines Configuration").push(CATEGORY_MACHINES);

        COMMON_BUILDER.comment("Rotational Compressor").push(CATEGORY_ROTATIONAL_COMPRESSOR);

        ROTATIONAL_COMPRESSOR_STRESS = COMMON_BUILDER
                .comment("Max Stress Impact of the machine (at 256 rpm)\nDefault value: 2048")
                .defineInRange("stress", 2048, 0, Integer.MAX_VALUE);
        ROTATIONAL_COMPRESSOR_VOLUME = makeVolumeField(5000);
        ROTATIONAL_COMPRESSOR_BASE_PRODUCTION = COMMON_BUILDER
                .comment("How much air this machine produces per tick if it is running at 128 rpm\nDefault value: 10.0")
                .defineInRange("base_production", 10d, 0d, 999999d);

        ROTATIONAL_COMPRESSOR_PRESSURE_TIER = makePressureTierFieldAndPushCategory();
        ROTATIONAL_COMPRESSOR_DANGER_PRESSURE = makeDangerPressureField(5d);
        ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE = makeCriticalPressureField(2d);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Air Blower").push(CATEGORY_AIR_BLOWER);

        AIR_BLOWER_VOLUME = makeVolumeField(500);
        AIR_BLOWER_WORK_PRESSURE = COMMON_BUILDER
                .comment("Pressure required by machine to operate\n" +
                        "Default value: 0.5")
                .defineInRange("work_pressure", 0.5d, 0d, 20d);
        AIR_BLOWER_OVERWORK_PRESSURE = COMMON_BUILDER
                .comment("Pressure required for double processing speed\n" +
                        "Default value: 4.8")
                .defineInRange("overwork_pressure", 4.8d, 0d, 20d);
        AIR_BLOWER_AIR_USAGE_PER_BAR = COMMON_BUILDER
                .comment("Air Usage per Bar (total air usage = this value * current pressure)")
                .defineInRange("air_usage_per_bar", 4d, 0d, 999999d);

        AIR_BLOWER_PRESSURE_TIER = makePressureTierFieldAndPushCategory();
        AIR_BLOWER_DANGER_PRESSURE = makeDangerPressureField(5d);
        AIR_BLOWER_CRITICAL_PRESSURE = makeCriticalPressureField(2d);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Compressed Air Engine").push(CATEGORY_ENGINE);

        COMPRESSED_AIR_ENGINE_STRESS = COMMON_BUILDER
                .comment("Max/Worl Stress Capacity of the machine (at 256 rpm)\n" +
                        "Default value: 1024")
                .defineInRange("stress", 1024, 0, Integer.MAX_VALUE);
        COMPRESSED_AIR_ENGINE_VOLUME = makeVolumeField(1000);
        COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK = COMMON_BUILDER
                .comment("Air usage when working\n" +
                        "Default value: 20.0")
                .defineInRange("air_usage_work", 20d, 0d, 999999d);
        COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE = COMMON_BUILDER
                .comment("Air usage when idle\n" +
                        "Default value: 40.0")
                .defineInRange("air_usage_idle", 40d, 0d, 999999d);

        COMPRESSED_AIR_ENGINE_WORK_PRESSURE = COMMON_BUILDER
                .comment("Pressure required to operate\n" +
                        "Default value: 3.0")
                .defineInRange("work_pressure", 3d, 0d, 20d);

        COMPRESSED_AIR_ENGINE_PRESSURE_TIER = makePressureTierFieldAndPushCategory();
        COMPRESSED_AIR_ENGINE_DANGER_PRESSURE = makeDangerPressureField(5d);
        COMPRESSED_AIR_ENGINE_CRITICAL_PRESSURE = makeCriticalPressureField(2d);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();



        COMMON_SPEC = COMMON_BUILDER.build();
    }
}
