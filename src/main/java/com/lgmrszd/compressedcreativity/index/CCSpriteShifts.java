package com.lgmrszd.compressedcreativity.index;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;

public class CCSpriteShifts {
    public static final CTSpriteShiftEntry COMPRESSED_IRON_CASING = omni("compressed_iron_casing");

    private static CTSpriteShiftEntry omni(String name) {
        return CTSpriteShifter.getCT(AllCTTypes.OMNIDIRECTIONAL,
                CCMisc.CCRL("block/" + name),
                CCMisc.CCRL("block/" + name + "_connected"));
    }
}
