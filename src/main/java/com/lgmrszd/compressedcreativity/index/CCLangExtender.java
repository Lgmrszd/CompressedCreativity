package com.lgmrszd.compressedcreativity.index;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.utility.FilesHelper;


public class CCLangExtender {
    public static void ExtendLang(CreateRegistrate registrate) {
        String filepath = "assets/" + CompressedCreativity.MOD_ID + "/lang/overrides/en_us.json";
        JsonElement element = FilesHelper.loadJsonResource(filepath);
        if (!element.isJsonObject()) {
            return;
        }
        JsonObject obj = element.getAsJsonObject();
        for (String key : obj.keySet()) {
            JsonElement value = obj.get(key);
            if (value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()) {
                CompressedCreativity.LOGGER.debug("Adding lang key " + key);
                registrate.addRawLang(key, value.getAsJsonPrimitive().getAsString());
            }
        }
    }
}
