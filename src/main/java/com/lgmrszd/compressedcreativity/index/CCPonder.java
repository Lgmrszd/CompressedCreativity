package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.PonderScenes;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import net.minecraft.resources.ResourceLocation;

public class CCPonder {
    public static final PonderTag PRESSURE = new PonderTag(new ResourceLocation(CompressedCreativity.MOD_ID, "pressure"))
            .item(CCBlocks.ROTATIONAL_COMPRESSOR.get(), true, false)
            .defaultLang("Pressure", "Components which use pressurized air").addToIndex();

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CompressedCreativity.MOD_ID);

    public static void register() {
        HELPER.addStoryBoard(CCBlocks.ROTATIONAL_COMPRESSOR, "rotational_compressor", PonderScenes::rotationalCompressor, PRESSURE);
        HELPER.addStoryBoard(CCBlocks.AIR_BLOWER, "air_blower", PonderScenes::airBlower, PRESSURE);
        HELPER.addStoryBoard(CCBlocks.COMPRESSED_AIR_ENGINE, "compressed_air_engine", PonderScenes::CompressedAirEngine, PRESSURE);

        PonderRegistry.TAGS.forTag(PRESSURE)
                .add(CCBlocks.ROTATIONAL_COMPRESSOR)
                .add(CCBlocks.COMPRESSED_AIR_ENGINE)
                .add(CCBlocks.AIR_BLOWER);
    }

    public static void registerLang(CreateRegistrate registrate) {
        register();
        PonderLocalization.provideRegistrateLang(registrate);
    }
}
