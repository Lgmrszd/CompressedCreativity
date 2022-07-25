package com.lgmrszd.compressedcreativity.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlock;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
//import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.components.saw.SawGenerator;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.repack.registrate.builders.BlockBuilder;
import com.simibubi.create.repack.registrate.util.entry.BlockEntry;
import com.simibubi.create.repack.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public class CCBlocks {

    // WORKAROUND: Currently, importing AllTags breaks datagen
    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
        return (b) -> b.tag(BlockTags.MINEABLE_WITH_AXE).tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }


    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> pickaxeOnly() {
        return (b) -> b.tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static final BlockEntry<RotationalCompressorBlock> ROTATIONAL_COMPRESSOR = REGISTRATE.block("rotational_compressor", RotationalCompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setImpact(CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get() / 256.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CompressedAirEngineBlock> COMPRESSED_AIR_ENGINE = REGISTRATE.block("compressed_air_engine", CompressedAirEngineBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::translucent)
            .transform(BlockStressDefaults.setCapacity(8.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<AirBlowerBlock> AIR_BLOWER = REGISTRATE.block("air_blower", AirBlowerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(axeOrPickaxe())
//            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .blockstate(AirBlowerBlockStateGenerator::blockState)
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static void register() {
    }
}
