package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerTileEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.common.block.entity.CompressedIronBlockBlockEntity;
import me.desht.pneumaticcraft.common.particle.AirParticleData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PonderScenes {

    private static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);

    private static void updatePressure(SceneBuilder scene, BlockPos airBlock) {
        scene.world.modifyTileEntity(airBlock, AirBlowerTileEntity.class, te -> {
            te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent(cap -> {
                cap.setPressure(cap.getDangerPressure());
            });
        });
    }

    private static void updateTemperature(SceneBuilder scene, BlockPos temperatureBlock, double temperature) {
        scene.world.modifyTileEntity(temperatureBlock, CompressedIronBlockBlockEntity.class, te -> {
            te.getCapability(PNCCapabilities.HEAT_EXCHANGER_CAPABILITY).ifPresent(cap -> {
                cap.setTemperature(temperature);
                cap.tick();
            });
            te.tickServer();
        });
    }

    public static void rotationalCompressor(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("rotational_compressor", "Generating Pressure using a Rotational Compressor");
        scene.configureBasePlate(0, 1, 5);
        scene.world.showSection(util.select.layer(0), Direction.UP);

        BlockPos compressor = util.grid.at(2, 1, 3);

        scene.idle(5);
        scene.world.showSection(util.select.fromTo(3, 1, 0, 2, 2, 2), Direction.DOWN);
//        scene.idle(5);
//        scene.world.showSection(util.select.position(3, 1, 0), Direction.DOWN);
//        for (int z = 0; z < 4; z++) {
//            scene.idle(5);
//            scene.world.showSection(util.select.position(2, 1, z), Direction.DOWN);
//        }
//        for (int x = 1; x < 3; x++) {
//            scene.idle(5);
//            scene.world.showSection(util.select.position(x, 1, 4), Direction.DOWN);
//        }
        scene.idle(5);
        scene.world.showSection(util.select.position(2, 1, 3), Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(util.select.position(1, 1, 3), Direction.DOWN);
        scene.idle(2);
        scene.world.showSection(util.select.position(1, 2, 3), Direction.DOWN);

        scene.idle(10);
        scene.overlay.showText(50)
                .text("The Rotational Compressor is used to generate pressure")
                .placeNearTarget()
                .pointAt(util.vector.topOf(compressor));
        scene.idle(60);


        scene.addKeyframe();

        BlockPos leverPos = util.grid.at(2, 2, 1);
        Selection reverse = util.select.fromTo(2, 1, 1, 2, 1, 3);

        scene.world.toggleRedstonePower(util.select.fromTo(leverPos, leverPos.below()));
        scene.effects.indicateRedstone(leverPos);
        scene.world.modifyKineticSpeed(reverse, (f) -> {
            return -f;
        });
//        scene.idle(20);
//        scene.world.modifyTileEntity(compressor, RotationalCompressorTileEntity.class, rct -> {
//            rct.onSpeedChanged(-128);
//        });
//        scene.world.modifyTileEntity(compressor, RotationalCompressorTileEntity.class, rct -> {
//            LazyOptional<IAirHandlerMachine> cap = rct.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY);
//            cap.ifPresent((h) -> {
//                logger.debug("Air handler in Ponder: " + h.getPressure() + " " + h.getAir());
//            });
//        });
        scene.overlay.showText(50)
                .text("Specific Rotational Direction needed in order for Compressor to work.")
                .placeNearTarget()
                .pointAt(util.vector.topOf(compressor));
        scene.idle(5);
        Vec3 itemVec = util.vector.blockSurface(util.grid.at(1, 2, 3), Direction.UP);
        scene.effects.emitParticles(itemVec, EmitParticlesInstruction.Emitter.simple(AirParticleData.DENSE, new Vec3(0.0D, 0.1D, 0.0D)), 1.0F, 120);
        scene.idle(60);

        scene.overlay.showText(50)
                .text("Air will leak from unconnected sides of tubes or some machines!")
                .placeNearTarget()
                .pointAt(itemVec);
        scene.idle(60);

        scene.addKeyframe();

        scene.world.destroyBlock(util.grid.at(1, 2, 3));
        scene.world.destroyBlock(util.grid.at(1, 1, 3));

        scene.idle(20);

        scene.overlay.showText(50)
                .text("Don't let pressure built up in the machines.")
                .placeNearTarget()
                .pointAt(util.vector.topOf(compressor));

        scene.idle(60);

        scene.overlay.showText(50)
                .text("If the pressure builds up...").colored(PonderPalette.RED)
                .placeNearTarget()
                .pointAt(util.vector.topOf(compressor));

        scene.idle(60);

        scene.world.destroyBlock(compressor);
        scene.effects.emitParticles(util.vector.centerOf(compressor), EmitParticlesInstruction.Emitter.simple(ParticleTypes.EXPLOSION, Vec3.ZERO), 2.0F, 1);
        scene.effects.emitParticles(util.vector.centerOf(compressor), EmitParticlesInstruction.Emitter.withinBlockSpace(AirParticleData.DENSE, Vec3.ZERO), 40.0F, 10);
        scene.idle(10);
        scene.overlay.showText(50)
                .text("Machine could explode!").colored(PonderPalette.RED)
                .placeNearTarget()
                .pointAt(util.vector.centerOf(compressor));

        scene.idle(60);
    }

    public static void airBlower(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("air_blower", "Air Flow from Air Pressure");
        scene.configureBasePlate(0, 1, 5);

        BlockPos air_blower = util.grid.at(1, 1, 4);

        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(5);
        scene.world.showSection(util.select.fromTo(3,1,0, 3,1,4).add(util.select.position(2,1,4)), Direction.DOWN);
//        scene.world.showSection(util.select.fromTo(2,1,4, 3,1,4), Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(util.select.position(air_blower), Direction.DOWN);
//        scene.world.showSection(util.select.layer(1), Direction.UP);
        scene.idle(40);

        scene.overlay.showText(80).text("Air Blower uses pressurized air to create Air Current")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(air_blower));

        updatePressure(scene, air_blower);

        scene.idle(90);

        updatePressure(scene, air_blower);

        scene.overlay.showText(160).text("Air Current speed, as well as air consumption rate, depends on current Air Pressure")
                .placeNearTarget()
                .pointAt(util.vector.topOf(air_blower));

        scene.idle(160);

        updatePressure(scene, air_blower);

        scene.markAsFinished();
    }

    public static void pressureTiers(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pressure_tiers", "Different Pressure Tiers");
        scene.configureBasePlate(0, 0, 7);

        BlockPos tier_1 = util.grid.at(5, 1, 2);
        BlockPos tier_1_5 = util.grid.at(3, 1, 2);
        BlockPos tier_2 = util.grid.at(1, 1, 2);

        scene.world.showSection(util.select.layer(0).substract(util.select.fromTo(1, 0, 5, 5, 0, 5)), Direction.UP);
        scene.idle(10);

        scene.world.showSection(util.select.layer(1).add(util.select.fromTo(1, 0, 5, 5, 0, 5)), Direction.DOWN);
        scene.idle(40);

        scene.overlay.showText(320).text("tiers_info")
                .independent()
                .attachKeyFrame();
        scene.idle(80);

        scene.overlay.showText(60).text("tier_1")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.centerOf(tier_1))
                .colored(PonderPalette.BLACK);
        scene.idle(80);

        scene.overlay.showText(60).text("tier_1_5")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.centerOf(tier_1_5))
                .colored(PonderPalette.RED);
        scene.idle(80);

        scene.overlay.showText(60).text("tier_2")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.centerOf(tier_2))
                .colored(PonderPalette.BLUE);
        scene.idle(100);

        scene.overlay.showText(80).text("tiers_info_additional")
                .independent()
                .attachKeyFrame();
        scene.idle(80);

        scene.markAsFinished();
    }


    public static void temperatureOne(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("temperature_1", "Basics about temperature");
        scene.configureBasePlate(0, 0, 5);

        BlockPos compIron_1 = util.grid.at(3, 1, 2);

        scene.world.showSection(util.select.layer(0), Direction.UP);
        scene.idle(10);

        scene.world.showSection(util.select.layer(1), Direction.DOWN);
        scene.idle(10);

//        scene.world.destroyBlock(compIron_1);

        updateTemperature(scene, compIron_1, 300);
        scene.idle(20);
        updateTemperature(scene, compIron_1, 10);
        scene.idle(20);
        scene.markAsFinished();
    }

}
