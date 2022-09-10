package com.lgmrszd.compressedcreativity.blocks.plastic_bracket;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.simibubi.create.content.contraptions.fluids.pipes.BracketBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@Mod.EventBusSubscriber(modid = CompressedCreativity.MOD_ID, value = Dist.CLIENT, bus = Bus.MOD)
public class PlasticBracketTextureTinting {

// Brackets rendering doesn't support tinted models :(

//    private static final int[] dyeColors = new int[DyeColor.values().length];
//
//    static {
//        for (DyeColor color : DyeColor.values()) {
//            float[] rgb = color.getTextureDiffuseColors();
//            dyeColors[color.getId()] = (int)(rgb[0] * 255.0F) << 16 | (int)(rgb[1] * 255.0F) << 8 | (int)(rgb[2] * 255.0F);
//        }
//    }
//
//    @SubscribeEvent
//    public static void registerItemColourHandlers(ColorHandlerEvent.Item event) {
//        for (DyeColor color : DyeColor.values()) {
//            BlockEntry<BracketBlock> bracket = CCBlocks.DYED_PLASTIC_BRACKETS.get(color);
//            event.getItemColors().register((stack, tintIndex) ->
//                            tintIndex == 0 ? dyeColors[color.getId()] : -1,
//                    bracket.get());
//        }
//
//    }
//
//
//    @SubscribeEvent
//    public static void registerBlockColourHandlers(ColorHandlerEvent.Block event) {
//        for (DyeColor color : DyeColor.values()) {
//            BlockEntry<BracketBlock> bracket = CCBlocks.DYED_PLASTIC_BRACKETS.get(color);
//            event.getBlockColors().register((state, blockAccess, pos, tintIndex) ->
//                            tintIndex == 0 ? dyeColors[color.getId()] : -1,
//                    bracket.get());
//        }
//    }
}
