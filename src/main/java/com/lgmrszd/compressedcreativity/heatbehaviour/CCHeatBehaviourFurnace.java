package com.lgmrszd.compressedcreativity.heatbehaviour;

import com.lgmrszd.compressedcreativity.config.CommonConfig;
import com.simibubi.create.content.contraptions.components.flywheel.engine.FurnaceEngineTileEntity;
import me.desht.pneumaticcraft.api.heat.HeatBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import static com.lgmrszd.compressedcreativity.index.CCMisc.OVERCLOCKED_ENGINE;
import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;


/* Copied from PNC:R code and modified to override original behaviour */

public class CCHeatBehaviourFurnace extends HeatBehaviour {

    static final ResourceLocation ID = RL("furnace");
    private int furnaceEngineConnectedTicks = 0;

    private boolean isFurnaceEngineConnected() {
        if (!(getCachedTileEntity() instanceof AbstractFurnaceBlockEntity furnace) || furnace.isRemoved()) {
            return false;
        }
        BlockPos furnacePos = getCachedTileEntity().getBlockPos();
        for (Direction dir: new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos newPos = furnacePos.relative(dir);
            BlockEntity newBE = getWorld().getBlockEntity(newPos);
            if (newBE instanceof FurnaceEngineTileEntity) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public boolean isApplicable() {
        return getBlockState().getBlock() instanceof AbstractFurnaceBlock;
    }

    @Override
    public void tick() {
        if (!(getCachedTileEntity() instanceof AbstractFurnaceBlockEntity furnace) || furnace.isRemoved()) {
            return;
        }
        if (getHeatExchanger().getTemperature() > 373) {
            if (isFurnaceEngineConnected()) {
                furnaceEngineConnectedTicks = furnaceEngineConnectedTicks + 1;
                BlockPos furnacePos = getCachedTileEntity().getBlockPos();
                if (getWorld() instanceof ServerLevel serverLevel) {
                    if (furnaceEngineConnectedTicks % 2 == 0) {
                        serverLevel.sendParticles(ParticleTypes.POOF, furnacePos.getX() + 0.5F, furnacePos.getY()  + 1F, furnacePos.getZ() + 0.5F, 2, 0.0D, 0.5D, 0.0D, 0.1D);
                    }
                    if (furnaceEngineConnectedTicks > 60) {
                        serverLevel.sendParticles(ParticleTypes.SMALL_FLAME, furnacePos.getX() + 0.5F, furnacePos.getY()  + 1F, furnacePos.getZ() + 0.5F, 15, 0.3D, 0.1D, 0.3D, 0.0D);
                    }
                    if (furnaceEngineConnectedTicks > 100) {
                        serverLevel.sendParticles(ParticleTypes.FLAME, furnacePos.getX() + 0.5F, furnacePos.getY()  + 1F, furnacePos.getZ() + 0.5F, 20, 0.2D, 0.5D, 0.2D, 0.3D);
                    }
                }
                if (furnaceEngineConnectedTicks > 140 && CommonConfig.FURNACE_ENGINE_EXPLODE.get()) {
                    getWorld().destroyBlock(furnacePos, true);
                    getWorld().explode(null, OVERCLOCKED_ENGINE, null, furnacePos.getX() + 0.5F, furnacePos.getY() + 0.5, furnacePos.getZ() + 0.5F, 1F, false, Explosion.BlockInteraction.NONE);
                }
                return;
            }
            furnaceEngineConnectedTicks = Math.max(0, furnaceEngineConnectedTicks - 2);
            if (furnace.litTime < 190 && !furnace.getItem(0).isEmpty()) {
                if (furnace.litTime == 0) {
                    getWorld().setBlockAndUpdate(getPos(), getBlockState().setValue(AbstractFurnaceBlock.LIT, true));
                }
                furnace.litDuration = 200;
                furnace.litTime += 10;
                getHeatExchanger().addHeat(-1);
            }
            if (furnace.cookingProgress > 0) {
                // Easy performance saver, the Furnace won't be ticked unnecessarily when there's nothing to
                // cook (or when just started cooking).
                int progress = Math.max(0, ((int) getHeatExchanger().getTemperature() - 343) / 30);
                progress = Math.min(5, progress);
                for (int i = 0; i < progress; i++) {
                    AbstractFurnaceBlockEntity.serverTick(getWorld(), furnace.getBlockPos(), furnace.getBlockState(), furnace);
                }
            }
        }

    }
}
