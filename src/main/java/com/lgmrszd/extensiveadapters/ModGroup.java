package com.lgmrszd.extensiveadapters;

import com.lgmrszd.extensiveadapters.index.EABlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModGroup {

    public static final ItemGroup MAIN = new ItemGroup(CompressedCreativity.MOD_ID+".main") {
        @Override
        public ItemStack makeIcon() {
//            return new ItemStack(ModItems.TEST_ITEM.get());
            return new ItemStack(EABlocks.ROTATIONAL_COMPRESSOR.get());
        }
    };
}
