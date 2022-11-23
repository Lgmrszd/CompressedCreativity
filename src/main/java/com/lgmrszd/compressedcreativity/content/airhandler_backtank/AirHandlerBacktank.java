package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AirHandlerBacktank extends IAirHandlerItem.Provider {
    private final float MAX_PRESSURE = 3;
    private final int volume;
    private final int max_volume;

    private final LazyOptional<IAirHandlerItem> holder = LazyOptional.of(() -> this);
    private final ItemStack backtank;

    public AirHandlerBacktank(ItemStack backtank) {
        this.backtank = backtank;
        this.max_volume = (2 * BackTankUtil.maxAir(backtank));
        this.volume = (int) (max_volume / MAX_PRESSURE);
    }

    @NotNull
    @Override
    public ItemStack getContainer() {
        return backtank;
    }

    @Override
    public float getPressure() {
        return MAX_PRESSURE * BackTankUtil.getAir(this.backtank) / BackTankUtil.maxAir(this.backtank);
    }

    @Override
    public float maxPressure() {
        return MAX_PRESSURE;
    }

    @Override
    public int getAir() {
        float air = max_volume * BackTankUtil.getAir(this.backtank) / BackTankUtil.maxAir(this.backtank);
//        int airFloor = (int) Math.round(air);
        return Math.round(air);
    }

    @Override
    public void addAir(int i) {
        BackTankUtil.consumeAir(null, backtank, (-1.0f * i * BackTankUtil.maxAir(this.backtank)) / max_volume);
    }

    @Override
    public int getBaseVolume() {
        return volume;
//        return (int) (VOLUME * BackTankUtil.getAir(this.backtank) / BackTankUtil.maxAir(this.backtank));
    }

    @Override
    public int getVolume() {
        return volume;
//        return (int) (VOLUME * BackTankUtil.getAir(this.backtank) / BackTankUtil.maxAir(this.backtank));
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
