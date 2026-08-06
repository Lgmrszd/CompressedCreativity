package com.lgmrszd.compressedcreativity.blocks.advanced_air_blower;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Flywheel visual for AdvancedAirBlowerBlockEntity.
 * Currently a placeholder - mesh rendering is handled by the BlockEntityRenderer.
 */
public class AdvancedAirBlowerVisual implements BlockEntityVisual<AdvancedAirBlowerBlockEntity> {
    // TODO: implement

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        // No crumbling instances for this visual
    }

    @Override
    public void update(float v) {
        // Visual updates handled by renderer
    }

    @Override
    public void delete() {
        // No cleanup required
    }
}
