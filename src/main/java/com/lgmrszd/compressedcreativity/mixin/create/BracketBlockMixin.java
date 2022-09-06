package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube.AxisPressureTubeBlock;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.LOGGER;

@Mixin(BracketBlock.class)
public abstract class BracketBlockMixin {

    @Shadow protected abstract Optional<BlockState> getSuitableBracket(Direction.Axis targetBlockAxis, Direction direction, BracketBlock.BracketType type);

    @Inject(method = "getSuitableBracket(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true, remap = false)
    public void inGetSuitableBracket(BlockState blockState, Direction direction, CallbackInfoReturnable<Optional<BlockState>> cir) {
        if(blockState.getBlock() instanceof AxisPressureTubeBlock) {
            cir.setReturnValue(getSuitableBracket(blockState.getValue(AxisPressureTubeBlock.AXIS), direction, BracketBlock.BracketType.SHAFT));
        }
    }
}
