package com.lgmrszd.compressedcreativity.blocks.common;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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

import javax.annotation.Nonnull;
import java.util.List;

import static com.lgmrszd.compressedcreativity.index.CCMisc.appendPneumaticHoverText;

public abstract class PneumaticHorizontalKineticBlock<T extends BlockEntity> extends HorizontalKineticBlock implements IPneumaticWrenchable, IBE<T> {
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
    public void appendHoverText(@Nonnull ItemStack stack, BlockGetter world, @Nonnull List<Component> infoList, @Nonnull TooltipFlag par4) {
        appendPneumaticHoverText(
                () -> newBlockEntity(BlockPos.ZERO, defaultBlockState()),
                infoList);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock() && !isMoving) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te != null) {
                IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
                miscHelpers.playMachineBreakEffect(te);
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }
}
