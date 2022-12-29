package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.config.ClientConfig;
import com.lgmrszd.compressedcreativity.config.MechanicalVisorConfig;
import com.lgmrszd.compressedcreativity.index.CCClientSetup;
import com.lgmrszd.compressedcreativity.index.CCCommonUpgradeHandlers;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IBlockTrackEntry;
import me.desht.pneumaticcraft.api.pneumatic_armor.ICommonArmorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockTrackerEntryKinetic implements IBlockTrackEntry {

    public static final ResourceLocation ID = new ResourceLocation(CompressedCreativity.MOD_ID, "block_tracker_module_kinetic");
    @Override
    public boolean shouldTrackWithThisEntry(BlockGetter world, BlockPos pos, BlockState state, BlockEntity te) {
        Player player = Minecraft.getInstance().player;
        ICommonArmorHandler handler = PneumaticRegistry.getInstance().getCommonArmorRegistry().getCommonArmorHandler(player);
        if (!handler.upgradeUsable(CCCommonUpgradeHandlers.mechanicalVisorHandler, true)) return false;
        if (te == null) return false;
        if (te instanceof IHaveHoveringInformation && (
                !ClientConfig.BLOCK_TRACKER_ADVANCED_CHECK.get()
                || (
                    ((IHaveHoveringInformation) te).addToTooltip(new ArrayList<>(), true)
                    && ((IHaveHoveringInformation) te).addToTooltip(new ArrayList<>(), false)
                )
        ))
            return true;
        return te instanceof IHaveGoggleInformation && (
                !ClientConfig.BLOCK_TRACKER_ADVANCED_CHECK.get()
                || (
                    ((IHaveGoggleInformation) te).addToGoggleTooltip(new ArrayList<>(), true)
                    && ((IHaveGoggleInformation) te).addToGoggleTooltip(new ArrayList<>(), false)
                )
        );
    }

    @Override
    public List<BlockPos> getServerUpdatePositions(BlockEntity te) {
        return te == null ? Collections.emptyList() : Collections.singletonList(te.getBlockPos());
    }

    @Override
    public int spamThreshold() {
        return 8;
    }

    @Override
    public void addInformation(Level world, BlockPos pos, BlockEntity te, Direction face, List<Component> infoList) {
        Player player = Minecraft.getInstance().player;
        boolean sneaking = (player != null && player.isShiftKeyDown());
        MechanicalVisorConfig.BlockTrackerMode mode = CCClientSetup.mechanicalVisorClientHandler.blockTrackerMode;
        if (mode.showsGoggles() && te instanceof IHaveGoggleInformation gte) {
            gte.addToGoggleTooltip(infoList, sneaking);
        }
        if (mode.showsTooltip() && te instanceof IHaveHoveringInformation kte) {
            kte.addToTooltip(infoList, sneaking);
        }
        if (mode == MechanicalVisorConfig.BlockTrackerMode.MIN) {
            infoList.add(Component.translatable("compressedcreativity.mechanical_visor.armor.gui.block_tracker.min"));
        }
    }

    @Override
    public ResourceLocation getEntryID() {
        return ID;
    }
}
