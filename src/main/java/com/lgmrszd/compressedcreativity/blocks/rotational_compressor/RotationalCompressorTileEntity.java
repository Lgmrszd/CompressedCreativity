package com.lgmrszd.compressedcreativity.blocks.rotational_compressor;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.lgmrszd.compressedcreativity.index.CCLang;
import com.lgmrszd.compressedcreativity.network.IObserveTileEntity;
import com.lgmrszd.compressedcreativity.network.ObservePacket;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.foundation.utility.Lang;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RotationalCompressorTileEntity extends KineticTileEntity implements IObserveTileEntity, IPneumaticTileEntity {

    private static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);


    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;
    private double airGeneratedPerTick = 0.0f;
    private boolean updateGeneratedAir = true;
    private boolean isWrongDirection = false;
//    private final Map<IAirHandlerMachine, List<Direction>> airHandlerMap = new IdentityHashMap();

    private float airBuffer = 0f;

    public RotationalCompressorTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(
                        CommonConfig.ROTATIONAL_COMPRESSOR_PRESSURE_TIER.get()
                                .getPressureTierDefinedOrCustom(
                                        PressureTierConfig.CustomTier.ROTATIONAL_COMPRESSOR_CUSTOM_TIER
                                ),
                        CommonConfig.ROTATIONAL_COMPRESSOR_VOLUME.get());
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
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
        // "Air generated:"
        CCLang.translate("tooltip.air_production")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0mL/t"
        CCLang.number(airGeneratedPerTick)
                .translate("unit.air_per_tick")
                .style(ChatFormatting.AQUA)
                .space()
                .add(Lang.translate("gui.goggles.at_current_speed")
                        .style(ChatFormatting.DARK_GRAY))
                .forGoggles(tooltip, 1);
        return true;
    }

    // TODO: Need to make data consistent between server and client
    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        ObservePacket.send(worldPosition, 0);
        boolean added = super.addToTooltip(tooltip, isPlayerSneaking);
        if (isWrongDirection) {
            // "Rotation Direction Requirement:"
            CCLang.translate("tooltip.rotational_compressor.wrong_direction")
                    .style(ChatFormatting.GOLD)
                    .forGoggles(tooltip);
            // "This machine would not work with rotation in this direction"
            CCLang.translate("tooltip.rotational_compressor.wrong_direction_desc")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
            added = true;
        }
        return added;
    }

    @Override
    public float calculateStressApplied() {
        float impact = CommonConfig.ROTATIONAL_COMPRESSOR_STRESS.get()/256f;
        this.lastStressApplied = impact;
        return impact;
    }


    public void initialize() {
        super.initialize();
        this.updateAirHandler();
    }


// TODO: check if overrides needed

//    @Override
//    public void handleUpdateTag(BlockState state, CompoundTag tag) {
//        super.handleUpdateTag(state, tag);
//        this.updateAirHandler();
//    }


//    @Override
//    public void clearCache() {
//        super.clearCache();
//        updateAirHandler();
//    }


    public void setRemoved() {
        super.setRemoved();
        airHandlerCap.invalidate();
    }

    public void tick() {
        super.tick();
        airHandler.tick(this);

        // TODO: Listen to config update
        if (updateGeneratedAir) {
            Direction facing = this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
            float speed = convertToDirection(getSpeed(), facing);
            isWrongDirection = speed < 0;
            airGeneratedPerTick = (speed > 0 && isSpeedRequirementFulfilled()) ? CommonConfig.ROTATIONAL_COMPRESSOR_BASE_PRODUCTION.get() * speed / 128f : 0f;
            logger.debug("New air/t generated: " + airGeneratedPerTick);
            updateGeneratedAir = false;
            notifyUpdate();
        }

        if (getLevel() != null) {
            if(airGeneratedPerTick > 0) {
                if (!getLevel().isClientSide) {
                    airBuffer += airGeneratedPerTick;
                    if (airBuffer >= 1f) {
                        int toAdd = (int) airBuffer;
                        airHandler.addAir(toAdd);
                        airBuffer -= toAdd;
                    }
                } else {
                    spawnAirParticle();
                }
            }
        }
    }


    private void spawnAirParticle() {
        if(this.getLevel() == null) return;
        Direction orientation = getBlockState().getValue(RotationalCompressorBlock.HORIZONTAL_FACING);
        if (this.getLevel().random.nextInt(5) == 0) {
            float px = (float)this.getBlockPos().getX() + 0.5F;
            float py = (float)this.getBlockPos().getY() + 0.5F + this.getLevel().random.nextFloat() * 0.4F;
            float pz = (float)this.getBlockPos().getZ() + 0.5F;
            float f3 = 0.9F;
            float f4 = this.getLevel().random.nextFloat() * 0.4F;
            switch (orientation) {
                case EAST ->
                        this.getLevel().addParticle(ParticleTypes.POOF, (px + f3), py, (pz + f4), -0.1D, 0.0D, 0.0D);
                case WEST ->
                        this.getLevel().addParticle(ParticleTypes.POOF, (px - f3), py, (pz + f4), 0.1D, 0.0D, 0.0D);
                case SOUTH ->
                        this.getLevel().addParticle(ParticleTypes.POOF, (px + f4), py, (pz + f3), 0.0D, 0.0D, -0.1D);
                case NORTH ->
                        this.getLevel().addParticle(ParticleTypes.POOF, (px + f4), py, (pz - f3), 0.0D, 0.0D, 0.1D);
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


    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
        if (clientPacket) {
            compound.putDouble("airGeneratedPerTick", airGeneratedPerTick);
            compound.putBoolean("isWrongDirection", isWrongDirection);
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
        if (clientPacket) {
            airGeneratedPerTick = compound.getDouble("airGeneratedPerTick");
            isWrongDirection = compound.getBoolean("isWrongDirection");
        }
    }

    public boolean shouldRenderNormally() {
        return true;
    }

    @Override
    public void onObserved(ServerPlayer var1, ObservePacket var2) {
//        logger.debug("I am being observed!");
    }

    @Override
    public float getDangerPressure() {
        return airHandler.getDangerPressure();
    }
}
