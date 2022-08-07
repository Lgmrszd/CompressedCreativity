package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
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

public class CompressedAirEngineTileEntity extends GeneratingKineticTileEntity implements IPneumaticTileEntity {

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airUsage, airBuffer;
//    private float currentSpeed;
//    private boolean updateRotation;
    private boolean working = false;
    public CompressedAirEngineTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(
                        CommonConfig.COMPRESSED_AIR_ENGINE_PRESSURE_TIER.get()
                                .getPressureTierDefinedOrCustom(
                                        PressureTierConfig.CustomTier.COMPRESSED_AIR_ENGINE_CUSTOM_TIER
                                ),
                        CommonConfig.COMPRESSED_AIR_ENGINE_VOLUME.get());
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
        airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue();
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
                if (airHandler.getPressure() < CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get()) {
                    working = false;
                    airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue();
                    updateGeneratedRotation();
                }
            } else {
                if (airHandler.getPressure() >= CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get()) {
                    working = true;
                    airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK.get().floatValue();
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
        return convertToDirection(working ? 256 : 0, getBlockState().getValue(CompressedAirEngineBlock.HORIZONTAL_FACING));
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

    @Override
    public float getDangerPressure() {
        return airHandler.getDangerPressure();
    }
}
