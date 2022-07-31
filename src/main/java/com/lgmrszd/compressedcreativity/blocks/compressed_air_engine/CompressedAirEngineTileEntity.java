package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CompressedAirEngineTileEntity extends GeneratingKineticTileEntity {

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airUsage, airBuffer;
//    private float currentSpeed;
//    private boolean updateRotation;
    private boolean working = false;
    public CompressedAirEngineTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(PressureTier.TIER_ONE, 1000);
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    @Override
    public void initialize() {
        super.initialize();
        updateAirHandler();
        updateGeneratedRotation();
        airUsage = 5;
        airBuffer = 0;
    }

    public void updateAirHandler() {
        ArrayList<Direction> sides = new ArrayList<>();
        for (Direction side: new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            if (canConnectPneumatic(side)) {
                sides.add(side);
            }
        }
        sides.add(Direction.UP);
        airHandler.setConnectedFaces(sides);
    }

    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        float treshold = 2.5F;
        boolean server = !level.isClientSide || isVirtual();

        if (server) {
            if (airHandler.getAir() > airUsage) {
                airBuffer += airUsage;
                if (airBuffer >= 1) {
                    int toRemove = (int) airBuffer;
                    airHandler.addAir(-toRemove);
                    airBuffer -= toRemove;
                }
            }
            if(working) {
                if (airHandler.getPressure() < treshold) {
                    working = false;
                    updateGeneratedRotation();
                }
            } else {
                if (airHandler.getPressure() > treshold) {
                    working = true;
                    updateGeneratedRotation();
                }
            }
//            airHandler.setSideLeaking(airHandler.getConnectedAirHandlers(this).isEmpty() ? Direction.UP : null);
        }

//        if (updateRotation) {
//            if (airHandler.getPressure() > treshold) {
//                currentSpeed = 64;
//            } else {
//                currentSpeed = 0;
//            }
//            updateGeneratedRotation();
//            updateRotation = false;
//        }
    }

    @Override
    public float getGeneratedSpeed() {
        return convertToDirection(working ? 64 : 0, getBlockState().getValue(CompressedAirEngineBlock.HORIZONTAL_FACING));
//        return convertToDirection(currentSpeed, getBlockState().getValue(CompressedAirEngineBlock.HORIZONTAL_FACING));
    }

    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
        if (clientPacket) {
            compound.putBoolean("working", working);
//            compound.putBoolean("updateRotation", updateRotation);
//            compound.putFloat("currentSpeed", currentSpeed);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
        if (clientPacket) {
            working = compound.getBoolean("working");
//            updateRotation = compound.getBoolean("updateRotation");
//            currentSpeed = compound.getFloat("currentSpeed");
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY && canConnectPneumatic(side)) {
            return airHandlerCap.cast();
        }
        return super.getCapability(cap, side);
    }

    public boolean canConnectPneumatic(Direction dir) {
        return dir == Direction.UP || dir == null ||
                (dir == getBlockState().getValue(CompressedAirEngineBlock.HORIZONTAL_FACING) &&
                        !getBlockState().getValue(CompressedAirEngineBlock.FRONT)) ||
                (dir == getBlockState().getValue(CompressedAirEngineBlock.HORIZONTAL_FACING).getOpposite() &&
                        !getBlockState().getValue(CompressedAirEngineBlock.BACK));
    }
}
