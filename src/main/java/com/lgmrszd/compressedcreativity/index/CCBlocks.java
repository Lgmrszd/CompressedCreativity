package com.lgmrszd.compressedcreativity.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.other.MachineScaffoldingBlock;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor_mk_ii.RotationalCompressorMkIIBlock;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;

public class CCBlocks {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .itemGroup(() -> ModGroup.MAIN);

    public static final BlockEntry<RotationalCompressorBlock> ROTATIONAL_COMPRESSOR = REGISTRATE.block("rotational_compressor", RotationalCompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setImpact(CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get() / 256.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<RotationalCompressorMkIIBlock> ROTATIONAL_COMPRESSOR_MK_II = REGISTRATE.block("rotational_compressor_mk_ii", RotationalCompressorMkIIBlock::new)
            .initialProperties(SharedProperties::stone)
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<MachineScaffoldingBlock> MACHINE_SCAFFOLDING_BLOCK = REGISTRATE.block("machine_scaffolding", MachineScaffoldingBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .simpleItem()
            .register();

    public static final BlockEntry<AirBlowerBlock> AIR_BLOWER = REGISTRATE.block("air_blower", AirBlowerBlock::new)
            .initialProperties(SharedProperties::stone)
            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
//        Create.registrate().addToSection();
    }
}
