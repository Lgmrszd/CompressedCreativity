package com.lgmrszd.compressedcreativity.blocks.common;

import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public interface IPneumaticTileEntity {
    float getDangerPressure();
    
    /**
     * Get the air handler for this block entity on the specified side.
     * @param side The side to get the air handler for, or null for no specific side
     * @return The air handler, or null if not available on this side
     */
    @Nullable
    IAirHandlerMachine getAirHandler(@Nullable Direction side);
}
