package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

//import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.index.CCMisc;
import com.simibubi.create.content.curiosities.armor.CopperBacktankItem;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
//import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AirHandlerBacktankAttach {
//    protected static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);
    @SubscribeEvent
    public static void onAttachingCapabilitiesItem(final AttachCapabilitiesEvent<ItemStack> event) {
        if (!(event.getObject().getItem() instanceof CopperBacktankItem)) return;


        AirHandlerBacktankItem airHandlerBacktankItem = new AirHandlerBacktankItem(event.getObject());
        LazyOptional<AirHandlerBacktankItem> airHandlerBacktankCap = LazyOptional.of(() -> airHandlerBacktankItem);

        ICapabilityProvider provider = new ICapabilityProvider() {
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
                if (cap == PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY) {
                    return airHandlerBacktankCap.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(CCMisc.CCRL("airhandler_backtank_item"), provider);
    }
    @SubscribeEvent
    public static void onAttachingCapabilitiesBE(final AttachCapabilitiesEvent<BlockEntity> event) {
        if (!(event.getObject() instanceof CopperBacktankTileEntity copperBacktankTileEntity)) return;

        AirHandlerBacktankBlockEntity airHandlerBacktankBlockEntity =
                new AirHandlerBacktankBlockEntity(copperBacktankTileEntity);
        LazyOptional<AirHandlerBacktankBlockEntity> airHandlerBacktankCap = LazyOptional.of(() -> airHandlerBacktankBlockEntity);

        ICapabilityProvider provider = new ICapabilityProvider() {
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
                if (cap == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY &&
                        (direction == null || direction == Direction.DOWN)) {
                    return airHandlerBacktankCap.cast();
                }
                return LazyOptional.empty();
            }
        };

        event.addCapability(CCMisc.CCRL("airhandler_backtank_be"), provider);
    }
// Debug just in case
//    @SubscribeEvent
//    public static void debug(PlayerInteractEvent.RightClickBlock event) {
//        if (event.getItemStack().isEmpty() ||
//                event.getItemStack().getItem().getRegistryName() == null ||
//                !event.getItemStack().getItem().getRegistryName().toString().equals("minecraft:stick"))
//            return;
//        BlockPos pos = event.getPos();
//        BlockEntity be = event.getWorld().getBlockEntity(pos);
//        if (!(be instanceof CopperBacktankTileEntity cbbe)) return;
//        logger.debug("Copper Backtank!");
//    }
}
