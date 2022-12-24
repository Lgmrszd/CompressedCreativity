package com.lgmrszd.compressedcreativity.mixin.create;

import com.lgmrszd.compressedcreativity.content.airhandler_backtank.AirHandlerBacktankBlockEntity;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperBacktankTileEntity.class)
public abstract class CopperBacktankTileEntityMixin extends BlockEntity implements ICapabilityProvider {
    @Unique
    private AirHandlerBacktankBlockEntity internalAirHandler = null;

    @Shadow(remap = false)
    private int capacityEnchantLevel;

    public CopperBacktankTileEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void inTick(CallbackInfo ci) {
        if (internalAirHandler == null) {
            LazyOptional<IAirHandlerMachine> lazyCap = getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, null);
            lazyCap.ifPresent(cap -> {
                if (cap instanceof AirHandlerBacktankBlockEntity backtankCap) {
                    internalAirHandler = backtankCap;
                    backtankCap.updateVolumeFromEnchant(capacityEnchantLevel);
                    lazyCap.addListener(self -> internalAirHandler = null);
                    // Don't miss one tick
                    backtankCap.tick(this);
                }
            });
        } else internalAirHandler.tick(this);
    }
}
