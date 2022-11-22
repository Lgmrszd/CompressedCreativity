package com.lgmrszd.compressedcreativity.content;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import net.minecraft.client.renderer.BiomeColors;

import java.util.Optional;

public class Mesh {
    public enum MeshType implements IMeshType {
        WATER("water", "Wet Mesh") {
            @Override
            public boolean shouldTint() {
                return true;
            }

            @Override
            public int getTintColor(AdvancedAirBlowerTileEntity te) {
                if (te.getLevel() == null) return 0xffffffff;
                return BiomeColors.getAverageWaterColor(te.getLevel(), te.getBlockPos());
            }
            @Override
            public Optional<PartialModel> getModelExtra() {
                return Optional.ofNullable(BlockPartials.MESHES.get(WATER_EMPTY.name));
            }
            @Override
            public Optional<InWorldProcessing.Type> getProcessingType(int temp) {
                return Optional.of(InWorldProcessing.Type.SPLASHING);
            }
        },
        WATER_FROZEN("water_frozen", "Wet Frozen Mesh") {
            @Override
            public Optional<PartialModel> getModelExtra() {
                return Optional.empty();
            }
        },
        WATER_EMPTY("water_empty", "Dried Mesh"),
        DENSE("dense", "Dense Mesh") {
            @Override
            public boolean shouldTint() {
                return true;
            }
            @Override
            public int getTintColor(AdvancedAirBlowerTileEntity te) {
                return  te.getCapability(PNCCapabilities.HEAT_EXCHANGER_CAPABILITY)
                        .map((cap) -> HeatUtil.getColourForTemperature(cap.getTemperatureAsInt()).getRGB())
                        .orElse(0xffffffff);
            }
            @Override
            public Optional<InWorldProcessing.Type> getProcessingType(int temp) {
                return temp > 373 ? Optional.of(InWorldProcessing.Type.BLASTING) :
                        temp > 323 ? Optional.of(InWorldProcessing.Type.SMOKING) : Optional.empty();
            }
        },
        HAUNT("haunt", "Soul Mesh") {
            @Override
            public Optional<InWorldProcessing.Type> getProcessingType(int temp) {
                return temp > 373 ? Optional.of(InWorldProcessing.Type.HAUNTING) : Optional.empty();
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
            return Optional.ofNullable(BlockPartials.MESHES.get(name));
        }
    }

    public interface IMeshType {
        default boolean shouldTint() {
            return false;
        }
        default int getTintColor(AdvancedAirBlowerTileEntity te) {
            return 0xffffffff;
        }
        default Optional<PartialModel> getModel() {
            return Optional.empty();
        }
        default Optional<PartialModel> getModelExtra() {
            return Optional.empty();
        }
        String getName();
        default Optional<InWorldProcessing.Type> getProcessingType(int temp) {return Optional.empty();}
    }
}
