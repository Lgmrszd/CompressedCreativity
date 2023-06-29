package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AirHandlerBacktankItem extends IAirHandlerItem.Provider {
    private final float MAX_PRESSURE = 3;
    private final int RATIO = 2;
    private final int volume;
//    private final int maxVolume;

    private final LazyOptional<IAirHandlerItem> holder = LazyOptional.of(() -> this);
    private final ItemStack backtank;

    public AirHandlerBacktankItem(ItemStack backtank) {
        this.backtank = backtank;
        int maxVolume = (RATIO * BacktankUtil.maxAir(backtank));
        this.volume = (int) (maxVolume / MAX_PRESSURE);
    }

    @NotNull
    @Override
    public ItemStack getContainer() {
        return backtank;
    }

    @Override
    public float getPressure() {
        return (float) getAir() / volume;
    }

    @Override
    public float maxPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public int getAir() {
        float air = RATIO * BacktankUtil.getAir(this.backtank);
//        int airFloor = (int) Math.round(air);
        return Math.round(air);
    }

    @Override
    public void addAir(int i) {
        BacktankUtil.consumeAir(null, backtank, -1.0f * i / RATIO);
    }

    @Override
    public int getBaseVolume() {
        return volume;
    }

    @Override
    public int getVolume() {
        return volume;
    }

    @Override
    public void setBaseVolume(int i) {

    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY.orEmpty(cap, holder);
    }
}
