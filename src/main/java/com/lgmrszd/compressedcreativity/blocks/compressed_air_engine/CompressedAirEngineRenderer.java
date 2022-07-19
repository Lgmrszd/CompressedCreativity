package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.jozufozu.flywheel.backend.Backend;
import com.lgmrszd.compressedcreativity.index.BlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;


public class CompressedAirEngineRenderer extends KineticTileEntityRenderer {
    public CompressedAirEngineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(KineticTileEntity ote, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

//        super.renderSafe(ote, partialTicks, ms, buffer, light, overlay);


        if (Backend.canUseInstancing(ote.getLevel())) return;

        if (!(ote instanceof CompressedAirEngineTileEntity te)) return;

        Direction direction = te.getBlockState()
                .getValue(CompressedAirEngineBlock.HORIZONTAL_FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        int lightBehind = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction.getOpposite()));
        int lightInFront = LevelRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction));

//        SuperByteBuffer shaftHalf =
//                CachedBufferer.partial(shaft(getRotationAxisOf(te)));
        SuperByteBuffer rotor =
                CachedBufferer.partialFacing(BlockPartials.AIR_ENGINE_ROTOR, te.getBlockState(), direction.getOpposite());

//        float time = AnimationTickHolder.getRenderTime(te.getLevel());
//        float angle = (time * te.getSpeed() * 3 / 10f) % 360;
//        angle = angle / 180f * (float) Math.PI;

//        standardKineticRotationTransform(shaftHalf, te, lightBehind).renderInto(ms, vb);
        BlockState shaftState = shaft(getRotationAxisOf(te));
        RenderType type = getRenderType(te, shaftState);
        renderRotatingBuffer(te, getRotatedModel(te, shaftState), ms, buffer.getBuffer(type), light);
        standardKineticRotationTransform(rotor, te, lightBehind).renderInto(ms, vb);
//        kineticRotationTransform(rotor, te, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);
    }
}
