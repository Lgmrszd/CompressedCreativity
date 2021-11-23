package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RotationalCompressorTileEntity extends KineticTileEntity {

    private static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);

    public static final int
            STRESS = CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get(),
            VOLUME = CommonConfig.ROTATIONAL_COMPRESSOR_VOLUME.get();
    public static final double
            DANGER_PRESSURE = CommonConfig.ROTATIONAL_COMPRESSOR_DANGER_PRESSURE.get(),
            CRITICAL_PRESSURE = CommonConfig.ROTATIONAL_COMPRESSOR_CRITICAL_PRESSURE.get() + CommonConfig.ROTATIONAL_COMPRESSOR_DANGER_PRESSURE.get(),
            BASE_PRODUCTION = CommonConfig.ROTATIONAL_COMPRESSOR_BASE_PRODUCTION.get(); // Per tick per 128 RPM
    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;
    private double airGeneratedPerTick = 0.0f;
    private boolean updateGeneratedAir = true;
    private boolean isWrongDirection = false;
//    private final Map<IAirHandlerMachine, List<Direction>> airHandlerMap = new IdentityHashMap();

    private float airBuffer = 0f;

    public RotationalCompressorTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler((float) DANGER_PRESSURE, (float) CRITICAL_PRESSURE, VOLUME);
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }

    @Override
    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if (added) {
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.pressure_summary").withStyle(TextFormatting.WHITE)));
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.pressure").withStyle(TextFormatting.GRAY)));
            tooltip.add(new StringTextComponent(spacing).append(new StringTextComponent(" " + airHandler.getPressure() + " bar")).withStyle(TextFormatting.AQUA));
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.air").withStyle(TextFormatting.GRAY)));
            tooltip.add(new StringTextComponent(spacing).append(new StringTextComponent(" " + airHandler.getAir() + " mL").withStyle(TextFormatting.AQUA)));
            tooltip.add(new StringTextComponent(spacing).append((new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.air_production")).withStyle(TextFormatting.GRAY)));
            tooltip.add(new StringTextComponent(spacing).append((new StringTextComponent(" " + ((airGeneratedPerTick > 0) ? airGeneratedPerTick : 0.0f) + "mL/t ")).withStyle(TextFormatting.AQUA)).append(Lang.translate("gui.goggles.at_current_speed").withStyle(TextFormatting.DARK_GRAY)));
        }
        return added;
    }

    // TODO: Need to make data consistent between server and client
    @Override
    public boolean addToTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToTooltip(tooltip, isPlayerSneaking);
        if (isWrongDirection) {
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.wrong_direction").withStyle(TextFormatting.GOLD)));
            tooltip.add(new StringTextComponent(spacing).append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.wrong_direction_desc").withStyle(TextFormatting.GRAY)));
            added = true;
        }
        return added;
    }

    @Override
    public float calculateStressApplied() {
        float impact = STRESS/256f;
        this.lastStressApplied = impact;
        return impact;
    }


    public void initialize() {
        super.initialize();
        this.updateAirHandler();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        this.updateAirHandler();
    }

    public void setRemoved() {
        super.setRemoved();
        airHandlerCap.invalidate();
    }

    public void tick() {
        super.tick();
        airHandler.tick(this);

        if (updateGeneratedAir) {
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            float speed = convertToDirection(getSpeed(), facing);
            isWrongDirection = speed < 0;
            airGeneratedPerTick = (speed > 0 && isSpeedRequirementFulfilled()) ? BASE_PRODUCTION * speed / 128f : 0f;
            logger.debug("New air/t generated: " + airGeneratedPerTick);
            updateGeneratedAir = false;
        }

        if (getLevel() != null && !getLevel().isClientSide) {
            if(airGeneratedPerTick > 0) {
                airBuffer += airGeneratedPerTick;
                if (airBuffer >= 1f) {
                    int toAdd = (int) airBuffer;
                    airHandler.addAir(toAdd);
                    airBuffer -= toAdd;
                }
            }
        }
    }

    public void updateAirHandler() {
        ArrayList<Direction> sides = new ArrayList<>();
        for (Direction side: new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST}) {
            if (canConnectPneumatic(side)) {
                sides.add(side);
            }
        }
        airHandler.setConnectedFaces(sides);
        logger.debug("Updated Air Handler! Side: " + getBlockState().getValue(RotationalCompressorBlock.HORIZONTAL_FACING));
    }

    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        updateGeneratedAir = true;
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
        Direction orientation = getBlockState().getValue(RotationalCompressorBlock.HORIZONTAL_FACING);
        return dir != Direction.UP && dir != Direction.DOWN && dir != orientation && dir != orientation.getOpposite();
    }

    @Override
    public void clearCache() {
        super.clearCache();
        updateAirHandler();
    }

    public void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
        if(clientPacket) {
            compound.putDouble("airGeneratedPerTick", airGeneratedPerTick);
            compound.putBoolean("isWrongDirection", isWrongDirection);
        }
    }

    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
        if (clientPacket) {
            airGeneratedPerTick = compound.getDouble("airGeneratedPerTick");
            isWrongDirection = compound.getBoolean("isWrongDirection");
        }
    }

    public boolean shouldRenderNormally() {
        return true;
    }
}
