package com.lgmrszd.compressedcreativity.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    public static final String CATEGORY_UPGRADES = "upgrades";
    public static final String CATEGORY_MECHANICAL_VISOR = "mechanical_visor";

    public static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec CLIENT_SPEC;

    public static ModConfigSpec.BooleanValue BLOCK_TRACKER_ADVANCED_CHECK;
    public static final ModConfigSpec.EnumValue<MechanicalVisorConfig.TooltipMode> MECHANICAL_VISOR_TOOLTIP_MODE;
    public static final ModConfigSpec.EnumValue<MechanicalVisorConfig.BlockTrackerMode> MECHANICAL_VISOR_BLOCK_TRACKER_MODE;

    static {

        CLIENT_BUILDER.comment("Upgrades settings").push(CATEGORY_UPGRADES);
        CLIENT_BUILDER.comment("Mechanical visor").push(CATEGORY_MECHANICAL_VISOR);
        BLOCK_TRACKER_ADVANCED_CHECK = CLIENT_BUILDER.comment("If Block Tracker should also check if actual tooltip is provided").define("block_tracker_advanced_check", false);
        MECHANICAL_VISOR_TOOLTIP_MODE = CLIENT_BUILDER.comment("Tooltip mode").defineEnum("tooltip_mode", MechanicalVisorConfig.TooltipMode.CLASSIC);
        MECHANICAL_VISOR_BLOCK_TRACKER_MODE = CLIENT_BUILDER.comment("Block Tracker mode").defineEnum("block_tracker_mode", MechanicalVisorConfig.BlockTrackerMode.ALL);
        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.pop();

        CLIENT_SPEC = CLIENT_BUILDER.build();
    }
}
