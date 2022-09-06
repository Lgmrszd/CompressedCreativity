package com.lgmrszd.compressedcreativity.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube.AxisPressureTubeBlock;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlock;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
//import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.fluids.PipeAttachmentModel;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import me.desht.pneumaticcraft.common.core.ModBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
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
            .blockstate(CompressedAirEngineBlockStateGenerator::blockState)
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::translucent)
            .transform(BlockStressDefaults.setCapacity(CommonConfig.COMPRESSED_AIR_ENGINE_STRESS.get() / 256.0))
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

    public static final BlockEntry<AxisPressureTubeBlock> AXIS_PRESSURE_TUBE = REGISTRATE.block("axis_pressure_tube", AxisPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .loot((p, b) -> p.dropOther(b, ModBlocks.PRESSURE_TUBE.get()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static void register() {
    }
}
