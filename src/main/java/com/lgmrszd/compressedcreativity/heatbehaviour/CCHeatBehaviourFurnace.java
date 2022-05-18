package com.lgmrszd.compressedcreativity.heatbehaviour;

import com.lgmrszd.compressedcreativity.CompressedCreativity;
import com.simibubi.create.content.contraptions.components.flywheel.engine.FurnaceEngineTileEntity;
import me.desht.pneumaticcraft.api.heat.HeatBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.lgmrszd.compressedcreativity.index.CCMisc.OVERCLOCKED_ENGINE;
import static me.desht.pneumaticcraft.api.PneumaticRegistry.RL;

public class CCHeatBehaviourFurnace extends HeatBehaviour {

    public enum FurnaceEngineHeatSettings {
        NONE(0),
        STALL(0),
        MISCHIEF(1),
        EVIL(2);

        private final float strength;
        public float getExplosionStrength() {
            return strength;
        }
        FurnaceEngineHeatSettings(float explosionStrength) {
            strength = explosionStrength;
        }
    }

    static final ResourceLocation ID = RL("furnace");
    
    private boolean wasFurnaceEngineConnected = false;

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
                if (!wasFurnaceEngineConnected) {
                    Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);
                    logger.debug("Furnace engine detected!");
                    BlockPos furnacePos = getCachedTileEntity().getBlockPos();
                    getWorld().destroyBlock(furnacePos, true);
//                    getWorld().explode(null, furnacePos.getX() + 0.5F, furnacePos.getY() + 0.5, furnacePos.getZ() + 0.5F, 2F, false, Explosion.BlockInteraction.BREAK);
//                    getWorld().explode(null, furnacePos.getX() + 0.5F, furnacePos.getY() + 0.5, furnacePos.getZ() + 0.5F, 2F, false, Explosion.BlockInteraction.NONE);
                    getWorld().explode(null, OVERCLOCKED_ENGINE, null, furnacePos.getX() + 0.5F, furnacePos.getY() + 0.5, furnacePos.getZ() + 0.5F, 1F, false, Explosion.BlockInteraction.NONE);
                    wasFurnaceEngineConnected = true;
                }
                return;
            }
            wasFurnaceEngineConnected = false;
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
