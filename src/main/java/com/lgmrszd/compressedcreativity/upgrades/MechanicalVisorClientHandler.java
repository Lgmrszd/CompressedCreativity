package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.IClientRegistry;
import me.desht.pneumaticcraft.api.client.IGuiAnimatedStat;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IArmorUpgradeClientHandler;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IGuiScreen;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IOptionPage;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import me.desht.pneumaticcraft.common.core.ModUpgrades;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class MechanicalVisorClientHandler extends IArmorUpgradeClientHandler.SimpleToggleableHandler<MechanicalVisorHandler>{

    private IGuiAnimatedStat visorInfo;

    private BlockEntity focusedBlockEntity;
    public MechanicalVisorClientHandler() {
        super(CCCommonUpgradeHandlers.mechanicalVisorHandler);
    }

    @Override
    public IOptionPage getGuiOptionsPage(IGuiScreen screen) {
        return new MechanicalVisorOptions(screen, this);
    }

    @Override
    public void tickClient(ICommonArmorHandler armorHandler) {
        super.tickClient(armorHandler);

        int blockTrackRange = 30 + Math.min(armorHandler.getUpgradeCount(EquipmentSlot.HEAD, ModUpgrades.RANGE.get()), 5) * 5;
        Player player = armorHandler.getPlayer();
        
        List<Component> textList = new ArrayList<>();

        if (focusedBlockEntity instanceof IHaveHoveringInformation hoveringBE) {
            hoveringBE.addToTooltip(textList, player.isShiftKeyDown());
        }
        if (focusedBlockEntity instanceof IHaveGoggleInformation goggleBE) {
            goggleBE.addToGoggleTooltip(textList, player.isShiftKeyDown());
        }
        if (textList.isEmpty()) {
            textList.add(new TranslatableComponent("Empty"));
        }

        textList.forEach(c -> {});


        visorInfo.setText(textList);

        checkBlockFocus(player, blockTrackRange);

    }

    private void checkBlockFocus(Player player, int range) {
        focusedBlockEntity = null;
        Vec3 eyes = player.getEyePosition(1.0f);
        Vec3 v = eyes;
        Vec3 lookVec = player.getLookAngle().scale(0.25);
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < range * 4; i++) {
            v = v.add(lookVec);
            checkPos.set(v.x, v.y, v.z);

            BlockState state = player.level.getBlockState(checkPos);
            BlockHitResult brtr = state.getShape(player.level, checkPos).clip(eyes, v, checkPos);
            if (brtr != null && brtr.getType() == HitResult.Type.BLOCK) {
                BlockEntity be = player.level.getBlockEntity(checkPos);
                if (be instanceof IHaveGoggleInformation || be instanceof IHaveHoveringInformation) {
                    focusedBlockEntity = be;
                    break;
                }
            }
        }
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
            IClientRegistry registry = PneumaticRegistry.getInstance().getClientRegistry();
            visorInfo = registry.getAnimatedStat(null, CCItems.MECHANICAL_VISOR_UPGRADE.asStack(), 0x60000000);
            visorInfo.setBaseX(10);
            visorInfo.setBaseY(10);
        }
        return visorInfo;
    }

    @Override
    public void onResolutionChanged() {
        visorInfo = null;
    }
}
