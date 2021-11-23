package com.lgmrszd.compressedcreativity;

import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, CompressedCreativity.MOD_ID);

//    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item",
//            () -> new Item(new Item.Properties().tab(ModGroup.MAIN)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
