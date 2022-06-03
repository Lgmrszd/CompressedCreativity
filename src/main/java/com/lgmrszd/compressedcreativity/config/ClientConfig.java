package com.lgmrszd.compressedcreativity.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final String CATEGORY_UPGRADES = "upgrades";
    public static final String CATEGORY_MECHANICAL_VISOR = "mechanical_visor";

    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.EnumValue<MechanicalVisorConfig.TooltipMode> MECHANICAL_VISOR_TOOLTIP_MODE;
    public static final ForgeConfigSpec.EnumValue<MechanicalVisorConfig.BlockTrackerMode> MECHANICAL_VISOR_BLOCK_TRACKER_MODE;

    static {

        CLIENT_BUILDER.comment("Upgrades settings").push(CATEGORY_UPGRADES);
        CLIENT_BUILDER.comment("Mechanical visor").push(CATEGORY_MECHANICAL_VISOR);
        MECHANICAL_VISOR_TOOLTIP_MODE = CLIENT_BUILDER.comment("Tooltip mode").defineEnum("tooltip_mode", MechanicalVisorConfig.TooltipMode.CLASSIC);
        MECHANICAL_VISOR_BLOCK_TRACKER_MODE = CLIENT_BUILDER.comment("Block Tracker mode").defineEnum("block_tracker_mode", MechanicalVisorConfig.BlockTrackerMode.ALL);
        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.pop();

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }
}
