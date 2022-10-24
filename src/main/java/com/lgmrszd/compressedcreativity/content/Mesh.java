package com.lgmrszd.compressedcreativity.content;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import me.desht.pneumaticcraft.common.heat.HeatUtil;

import java.util.Optional;

public class Mesh {
    public enum MeshType implements IMeshType {
        WATER("water", "Wet Mesh"),
        WATER_FROZEN("water_frozen", "Wet Frozen Mesh"),
        WATER_EMPTY("water_empty", "Dried Mesh"),
        DENSE("dense", "Dense Mesh") {
            @Override
            public boolean shouldTint() {
                return true;
            }

            @Override
            public int getTintColor(int temp) {
                return HeatUtil.getColourForTemperature(temp).getRGB();
            }
        },
        HAUNT("haunt", "Soul Mesh");

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
        default int getTintColor(int temp) {
            return 0xffffffff;
        }
        default Optional<PartialModel> getModel() {
            return Optional.empty();
        }
    }
}
