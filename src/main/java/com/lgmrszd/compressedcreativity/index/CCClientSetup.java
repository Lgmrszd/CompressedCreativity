package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.GoggledChecker;
import com.lgmrszd.compressedcreativity.upgrades.BlockTrackerEntryKinetic;
import com.lgmrszd.compressedcreativity.upgrades.MechanicalVisorClientHandler;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IClientArmorRegistry;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class CCClientSetup {

    public static MechanicalVisorClientHandler mechanicalVisorClientHandler;
    public static void init(FMLClientSetupEvent event) {
        IClientArmorRegistry clientRegistry = PneumaticRegistry.getInstance().getClientArmorRegistry();

        clientRegistry.registerBlockTrackEntry(BlockTrackerEntryKinetic.ID, BlockTrackerEntryKinetic::new);

        mechanicalVisorClientHandler = new MechanicalVisorClientHandler();
        clientRegistry.registerUpgradeHandler(CCCommonUpgradeHandlers.mechanicalVisorHandler, mechanicalVisorClientHandler);

        GogglesItem.addIsWearingPredicate(GoggledChecker::hasMechanicalVisorUpgrade);
        event.enqueueWork(CCClientSetup::initLate);

    }

    private static void initLate() {
        PonderIndex.addPlugin(new CCPonderPlugin());
    }
}
