package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import me.desht.pneumaticcraft.api.item.PNCUpgrade;
import me.desht.pneumaticcraft.api.pneumatic_armor.BaseArmorUpgradeHandler;
import me.desht.pneumaticcraft.api.pneumatic_armor.IArmorExtensionData;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;


public class MechanicalVisorHandler extends BaseArmorUpgradeHandler<IArmorExtensionData> {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(CompressedCreativity.MOD_ID, "mechanical_visor");
    }

    @Override
    public PNCUpgrade[] getRequiredUpgrades() {
        return new PNCUpgrade[] {CCUpgrades.MECHANICAL_VISOR.get()};
    }

    @Override
    public float getIdleAirUsage(ICommonArmorHandler armorHandler) {
        return 0;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
