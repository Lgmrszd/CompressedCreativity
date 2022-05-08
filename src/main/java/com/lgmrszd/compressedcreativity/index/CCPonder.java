package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.PonderScenes;
import com.simibubi.create.foundation.ponder.PonderRegistrationHelper;
import com.simibubi.create.foundation.ponder.PonderTag;
import me.desht.pneumaticcraft.common.core.ModBlocks;
import net.minecraft.resources.ResourceLocation;

public class CCPonder {
    public static final PonderTag PRESSURE = new PonderTag(new ResourceLocation(CompressedCreativity.MOD_ID, "pressure")).item(CCBlocks.ROTATIONAL_COMPRESSOR.get(), true, false)
            .defaultLang("Pressure", "Components which use pressurized air");

    static final PonderRegistrationHelper HELPER = new PonderRegistrationHelper(CompressedCreativity.MOD_ID);

    public static void register() {
        HELPER.addStoryBoard(CCBlocks.ROTATIONAL_COMPRESSOR, "rotational_compressor", PonderScenes::rotationalCompressor, PRESSURE);
        HELPER.addStoryBoard(CCBlocks.AIR_BLOWER, "air_blower", PonderScenes::airBlower, PRESSURE);
        HELPER.addStoryBoard(ModBlocks.PRESSURE_TUBE.getId(), "pressure_tiers", PonderScenes::pressureTiers, PRESSURE);
        HELPER.addStoryBoard(ModBlocks.REINFORCED_PRESSURE_TUBE.getId(), "pressure_tiers", PonderScenes::pressureTiers, PRESSURE);
        HELPER.addStoryBoard(ModBlocks.ADVANCED_PRESSURE_TUBE.getId(), "pressure_tiers", PonderScenes::pressureTiers, PRESSURE);
        HELPER.addStoryBoard(ModBlocks.COMPRESSED_IRON_BLOCK.getId(), "temperature_1", PonderScenes::temperatureOne, PRESSURE);
    }
}
