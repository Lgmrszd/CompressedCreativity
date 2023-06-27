package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerBlockEntity;
import com.simibubi.create.content.kinetics.fan.FanProcessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FanProcessing.Type.class)
public class InWorldProcessingMixin {
    @Inject(method = "byBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inByBlock(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<FanProcessing.Type> cir) {
        BlockEntity be = reader.getBlockEntity(pos);
        if (be instanceof AdvancedAirBlowerBlockEntity abbe) {
            abbe.getProcessingType().ifPresent(cir::setReturnValue);
        }
    }
}
