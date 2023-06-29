package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.config.ClientConfig;
import com.lgmrszd.compressedcreativity.config.MechanicalVisorConfig;
import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.IGuiAnimatedStat;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IArmorUpgradeClientHandler;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MechanicalVisorClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<MechanicalVisorHandler>{

    public MechanicalVisorConfig.TooltipMode tooltipMode;

    public MechanicalVisorConfig.BlockTrackerMode blockTrackerMode;
    private IGuiAnimatedStat visorInfo;

    private BlockEntity focusedBlockEntity;
    public MechanicalVisorClientHandler() {
        super(CCCommonUpgradeHandlers.mechanicalVisorHandler);
        initConfig();
    }

    @Override
    public IOptionPage getGuiOptionsPage(IGuiScreen screen) {
        return new MechanicalVisorOptions(screen, this);
    }

    @Override
    public void initConfig() {
        tooltipMode = ClientConfig.MECHANICAL_VISOR_TOOLTIP_MODE.get();
        blockTrackerMode = ClientConfig.MECHANICAL_VISOR_BLOCK_TRACKER_MODE.get();
    }

    @Override
    public void saveToConfig() {
        ClientConfig.MECHANICAL_VISOR_TOOLTIP_MODE.set(tooltipMode);
        ClientConfig.MECHANICAL_VISOR_BLOCK_TRACKER_MODE.set(blockTrackerMode);
    }

    @Override
    public void tickClient(ICommonArmorHandler armorHandler) {
        super.tickClient(armorHandler);

        if (!tooltipMode.isWidget()) {
            visorInfo.setText(Collections.emptyList());
            return;
        }

        Player player = armorHandler.getPlayer();
        
        List<Component> textList = new ArrayList<>();

        if (focusedBlockEntity instanceof IHaveGoggleInformation goggleBE) {
            goggleBE.addToGoggleTooltip(textList, player.isShiftKeyDown());
        }
        if (focusedBlockEntity instanceof IHaveHoveringInformation hoveringBE) {
            hoveringBE.addToTooltip(textList, player.isShiftKeyDown());
        }

        visorInfo.setText(textList);

        checkBlockFocus();
    }

    private void checkBlockFocus() {
        focusedBlockEntity = null;
        PneumaticRegistry.getInstance().getClientArmorRegistry().getBlockTrackerFocus().ifPresent(focus -> {
            BlockPos focusedBlockPos = focus.pos();
            if (focusedBlockPos == null) return;
            if (Minecraft.getInstance().level == null) return;
            BlockEntity currentBlockEntity = Minecraft.getInstance().level.getBlockEntity(focusedBlockPos);
            if (currentBlockEntity instanceof IHaveHoveringInformation
                    || currentBlockEntity instanceof IHaveGoggleInformation)
                focusedBlockEntity = currentBlockEntity;
        });
    }

    @Override
    public void render3D(PoseStack matrixStack, MultiBufferSource buffer, float partialTicks) {
        super.render3D(matrixStack, buffer, partialTicks);
    }

    @Override
    public void render2D(PoseStack matrixStack, float partialTicks, boolean armorPieceHasPressure) {
        super.render2D(matrixStack, partialTicks, armorPieceHasPressure);
    }

    @Override
    public IGuiAnimatedStat getAnimatedStat() {
        if (visorInfo == null) {
            visorInfo = PneumaticRegistry.getInstance().getClientArmorRegistry().makeHUDStatPanel(
                    Component.translatable("compressedcreativity.mechanical_visor.armor.gui.title"),
                    CCItems.MECHANICAL_VISOR_UPGRADE.asStack(),
                    this
            );
        }
        return visorInfo;
    }

    @Override
    public void onResolutionChanged() {
        visorInfo = null;
    }
}
