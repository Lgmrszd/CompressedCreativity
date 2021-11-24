package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.PonderScenes;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.content.PonderTag;
import net.minecraft.util.ResourceLocation;

public class CCPonder {
    public static final PonderTag PRESSURE = new PonderTag(new ResourceLocation(CompressedCreativity.MOD_ID, "pressure")).item(CCBlocks.ROTATIONAL_COMPRESSOR.get(), true, false)
            .defaultLang("Pressure", "Components which use pressurized air");

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CompressedCreativity.MOD_ID);

    public static void register() {
        HELPER.addStoryBoard(CCBlocks.ROTATIONAL_COMPRESSOR, "rotational_compressor", PonderScenes::rotationalCompressor, PRESSURE);
    }
}
