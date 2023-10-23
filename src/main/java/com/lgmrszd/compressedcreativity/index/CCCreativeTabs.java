package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.index.CCBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.utility.Components;
import me.desht.pneumaticcraft.common.core.ModCreativeModeTab;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CompressedCreativity.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BASE_CREATIVE_TAB = REGISTER.register("base",
            () -> CreativeModeTab.builder()
                    .title(Components.translatable("itemGroup."+CompressedCreativity.MOD_ID+".main"))
                    .withTabsBefore(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey(), ModCreativeModeTab.DEFAULT.getKey())
                    .icon(CCBlocks.ROTATIONAL_COMPRESSOR::asStack)
                    .displayItems((params, output) -> {
                        List<ItemStack> items = CompressedCreativity.REGISTRATE.getAll(Registries.ITEM)
                                .stream()
                                .map((regItem) -> new ItemStack(regItem.get()))
                                .toList();
                        output.acceptAll(items);
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
