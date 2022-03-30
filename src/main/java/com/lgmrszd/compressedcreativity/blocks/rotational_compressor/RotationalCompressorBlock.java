package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.index.CCTileEntities;
import com.lgmrszd.compressedcreativity.index.CCShapes;
import com.simibubi.create.content.contraptions.base.HorizontalKineticBlock;
import com.simibubi.create.content.contraptions.base.IRotate;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.core.ModSounds;
import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketSpawnParticle;
import me.desht.pneumaticcraft.common.particle.AirParticleData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;


//public class RotationalCompressorBlock extends HorizontalKineticBlock implements ITE<RotationalCompressorTileEntity>, IRotate {
import com.simibubi.create.content.contraptions.base.IRotate.SpeedLevel;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class RotationalCompressorBlock extends HorizontalKineticBlock implements IRotate {

//    public static final VoxelShape shape = Block.box(0, 0, 0, 16, 10, 16);

    public RotationalCompressorBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
        return CCShapes.ROTATIONAL_COMPRESSOR.get(state.getValue(HORIZONTAL_FACING)) ;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
    }


    // TODO: check if it's needed at all


//    @Override
//    public boolean hasTileEntity(BlockState state) {
//        return true;
//    }

//    @Override
//    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//        return CCTileEntities.ROTATIONAL_COMPRESSOR.create(pos, state);
//    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == state.getValue(HORIZONTAL_FACING).getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING)
                .getAxis();
    }

    // TODO: check docs for diff in onNeighborChange / neighborChanged

    @Override
    public void onNeighborChange(BlockState state, LevelReader world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, world, pos, neighbor);
        BlockEntity te = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        if (te instanceof RotationalCompressorTileEntity) {
            RotationalCompressorTileEntity rcte = (RotationalCompressorTileEntity) te;
            rcte.updateAirHandler();
        }
    }

//    @Override
//    public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, BlockPos p_60513_, boolean p_60514_) {
//        super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
//    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof RotationalCompressorTileEntity) {
                te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent(handler -> {
                    if (handler.getAir() > 0) {
                        NetworkHandler.sendToAllTracking(new PacketSpawnParticle(AirParticleData.DENSE, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 0.0D, 0.0D, 0.0D, (int)(5.0F * handler.getPressure()), 1.0D, 1.0D, 1.0D), world, pos);
                        world.playSound((Player)null, pos, (SoundEvent) ModSounds.SHORT_HISS.get(), SoundSource.BLOCKS, 0.3F, 0.8F);
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