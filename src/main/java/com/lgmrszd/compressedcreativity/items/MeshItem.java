package com.lgmrszd.compressedcreativity.items;

import com.lgmrszd.compressedcreativity.content.Mesh;
import net.minecraft.world.item.Item;

public class MeshItem extends Item {
    private final Mesh.MeshType meshType;
    public MeshItem(Properties properties, Mesh.MeshType meshType) {
        super(properties);
        this.meshType = meshType;
    }
    public Mesh.IMeshType getMeshType() {
        return meshType;
    }
}
