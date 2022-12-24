package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerTileEntity;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InWorldProcessing.Type.class)
public class InWorldProcessingMixin {
    @Inject(method = "byBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private static void inByBlock(BlockGetter reader, BlockPos pos, CallbackInfoReturnable<InWorldProcessing.Type> cir) {
        BlockEntity be = reader.getBlockEntity(pos);
        if (be instanceof AdvancedAirBlowerTileEntity abbe) {
            abbe.getProcessingType().ifPresent(cir::setReturnValue);
        }
    }
}
