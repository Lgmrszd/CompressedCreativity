package com.lgmrszd.compressedcreativity.index;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.ModGroup;
import com.lgmrszd.compressedcreativity.blocks.advanced_air_blower.AdvancedAirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedAdvancedPressureTubeBlock;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedPressureTubeBlock;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedPressureTubeBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedReinforcedPressureTubeBlock;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlock;
import com.lgmrszd.compressedcreativity.blocks.compressed_air_engine.CompressedAirEngineBlockStateGenerator;
import com.lgmrszd.compressedcreativity.blocks.plastic_bracket.PlasticBracketGenerator;
import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorBlock;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
//import com.simibubi.create.AllTags;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.fluids.PipeAttachmentModel;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlock;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlockItem;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

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

    public static final BlockEntry<AdvancedAirBlowerBlock> ADVANCED_AIR_BLOWER = REGISTRATE.block("advanced_air_blower", AdvancedAirBlowerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(axeOrPickaxe())
//            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .blockstate(AirBlowerBlockStateGenerator::blockState)
//            .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BracketedPressureTubeBlock> BRACKETED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_pressure_tube", BracketedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .blockstate(BracketedPressureTubeBlockStateGenerator::blockState)
            .loot((p, b) -> p.dropOther(b, CCModsReference.PNCPressureTube.getBlockByTier(0).asItem()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static final BlockEntry<BracketedReinforcedPressureTubeBlock> BRACKETED_REINFORCED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_reinforced_pressure_tube", BracketedReinforcedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .blockstate(BracketedPressureTubeBlockStateGenerator::blockState)
            .loot((p, b) -> p.dropOther(b, CCModsReference.PNCPressureTube.getBlockByTier(1).asItem()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static final BlockEntry<BracketedAdvancedPressureTubeBlock> BRACKETED_ADVANCED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_advanced_pressure_tube", BracketedAdvancedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .blockstate(BracketedPressureTubeBlockStateGenerator::blockState)
            .loot((p, b) -> p.dropOther(b, CCModsReference.PNCPressureTube.getBlockByTier(2).asItem()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static final DyedBlockList<BracketBlock> DYED_PLASTIC_BRACKETS = new DyedBlockList<>(colour -> {
        String colourName = colour.getSerializedName();
        return REGISTRATE.block(colourName + "_plastic_bracket", BracketBlock::new)
                .properties(p -> p.color(colour.getMaterialColor()))
                .blockstate(new PlasticBracketGenerator(colourName + "_plastic")::generate)
                .properties(p -> p.sound(SoundType.WOOD))
                .transform(pickaxeOnly())
                .recipe((c, p) -> {
                    ShapedRecipeBuilder.shaped(c.get(), 6)
                            .define('P', CCModsReference.getPlasticBrickBlockByColor(colour).asItem())
                            .define('A', AllItems.ANDESITE_ALLOY.get())
                            .pattern(" P ")
                            .pattern("PAP")
                            .unlockedBy("has_" + c.getName(), RegistrateRecipeProvider.has(c.get()))
                            .save(p, CCMisc.CCRL("crafting/" + c.getName()));
                })
                .item(BracketBlockItem::new)
                .transform(PlasticBracketGenerator.itemModel(colourName + "_plastic"))
                .register();
    });

    public static void register() {
    }
}
