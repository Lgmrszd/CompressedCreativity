package com.lgmrszd.compressedcreativity.index;

import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;
import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;

public class CCUpgrades {
    public static final DeferredRegister<PNCUpgrade> UPGRADES_DEFERRED = DeferredRegister.create(RL("upgrades"), MOD_ID);

    public static final RegistryObject<PNCUpgrade> MECHANICAL_VISOR = UPGRADES_DEFERRED.register(
            "mechanical_visor",
            () -> new PNCUpgrade(1)
    );



}
