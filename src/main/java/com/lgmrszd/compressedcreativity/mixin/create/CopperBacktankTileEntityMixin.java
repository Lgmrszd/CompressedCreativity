package com.lgmrszd.compressedcreativity.mixin.create;

import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CopperBacktankTileEntity.class)
public abstract class CopperBacktankTileEntityMixin extends BlockEntity implements ICapabilityProvider {

    public CopperBacktankTileEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void inTick(CallbackInfo ci) {
        getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY, null).ifPresent((cap)-> {
            cap.tick(this);
        });
    }
}
