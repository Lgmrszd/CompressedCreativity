package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.simibubi.create.AllBlockPartials;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.base.KineticTileEntityRenderer;
import com.simibubi.create.foundation.render.PartialBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;


public class RotationalCompressorRenderer extends KineticTileEntityRenderer {
    public RotationalCompressorRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    protected SuperByteBuffer getRotatedModel(KineticTileEntity te) {
        return super.getRotatedModel(te);

//        return PartialBufferer.getFacing(AllBlockPartials.SHAFT_HALF, te.getBlockState(), ((Direction)te.getBlockState().getValue(RotationalCompressorBlock.HORIZONTAL_FACING)).getOpposite());
    }

    @Override
    protected void renderSafe(KineticTileEntity ote, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffer,
                              int light, int overlay) {

//        super.renderSafe(ote, partialTicks, ms, buffer, light, overlay);
//


        if (Backend.getInstance().canUseInstancing(ote.getLevel())) return;

        if (!(ote instanceof RotationalCompressorTileEntity)) return;
        RotationalCompressorTileEntity te = (RotationalCompressorTileEntity) ote;

        Direction direction = te.getBlockState()
                .getValue(RotationalCompressorBlock.HORIZONTAL_FACING);
        IVertexBuilder vb = buffer.getBuffer(RenderType.cutoutMipped());

        int lightBehind = WorldRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction.getOpposite()));
        int lightInFront = WorldRenderer.getLightColor(te.getLevel(), te.getBlockPos().relative(direction));

        SuperByteBuffer shaftHalf =
                PartialBufferer.getFacing(AllBlockPartials.SHAFT_HALF, te.getBlockState(), direction.getOpposite());
        SuperByteBuffer fanInner =
                PartialBufferer.getFacing(AllBlockPartials.ENCASED_FAN_INNER, te.getBlockState(), direction.getOpposite());

        float time = AnimationTickHolder.getRenderTime(te.getLevel());
        float speed = te.getSpeed() * 5;
        if (speed > 0)
            speed = MathHelper.clamp(speed, 80, 64 * 20);
        if (speed < 0)
            speed = MathHelper.clamp(speed, -64 * 20, -80);
        float angle = (time * speed * 3 / 10f) % 360;
        angle = angle / 180f * (float) Math.PI;

        standardKineticRotationTransform(shaftHalf, te, lightBehind).renderInto(ms, vb);
        kineticRotationTransform(fanInner, te, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);
    }
}
