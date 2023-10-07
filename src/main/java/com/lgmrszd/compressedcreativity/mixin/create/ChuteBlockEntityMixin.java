package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockEntity;
import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.simibubi.create.content.logistics.chute.ChuteBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChuteBlockEntity.class)
public abstract class ChuteBlockEntityMixin extends SmartBlockEntity {
    public ChuteBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Inject(
            method = "calculatePush",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT,
            remap = false
    )
    private void addAirBlowersInCalculatePush(int branchCount, CallbackInfoReturnable<Float> cir, BlockState blockStateBelow) {
        if ((CCBlocks.AIR_BLOWER.has(blockStateBelow) || CCBlocks.INDUSTRIAL_AIR_BLOWER.has(blockStateBelow))
                && blockStateBelow.getValue(AirBlowerBlock.FACING) == Direction.UP) {
            BlockEntity be = level.getBlockEntity(worldPosition.below());
            if (be instanceof AirBlowerBlockEntity abbe && !be.isRemoved()) {
                cir.setReturnValue(abbe.getSpeed());
            }
        }
    }


    @Inject(
            method = "calculatePull",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT,
            remap = false
    )
    private void addAirBlowersInCalculatePull(CallbackInfoReturnable<Float> cir, BlockState blockStateAbove) {
        if ((CCBlocks.AIR_BLOWER.has(blockStateAbove) || CCBlocks.INDUSTRIAL_AIR_BLOWER.has(blockStateAbove))
                && blockStateAbove.getValue(AirBlowerBlock.FACING) == Direction.DOWN) {
            BlockEntity be = level.getBlockEntity(worldPosition.above());
            if (be instanceof AirBlowerBlockEntity abbe && !be.isRemoved()) {
                cir.setReturnValue(abbe.getSpeed());
            }
        }
    }
}
