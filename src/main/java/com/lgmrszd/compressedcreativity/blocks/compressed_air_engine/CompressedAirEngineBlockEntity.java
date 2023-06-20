package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.lgmrszd.compressedcreativity.index.CCLang;
import com.lgmrszd.compressedcreativity.network.IObserveTileEntity;
import com.lgmrszd.compressedcreativity.network.ObservePacket;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.utility.Lang;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.ChatFormatting;
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

public class CompressedAirEngineBlockEntity extends GeneratingKineticBlockEntity implements IPneumaticTileEntity, IObserveTileEntity {

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airUsage, airBuffer;
//    private float currentSpeed;
//    private boolean updateRotation;
    private boolean working = false;
    public CompressedAirEngineBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(
                        PressureTierConfig.CustomTier.COMPRESSED_AIR_ENGINE_TIER,
                        CommonConfig.COMPRESSED_AIR_ENGINE_VOLUME.get());
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ObservePacket.send(worldPosition, 0);
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        // "Pressure Stats:"
        CCLang.translate("tooltip.pressure_summary")
                .forGoggles(tooltip);
        // "Pressure:"
        CCLang.translate("tooltip.pressure")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0bar"
        CCLang.number(airHandler.getPressure())
                .translate("unit.bar")
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        // "Air:"
        CCLang.translate("tooltip.air")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0mL"
        CCLang.number(airHandler.getAir())
                .translate("unit.air")
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        // "Air used:"
        CCLang.translate("tooltip.air_usage")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0mL/t"
        CCLang.number(
                (airHandler.getPressure() < CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get() || overStressed) ?
                        Math.min(
                                CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue(),
                                airHandler.getAir()
                        ) :
                        CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK.get().floatValue()
                )
                .translate("unit.air_per_tick")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return true;
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToTooltip(tooltip, isPlayerSneaking);
        if (airHandler.getAir() > 0 && (airHandler.getPressure() < CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get() || overStressed)) {
            added = true;
            // "This machine is currently idle:"
            CCLang.translate("tooltip.compressed_air_engine.idle_1")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip);
            // "This machine uses air even though it's not currently working"
            CCLang.translate("tooltip.compressed_air_engine.idle_2")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
        }
        return added;
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
    public void invalidate() {
        super.invalidate();
        airHandlerCap.invalidate();
    }

    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        boolean server = level != null && !level.isClientSide || isVirtual();

        if (server) {
            if (airHandler.getAir() > 0) {
                airBuffer += Math.min(airUsage, airHandler.getAir());
                if (airBuffer >= 1) {
                    int toRemove = (int) airBuffer;
                    airHandler.addAir(-toRemove);
                    airBuffer -= toRemove;
                }
            }
            if(working) {
                if (isOverStressed())
                    airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue();
                else
                    airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK.get().floatValue();
                if (airHandler.getPressure() < CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get()) {
                    working = false;
                    airUsage = CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue();
                    updateGeneratedRotation();
                }
            } else {
                if (airHandler.getPressure() >= CommonConfig.COMPRESSED_AIR_ENGINE_WORK_PRESSURE.get()) {
                    working = true;
                    airUsage = overStressed ?
                            CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_IDLE.get().floatValue() :
                            CommonConfig.COMPRESSED_AIR_ENGINE_AIR_USAGE_WORK.get().floatValue();
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
