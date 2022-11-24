package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.content.curiosities.armor.CopperBacktankTileEntity;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.util.PneumaticCraftUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class AirHandlerBacktankBlockEntity implements IAirHandlerMachine {
    private final float MAX_PRESSURE = 3;
    private final int volume;
    private final int max_volume;
    private CopperBacktankTileEntity copperBacktankBE;

    public AirHandlerBacktankBlockEntity(CopperBacktankTileEntity copperBacktankBE) {
        this.copperBacktankBE = copperBacktankBE;
        this.max_volume = 2 * 900;
//        this.max_volume = 2 * BackTankUtil.maxAir(copperBacktankBE.setCapacityEnchantLevel());
        this.volume = (int) (max_volume / MAX_PRESSURE);
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
        disperseAir(ownerTE);
    }

    private void disperseAir(BlockEntity ownerTE) {

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
        return getConnectedAirHandlers(ownerTE, false);
    }

    private List<Connection> getConnectedAirHandlers(BlockEntity ownerTE, boolean onlyLowerPressure) {
        return null;
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
