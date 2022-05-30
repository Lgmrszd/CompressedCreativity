package com.lgmrszd.compressedcreativity.upgrades;

import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.ICheckboxWidget;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IPneumaticHelmetRegistry;
import net.minecraft.resources.ResourceLocation;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;

public class MechanicalVisorOptions extends IOptionPage.SimpleOptionPage<MechanicalVisorClientHandler> {

    private ICheckboxWidget checkBoxTrackBlocks;

    public MechanicalVisorOptions(IGuiScreen screen, MechanicalVisorClientHandler clientUpgradeHandler) {
        super(screen, clientUpgradeHandler);
    }

    @Override
    public void populateGui(IGuiScreen gui) {
        super.populateGui(gui);

        IPneumaticHelmetRegistry registry = PneumaticRegistry.getInstance().getHelmetRegistry();
        ResourceLocation ownerID = getClientUpgradeHandler().getCommonHandler().getID();
        checkBoxTrackBlocks = registry.makeKeybindingCheckBox(
                new ResourceLocation(MOD_ID, "mechanical_visor.module.track_block"),
                5,
                45,
                0xFFFFFFFF,
                this::setTrackBlocks
        ).withOwnerUpgradeID(ownerID);
        gui.addWidget(checkBoxTrackBlocks.asWidget());
    }

    private void setTrackBlocks(ICheckboxWidget cb) {

    }
}
