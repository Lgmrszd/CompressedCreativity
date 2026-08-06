package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.content.Mesh;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class CCBlockPartials {
    public static final PartialModel
        AIR_ENGINE_ROTOR = PartialModel.of(ResourceLocation.fromNamespaceAndPath(CompressedCreativity.MOD_ID, "block/compressed_air_engine/rotor"));

    public static Map<String, PartialModel> MESHES = new HashMap<>();

    static {
        for (Mesh.MeshType meshType : Mesh.MeshType.values()) {
            MESHES.put(
                    meshType.getName(),
                    PartialModel.of(ResourceLocation.fromNamespaceAndPath(
                            CompressedCreativity.MOD_ID,
                            "block/industrial_air_blower/mesh/" + meshType.getName()
                    ))
            );
        }
    }

    public static void init() {

    }
}
