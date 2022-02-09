package com.lgmrszd.compressedcreativity.blocks.other;

import com.lgmrszd.compressedcreativity.items.MachineConstructItem;
import com.lgmrszd.compressedcreativity.multiblock_helper.IMachineScaffoldingBlock;
import com.lgmrszd.compressedcreativity.multiblock_helper.BlockFinderBFS;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;

public class MachineScaffoldingBlock extends Block implements IMachineScaffoldingBlock {

    public MachineScaffoldingBlock(Properties properties) {
        super(properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (handIn == Hand.MAIN_HAND) {
            ItemStack item = player.getMainHandItem();
            if (!item.isEmpty()){
                if (item.getItem() instanceof MachineConstructItem) {
                    if (!worldIn.isClientSide()) {
                        player.sendMessage(new StringTextComponent("Test!"), player.getUUID());
                        processClick(worldIn, pos, hit.getDirection());
                    }
                    return ActionResultType.CONSUME;
                }
            } else {
                if (player.isShiftKeyDown()) {
                    if (!worldIn.isClientSide()) {
                        this.collect(worldIn, pos, player);
                    }
                    return ActionResultType.CONSUME;
                }
            }
        }

        return super.use(state, worldIn, pos, player, handIn, hit);
    }

    public void processClick(World worldIn, BlockPos pos, Direction dir) {
        BlockFinderBFS finder = new BlockFinderBFS(worldIn, pos, this);
        finder.scan();
    }

    public boolean tryCollect(World world, BlockPos pos) {
        if (this != world.getBlockState(pos).getBlock())
            return false;
        world.destroyBlock(pos, false);
        return true;
    }

    public void collect(World world, BlockPos pos, PlayerEntity player) {
        player.sendMessage(new StringTextComponent("Empty hand!"), player.getUUID());
        Set<BlockPos> blocks = BlockFinderBFS.findAdjacentBlocks(world, pos, this);
        int i = 0;
        for (BlockPos block: blocks) {
            if(tryCollect(world, block))
                i++;
        }
        if (i > 0) {
            ItemStack drops = new ItemStack(this, i);
            player.inventory.placeItemBackInInventory(world, drops);
        }
    }
}
