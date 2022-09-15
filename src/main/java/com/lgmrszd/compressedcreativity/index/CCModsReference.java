package com.lgmrszd.compressedcreativity.index;

import com.lgmrszd.compressedcreativity.blocks.bracketed_pressure_tube.BracketedPressureTubeBlock;
import me.desht.pneumaticcraft.api.PneumaticRegistry;
import me.desht.pneumaticcraft.api.misc.IMiscHelpers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class CCModsReference {

    public static ParticleOptions getAirParticle () {
        IMiscHelpers miscHelpers = PneumaticRegistry.getInstance().getMiscHelpers();
        return miscHelpers.airParticle();
    }
    public static Block getPlasticBrickBlockByColor(DyeColor color) {
        return ForgeRegistries.BLOCKS.getValue(
                new ResourceLocation(PneumaticRegistry.MOD_ID, "plastic_brick_" + color.getName()));
    }
    public enum PNCPressureTube {
        BASIC(0, "pressure_tube"),
        REINFORCED(1, "reinforced_pressure_tube"),
        ADVANCED(2, "advanced_pressure_tube");

        private final int tier;
        private final String id;

        private final Block block;
        PNCPressureTube(int tier, String id) {
            this.tier = tier;
            this.id = id;
            this.block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PneumaticRegistry.MOD_ID, id));
//            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(PneumaticRegistry.MOD_ID, id));
//            this.block = block == null ? LazyOptional.empty() : LazyOptional.of(() -> block);
        }

        public int getTier() {
            return tier;
        }

        public String getId() {
            return id;
        }

        public Block getBlock() {
//            return block.resolve().orElse(null);
            return block;
        }

        public static PNCPressureTube getByTier(int tier) {
            return switch (tier) {
//                case 0 -> BASIC;
                case 1 -> REINFORCED;
                case 2 -> ADVANCED;
                default -> BASIC;
            };
        }

        public static Block getBlockByTier(int tier) {
            return getByTier(tier).getBlock();
        }

        public static PNCPressureTube getByBlock(Block block){
            ResourceLocation loc = block.getRegistryName();
            if (loc == null) return null;
            if (!loc.getNamespace().equals(PneumaticRegistry.MOD_ID)) return null;
            String path = loc.getPath();
            if (path.equals(BASIC.id)) return BASIC;
            if (path.equals(ADVANCED.id)) return ADVANCED;
            if (path.equals(REINFORCED.id)) return REINFORCED;
            return null;
        }
    }

    public enum BracketedPressureTube {
        BASIC(0, CCBlocks.BRACKETED_PRESSURE_TUBE.get()),
        REINFORCED(1, CCBlocks.BRACKETED_REINFORCED_PRESSURE_TUBE.get()),
        ADVANCED(2, CCBlocks.BRACKETED_ADVANCED_PRESSURE_TUBE.get());

        private final int tier;
        private final BracketedPressureTubeBlock block;

        BracketedPressureTube(int tier, BracketedPressureTubeBlock block) {
            this.tier = tier;
            this.block = block;
        }

        public int getTier() {
            return tier;
        }

        public BracketedPressureTubeBlock getBlock() {
            return block;
        }

        public static BracketedPressureTube getByTier(int tier) {
            return switch (tier) {
//                case 0 -> BASIC;
                case 1 -> REINFORCED;
                case 2 -> ADVANCED;
                default -> BASIC;
            };
        }

        public static BracketedPressureTube getByBlock(Block block) {
            if (!(block instanceof BracketedPressureTubeBlock tubeBlock)) return null;
            if (tubeBlock == BASIC.block) return BASIC;
            if (tubeBlock == REINFORCED.block) return REINFORCED;
            if (tubeBlock == ADVANCED.block) return ADVANCED;
            return null;
        }
    }
}
