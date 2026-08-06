package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.content.airhandler_backtank.AirHandlerBacktankAttach;
import com.lgmrszd.compressedcreativity.content.airhandler_backtank.AirHandlerBacktankBlockEntity;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.lgmrszd.compressedcreativity.config.CommonConfig.BACKTANK_COMPAT_BLOCK;

@Mixin(BacktankBlockEntity.class)
public abstract class BacktankBlockEntityMixin extends BlockEntity {
    @Unique
    private AirHandlerBacktankBlockEntity compressedcreativity$internalAirHandler = null;

    @Shadow(remap = false)
    private int capacityEnchantLevel;

    public BacktankBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void compressedcreativity$inTick(CallbackInfo ci) {
        if (!BACKTANK_COMPAT_BLOCK.get()) return;

        BacktankBlockEntity self = (BacktankBlockEntity) (Object) this;
        if (compressedcreativity$internalAirHandler == null) {
            compressedcreativity$internalAirHandler = AirHandlerBacktankAttach.getOrCreate(self);
            compressedcreativity$internalAirHandler.updateVolumeFromEnchant(capacityEnchantLevel);
            // Don't miss one tick
            compressedcreativity$internalAirHandler.tick(self);
        } else {
            compressedcreativity$internalAirHandler.tick(self);
        }
    }
}
