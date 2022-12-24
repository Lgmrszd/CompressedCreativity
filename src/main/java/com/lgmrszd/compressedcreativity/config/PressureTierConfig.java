package com.lgmrszd.compressedcreativity.config;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import me.desht.pneumaticcraft.api.pressure.PressureTier;

public class PressureTierConfig {
    public enum CustomTier implements PressureTier {
        ROTATIONAL_COMPRESSOR_TIER("rotational_compressor"),
        AIR_BLOWER_TIER("air_blower"),
        INDUSTRIAL_AIR_BLOWER_TIER("industrial_air_blower"),
        COMPRESSED_AIR_ENGINE_TIER("compressed_air_engine");

        private final String key;

        CustomTier(String key) {
            this.key = key;
        }

        @Override
        public float getDangerPressure() {
            if (!CommonConfig.MACHINE_PRESSURE_TIERS.containsKey(key)) return fallbackTier(false);
            if (CommonConfig.MACHINE_PRESSURE_TIERS.get(key).get() != PressureTierEnum.CUSTOM)
                return CommonConfig.MACHINE_PRESSURE_TIERS.get(key).get().getPressureTier().getDangerPressure();
            if (!CommonConfig.CUSTOM_DANGER_PRESSURE.containsKey(key)) return fallbackPressure(false);
            return CommonConfig.CUSTOM_DANGER_PRESSURE.get(key).get().floatValue();
        }

        @Override
        public float getCriticalPressure() {
            if (!CommonConfig.MACHINE_PRESSURE_TIERS.containsKey(key)) return fallbackTier(true);
            if (CommonConfig.MACHINE_PRESSURE_TIERS.get(key).get() != PressureTierEnum.CUSTOM)
                return CommonConfig.MACHINE_PRESSURE_TIERS.get(key).get().getPressureTier().getCriticalPressure();
            if (!CommonConfig.CUSTOM_CRITICAL_PRESSURE.containsKey(key)) return fallbackPressure(true);
            return CommonConfig.CUSTOM_CRITICAL_PRESSURE.get(key).get().floatValue();
        }

        private float fallbackTier(boolean critical) {
            CompressedCreativity.LOGGER.error("Config entry of pressure tier for {} is missing", key);
            return critical ? 7 : 5;
        }

        private float fallbackPressure(boolean critical) {
            CompressedCreativity.LOGGER.error("Config entry of custom {} pressure for {} is missing",
                    critical ? "critical" : "danger", key);
            return critical ? 7 : 5;
        }

        public String getKey() {
            return key;
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

    }
}