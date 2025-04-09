package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.PonderScenes;
//import com.simibubi.create.foundation.ponder.PonderLocalization;
//import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
//import com.simibubi.create.foundation.ponder.PonderRegistry;
//import com.simibubi.create.foundation.ponder.PonderTag;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class CCPonder {

    public static final ResourceLocation PRESSURE = new ResourceLocation(CompressedCreativity.MOD_ID, "pressure");

    public static void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.registerTag(PRESSURE)
                .addToIndex()
                .item(CCBlocks.ROTATIONAL_COMPRESSOR.get())
                .title("Pressure")
                .description("Components which use pressurized air")
                .register();

        HELPER.addToTag(PRESSURE)
                .add(CCBlocks.ROTATIONAL_COMPRESSOR)
                .add(CCBlocks.COMPRESSED_AIR_ENGINE)
                .add(CCBlocks.AIR_BLOWER);
    }

    public static void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(CCBlocks.ROTATIONAL_COMPRESSOR, "rotational_compressor", PonderScenes::rotationalCompressor, PRESSURE);
        HELPER.addStoryBoard(CCBlocks.AIR_BLOWER, "air_blower", PonderScenes::airBlower, PRESSURE);
        HELPER.addStoryBoard(CCBlocks.COMPRESSED_AIR_ENGINE, "compressed_air_engine", PonderScenes::CompressedAirEngine, PRESSURE);
    }
}
