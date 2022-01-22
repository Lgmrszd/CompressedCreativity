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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AirBlowerTileEntity extends SmartTileEntity implements IHaveGoggleInformation, IObserveTileEntity, IAirCurrentSource {

    public static final int
            VOLUME = 1000;
    public static final double
            DANGER_PRESSURE = 5.0D,
            CRITICAL_PRESSURE = 7.0D,
            OVERWORK_PRESSURE = 2.0D,
            BASE_USAGE = 10.0D;

    public AirCurrent airCurrent;
    protected int entitySearchCooldown;
    protected int airCurrentUpdateCooldown;
    protected boolean updateAirFlow;
//    protected boolean isWorking;

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;

    private float airBuffer;

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
        tooltip.add(new StringTextComponent("      Test!"));
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
        return true;
    }


    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);

        boolean server = !level.isClientSide || isVirtual();

        if (server) {
            if (airCurrentUpdateCooldown-- <= 0) {
                airCurrentUpdateCooldown = AllConfigs.SERVER.kinetics.fanBlockCheckRate.get();
                updateAirFlow = true;
            }
//            isWorking = airHandler.getPressure() > WORK_PRESSURE;
//            isWorking = airHandler.getPressure() > 0;
            if (airHandler.getPressure() > 0) {
                airBuffer += BASE_USAGE;
                if (airHandler.getPressure() > OVERWORK_PRESSURE) airBuffer += BASE_USAGE;
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

        if ((airHandler.getPressure() > OVERWORK_PRESSURE)) airCurrent.tick();
        airCurrent.tick();
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
        return airHandler.getPressure() > 0 ? (airHandler.getPressure() > OVERWORK_PRESSURE ? 64.0f : 32.0f) : 0.0f;
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
