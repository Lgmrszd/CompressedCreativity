package com.lgmrszd.compressedcreativity.upgrades;

import com.mojang.blaze3d.vertex.PoseStack;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import static com.lgmrszd.compressedcreativity.CompressedCreativity.MOD_ID;

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

        tooltipMode = new ExtendedButton(30, 50, 150, 20,
                Component.empty(),
                b -> {
                    mvClientHandled.tooltipMode = mvClientHandled.tooltipMode.getNext();
                    updateButtonText();
                    mvClientHandled.saveToConfig();
                }
        );
        blockTrackerMode = new ExtendedButton(30, 82, 150, 20,
                Component.empty(),
                b -> {
                    mvClientHandled.blockTrackerMode = mvClientHandled.blockTrackerMode.getNext();
                    updateButtonText();
                    mvClientHandled.saveToConfig();
                }
        );
//        gui.addWidget()
        gui.addWidget(tooltipMode);
        gui.addWidget(blockTrackerMode);
        gui.addWidget(PneumaticRegistry.getInstance().getClientArmorRegistry().makeStatMoveButton(30, 128, getClientUpgradeHandler()));
        updateButtonText();
    }

    @Override
    public void renderPost(PoseStack matrixStack, int x, int y, float partialTicks) {
        Font fontRenderer = Minecraft.getInstance().font;
        FormattedCharSequence widgetCharSequence =
                Component.translatable("compressedcreativity.mechanical_visor.armor.gui.tooltip_mode")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        FormattedCharSequence trackerCharSequence =
                Component.translatable("compressedcreativity.mechanical_visor.armor.gui.block_tracker_mode")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        FormattedCharSequence settingsCharSequence =
                Component.translatable("pneumaticcraft.armor.gui.misc.settings")
                        .withStyle(ChatFormatting.GOLD).getVisualOrderText();
        fontRenderer.drawShadow(matrixStack, widgetCharSequence, (float) (105 - fontRenderer.width(widgetCharSequence) / 2), 40, 0xFFFFFFFF);
        fontRenderer.drawShadow(matrixStack, trackerCharSequence, (float) (105 - fontRenderer.width(trackerCharSequence) / 2), 72 , 0xFFFFFFFF);
        fontRenderer.drawShadow(matrixStack, settingsCharSequence, (float) (105 - fontRenderer.width(settingsCharSequence) / 2), settingsYposition() , 0xFFFFFFFF);
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
}
