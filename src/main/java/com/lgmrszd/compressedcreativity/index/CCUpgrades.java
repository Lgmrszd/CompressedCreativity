package com.lgmrszd.compressedcreativity.index;

import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;
import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;

public class CCUpgrades {
    public static final DeferredRegister<PNCUpgrade> UPGRADES_DEFERRED = DeferredRegister.create(RL("upgrades"), MOD_ID);

    public static final RegistryObject<PNCUpgrade> ENGINEER_GOGGLES = UPGRADES_DEFERRED.register(
            "engineer_goggles",
            () -> new PNCUpgrade(1)
    );



}
