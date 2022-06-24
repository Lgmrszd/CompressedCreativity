package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.heatbehaviour.CCHeatBehaviourFurnace;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatRegistry;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;

public class CCHeatBehaviour {
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(CCHeatBehaviour::registerHeatBehaviour);
    }

    public static void registerHeatBehaviour() {
        IHeatRegistry heatRegistry = PneumaticRegistry.getInstance().getHeatRegistry();
        heatRegistry.registerHeatBehaviour(RL("furnace"), CCHeatBehaviourFurnace::new);
    }
}
