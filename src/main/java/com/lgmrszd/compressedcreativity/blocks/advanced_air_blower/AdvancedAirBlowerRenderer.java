package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import com.jozufozu.flywheel.core.PartialModel;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class AdvancedAirBlowerRenderer extends SafeBlockEntityRenderer<AdvancedAirBlowerBlockEntity> {
    public AdvancedAirBlowerRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected void renderSafe(AdvancedAirBlowerBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        // TODO: fix Flywheel Instance
//        if (Backend.canUseInstancing(te.getLevel())) return;

        VertexConsumer vb = buffer.getBuffer(RenderType.translucent());

        BlockState blockState = te.getBlockState();
        Direction facing = blockState.getValue(AdvancedAirBlowerBlock.FACING);

        Mesh.IMeshType meshType = te.getMeshType();
        if (meshType == null) return;
        Optional<PartialModel> meshPartial = meshType.getModel();
        meshPartial.ifPresent((meshModel) -> {
            // Not sure why it needs to be opposite face
            SuperByteBuffer mesh = CachedBufferer.partialFacing(meshModel, blockState, facing.getOpposite());
//            rotateToFacing(mesh, facing);
            if (meshType.shouldTint()) {
                mesh.color(meshType.getTintColor(te));
            }
            mesh.light(light);
            mesh.renderInto(ms, vb);
        });
        Optional<PartialModel> meshPartialExtra = meshType.getModelExtra();
        meshPartialExtra.ifPresent((meshModelExtra) -> {
            SuperByteBuffer mesh = CachedBufferer.partialFacing(meshModelExtra, blockState, facing.getOpposite());
            mesh.light(light);
            mesh.renderInto(ms, vb);
        });
    }
}
