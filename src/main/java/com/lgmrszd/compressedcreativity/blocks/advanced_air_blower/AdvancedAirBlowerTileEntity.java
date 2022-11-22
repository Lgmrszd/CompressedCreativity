package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerTileEntity;
import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.lgmrszd.compressedcreativity.config.PressureTierConfig;
import com.lgmrszd.compressedcreativity.content.Mesh;
import com.lgmrszd.compressedcreativity.index.CCItems;
import com.lgmrszd.compressedcreativity.items.MeshItem;
import com.lgmrszd.compressedcreativity.network.ForceUpdatePacket;
import com.lgmrszd.compressedcreativity.network.IUpdateBlockEntity;
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
import net.minecraft.util.Mth;
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

public class AdvancedAirBlowerTileEntity extends AirBlowerTileEntity implements IUpdateBlockEntity {
    private ItemStack mesh;
    protected final IHeatExchangerLogic heatExchanger;
    private final LazyOptional<IHeatExchangerLogic> heatCap;

//    private LazyOptional<Mesh.MeshType>


    public AdvancedAirBlowerTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(
                type,
                pos,
                state,
                PressureTierConfig.CustomTier.INDUSTRIAL_AIR_BLOWER_TIER,
                CommonConfig.INDUSTRIAL_AIR_BLOWER_VOLUME.get()
        );
        heatExchanger = PneumaticRegistry.getInstance().getHeatRegistry().makeHeatExchangerLogic();
        heatCap = LazyOptional.of(() -> heatExchanger);
        heatExchanger.setThermalCapacity(5);
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

    public Mesh.IMeshType getMeshType() {
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

    @Override
    protected float calculateAirUsage() {
        return Math.max(1f, (float) Math.floor(airHandler.getPressure() * CommonConfig.AIR_BLOWER_AIR_USAGE_PER_BAR.get().floatValue() * 150) / 100);
    }

    @Override
    protected float calculateProcessingSpeed() {
        float pressure = airHandler.getPressure();
        if (pressure < 1) {
            float x = pressure - 1;
            return (1 - x*x*x*x);
        } else if (pressure < 9) {
            float x = (pressure - 1) / 8.5f;
            return 1 + x*x*3;
        } else return 4;
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
        boolean server = level != null && !level.isClientSide();
        int oldTemp = heatExchanger.getTemperatureAsInt();
        heatExchanger.tick();
        if (server) {
            int diff = Math.abs(heatExchanger.getTemperatureAsInt() - oldTemp);
            if (diff > 2) {
                logger.debug("Temp diff: {}", diff);
                updateTintServer();
                setChanged();
                sendData();
            }
        }   
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (level != null && level.isClientSide) updateTintClient();
        if (mesh.getItem() instanceof MeshItem meshItem && meshItem.getMeshType() == Mesh.MeshType.WATER) {
            if (heatExchanger.getTemperatureAsInt() < 273 - 5)
                setMesh(
                        new ItemStack(CCItems.MESHES.get(Mesh.MeshType.WATER_FROZEN.getName()).get())
                );
            if (heatExchanger.getTemperatureAsInt() > 373 + 5)
                setMesh(
                        new ItemStack(CCItems.MESHES.get(Mesh.MeshType.WATER_EMPTY.getName()).get())
                );
        } else if (mesh.getItem() instanceof MeshItem meshItem && meshItem.getMeshType() == Mesh.MeshType.WATER_FROZEN) {
            if (heatExchanger.getTemperatureAsInt() > 273 + 5)
                setMesh(
                        new ItemStack(CCItems.MESHES.get(Mesh.MeshType.WATER.getName()).get())
                );
        }
//        logger.debug("Updating!");
//        setChanged();
        sendData();
//        setLazyTickRate(10);
//        setLazyTickRate(Math.min(10, lazyTickRate + 1));
    }

    public void updateTintClient() {
        if (level == null) return;
        if (level.isClientSide) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
    }

    public void updateTintServer() {
        if (level == null) return;
        if (!level.isClientSide) ForceUpdatePacket.send(level, getBlockPos());
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

    @Override
    public void forceUpdate() {
        updateTintClient();
    }

    @Override
    public float getMaxDistance() {
        float speed = Math.abs(this.getSpeed());
        float distanceFactor = Math.min(speed / 256f, 1);
        return Mth.lerp(distanceFactor, 3, 32);
    }
}
