package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class AirHandlerBacktankBlockEntity implements IAirHandlerMachine {
    protected static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);
    private final float MAX_PRESSURE = 3;
    private final int volume;
    private final int max_volume;
    private final CopperBacktankTileEntity copperBacktankBE;
    private IAirHandlerMachine connectedAirHandler;

    public AirHandlerBacktankBlockEntity(CopperBacktankTileEntity copperBacktankBE) {
        this.copperBacktankBE = copperBacktankBE;
        this.max_volume = 2 * 900;
//        this.max_volume = 2 * BackTankUtil.maxAir(copperBacktankBE.setCapacityEnchantLevel());
        this.volume = (int) (max_volume / MAX_PRESSURE);
        this.connectedAirHandler = null;
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
        if (!(ownerTE instanceof CopperBacktankTileEntity cbte)) return;
        if (this.copperBacktankBE != cbte) return;
        Level world = Objects.requireNonNull(ownerTE.getLevel());
        if (!world.isClientSide) {
            disperseAir();
        }
    }

    private void disperseAir() {
        // Basically copying code from MachineAirHandler
        // 1. Since we only have one connection we don't need lists
        if (connectedAirHandler == null) {
            logger.debug("Are we there yet");
            BlockEntity blockEntityBelow = Objects.requireNonNull(copperBacktankBE.getLevel())
                    .getBlockEntity(copperBacktankBE.getBlockPos().relative(Direction.DOWN));
            if (blockEntityBelow == null) return;
            LazyOptional<IAirHandlerMachine> lazyCap = blockEntityBelow.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY);
            lazyCap.ifPresent((cap) -> {
                connectedAirHandler = cap;
                lazyCap.addListener(self -> {
                    connectedAirHandler = null;
                });
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
        return MAX_PRESSURE * getAir() / max_volume;
    }

    @Override
    public int getAir() {
        return 2 * copperBacktankBE.getAirLevel();
    }

    @Override
    public void addAir(int amount) {
        int diff = getAir() + amount;
        setAir(diff);
    }

    private void setAir(int amount) {
        copperBacktankBE.setAirLevel(amount / 2);
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
