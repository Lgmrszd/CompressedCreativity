package com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.particle.AirParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BracketedPressureTubeTileEntity extends SmartTileEntity {
    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;
    public BracketedPressureTubeTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, PressureTier.TIER_ONE, 1000);
    }
    protected BracketedPressureTubeTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier tier, int volume) {
        super(type, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(tier, volume);
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }
//
//    public BracketedPressureTubeTileEntity basic(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        return new BracketedPressureTubeTileEntity(type, pos, state, PressureTier.TIER_ONE, 1000);
//    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        behaviours.add(new BracketedTileEntityBehaviour(this));
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updateAirHandler();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        airHandlerCap.invalidate();
    }

    private Direction getLeakDirection() {
        if (airHandler.getConnectedAirHandlers(this).size() == 2) {
            return null;
        }
        if (airHandler.getConnectedAirHandlers(this).size() == 1) {
            Direction dir = airHandler.getConnectedAirHandlers(this).get(0).getDirection();
            if (dir != null) return dir.getOpposite();
        }
        Direction.Axis axis = getBlockState().getValue(BracketedPressureTubeBlock.AXIS);
        for (Direction dir : Direction.values()) {
            if (dir.getAxis() == axis) return dir;
        }
        return null;
    }


    private void spawnLeakParticles(Direction dir) {
        Level world = this.getLevel();
        if (world == null) return;
        BlockPos pos = getBlockPos();
        float pressure = airHandler.getPressure();
        double mx = dir.getStepX();
        double my = dir.getStepY();
        double mz = dir.getStepZ();
        double speed = (this.airHandler.getPressure() * 0.1F);
        if (this.airHandler.getAir() <= 0) {
            if (this.airHandler.getAir() < 0 && world.random.nextBoolean()) {
                world.addParticle(AirParticleData.DENSE, (double)pos.getX() + 0.5 + mx, (double)pos.getY() + 0.5 + my, (double)pos.getZ() + 0.5 + mz, mx * speed, my * speed, mz * speed);
            }
        } else if (pressure > 1.0F || pressure > 0.5F && world.random.nextBoolean() || world.random.nextInt(3) == 0) {
            world.addParticle(AirParticleData.DENSE, (double)pos.getX() + 0.5 + mx * 0.6, (double)pos.getY() + 0.5 + my * 0.6, (double)pos.getZ() + 0.5 + mz * 0.6, mx * speed, my * speed, mz * speed);
        }

    }
    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        boolean server = !level.isClientSide || isVirtual();
        if (server) {
            airHandler.setSideLeaking(airHandler.getConnectedAirHandlers(this).size() < 2 ?
                    getLeakDirection() :
                    null);
        } else {
            if (airHandler.getPressure() != 0 &&
                    airHandler.getConnectedAirHandlers(this).isEmpty()) {
                Direction dir = getLeakDirection();
                if (dir != null) spawnLeakParticles(dir.getOpposite());
            }
        }
    }

    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY && canConnectPneumatic(side)) {
            return airHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    public void updateAirHandler() {
        ArrayList<Direction> sides = new ArrayList<>();
        for (Direction side: Direction.values()) {
            if (canConnectPneumatic(side)) {
                sides.add(side);
            }
        }
        airHandler.setConnectedFaces(sides);
    }

    public boolean canConnectPneumatic(Direction dir) {
        Direction.Axis axis = getBlockState().getValue(BracketedPressureTubeBlock.AXIS);
        return dir == null || dir.getAxis() == axis;
    }

}
