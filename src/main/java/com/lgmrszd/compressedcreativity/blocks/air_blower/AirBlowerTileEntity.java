package com.lgmrszd.compressedcreativity.blocks.air_blower;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.lgmrszd.compressedcreativity.network.IObserveTileEntity;
import com.lgmrszd.compressedcreativity.network.ObservePacket;
import com.simibubi.create.content.contraptions.components.fan.AirCurrent;
import com.simibubi.create.content.contraptions.components.fan.IAirCurrentSource;
import com.simibubi.create.content.contraptions.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AirBlowerTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IObserveTileEntity, IAirCurrentSource {

    private static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);

    public AirCurrent airCurrent;
    protected int entitySearchCooldown;
    protected int airCurrentUpdateCooldown;
    protected boolean updateAirFlow;
//    protected boolean isWorking;

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airBuffer;
    private float airUsage = 0.0f;

    public AirBlowerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(
                        CommonConfig.AIR_BLOWER_PRESSURE_TIER.get()
                                .getPressureTierDefinedOrCustom(
                                        PressureTierConfig.CustomTier.AIR_BLOWER_CUSTOM_TIER
                                ),
                        CommonConfig.AIR_BLOWER_VOLUME.get()
                );
        airHandlerCap = LazyOptional.of(() -> airHandler);

        airCurrent = new AirCurrent(this);
        updateAirFlow = true;

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking){
        ObservePacket.send(worldPosition, 0);
        // "Pressure Stats:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".tooltip.pressure_summary")));
        // "Pressure:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".tooltip.pressure")
                        .withStyle(ChatFormatting.GRAY)));
        // "0.0bar"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TextComponent(" " + airHandler.getPressure())
                        .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".unit.bar"))
                        .withStyle(ChatFormatting.AQUA)));
        // "Air:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".tooltip.air")
                        .withStyle(ChatFormatting.GRAY)));
        // "0.0mL"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TextComponent(" " + airHandler.getAir())
                        .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".unit.air"))
                        .withStyle(ChatFormatting.AQUA)));
        // "Air usage:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".tooltip.air_usage")
                        .withStyle(ChatFormatting.GRAY)));
        // "0.0mL/t"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TextComponent(" " + airUsage)
                        .append(new TranslatableComponent(CompressedCreativity.MOD_ID + ".unit.air_per_tick"))
                        .withStyle(ChatFormatting.AQUA)));
        return true;
    }


    private float calculateAirUsage(float pressure) {
        return (float) Math.floor(pressure * CommonConfig.AIR_BLOWER_AIR_USAGE_PER_BAR.get().floatValue() * 100) / 100;
//        return (float) Math.floor((double) airHandler.getPressure() * 10) / 10 * CommonConfig.AIR_BLOWER_AIR_USAGE_PER_BAR.get().floatValue();
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


    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        boolean server = !level.isClientSide || isVirtual();

        airUsage = calculateAirUsage(airHandler.getPressure());

        if (server) {
            if (airCurrentUpdateCooldown-- <= 0) {
                airCurrentUpdateCooldown = AllConfigs.SERVER.kinetics.fanBlockCheckRate.get();
                updateAirFlow = true;
            }


            if (airHandler.getPressure() > CommonConfig.AIR_BLOWER_WORK_PRESSURE.get()) {
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

        if (airHandler.getPressure() > CommonConfig.AIR_BLOWER_WORK_PRESSURE.get()) {
            airCurrent.tick();
            if ((airHandler.getPressure() > CommonConfig.AIR_BLOWER_OVERWORK_PRESSURE.get())) airCurrent.tick();
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

// TODO: check if other overrides are needed

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
//        compound.putBoolean("isWorking", isWorking);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
//        isWorking = compound.getBoolean("isWorking");
        if (clientPacket)
            airCurrent.rebuild();
    }

    @Override
    public void onObserved(ServerPlayer var1, ObservePacket var2) {

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
        float speed_ratio = 256f / calculateAirUsage(airHandler.getDangerPressure());
        return airHandler.getPressure() > CommonConfig.AIR_BLOWER_WORK_PRESSURE.get() ? (float) (Math.ceil(airUsage * speed_ratio / 8) * 8) : 0.0f;
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
}
