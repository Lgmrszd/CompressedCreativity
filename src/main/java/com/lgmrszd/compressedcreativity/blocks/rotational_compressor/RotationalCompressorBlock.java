package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.base.IRotate;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.core.ModSounds;
import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketSpawnParticle;
import me.desht.pneumaticcraft.common.particle.AirParticleData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;


//public class RotationalCompressorBlock extends HorizontalKineticBlock implements ITE<RotationalCompressorTileEntity>, IRotate {
public class RotationalCompressorBlock extends HorizontalKineticBlock implements IRotate {

    public static final VoxelShape shape = Block.box(0, 0, 0, 16, 12, 16);

    public RotationalCompressorBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return RotationalCompressorBlock.shape;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState blockState, IBlockReader iBlockReader) {
        return CCTileEntities.ROTATIONAL_COMPRESSOR.create();
    }

    @Override
    public boolean hasShaftTowards(IWorldReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        TileEntity te = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof RotationalCompressorTileEntity) {
            RotationalCompressorTileEntity rcte = (RotationalCompressorTileEntity) te;
            rcte.updateAirHandler();
        }
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof RotationalCompressorTileEntity) {
                te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent(handler -> {
                    if (handler.getAir() > 0) {
                        NetworkHandler.sendToAllTracking(new PacketSpawnParticle(AirParticleData.DENSE, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 0.0D, 0.0D, 0.0D, (int)(5.0F * handler.getPressure()), 1.0D, 1.0D, 1.0D), world, pos);
                        world.playSound((PlayerEntity)null, pos, (SoundEvent) ModSounds.SHORT_HISS.get(), SoundCategory.BLOCKS, 0.3F, 0.8F);
//                        world.explode(null, pos.getX() + 0.5F, pos.getY() + 0.5, pos.getZ() + 0.5F, 1.0F, false, Explosion.Mode.BREAK);
//                        world.explode(null, pos.getX() + 0.5F, pos.getY() + 0.5, pos.getZ() + 0.5F, 1.5F, Explosion.Mode.NONE);
                    }
                });
            }
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.FAST;
    }



//    @Override
//    public BlockState updateAfterWrenched(BlockState newState, ItemUseContext context) {
//        BlockState updatedState = super.updateAfterWrenched(newState, context);
//        TileEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
//        if (te instanceof RotationalCompressorTileEntity) {
//            RotationalCompressorTileEntity rcte = (RotationalCompressorTileEntity) te;
//            rcte.updateAirHandler();
//        }
//        return updatedState;
//    }

    //    @Override
//    public Class<RotationalCompressorTileEntity> getTileEntityClass() {
//        return RotationalCompressorTileEntity.class;
//    }
}