package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import com.simibubi.create.content.contraptions.processing.InWorldProcessing;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.heat.IHeatExchangerLogic;
import me.desht.pneumaticcraft.common.heat.HeatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdvancedAirBlowerTileEntity extends AirBlowerTileEntity {
    private ItemStack mesh;
    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;


    public AdvancedAirBlowerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        heatCap = LazyOptional.of(() -> heatExchanger);
        heatExchanger.setThermalCapacity(1);
        mesh = ItemStack.EMPTY;
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean added = super.addToTooltip(tooltip, isPlayerSneaking);
        if (!mesh.isEmpty()) {
            if (mesh.getItem() instanceof MeshItem meshItem) {
                tooltip.add(new TextComponent(""));
                tooltip.add(new TextComponent("Mesh installed type: " + meshItem.getMeshType()));
            }
            return true;
        }
        return added;
    }

    public int getTintColor(int tintIndex) {
        if (tintIndex == 0)
            return HeatUtil.getColourForTemperature(heatExchanger.getTemperatureAsInt()).getRGB();
        return HeatUtil.getColourForTemperature(300).getRGB();
    }

    protected ItemStack getMesh() {
        return mesh.copy();
    }

    @Override
    public void initialize() {
        super.initialize();
        updateHeatExchanger();
    }

    public Mesh.MeshType getMeshType() {
        return getMesh().getItem() instanceof MeshItem meshItem ? meshItem.getMeshType() : null;
    }

    protected void setMesh(ItemStack stack) {
        mesh = stack.copy();
        mesh.setCount(1);
        setChanged();
        sendData();
    }

    public Optional<InWorldProcessing.Type> getProcessingType() {
        if (getMesh().getItem() instanceof MeshItem meshItem) {
            if (meshItem.getMeshType() == Mesh.MeshType.WATER) {
                return Optional.of(InWorldProcessing.Type.SPLASHING);
            }
        }
        if (heatExchanger.getTemperatureAsInt() > 473)
            return Optional.of(InWorldProcessing.Type.BLASTING);
        if (heatExchanger.getTemperatureAsInt() > 373) {
            if (getMesh().getItem() instanceof MeshItem meshItem) {
                if (meshItem.getMeshType() == Mesh.MeshType.HAUNT) {
                    return Optional.of(InWorldProcessing.Type.HAUNTING);
                }
            }
            return Optional.of(InWorldProcessing.Type.SMOKING);
        }
        return Optional.empty();
    }

    public void updateHeatExchanger() {
        ArrayList<Direction> sides = new ArrayList<>();
        for (Direction side: new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN}) {
            if (canConnectPneumatic(side)) {
                sides.add(side);
            }
        }
        Direction[] sides_2 = new Direction[sides.size()];
        heatExchanger.initializeAsHull(getLevel(), getBlockPos(), (levelAccessor, blockPos) -> true, sides.toArray(sides_2));
        logger.debug("Updated Heat Exchanger! Side: " + getBlockState().getValue(AirBlowerBlock.FACING));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PNCCapabilities.HEAT_EXCHANGER_CAPABILITY && canConnectPneumatic(side)) {
            return heatCap.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        super.tick();
        heatExchanger.tick();
        setChanged();
        sendData();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        heatCap.invalidate();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("mesh", getMesh().serializeNBT());
        compound.put("HeatExchanger", heatExchanger.serializeNBT());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        mesh = ItemStack.of(compound.getCompound("mesh"));
        heatExchanger.deserializeNBT(compound.getCompound("HeatExchanger"));
        super.read(compound, clientPacket);
    }
}
