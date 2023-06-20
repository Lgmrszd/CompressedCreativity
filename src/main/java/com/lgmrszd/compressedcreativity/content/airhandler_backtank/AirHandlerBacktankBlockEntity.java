package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class AirHandlerBacktankBlockEntity implements IAirHandlerMachine {
    private final float MAX_PRESSURE = 3;
    private final int RATIO = 2;
    private int volume;
    private int maxVolume;
    private final BacktankBlockEntity BacktankBE;
    private IAirHandlerMachine connectedAirHandler;
    private int delta;

    public AirHandlerBacktankBlockEntity(BacktankBlockEntity BacktankBE) {
        this.BacktankBE = BacktankBE;
        maxVolume = RATIO * AllConfigs.server().equipment.airInBacktank.get();
        volume = (int) (maxVolume / MAX_PRESSURE);
        delta = 0;
        connectedAirHandler = null;
    }

    public void updateVolumeFromEnchant(int capacityEnchantLevel) {
        maxVolume = RATIO * BacktankUtil.maxAir(capacityEnchantLevel);
        volume = (int) (maxVolume / MAX_PRESSURE);
    }

    @Override
    public void setPressure(float newPressure) {
//        setAir((int) (newPressure * getVolume()));
    }

    @Override
    public void setVolumeUpgrades(int newVolumeUpgrades) {

    }

    @Override
    public void enableSafetyVenting(Predicate<Float> pressureCheck, Direction dir) {

    }

    @Override
    public void disableSafetyVenting() {

    }

    @Override
    public void tick(BlockEntity ownerTE) {
        if (!(ownerTE instanceof BacktankBlockEntity bbe)) return;
        if (this.BacktankBE != bbe) return;
        Level world = Objects.requireNonNull(ownerTE.getLevel());
        if (!world.isClientSide) {
            disperseAir();
        }
        // Release overpressure
        if (delta > 5) {
            delta = 0;
        }
    }

    private void disperseAir() {
        // Basically copying code from MachineAirHandler default implementation
        // 1. Since we only have one connection we don't need lists, we simply cache AirHandler itself
        if (connectedAirHandler == null) {
            BlockEntity blockEntityBelow = Objects.requireNonNull(BacktankBE.getLevel())
                    .getBlockEntity(BacktankBE.getBlockPos().relative(Direction.DOWN));
            if (blockEntityBelow == null) return;
            LazyOptional<IAirHandlerMachine> lazyCap = blockEntityBelow.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY);
            lazyCap.ifPresent(cap -> {
                connectedAirHandler = cap;
                lazyCap.addListener(self -> connectedAirHandler = null);
            });
        }
        if (connectedAirHandler == null) return;
        if (connectedAirHandler.getPressure() > getPressure()) return;
        // 2. Get the total volume and air amount in this and connected handler
        int totalVolume = this.getVolume();
        totalVolume += connectedAirHandler.getVolume();
        int totalAir = this.getAir();
        totalAir += connectedAirHandler.getAir();
        // 3. figure out how much air will be dispersed
        int totalMachineAir = (int) ((long) totalAir * connectedAirHandler.getVolume() / totalVolume);
        int air = Math.max(0, totalMachineAir - connectedAirHandler.getAir());
        // 4. finally, actually disperse the air
        if (air != 0) {
            connectedAirHandler.addAir(air);
            addAir(-air);
        }
    }

    @Override
    public void setSideLeaking(@Nullable Direction dir) {

    }

    @Nullable
    @Override
    public Direction getSideLeaking() {
        return null;
    }

    @Override
    public List<Connection> getConnectedAirHandlers(BlockEntity ownerTE) {
        // This method is not supposed to be called
        return Collections.emptyList();
    }

    @Override
    public Tag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {

    }

    @Override
    public void setConnectedFaces(List<Direction> sides) {

    }

    @Override
    public float getPressure() {
        return ((float) getAir()) / volume;
    }

    @Override
    public int getAir() {
        return RATIO * BacktankBE.getAirLevel() + delta;
    }

    @Override
    public void addAir(int amount) {
        int total = getAir() + amount;
        int transformed = Math.min(total, maxVolume) / RATIO;
        delta = total - transformed * RATIO;
        BacktankBE.setAirLevel(transformed);
//        if (total == maxVolume) copperBacktankBE.sendData();
    }

    @Override
    public int getBaseVolume() {
        return volume;
    }

    @Override
    public void setBaseVolume(int newBaseVolume) {

    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public float maxPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public float getDangerPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public float getCriticalPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public void printManometerMessage(Player player, List<Component> curInfo) {
        curInfo.add(new TranslatableComponent("pneumaticcraft.gui.tooltip.pressure",
                PneumaticCraftUtils.roundNumberTo(getPressure(), 1)));
    }
}
