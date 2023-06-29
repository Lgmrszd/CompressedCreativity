package com.lgmrszd.compressedcreativity;

import com.lgmrszd.compressedcreativity.blocks.air_blower.AirBlowerBlockEntity;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.instruction.EmitParticlesInstruction;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.lgmrszd.compressedcreativity.index.CCModsReference.getAirParticle;

public class PonderScenes {

    private static final Logger logger = LogManager.getLogger(CompressedCreativity.MOD_ID);

    private static void updatePressure(SceneBuilder scene, BlockPos air_blower) {
        scene.world.modifyBlockEntity(air_blower, AirBlowerBlockEntity.class, te -> {
            te.getCapability(PNCCapabilities.AIR_HANDLER_MACHINE_CAPABILITY).ifPresent(cap -> {
                cap.setPressure(cap.getDangerPressure());
            });
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
        scene.effects.emitParticles(itemVec, EmitParticlesInstruction.Emitter.simple(getAirParticle(), new Vec3(0.0D, 0.1D, 0.0D)), 1.0F, 120);
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
        scene.effects.emitParticles(util.vector.centerOf(compressor), EmitParticlesInstruction.Emitter.withinBlockSpace(getAirParticle(), Vec3.ZERO), 40.0F, 10);
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

    public static void CompressedAirEngine(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("compressed_air_engine", "Rotation from Pressure");

        BlockPos cae1 = util.grid.at(3, 1, 2);
        BlockPos cae2 = util.grid.at(1, 1, 1);

        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(
                util.select.layer(0)
                        .substract(util.select.position(1,0,5))
                        .substract(util.select.position(3,0,5)),
                Direction.UP);
        scene.idle(5);
        scene.world.showSection(util.select.position(cae1), Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(
                util.select.fromTo(3,2,2, 3, 2, 5)
                        .add(util.select.fromTo(3, 0, 5, 3, 2, 5)),
                Direction.DOWN);
        scene.idle(20);
        scene.overlay.showText(80).text("Compressed Air Engine uses pressurized air to create rotational force")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(cae1));
        scene.idle(90);
//        scene.addKeyframe();
        scene.world.showSection(
                util.select.fromTo(1, 1, 1, 1, 1, 3),
                Direction.DOWN);
        scene.idle(5);
        scene.world.showSection(
                util.select.fromTo(1, 2, 3, 1, 2, 5)
                        .add(util.select.fromTo(1, 0, 5, 1, 2, 5)),
                Direction.DOWN);
        scene.idle(10);
        scene.overlay.showText(80).text("Multiple Engines can be combined together by placing one on another or attaching with wrench")
                .attachKeyFrame()
                .placeNearTarget()
                .pointAt(util.vector.topOf(cae2));
        scene.idle(90);
//        scene.idle(160);
        scene.markAsFinished();
    }
}
