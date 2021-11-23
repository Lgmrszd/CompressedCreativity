package com.lgmrszd.extensiveadapters;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, ExtensiveAdapters.MOD_ID);

//    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item",
//            () -> new Item(new Item.Properties().tab(ModGroup.MAIN)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
