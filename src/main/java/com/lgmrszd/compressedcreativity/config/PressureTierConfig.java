package com.lgmrszd.compressedcreativity.config;

import me.desht.pneumaticcraft.api.pressure.PressureTier;
import net.minecraftforge.common.ForgeConfigSpec;

public class PressureTierConfig {

    public enum CustomTier implements PressureTier {
        ROTATIONAL_COMPRESSOR_CUSTOM_TIER(
                CommonConfig.ROTATIONAL_COMPRESSOR_DANGER_PRESSURE,
                CommonConfig.ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE),
        AIR_BLOWER_CUSTOM_TIER(
                CommonConfig.AIR_BLOWER_DANGER_PRESSURE,
                CommonConfig.AIR_BLOWER_CRITICAL_PRESSURE),

        COMPRESSED_AIR_ENGINE_CUSTOM_TIER(
                CommonConfig.COMPRESSED_AIR_ENGINE_DANGER_PRESSURE,
                CommonConfig.COMPRESSED_AIR_ENGINE_CRITICAL_PRESSURE);

        private final ForgeConfigSpec.DoubleValue dangerPressure;
        private final ForgeConfigSpec.DoubleValue criticalPressure;

        CustomTier(ForgeConfigSpec.DoubleValue dangerPressure, ForgeConfigSpec.DoubleValue criticalPressure) {
            this.dangerPressure = dangerPressure;
            this.criticalPressure = criticalPressure;
        }

        @Override
        public float getDangerPressure() {
            return dangerPressure.get().floatValue();
        }

        @Override
        public float getCriticalPressure() {
            return dangerPressure.get().floatValue() + criticalPressure.get().floatValue();
        }
    }

    public enum PressureTierEnum {
        TIER_ONE(PressureTier.TIER_ONE),
        TIER_ONE_HALF(PressureTier.TIER_ONE_HALF),
        TIER_TWO(PressureTier.TIER_TWO),
        CUSTOM();

        private final PressureTier pressureTier;

        PressureTierEnum(PressureTier pressureTier) {
            this.pressureTier = pressureTier;
        }

        PressureTierEnum() {
            this.pressureTier = PressureTier.TIER_ONE;
        }

        public PressureTier getPressureTier() {
            return pressureTier;
        }

        public PressureTier getPressureTierDefinedOrCustom(PressureTier pressureTierCustom) {
            return (this == CUSTOM) ? pressureTierCustom : pressureTier;
        }
    }
}