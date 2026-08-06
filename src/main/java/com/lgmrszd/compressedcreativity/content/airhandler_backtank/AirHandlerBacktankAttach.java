package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import static com.lgmrszd.compressedcreativity.config.CommonConfig.BACKTANK_COMPAT_BLOCK;
import static com.lgmrszd.compressedcreativity.config.CommonConfig.BACKTANK_COMPAT_ITEM;

public class AirHandlerBacktankAttach {
    private static final Map<BacktankBlockEntity, AirHandlerBacktankBlockEntity> HANDLERS =
            Collections.synchronizedMap(new WeakHashMap<>());

    public static AirHandlerBacktankBlockEntity getOrCreate(BacktankBlockEntity be) {
        return HANDLERS.computeIfAbsent(be, AirHandlerBacktankBlockEntity::new);
    }

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                PNCCapabilities.AIR_HANDLER_MACHINE,
                AllBlockEntityTypes.BACKTANK.get(),
                (be, side) -> {
                    if (!BACKTANK_COMPAT_BLOCK.get()) return null;
                    if (!(be instanceof BacktankBlockEntity backtankBE)) return null;
                    if (side != null && side != Direction.DOWN) return null;
                    return getOrCreate(backtankBE);
                }
        );

        event.registerItem(
                PNCCapabilities.AIR_HANDLER_ITEM,
                (stack, ctx) -> {
                    if (!BACKTANK_COMPAT_ITEM.get()) return null;
                    return new AirHandlerBacktankItem(stack);
                },
                AllItems.COPPER_BACKTANK.get(),
                AllItems.NETHERITE_BACKTANK.get()
        );
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
