package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.items.MachineConstructItem;
import com.simibubi.create.content.curiosities.weapons.PotatoCannonItemRenderer;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.repack.registrate.util.entry.ItemEntry;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final CreateRegistrate REGISTRATE = CompressedCreativity.registrate()
            .itemGroup(() -> ModGroup.MAIN);

//    public static final DeferredRegister<Item> ITEMS
//            = DeferredRegister.create(ForgeRegistries.ITEMS, CompressedCreativity.MOD_ID);

    public static final ItemEntry<MachineConstructItem> TEST_CONSTRUCT = REGISTRATE.item("test_construct", MachineConstructItem::new).register();


    public static void register(IEventBus eventBus) {
//        ITEMS.register(eventBus);
    }
}
