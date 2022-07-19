package com.lgmrszd.compressedcreativity.blocks.compressed_air_engine;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlock;
import com.simibubi.create.content.contraptions.base.GeneratingKineticTileEntity;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.pressure.PressureTier;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class CompressedAirEngineTileEntity extends GeneratingKineticTileEntity {

    protected final IAirHandlerMachine airHandler;
    private final LazyOptional<IAirHandlerMachine> airHandlerCap;
    public CompressedAirEngineTileEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        this.airHandler = PneumaticRegistry.getInstance().getAirHandlerMachineFactory()
                .createAirHandler(PressureTier.TIER_ONE, 1000);
        this.airHandlerCap = LazyOptional.of(() -> airHandler);
    }

    @Override
    public void initialize() {
        super.initialize();
        updateAirHandler();
    }

    public void updateAirHandler() {
        ArrayList<Direction> sides = new ArrayList<>();
//        for (Direction side: new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP, Direction.DOWN}) {
//            if (canConnectPneumatic(side)) {
//                sides.add(side);
//            }
//        }
        sides.add(Direction.UP);
        airHandler.setConnectedFaces(sides);
    }

    @Override
    public void tick() {
        super.tick();
        airHandler.tick(this);


        boolean server = !level.isClientSide || isVirtual();
        if (server) {
            airHandler.setSideLeaking(airHandler.getConnectedAirHandlers(this).isEmpty() ? Direction.UP : null);
        }
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
        return dir == Direction.UP || dir == null;
    }
}
