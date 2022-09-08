package com.lgmrszd.compressedcreativity.blocks.axis_pressure_tube;

import com.simibubi.create.content.contraptions.relays.elementary.BracketedTileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AxisPressureTubeTileEntity extends SmartTileEntity {
    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;
    public AxisPressureTubeTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(type, pos, state, PressureTier.TIER_ONE, 1000);
    }
    protected AxisPressureTubeTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier tier, int volume) {
        super(type, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(tier, volume);
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }
//
//    public AxisPressureTubeTileEntity basic(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        return new AxisPressureTubeTileEntity(type, pos, state, PressureTier.TIER_ONE, 1000);
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

    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);
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
        Direction.Axis axis = getBlockState().getValue(AxisPressureTubeBlock.AXIS);
        return dir == null || dir.getAxis() == axis;
    }

}
