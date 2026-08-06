package com.lgmrszd.compressedcreativity.content.airhandler_backtank;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AirHandlerBacktankItem implements IAirHandlerItem {
    private final float MAX_PRESSURE = 3;
    private final int RATIO = 2;
    private final int volume;

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
        return Math.round(air);
    }

    @Override
    public void addAir(int i) {
        BacktankUtil.consumeAir(null, backtank, Math.round(-1.0f * i / RATIO));
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
}
