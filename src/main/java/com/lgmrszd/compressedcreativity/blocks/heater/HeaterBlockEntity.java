package com.lgmrszd.compressedcreativity.blocks.heater;

import com.lgmrszd.compressedcreativity.blocks.ITintedBlockEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.index.CCLang;
import com.lgmrszd.compressedcreativity.network.ForceUpdatePacket;
import com.lgmrszd.compressedcreativity.network.IUpdateBlockEntity;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
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
import java.util.List;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HEAT_LEVEL;

public class HeaterBlockEntity extends SmartBlockEntity implements ITintedBlockEntity, IUpdateBlockEntity, IHaveGoggleInformation {
    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;
    private BlazeBurnerBlock.HeatLevel heatLevel = BlazeBurnerBlock.HeatLevel.NONE;
    public HeaterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        heatExchanger.setThermalCapacity(CommonConfig.HEATER_THERMAL_CAPACITY.get());
        heatExchanger.setThermalResistance(CommonConfig.HEATER_THERMAL_RESISTANCE.get());
        heatCap = LazyOptional.of(() -> heatExchanger);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // "Heat Stats:"
        CCLang.translate("tooltip.heat_summary")
                .forGoggles(tooltip);
        // "Temperature:"
        CCLang.translate("tooltip.temperature")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0C°"
        CCLang.number(heatExchanger.getTemperature() - 273)
                .translate("unit.temp")
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        CCLang.builder().text("[DEV] Debug info")
                .forGoggles(tooltip);
        CCLang.builder().text(String.format("Cooling speed: %f heat/tick", getCoolingSpeed()))
                .forGoggles(tooltip);
        CCLang.builder().text(String.format("Heat level: %s", heatLevel))
                .forGoggles(tooltip);
        CCLang.builder().text(String.format("Thermal capacity: %f", heatExchanger.getThermalCapacity()))
                .forGoggles(tooltip);
        CCLang.builder().text(String.format("Thermal resistance: %f", heatExchanger.getThermalResistance()))
                .forGoggles(tooltip);
        return true;
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
        double heat = CommonConfig.HEATER_TEMPERATURE_COEFFICIENT.get() * (heatExchanger.getTemperature() - CommonConfig.HEATER_STARTING_TEMPERATURE.get() - 273);
        return heat > 0 ? heat : 0;
//        return 0;
    }

    @Override
    public void tick() {
        super.tick();
        boolean server = level != null && !level.isClientSide();
        int oldTemp = heatExchanger.getTemperatureAsInt();
        if (server) {
            heatExchanger.tick();
            heatExchanger.addHeat(-getCoolingSpeed());
            int diff = Math.abs(heatExchanger.getTemperatureAsInt() - oldTemp);
            if (diff > 5) {
                updateTintServer();
                setChanged();
                sendData();
            }
        }
        BlazeBurnerBlock.HeatLevel oldHeatLevel = heatLevel;
        updateHeatLevel();
        if (heatLevel != oldHeatLevel) {
            updateBlockHeat();
//            if (level == null || level.isClientSide()) {
//                return;
//            }
//            if (level.getBlockEntity(getBlockPos().above()) instanceof FluidTankBlockEntity ftbe) {
//                ftbe.updateBoilerTemperature();
//            }
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
        int temp = heatExchanger.getTemperatureAsInt() - 273;
        heatLevel = temp < CommonConfig.HEATER_TEMPERATURE_PASSIVE.get() ? BlazeBurnerBlock.HeatLevel.NONE :
                temp < CommonConfig.HEATER_TEMPERATURE_KINDLED.get() ? BlazeBurnerBlock.HeatLevel.SMOULDERING :
                        temp < CommonConfig.HEATER_TEMPERATURE_SEETHING.get() ? BlazeBurnerBlock.HeatLevel.KINDLED :
                                BlazeBurnerBlock.HeatLevel.SEETHING;
    }

    private void updateBlockHeat() {
        BlockState blockState = getBlockState();
        BlazeBurnerBlock.HeatLevel oldHeatLevel = BlazeBurnerBlock.getHeatLevelOf(blockState);
        if (oldHeatLevel == heatLevel) return;
        if (level != null) level.setBlockAndUpdate(worldPosition, blockState.setValue(HEAT_LEVEL, heatLevel));
    }

    @Override
    public int getTintColor(int tintIndex) {
        if (tintIndex == 0)
            return HeatUtil.getColourForTemperature(heatExchanger.getTemperatureAsInt()).getRGB();
        return HeatUtil.getColourForTemperature(300).getRGB();
    }
}
