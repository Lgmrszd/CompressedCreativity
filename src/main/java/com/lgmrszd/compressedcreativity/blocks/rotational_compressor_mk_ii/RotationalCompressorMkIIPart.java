package com.lgmrszd.compressedcreativity.blocks.rotational_compressor_mk_ii;

import net.minecraft.util.IStringSerializable;

public enum RotationalCompressorMkIIPart implements IStringSerializable {
    HEAD("head"),
    MIDDLE("middle"),
    END("end");

    private final String name;

    private RotationalCompressorMkIIPart(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
