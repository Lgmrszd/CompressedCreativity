package com.lgmrszd.compressedcreativity.index;

import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;

public class CCUpgrades {
    public static final DeferredRegister<PNCUpgrade> UPGRADES_DEFERRED = DeferredRegister.create(PNCUpgrade.class, MOD_ID);

    public static final RegistryObject<PNCUpgrade> ENGINEER_GOGGLES = UPGRADES_DEFERRED.register(
            "engineer_goggles",
            () -> new PNCUpgrade(1)
    );



}
