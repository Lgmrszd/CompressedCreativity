package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.index.CCUpgrades;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.StatPanelLayout;
import me.desht.pneumaticcraft.common.config.subconfig.ArmorHUDLayout;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;


public class MechanicalVisorOptions extends IOptionPage.SimpleOptionPage<MechanicalVisorClientHandler> {

    private Button tooltipMode;
    private Button blockTrackerMode;

    private final MechanicalVisorClientHandler mvClientHandled = getClientUpgradeHandler();
    public MechanicalVisorOptions(IGuiScreen screen, MechanicalVisorClientHandler clientUpgradeHandler) {
        super(screen, clientUpgradeHandler);
    }

    @Override
    public boolean displaySettingsHeader() {
        return false;
    }

    @Override
    public void populateGui(IGuiScreen gui) {
        super.populateGui(gui);

        tooltipMode = new ExtendedButton(30, 63, 150, 20,
                Component.empty(),
                b -> {
                    mvClientHandled.tooltipMode = mvClientHandled.tooltipMode.getNext();
                    StatPanelLayout layout = ArmorHUDLayout.INSTANCE.getLayoutFor(CCUpgrades.MECHANICAL_VISOR.getId(), null);
                    if (layout != null) ArmorHUDLayout.INSTANCE.updateLayout(
                            CCUpgrades.MECHANICAL_VISOR.getId(),
                            layout.x(),
                            layout.y(),
                            layout.expandsLeft(),
                            !mvClientHandled.tooltipMode.isWidget()
                    );
                    updateButtonText();
                    mvClientHandled.saveToConfig();
                }
        );
        blockTrackerMode = new ExtendedButton(30, 95, 150, 20,
                Component.empty(),
                b -> {
                    mvClientHandled.blockTrackerMode = mvClientHandled.blockTrackerMode.getNext();
                    updateButtonText();
                    mvClientHandled.saveToConfig();
                }
        );
        gui.addWidget(tooltipMode);
        gui.addWidget(blockTrackerMode);
        gui.addWidget(PneumaticRegistry.getInstance().getClientArmorRegistry().makeStatMoveButton(30, settingsYposition() + 12, getClientUpgradeHandler()));
        updateButtonText();
    }

    @Override
    public void renderPost(GuiGraphics graphics, int x, int y, float partialTicks) {
        Font font = this.getGuiScreen().getFontRenderer();
        Screen guiScreen = this.getGuiScreen().getScreen();
        FormattedCharSequence widgetCharSequence =
                Component.translatable("compressedcreativity.mechanical_visor.armor.gui.tooltip_mode")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        FormattedCharSequence trackerCharSequence =
                Component.translatable("compressedcreativity.mechanical_visor.armor.gui.block_tracker_mode")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        FormattedCharSequence settingsCharSequence =
                Component.translatable("pneumaticcraft.armor.gui.misc.settings")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        graphics.drawString(font, widgetCharSequence, (float) (105 - font.width(widgetCharSequence) / 2), 53, 0xFFFFFFFF, false);
        graphics.drawString(font, trackerCharSequence, (float) (105 - font.width(trackerCharSequence) / 2), 85, 0xFFFFFFFF, false);
        graphics.drawString(font, settingsCharSequence, (float) (105 - font.width(settingsCharSequence) / 2), settingsYposition(), 0xFFFFFFFF, false);
    }

    private void updateButtonText() {
        tooltipMode.setMessage(Component.translatable(
                "compressedcreativity.mechanical_visor.armor.gui.tooltip_mode." +
                mvClientHandled.tooltipMode.getID())
        );
        blockTrackerMode.setMessage(Component.translatable(
                "compressedcreativity.mechanical_visor.armor.gui.block_tracker_mode." +
                        mvClientHandled.blockTrackerMode.getID())
        );
    }

    @Override
    public int settingsYposition() {
        return super.settingsYposition() + 20;
    }
}
