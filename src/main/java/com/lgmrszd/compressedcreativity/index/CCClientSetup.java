package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.GoggledChecker;
import com.lgmrszd.compressedcreativity.upgrades.BlockTrackerEntryKinetic;
import com.lgmrszd.compressedcreativity.upgrades.MechanicalVisorClientHandler;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IClientArmorRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CCClientSetup {

    public static MechanicalVisorClientHandler mechanicalVisorClientHandler;
    public static void init(FMLClientSetupEvent event) {
        IClientArmorRegistry clientRegistry = PneumaticRegistry.getInstance().getClientArmorRegistry();

        clientRegistry.registerBlockTrackEntry(BlockTrackerEntryKinetic.ID, BlockTrackerEntryKinetic::new);

        mechanicalVisorClientHandler = new MechanicalVisorClientHandler();
        clientRegistry.registerUpgradeHandler(CCCommonUpgradeHandlers.mechanicalVisorHandler, mechanicalVisorClientHandler);

//        CCBlockPartials.init();

        GogglesItem.addIsWearingPredicate(GoggledChecker::hasMechanicalVisorUpgrade);
        event.enqueueWork(CCClientSetup::initLate);

    }

    private static void initLate() {
        CCPonder.register();
    }
}
