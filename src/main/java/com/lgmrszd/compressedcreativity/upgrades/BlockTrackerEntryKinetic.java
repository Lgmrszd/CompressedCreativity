package com.lgmrszd.compressedcreativity.upgrades;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import me.desht.pneumaticcraft.api.client.pneumatic_helmet.IBlockTrackEntry;
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

import java.util.Collections;
import java.util.List;

public class BlockTrackerEntryKinetic implements IBlockTrackEntry {
    @Override
    public boolean shouldTrackWithThisEntry(BlockGetter world, BlockPos pos, BlockState state, BlockEntity te) {
        if (te == null) return false;
        if (te instanceof IHaveHoveringInformation) return true;
        return te instanceof IHaveGoggleInformation;
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
        if (te instanceof IHaveHoveringInformation kte) {
            kte.addToTooltip(infoList, sneaking);
        }
        if (te instanceof IHaveGoggleInformation gte) {
            gte.addToGoggleTooltip(infoList, sneaking);
        }
    }

    @Override
    public ResourceLocation getEntryID() {
        return new ResourceLocation(CompressedCreativity.MOD_ID, "block_tracker_module_kinetic");
    }
}
