package com.lgmrszd.extensiveadapters.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import com.lgmrszd.extensiveadapters.ExtensiveAdapters;
import com.lgmrszd.extensiveadapters.ModGroup;
import com.lgmrszd.extensiveadapters.blocks.rotational_compressor.RotationalCompressorBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;

public class EABlocks {

    private static final CreateRegistrate REGISTRATE = ExtensiveAdapters.registrate()
            .itemGroup(() -> ModGroup.MAIN);

    public static final BlockEntry<RotationalCompressorBlock> ROTATIONAL_COMPRESSOR = REGISTRATE.block("rotational_compressor", RotationalCompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .transform(BlockStressDefaults.setImpact(2048.0 / 256))
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
//        Create.registrate().addToSection();
    }
}
