package com.lgmrszd.compressedcreativity.index;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.REGISTRATE;
import static com.simibubi.create.foundation.data.CreateRegistrate.casingConnectivity;
import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

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
//import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.TagGen;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.contraptions.base.CasingBlock;
import com.simibubi.create.content.contraptions.fluids.PipeAttachmentModel;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlock;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlockItem;
import com.simibubi.create.content.contraptions.relays.encased.EncasedCTBehaviour;
import com.simibubi.create.foundation.block.BlockStressDefaults;
import com.simibubi.create.foundation.block.DyedBlockList;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class CCBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> ModGroup.MAIN);
    }

    public static <B extends CasingBlock> NonNullUnaryOperator<BlockBuilder<B, CreateRegistrate>> myCasing(
            Supplier<CTSpriteShiftEntry> ct) {
        return b -> b.initialProperties(SharedProperties::stone)
                .properties(p -> p.sound(SoundType.WOOD))
                .transform(TagGen.axeOrPickaxe())
                .blockstate((c, p) -> p.simpleBlock(c.get()))
                .onRegister(connectedTextures(() -> new EncasedCTBehaviour(ct.get())))
                .onRegister(casingConnectivity((block, cc) -> cc.makeCasing(block, ct.get())))
                .tag(AllTags.AllBlockTags.CASING.tag)
                .item()
                .tag(AllTags.AllItemTags.CASING.tag)
                .build();
    }

    public static final BlockEntry<RotationalCompressorBlock> ROTATIONAL_COMPRESSOR = REGISTRATE.block("rotational_compressor", RotationalCompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.axeOrPickaxe())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .addLayer(() -> RenderType::cutoutMipped)
            .transform(BlockStressDefaults.setImpact(8.0))
//            .transform(BlockStressDefaults.setImpact(CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get() / 256.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CompressedAirEngineBlock> COMPRESSED_AIR_ENGINE = REGISTRATE.block("compressed_air_engine", CompressedAirEngineBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .transform(TagGen.pickaxeOnly())
            .blockstate(CompressedAirEngineBlockStateGenerator::blockState)
            .addLayer(() -> RenderType::translucent)
            .transform(BlockStressDefaults.setCapacity(4.0))
//            .transform(BlockStressDefaults.setCapacity(CommonConfig.COMPRESSED_AIR_ENGINE_STRESS.get() / 256.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<AirBlowerBlock> AIR_BLOWER = REGISTRATE.block("air_blower", AirBlowerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.axeOrPickaxe())
            .blockstate(AirBlowerBlockStateGenerator::blockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<AdvancedAirBlowerBlock> INDUSTRIAL_AIR_BLOWER = REGISTRATE.block("industrial_air_blower", AdvancedAirBlowerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.axeOrPickaxe())
            .blockstate(AirBlowerBlockStateGenerator::blockState)
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BracketedPressureTubeBlock> BRACKETED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_pressure_tube", BracketedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .blockstate(BracketedPressureTubeBlockStateGenerator::blockState)
            .loot((p, b) -> p.dropOther(b, CCModsReference.PNCPressureTube.getBlockByTier(0).asItem()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static final BlockEntry<BracketedReinforcedPressureTubeBlock> BRACKETED_REINFORCED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_reinforced_pressure_tube", BracketedReinforcedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
            .blockstate(BracketedPressureTubeBlockStateGenerator::blockState)
            .loot((p, b) -> p.dropOther(b, CCModsReference.PNCPressureTube.getBlockByTier(1).asItem()))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::new))
            .register();

    public static final BlockEntry<BracketedAdvancedPressureTubeBlock> BRACKETED_ADVANCED_PRESSURE_TUBE =
            REGISTRATE.block("bracketed_advanced_pressure_tube", BracketedAdvancedPressureTubeBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(TagGen.pickaxeOnly())
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
                .transform(TagGen.pickaxeOnly())
                .recipe((c, p) -> ShapedRecipeBuilder.shaped(c.get(), 6)
                        .define('P', CCModsReference.getPlasticBrickBlockByColor(colour).asItem())
                        .define('A', AllItems.ANDESITE_ALLOY.get())
                        .pattern(" P ")
                        .pattern("PAP")
                        .unlockedBy("has_" + c.getName(), RegistrateRecipeProvider.has(c.get()))
                        .save(p, CCMisc.CCRL("crafting/" + c.getName())))
                .item(BracketBlockItem::new)
                .transform(PlasticBracketGenerator.itemModel(colourName + "_plastic"))
                .register();
    });

    public static final BlockEntry<CasingBlock> COMPRESSED_IRON_CASING =
            REGISTRATE.block("compressed_iron_casing", CasingBlock::new)
                    .properties(p -> p.color(MaterialColor.TERRACOTTA_GRAY))
                    .transform(myCasing(() -> CCSpriteShifts.COMPRESSED_IRON_CASING))
                    .register();

    public static void register() {
    }
}
