package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerBlockEntity;
import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AirCurrent.class)
public class AirCurrentMixin {
    @ModifyVariable(
            method = "rebuild",
            at = @At(value = "STORE", ordinal = 0),
            name = "type",
            remap = false
    )
    public FanProcessingType AirBlowerMeshProcessingTypeInRebuild(FanProcessingType type, @Local(name = "world") Level world, @Local(name = "start") BlockPos start) {
        BlockEntity be = world.getBlockEntity(start);
        if (be instanceof AdvancedAirBlowerBlockEntity abbe) {
            return abbe.getProcessingType().orElse(type);
        }
        return type;
    }
}
