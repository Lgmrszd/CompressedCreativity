package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import com.lgmrszd.compressedcreativity.content.airhandler_backtank.AirHandlerBacktankAttach;
import com.lgmrszd.compressedcreativity.index.*;
import com.lgmrszd.compressedcreativity.network.CCNetwork;
import com.simibubi.create.foundation.data.CreateRegistrate;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(CompressedCreativity.MOD_ID)
public class CompressedCreativity {
    
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "compressedcreativity";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CompressedCreativity(IEventBus modEventBus, ModContainer modContainer) {
        REGISTRATE.registerEventListeners(modEventBus);

        CCConfigHelper.init();
        CCConfigHelper.registerConfigListener(modEventBus);
        
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::doClientStuff);
        modEventBus.addListener(this::postInit);
        modEventBus.addListener(this::registerCapabilities);
        modEventBus.addListener(CCNetwork::register);
        modEventBus.addListener(EventPriority.LOWEST, CompressedCreativity::gatherData);
        
        if (net.neoforged.fml.loading.FMLEnvironment.dist == Dist.CLIENT) {
            CCBlockPartials.init();
        }

        NeoForge.EVENT_BUS.addListener(this::serverStart);

        // Disable Registrate's default SEARCH tab assignment for items.
        // Custom tab's displayItems callback adds items with PARENT_AND_SEARCH_TABS
        // visibility, which already populates the search tab. Without this, Registrate's
        // onBuildCreativeModeTabContents handler would try to re-add them to the search
        // tab during BuildCreativeModeTabContentsEvent, causing duplicate entry crashes.
        @SuppressWarnings("unchecked")
        ResourceKey<CreativeModeTab> noTab = (ResourceKey<CreativeModeTab>) null;
        REGISTRATE.defaultCreativeTab(noTab);

        CCCreativeTabs.register(modEventBus);
        CCItems.register(modEventBus);
        CCBlocks.register();
        CCBlockEntities.register();
    }

    private void setup(final FMLCommonSetupEvent event) {
        CCCommonSetup.init(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        CCClientSetup.init(event);
    }

    private void serverStart(final ServerAboutToStartEvent event) {
        
    }

    private void postInit(final FMLLoadCompleteEvent event) {
    
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        LOGGER.info("Registering PneumaticCraft air handler capabilities for CompressedCreativity blocks");
        
        // Register air handler capability for all IPneumaticTileEntity block entities
        event.registerBlockEntity(PNCCapabilities.AIR_HANDLER_MACHINE, CCBlockEntities.ROTATIONAL_COMPRESSOR.get(), 
            (be, side) -> {
                if (be instanceof IPneumaticTileEntity pneumatic) {
                    return pneumatic.getAirHandler(side);
                }
                return null;
            });
        
        event.registerBlockEntity(PNCCapabilities.AIR_HANDLER_MACHINE, CCBlockEntities.COMPRESSED_AIR_ENGINE.get(), 
            (be, side) -> {
                if (be instanceof IPneumaticTileEntity pneumatic) {
                    return pneumatic.getAirHandler(side);
                }
                return null;
            });
        
        event.registerBlockEntity(PNCCapabilities.AIR_HANDLER_MACHINE, CCBlockEntities.AIR_BLOWER.get(), 
            (be, side) -> {
                if (be instanceof IPneumaticTileEntity pneumatic) {
                    return pneumatic.getAirHandler(side);
                }
                return null;
            });
        
        event.registerBlockEntity(PNCCapabilities.AIR_HANDLER_MACHINE, CCBlockEntities.INDUSTRIAL_AIR_BLOWER.get(), 
            (be, side) -> {
                if (be instanceof IPneumaticTileEntity pneumatic) {
                    return pneumatic.getAirHandler(side);
                }
                return null;
            });

        AirHandlerBacktankAttach.register(event);
    }

    public static void gatherData(GatherDataEvent event) {
        // Language overrides are provided in resources/assets/compressedcreativity/lang/overrides/
        // No data generation needed for language files
    }
}
