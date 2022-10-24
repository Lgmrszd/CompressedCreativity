package com.lgmrszd.compressedcreativity.index;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.simibubi.create.Create;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class BlockPartials {
    public static final PartialModel
        AIR_ENGINE_ROTOR = new PartialModel(new ResourceLocation(CompressedCreativity.MOD_ID, "block/compressed_air_engine/rotor")),
        MESH = new PartialModel(new ResourceLocation(CompressedCreativity.MOD_ID, "block/advanced_air_blower/mesh/mesh")),
        MESH_WATER = new PartialModel(new ResourceLocation(CompressedCreativity.MOD_ID, "block/advanced_air_blower/mesh/mesh_water"));

    public static Map<String, PartialModel> MESHES = new HashMap<>();

    static {
        for (Mesh.MeshType meshType : Mesh.MeshType.values()) {
            MESHES.put(
                    meshType.getName(),
                    new PartialModel(new ResourceLocation(
                            CompressedCreativity.MOD_ID,
                            "block/advanced_air_blower/mesh/" + meshType.getName()
                    ))
            );
        }
    }

    public static void init() {

    }
}
