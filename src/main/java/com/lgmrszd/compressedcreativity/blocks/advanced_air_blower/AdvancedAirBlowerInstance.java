package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import net.minecraft.core.Direction;

public class AdvancedAirBlowerInstance extends BlockEntityInstance<AdvancedAirBlowerTileEntity> {

    protected final ModelData mesh;
    private final Direction facing;

    private final Mesh.MeshType meshType;

    public AdvancedAirBlowerInstance(MaterialManager materialManager, AdvancedAirBlowerTileEntity blockEntity) {
        super(materialManager, blockEntity);

        facing = blockState.getValue(AdvancedAirBlowerBlock.FACING);
        meshType = blockEntity.getMeshType();
        PartialModel meshPartial;
        if (meshType == Mesh.MeshType.WATER)
            meshPartial = BlockPartials.MESH_WATER;
        else
            meshPartial = BlockPartials.MESH;

        mesh = getTransformMaterial()
                .getModel(meshPartial, blockState, facing)
                .createInstance();
    }

    @Override
    protected void remove() {
        mesh.delete();
    }

    @Override
    public boolean shouldReset() {
        return super.shouldReset() || blockEntity.getMeshType() != meshType;
    }
}
