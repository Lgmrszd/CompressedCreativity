package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.CCBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup {

    public static final CreativeModeTab MAIN = new CreativeModeTab(CompressedCreativity.MOD_ID+".main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CCBlocks.ROTATIONAL_COMPRESSOR.get());
        }
    };
}
