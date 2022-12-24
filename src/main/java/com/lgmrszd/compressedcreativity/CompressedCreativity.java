package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.index.*;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.lgmrszd.compressedcreativity.index.recipe.CCSequencedAssemblyRecipeGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedCreativity.MOD_ID)
public class CompressedCreativity
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "compressedcreativity";

//    private static final NonNullSupplier<CreateRegistrate> registrate = CreateRegistrate.lazy(CompressedCreativity.MOD_ID);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CompressedCreativity.MOD_ID);


    public CompressedCreativity() {

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(eventBus);

        CCConfigHelper.init();
        eventBus.addListener(this::setup);
        // Register the enqueueIMC method for modloading
        eventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        eventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);
        // TODO: put it in clientstuff if possible
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> CCBlockPartials::init);
        eventBus.addListener(this::postInit);

        eventBus.addListener(CCColorHandlers::registerBlockColorHandlers);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::serverStart);


        CCItems.register(eventBus);
        CCBlocks.register();
        CCTileEntities.register();

        CCUpgrades.UPGRADES_DEFERRED.register(eventBus);


        eventBus.addListener(EventPriority.LOWEST, CompressedCreativity::gatherData);
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        CCCommonSetup.init(event);
//        CCHeatBehaviour.init(event);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        CCClientSetup.init(event);
    }

    private void serverStart(final ServerAboutToStartEvent event) {
//        CCHeatBehaviour.registerHeatBehaviour();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
    }


    public void postInit(FMLLoadCompleteEvent evt) {

    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        CCPonder.registerLang(REGISTRATE);
        CCLangExtender.ExtendLang(REGISTRATE);
        gen.addProvider(new CCSequencedAssemblyRecipeGen((gen)));
    }
}
