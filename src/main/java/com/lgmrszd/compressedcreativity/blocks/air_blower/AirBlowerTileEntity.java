package com.lgmrszd.compressedcreativity.blocks.air_blower;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.blocks.common.IPneumaticTileEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.lgmrszd.compressedcreativity.index.CCLang;
import com.lgmrszd.compressedcreativity.network.IObserveTileEntity;
import com.lgmrszd.compressedcreativity.network.ObservePacket;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.fan.IAirCurrentSource;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.contraptions.goggles.IHaveHoveringInformation;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AirBlowerTileEntity extends SmartTileEntity implements IHaveHoveringInformation, IHaveGoggleInformation, IObserveTileEntity, IAirCurrentSource, IPneumaticTileEntity {

    protected static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);

    public AirCurrent airCurrent;
    protected int entitySearchCooldown;
    protected int airCurrentUpdateCooldown;
    protected boolean updateAirFlow;
//    protected boolean isWorking;

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airBuffer;
    private float airUsage = 0.0f;

    private float processing_status = 0.0f;
//            processing_speed = 0.0f;

    public AirBlowerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        this(
                type,
                pos,
                state,
                PressureTierConfig.CustomTier.AIR_BLOWER_TIER,
                CommonConfig.AIR_BLOWER_VOLUME.get()
        );
    }

    protected AirBlowerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, PressureTier pressureTier, int volume) {
        super(type, pos, state);
        airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(pressureTier, volume);
        airHandlerCap = LazyOptional.of(() -> airHandler);

        airCurrent = new AirCurrent(this);
        updateAirFlow = true;

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//        if (airHandler.getPressure() < 1) {
//            CCLang.translate("tooltip.not_enough_pressure")
//                    .style(ChatFormatting.GOLD)
//                    .forGoggles(tooltip);
//            CCLang.translate("tooltip.not_enough_pressure_2")
//                    .style(ChatFormatting.GRAY)
//                    .forGoggles(tooltip);
//            return true;
//        }
        return false;
    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking){
        ObservePacket.send(worldPosition, 0);
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
//        if (airHandler.getPressure() <= CommonConfig.AIR_BLOWER_WORK_PRESSURE.get()) {
//            return true;
//        }
        // "Air usage:"
        CCLang.translate("tooltip.air_usage")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        // "0.0mL/t"
        CCLang.number(airUsage)
                .translate("unit.air_per_tick")
                .style(ChatFormatting.AQUA)
                .forGoggles(tooltip, 1);
        CCLang.translate("tooltip.processing_speed")
                .style(ChatFormatting.GRAY)
                .add(
                        CCLang.number(calculateProcessingSpeed())
                                .style(ChatFormatting.AQUA)
                )
                .forGoggles(tooltip, 1);
        return true;
    }


    protected float calculateAirUsage() {
        return Math.max(1f, (float) Math.floor(airHandler.getPressure() * CommonConfig.AIR_BLOWER_AIR_USAGE_PER_BAR.get().floatValue() * 100) / 100);
//        return (float) Math.floor((double) airHandler.getPressure() * 10) / 10 * CommonConfig.AIR_BLOWER_AIR_USAGE_PER_BAR.get().floatValue();
    }

    protected float calculateProcessingSpeed() {
        float pressure = airHandler.getPressure();
        if (pressure < 1) {
            float x = pressure - 1;
            return (1 - x*x*x*x);
        } else if (pressure < 3.9) {
            float x = (pressure - 1) / 3.9f;
            return 1 + x*x;
        } else return 2;
    }

    public void updateAirHandler() {
        ArrayList<Direction> sides = new ArrayList<>();
        for (Direction side: new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN}) {
            if (canConnectPneumatic(side)) {
                sides.add(side);
            }
        }
        airHandler.setConnectedFaces(sides);
        logger.debug("Updated Air Handler! Side: " + getBlockState().getValue(AirBlowerBlock.FACING));
    }


    public void extraAirCurrentTick() {
    }

    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        boolean server = !level.isClientSide || isVirtual();

        airUsage = calculateAirUsage();

        if (server) {
            if (airCurrentUpdateCooldown-- <= 0) {
                airCurrentUpdateCooldown = AllConfigs.SERVER.kinetics.fanBlockCheckRate.get();
                updateAirFlow = true;
            }


            if (airHandler.getPressure() > 0) {
                airBuffer += airUsage;
                if (airBuffer > 1f) {
                    int toRemove = Math.min((int) airBuffer, airHandler.getAir());
                    airHandler.addAir(-toRemove);
                    airBuffer -= toRemove;
                }
            }
        }

        if (updateAirFlow) {
            updateAirFlow = false;
            airCurrent.rebuild();
            sendData();
        }

        if (airHandler.getPressure() <= 0) {
            return;
        }

        if (entitySearchCooldown-- <= 0) {
            entitySearchCooldown = 5;
            airCurrent.findEntities();
        }

        if (airHandler.getPressure() > 0) {

            processing_status += calculateProcessingSpeed();
            if (Math.floor(processing_status) > 1) {
                int air_ticks = (int) Math.floor(processing_status);
                for (int i = air_ticks; i > 0; i--) {
                    airCurrent.tick();
                    if (server) extraAirCurrentTick();
                }
                processing_status -= air_ticks;
            }
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updateAirHandler();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        airHandlerCap.invalidate();
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
        Direction orientation = getBlockState().getValue(AirBlowerBlock.FACING);
        return dir != orientation;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
        if (clientPacket)
            airCurrent.rebuild();
    }

    @Nullable
    @Override
    public AirCurrent getAirCurrent() {
        return airCurrent;
    }

    @Nullable
    @Override
    public Level getAirCurrentWorld() {
        return level;
    }

    @Override
    public BlockPos getAirCurrentPos() {
        return worldPosition;
    }

    @Override
    public float getSpeed() {
        float speed = 256f * airHandler.getPressure() / airHandler.getDangerPressure();
        return (float) (Math.ceil(speed / 8) * 8);
    }

    @Override
    public float getMaxDistance() {
        float speed = Math.abs(this.getSpeed());
        float distanceFactor = Math.min(speed / 256f, 1);
        return Mth.lerp(distanceFactor, 3, 12);
    }

    @Override
    public Direction getAirflowOriginSide() {
        return this.getBlockState().getValue(AirBlowerBlock.FACING);
    }

    @Nullable
    @Override
    public Direction getAirFlowDirection() {
        return getBlockState().getValue(AirBlowerBlock.FACING);
    }

    @Override
    public boolean isSourceRemoved() {
        return remove;
    }

    @Override
    public float getDangerPressure() {
        return airHandler.getDangerPressure();
    }
}
