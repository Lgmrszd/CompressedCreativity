package com.lgmrszd.compressedcreativity.blocks.air_blower;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
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
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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

    public static final int
            VOLUME = 500;
    public static final double
            DANGER_PRESSURE = 5.0D,
            CRITICAL_PRESSURE = 7.0D,
            OVERWORK_PRESSURE = 4.8D;
    public static final double WORKING_PRESSURE = 0.5;

    public AirCurrent airCurrent;
    protected int entitySearchCooldown;
    protected int airCurrentUpdateCooldown;
    protected boolean updateAirFlow;
//    protected boolean isWorking;

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airBuffer;
    private float airUsage = 0.0f;

    public AirBlowerTileEntity(TileEntityType<?> typeIn) {
        super(typeIn);
        airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler((float) DANGER_PRESSURE, (float) CRITICAL_PRESSURE, VOLUME);
        airHandlerCap = LazyOptional.of(() -> airHandler);

        airCurrent = new AirCurrent(this);
        updateAirFlow = true;

    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {

    }

    public boolean addToGoggleTooltip(List<ITextComponent> tooltip, boolean isPlayerSneaking){
        ObservePacket.send(worldPosition, 0);
        // "Pressure Stats:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.pressure_summary")));
        // "Pressure:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.pressure")
                        .withStyle(TextFormatting.GRAY)));
        // "0.0bar"
        tooltip.add(componentSpacing.plainCopy()
                .append(new StringTextComponent(" " + airHandler.getPressure())
                        .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".unit.bar"))
                        .withStyle(TextFormatting.AQUA)));
        // "Air:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.air")
                        .withStyle(TextFormatting.GRAY)));
        // "0.0mL"
        tooltip.add(componentSpacing.plainCopy()
                .append(new StringTextComponent(" " + airHandler.getAir())
                        .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".unit.air"))
                        .withStyle(TextFormatting.AQUA)));
        // "Air usage:"
        tooltip.add(componentSpacing.plainCopy()
                .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".tooltip.rotational_compressor.air_usage")
                        .withStyle(TextFormatting.GRAY)));
        // "0.0mL/t"
        tooltip.add(componentSpacing.plainCopy()
                .append(new StringTextComponent(" " + airUsage)
                        .append(new TranslationTextComponent(CompressedCreativity.MOD_ID + ".unit.air_per_tick"))
                        .withStyle(TextFormatting.AQUA)));
        return true;
    }


    private float calculateAirUsage() {
        return (float) Math.floor((double) airHandler.getPressure() * 10) / 10 * 4;
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

        airUsage = calculateAirUsage();

        if (server) {
            if (airCurrentUpdateCooldown-- <= 0) {
                airCurrentUpdateCooldown = AllConfigs.SERVER.kinetics.fanBlockCheckRate.get();
                updateAirFlow = true;
            }


            if (airHandler.getPressure() > WORKING_PRESSURE) {
                airBuffer += airUsage;
//                if (airHandler.getPressure() > OVERWORK_PRESSURE) airBuffer += airHandler.getPressure() * 4;
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

        if (airHandler.getPressure() > WORKING_PRESSURE) {
            airCurrent.tick();
            if ((airHandler.getPressure() > OVERWORK_PRESSURE)) airCurrent.tick();
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.updateAirHandler();
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        this.updateAirHandler();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        airHandlerCap.invalidate();
    }

    @Override
    public void clearCache() {
        super.clearCache();
        updateAirHandler();
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
    public void write(CompoundNBT compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("AirHandler", airHandler.serializeNBT());
//        compound.putBoolean("isWorking", isWorking);
    }

    @Override
    protected void fromTag(BlockState state, CompoundNBT compound, boolean clientPacket) {
        super.fromTag(state, compound, clientPacket);
        airHandler.deserializeNBT(compound.getCompound("AirHandler"));
//        isWorking = compound.getBoolean("isWorking");
        if (clientPacket)
            airCurrent.rebuild();
    }

    @Override
    public void onObserved(ServerPlayerEntity var1, ObservePacket var2) {

    }

    @Nullable
    @Override
    public AirCurrent getAirCurrent() {
        return airCurrent;
    }

    @Nullable
    @Override
    public World getAirCurrentWorld() {
        return level;
    }

    @Override
    public BlockPos getAirCurrentPos() {
        return worldPosition;
    }

    @Override
    public float getSpeed() {
        return airHandler.getPressure() > WORKING_PRESSURE ? (float) (Math.ceil((airUsage / 10 * 128.0f) / 8.0f) * 8.0f) : 0.0f;
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
