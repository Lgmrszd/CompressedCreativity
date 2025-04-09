package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.lang.LangNumberFormat;

public class CCLang {

    public static LangBuilder builder() {
        return new LangBuilder(CompressedCreativity.MOD_ID);
    }

    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

    public static LangBuilder number(double d) {
        return builder().text(LangNumberFormat.format(d));
    }
}
