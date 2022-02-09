package com.lgmrszd.compressedcreativity.multiblock_helper;

import com.lgmrszd.compressedcreativity.blocks.other.MachineScaffoldingBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;

import static net.minecraft.block.Blocks.IRON_BLOCK;

public class BlockFinderBFS {
    
    private final World world;
    private final MachineScaffoldingBlock block;

    private final Set<BlockPos> foundBlocks = new HashSet<>();

    private final Queue<BlockPos> scanQueue = new LinkedList<>();

    public BlockFinderBFS(World world, BlockPos pos, MachineScaffoldingBlock block) {
        this.world = world;
        this.block = block;
        scanQueue.add(pos);
    }

    private void scanStep(BlockPos pos) {
        if(block == world.getBlockState(pos).getBlock() && !foundBlocks.contains(pos)) {
            foundBlocks.add(pos);
            for (Direction dir: Direction.values()){
                if(!foundBlocks.contains(pos.relative(dir)))
                    scanQueue.add(pos.relative(dir));
            }
        }
    }

    public void scan() {
        int scanLimit = 256;
        while (!scanQueue.isEmpty() && foundBlocks.size() < scanLimit) {
            BlockPos nextPos = scanQueue.remove();
            scanStep(nextPos);
        }
//        for (BlockPos foundPos: foundBlocks) {
//            world.setBlock(foundPos, IRON_BLOCK.defaultBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
//        }
    }

    public Set<BlockPos> getFoundBlocks() {
        return foundBlocks;
    }

    public static Set<BlockPos> findAdjacentBlocks (World world, BlockPos pos, MachineScaffoldingBlock block) {
        BlockFinderBFS blockFinderBFS = new BlockFinderBFS(world, pos, block);
        blockFinderBFS.scan();
        return blockFinderBFS.getFoundBlocks();
    }
}
