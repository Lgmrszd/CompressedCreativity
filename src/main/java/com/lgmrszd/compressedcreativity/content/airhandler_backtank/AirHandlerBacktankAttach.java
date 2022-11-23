package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.simibubi.create.content.curiosities.armor.CopperBacktankItem;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AirHandlerBacktankAttach {
    @SubscribeEvent
    public static void onAttachingCapabilities(final AttachCapabilitiesEvent<ItemStack> event) {
        if (!(event.getObject().getItem() instanceof CopperBacktankItem)) return;


        AirHandlerBacktank airHandlerBacktank = new AirHandlerBacktank(event.getObject());
        LazyOptional<AirHandlerBacktank> airHandlerBacktankCap = LazyOptional.of(() -> airHandlerBacktank);

        ICapabilityProvider provider = new ICapabilityProvider() {
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
                if (cap == PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY) {
                    return airHandlerBacktankCap.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(CCMisc.CCRL("airhandler_backtank"), provider);
    }
}
