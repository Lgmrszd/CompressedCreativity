package com.lgmrszd.compressedcreativity.config;

import com.simibubi.create.content.kinetics.BlockStressValues;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;

public class CommonConfig {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_MACHINES = "machines";
    public static final String CATEGORY_ROTATIONAL_COMPRESSOR = "rotational_compressor";
    public static final String CATEGORY_AIR_BLOWER = "air_blower";
    public static final String CATEGORY_INDUSTRIAL_AIR_BLOWER = "industrial_air_blower";
    public static final String CATEGORY_ENGINE = "compressed_air_engine";
    public static final String CATEGORY_HEATER = "heater";
    public static final String CATEGORY_CUSTOM_PRESSURE = "custom_pressure";

    public static final Map<String, ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum>>
            MACHINE_PRESSURE_TIERS = new HashMap<>();

    public static final Map<String, ForgeConfigSpec.DoubleValue>
            CUSTOM_DANGER_PRESSURE = new HashMap<>();

    public static final Map<String, ForgeConfigSpec.DoubleValue>
            CUSTOM_CRITICAL_PRESSURE = new HashMap<>();

    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;

    public static final ForgeConfigSpec.BooleanValue BACKTANK_COMPAT_ITEM;

    public static final ForgeConfigSpec.BooleanValue BACKTANK_COMPAT_BLOCK;

    public static final ForgeConfigSpec.BooleanValue CHESTPLATE_COMPAT;

    public static final ForgeConfigSpec.DoubleValue CHESTPLATE_MIN_PRESSURE;
//    public static final ForgeConfigSpec.IntValue BACKTANK_VOLUME;

    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_STRESS;
    public static final ForgeConfigSpec.IntValue ROTATIONAL_COMPRESSOR_VOLUME;
    public static final ForgeConfigSpec.DoubleValue ROTATIONAL_COMPRESSOR_BASE_PRODUCTION;

    public static final ForgeConfigSpec.IntValue AIR_BLOWER_VOLUME;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_WORK_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_OVERWORK_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue AIR_BLOWER_AIR_USAGE_PER_BAR;

    public static final ForgeConfigSpec.IntValue INDUSTRIAL_AIR_BLOWER_VOLUME;


    public static final ForgeConfigSpec.IntValue COMPRESSED_AIR_ENGINE_VOLUME;
    public static final ForgeConfigSpec.IntValue COMPRESSED_AIR_ENGINE_STRESS;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_WORK_PRESSURE;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE;
    public static final ForgeConfigSpec.DoubleValue COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK;

    public static final ForgeConfigSpec.DoubleValue HEATER_THERMAL_CAPACITY;
    public static final ForgeConfigSpec.DoubleValue HEATER_THERMAL_RESISTANCE;
    public static final ForgeConfigSpec.IntValue HEATER_TEMPERATURE_PASSIVE;
    public static final ForgeConfigSpec.IntValue HEATER_TEMPERATURE_KINDLED;
    public static final ForgeConfigSpec.IntValue HEATER_TEMPERATURE_SEETHING;
    public static final ForgeConfigSpec.IntValue HEATER_STARTING_TEMPERATURE;
    public static final ForgeConfigSpec.DoubleValue HEATER_TEMPERATURE_COEFFICIENT;

    private static ForgeConfigSpec.IntValue makeVolumeField(int def) {
        return COMMON_BUILDER
                .comment("Air Volume of the machine\n" +
                        "Default value: " + def)
                .defineInRange("volume", def, 0, Integer.MAX_VALUE);
    }

    private static void makePressureFields(String name, PressureTierConfig.PressureTierEnum pressureTier) {
        ForgeConfigSpec.EnumValue<PressureTierConfig.PressureTierEnum> pressure_tier = COMMON_BUILDER
                .comment("Pressure Tier of the machine\n" +
                        "All but CUSTOM one match Pressure Tiers from PNC:R")
                .defineEnum("pressure_tier", pressureTier);
        COMMON_BUILDER
                .comment("Values from Custom Air Pressure for this machine").push(CATEGORY_CUSTOM_PRESSURE);
        ForgeConfigSpec.DoubleValue dangerPressure = COMMON_BUILDER
                .comment("Danger Pressure of the machine\n" +
                        "Default value: " + 5d)
                .defineInRange("danger_pressure", 5d, 0d, 20d);
        ForgeConfigSpec.DoubleValue criticalPressure = COMMON_BUILDER
                .comment("""
                        Additional Critical Pressure of the machine.
                        Actual Critical Pressure is the sum of this value and Danger Pressure
                        Default value:\040""" + 2d)
                .defineInRange("critical_pressure", 2d, 0d, 20d);
        COMMON_BUILDER.pop();

        MACHINE_PRESSURE_TIERS.put(name, pressure_tier);
        CUSTOM_DANGER_PRESSURE.put(name, dangerPressure);
        CUSTOM_CRITICAL_PRESSURE.put(name, criticalPressure);
    }

    private static void makePressureFields(String name) {
        makePressureFields(name, PressureTierConfig.PressureTierEnum.TIER_ONE);
    }

    static {
        COMMON_BUILDER.comment("General Configuration").push(CATEGORY_GENERAL);

        BACKTANK_COMPAT_ITEM = COMMON_BUILDER
                .comment("If Copper Backtank has pneumatic capability in item form")
                .define("backtank_compat_item", true);

        BACKTANK_COMPAT_BLOCK = COMMON_BUILDER
                .comment("If Copper Backtank has pneumatic capability in block form")
                .define("backtank_compat_block", true);

        CHESTPLATE_COMPAT = COMMON_BUILDER
                .comment("If Pneumatic Chestplate (with charging upgrade) can provide air to Create tools")
                .define("chestplate_compat", true);

        CHESTPLATE_MIN_PRESSURE = COMMON_BUILDER
                .comment("Minimal Pneumatic Chestplate pressure required to act as Copper Backtank")
                .defineInRange("chestplate_min_pressure", 2.0, 0.0, 9.0);

//        BACKTANK_VOLUME = COMMON_BUILDER
//                .comment("Air volume of Copper Backtank that is used in calculations of air usage of Create tools",
//                        "(air used = this / uses per tank)")
//                .defineInRange("backtank_volume", 56000, 1000, 280000);

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

        makePressureFields(PressureTierConfig.CustomTier.ROTATIONAL_COMPRESSOR_TIER.getKey());

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

        makePressureFields(PressureTierConfig.CustomTier.AIR_BLOWER_TIER.getKey());

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Industrial Air Blower").push(CATEGORY_INDUSTRIAL_AIR_BLOWER);

        INDUSTRIAL_AIR_BLOWER_VOLUME = makeVolumeField(1000);
        makePressureFields(PressureTierConfig.CustomTier.INDUSTRIAL_AIR_BLOWER_TIER.getKey(), PressureTierConfig.PressureTierEnum.TIER_ONE_HALF);

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
                .defineInRange("air_usage_work", 40d, 0d, 999999d);
        COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE = COMMON_BUILDER
                .comment("Air usage when idle\n" +
                        "Default value: 40.0")
                .defineInRange("air_usage_idle", 80d, 0d, 999999d);

        COMPRESSED_AIR_ENGINE_WORK_PRESSURE = COMMON_BUILDER
                .comment("Pressure required to operate\n" +
                        "Default value: 3.0")
                .defineInRange("work_pressure", 3d, 0d, 20d);

        makePressureFields(PressureTierConfig.CustomTier.COMPRESSED_AIR_ENGINE_TIER.getKey());

        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Heater").push(CATEGORY_HEATER);

        HEATER_STARTING_TEMPERATURE = COMMON_BUILDER
                .comment("At what temperature the heater will start cooling down")
                .defineInRange("starting_temperature", 100, 0, Integer.MAX_VALUE);

        HEATER_TEMPERATURE_COEFFICIENT = COMMON_BUILDER
                .comment("Temperature coefficient increase, this much heat is consumed for each degree over starting temperature")
                .defineInRange("temperature_coefficient", 0.1, 0, 100);

        HEATER_TEMPERATURE_PASSIVE = COMMON_BUILDER
                .comment("At what temperature the heater will be considered as passive heat source for Steam Engine (like a campfire)")
                .defineInRange("temperature_passive", 100, 0, Integer.MAX_VALUE);

        HEATER_TEMPERATURE_KINDLED = COMMON_BUILDER
                .comment("At what temperature the heater will be considered as active heat source for Steam Engine and Basin recipes (like a fueled Blaze Burner)")
                .defineInRange("temperature_kindled", 200, 0, Integer.MAX_VALUE);

        HEATER_TEMPERATURE_SEETHING = COMMON_BUILDER
                .comment("At what temperature the heater will be considered as seething (like a Blaze Burner fed with Blaze Cake)")
                .defineInRange("temperature_seething", 300, 0, Integer.MAX_VALUE);

        HEATER_THERMAL_CAPACITY = COMMON_BUILDER
                .comment("Thermal capacity of a Heater")
                .defineInRange("thermal_capacity", 2., 0, 100);

        HEATER_THERMAL_RESISTANCE = COMMON_BUILDER
                .comment("Thermal resistance of a Heater")
                .defineInRange("thermal_resistance", 1., 0, 100);

        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_SPEC = COMMON_BUILDER.build();

        CCStressProvider stressProvider = new CCStressProvider();
        BlockStressValues.registerProvider(MOD_ID, stressProvider);
    }
}
