package com.lgmrszd.compressedcreativity.content;

public class Mesh {
    public enum MeshType {
        WATER("water", "Wet Mesh"),
        HAUNT("haunt", "Soul Mesh");

        private final String name;
        private final String displayName;
        MeshType(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

//    public interface IMeshType {
//        default boolean shouldColor() {
//            return false;
//        }
//    }
}
