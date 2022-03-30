package com.lgmrszd.compressedcreativity;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraftforge.eventbus.api.IEventBus;
public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);


    public static void register(IEventBus eventBus) {
//        ITEMS.register(eventBus);
    }
}
