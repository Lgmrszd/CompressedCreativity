package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.GoggledChecker;
import com.lgmrszd.compressedcreativity.upgrades.BlockTrackerEntryKinetic;
import com.lgmrszd.compressedcreativity.upgrades.MechanicalVisorClientHandler;
import com.simibubi.create.content.contraptions.goggles.GogglesItem;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IPneumaticHelmetRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CCClientSetup {

    public static MechanicalVisorClientHandler mechanicalVisorClientHandler;
    public static void init(FMLClientSetupEvent event) {
        IPneumaticHelmetRegistry clientRegistry = PneumaticRegistry.getInstance().getHelmetRegistry();

        clientRegistry.registerBlockTrackEntry(BlockTrackerEntryKinetic.ID, BlockTrackerEntryKinetic::new);

        mechanicalVisorClientHandler = new MechanicalVisorClientHandler();
        clientRegistry.registerRenderHandler(CCCommonUpgradeHandlers.mechanicalVisorHandler, mechanicalVisorClientHandler);

        GogglesItem.addIsWearingPredicate(GoggledChecker::hasMechanicalVisorUpgrade);
        event.enqueueWork(CCClientSetup::initLate);

    }

    private static void initLate() {
        CCPonder.register();
    }
}
