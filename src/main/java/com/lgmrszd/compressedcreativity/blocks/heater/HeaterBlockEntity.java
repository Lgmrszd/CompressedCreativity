package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.blocks.ITintedBlockEntity;
import com.lgmrszd.compressedcreativity.network.ForceUpdatePacket;
import com.lgmrszd.compressedcreativity.network.IUpdateBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class HeaterBlockEntity extends SmartBlockEntity implements ITintedBlockEntity, IUpdateBlockEntity {
    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;
    private int heatLevel = -1;
    public HeaterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        heatExchanger.setThermalCapacity(1);
        heatCap = LazyOptional.of(() -> heatExchanger);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PNCCapabilities.HEAT_EXCHANGER_CAPABILITY && (side != Direction.UP)) {
            return heatCap.cast();
        }
        return super.getCapability(cap, side);
    }

    public void updateHeatExchanger() {
        Direction[] sides = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};
        heatExchanger.initializeAsHull(getLevel(), getBlockPos(), (levelAccessor, blockPos) -> true, sides);
//        CompressedCreativity.LOGGER.debug("Updated Heat Exchanger!");
    }

    @Override
    public void initialize() {
        super.initialize();
        updateHeatExchanger();
    }

    public double getCoolingSpeed() {
        double heat = 0.1 * (heatExchanger.getTemperature()-373);
        return heat > 0 ? heat : 0;
//        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        boolean server = level != null && !level.isClientSide();
        int oldTemp = heatExchanger.getTemperatureAsInt();
        heatExchanger.tick();
        heatExchanger.addHeat(-getCoolingSpeed());
        if (server) {
            int diff = Math.abs(heatExchanger.getTemperatureAsInt() - oldTemp);
            if (diff > 2) {
                updateTintServer();
                setChanged();
                sendData();
            }
        }
        int oldHeatLevel = heatLevel;
        updateHeatLevel();
        if (heatLevel != oldHeatLevel) {
            if (level == null || level.isClientSide()) {
                return;
            }
            if (level.getBlockEntity(getBlockPos().above()) instanceof FluidTankBlockEntity ftbe) {
                ftbe.updateBoilerTemperature();
            }
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level != null && level.isClientSide) {
            updateTintClient();
            return;
        }
        sendData();
    }

    public void updateTintClient() {
        if (level == null) return;
        if (level.isClientSide) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
    }

    public void updateTintServer() {
        if (level == null) return;
        if (!level.isClientSide) ForceUpdatePacket.send(level, getBlockPos());
    }

    @Override
    public void forceUpdate() {
        updateTintClient();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        heatCap.invalidate();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("HeatExchanger", heatExchanger.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        heatExchanger.deserializeNBT(compound.getCompound("HeatExchanger"));
        super.read(compound, clientPacket);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    private void updateHeatLevel() {
        int temp = heatExchanger.getTemperatureAsInt();
        heatLevel = temp < 373 ? -1 :
                temp < 473 ? 0 : 1;
    }
    public float getHeatLevel() {
        return heatLevel;
    }

    @Override
    public int getTintColor(int tintIndex) {
        if (tintIndex == 0)
            return HeatUtil.getColourForTemperature(heatExchanger.getTemperatureAsInt()).getRGB();
        return HeatUtil.getColourForTemperature(300).getRGB();
    }
}
