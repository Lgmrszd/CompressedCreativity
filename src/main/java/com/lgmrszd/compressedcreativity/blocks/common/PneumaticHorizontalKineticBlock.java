package com.lgmrszd.compressedcreativity.blocks.common;

import com.lgmrszd.compressedcreativity.blocks.rotational_compressor.RotationalCompressorTileEntity;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.ITE;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PneumaticHorizontalKineticBlock<T extends BlockEntity> extends HorizontalKineticBlock implements IPneumaticWrenchable, ITE<T> {
    public PneumaticHorizontalKineticBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onWrenched(Level world, Player player, BlockPos pos, Direction side, InteractionHand hand) {
        UseOnContext ctx = new UseOnContext(player, hand, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getX()), side, pos, false));
        return ctx.getPlayer() != null && (
                ctx.getPlayer().isCrouching() ?
                        onSneakWrenched(world.getBlockState(pos), ctx) :
                        onWrenched(world.getBlockState(pos), ctx)
        )  == InteractionResult.SUCCESS;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> infoList, TooltipFlag par4) {
        if (Screen.hasShiftDown()) {
//            infoList.add(new TranslatableComponent("compressedcreativity.gui.").withStyle(ChatFormatting.GOLD));
            BlockEntity te = newBlockEntity(BlockPos.ZERO, defaultBlockState());
            if (te instanceof IPneumaticTileEntity pte) {
                infoList.add(new TranslatableComponent("pneumaticcraft.gui.tooltip.maxPressure", pte.getDangerPressure()).withStyle(ChatFormatting.GOLD));
            }
//            PneumaticRegistry.getInstance().getUpgradeRegistry().addUpgradeTooltip(upgrade.get(), infoList);
        } else {
            infoList.add(new TranslatableComponent("compressedcreativity.gui.tooltip.expand").withStyle(ChatFormatting.GOLD));
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
            miscHelpers.playMachineBreakEffect(te);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}
