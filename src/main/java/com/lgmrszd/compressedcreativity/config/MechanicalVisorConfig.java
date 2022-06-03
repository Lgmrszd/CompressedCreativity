package com.lgmrszd.compressedcreativity.config;

public class MechanicalVisorConfig {
    public enum TooltipMode {
        CLASSIC("classic", true, false),
        WIDGET("widget", false, true),
        BOTH("both", true, true),
        NONE("none", false, false);

        private final boolean goggled;
        private final boolean widget;

        private final String ID;

        TooltipMode(String id, boolean goggled, boolean widget) {
            ID = id;
            this.goggled = goggled;
            this.widget = widget;
        }

        public String getID() {
            return ID;
        }

        public boolean isGoggled() {
            return goggled;
        }

        public boolean isWidget() {
            return widget;
        }

        public TooltipMode getNext() {
            TooltipMode[] values = TooltipMode.values();
            int next = (ordinal() + 1) % values.length;
            return values[next];
        }
    }

    public enum BlockTrackerMode {
        ALL("all", true, true),
        GOGGLES("goggles", false, true),
        TOOLTIP("tooltip", true, false),
        MIN("min", false, false);
        private final String ID;
        private final boolean hasTooltip;
        private final boolean hasGoggles;

        BlockTrackerMode(String id, boolean hasTooltip, boolean hasGoggles) {
            ID = id;
            this.hasTooltip = hasTooltip;
            this.hasGoggles = hasGoggles;
        }

        public String getID() {
            return ID;
        }

        public boolean showsTooltip() {
            return hasTooltip;
        }

        public boolean showsGoggles() {
            return hasGoggles;
        }

        public BlockTrackerMode getNext() {
            BlockTrackerMode[] values = BlockTrackerMode.values();
            int next = (ordinal() + 1) % values.length;
            return values[next];
        }
    }
}
