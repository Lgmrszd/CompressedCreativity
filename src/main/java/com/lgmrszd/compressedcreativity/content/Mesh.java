package com.lgmrszd.compressedcreativity.content;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerBlockEntity;
import com.lgmrszd.compressedcreativity.index.CCBlockPartials;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import net.minecraft.client.renderer.BiomeColors;

import java.util.Optional;

public class Mesh {
    public enum MeshType implements IMeshType {
        SPLASHING("splashing", "Water Soaked Mesh") {
            @Override
            public boolean shouldTint() {
                return true;
            }

            @Override
            public int getTintColor(AdvancedAirBlowerBlockEntity te) {
                if (te.getLevel() == null) return 0xffffffff;
                return BiomeColors.getAverageWaterColor(te.getLevel(), te.getBlockPos());
            }
            @Override
            public Optional<PartialModel> getModelExtra() {
                return Optional.ofNullable(CCBlockPartials.MESHES.get(WOVEN.name));
            }
            @Override
            public Optional<FanProcessingType> getProcessingType(int temp) {
                return Optional.of(AllFanProcessingTypes.SPLASHING);
            }
        },
        SPLASHING_FROZEN("splashing_frozen", "Frozen Mesh") {
            @Override
            public Optional<PartialModel> getModelExtra() {
                return Optional.empty();
            }
        },
        WOVEN("woven", "Woven Mesh"),
        DENSE("dense", "Dense Mesh") {
            @Override
            public boolean shouldTint() {
                return true;
            }
            @Override
            public int getTintColor(AdvancedAirBlowerBlockEntity te) {
                return  te.getCapability(PNCCapabilities.HEAT_EXCHANGER_CAPABILITY)
                        .map((cap) -> HeatUtil.getColourForTemperature(cap.getTemperatureAsInt()).getRGB())
                        .orElse(0xffffffff);
            }
            @Override
            public Optional<FanProcessingType> getProcessingType(int temp) {
                return temp > 373 ? Optional.of(AllFanProcessingTypes.BLASTING) : // 100
                        temp > 323 ? Optional.of(AllFanProcessingTypes.SMOKING) :
                                Optional.of(AllFanProcessingTypes.NONE); // 50
            }

            @Override
            public int getCoolingFactor() {
                return 2;
            }
        },
        HAUNTED("haunted", "Haunted Mesh") {
            @Override
            public Optional<FanProcessingType> getProcessingType(int temp) {
                return temp > 373 ? Optional.of(AllFanProcessingTypes.HAUNTING) : Optional.empty();
            }
        };

        private final String name;
        private final String displayName;
        MeshType(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
        public String getName() {
            return name;
        }
        public String getDisplayName() {
            return displayName;
        }
        @Override
        public Optional<PartialModel> getModel() {
            return Optional.ofNullable(CCBlockPartials.MESHES.get(name));
        }
    }

    public interface IMeshType {
        default boolean shouldTint() {
            return false;
        }
        default int getTintColor(AdvancedAirBlowerBlockEntity te) {
            return 0xffffffff;
        }
        default Optional<PartialModel> getModel() {
            return Optional.empty();
        }
        default Optional<PartialModel> getModelExtra() {
            return Optional.empty();
        }
        String getName();
        default Optional<FanProcessingType> getProcessingType(int temp) {return Optional.empty();}
        default int getCoolingFactor() {return 1;}
    }
}
