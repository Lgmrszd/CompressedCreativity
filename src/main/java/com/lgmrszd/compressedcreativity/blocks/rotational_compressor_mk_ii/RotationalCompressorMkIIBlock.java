package com.lgmrszd.compressedcreativity.blocks.rotational_compressor_mk_ii;

import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.world.IBlockReader;

public class RotationalCompressorMkIIBlock extends HorizontalKineticBlock {

    public static final EnumProperty<RotationalCompressorMkIIPart> PART = EnumProperty.create("part", RotationalCompressorMkIIPart.class);

    public RotationalCompressorMkIIBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(PART, RotationalCompressorMkIIPart.HEAD));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return null;
    }

    @Override
    public ActionResultType onWrenched(BlockState state, ItemUseContext context) {
        return ActionResultType.PASS;
    }
}
