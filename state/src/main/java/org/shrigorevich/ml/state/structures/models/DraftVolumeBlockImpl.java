package org.shrigorevich.ml.state.structures.models;

public record DraftVolumeBlockImpl(
        int x, int y, int z, String material, String blockData) implements DraftVolumeBlock {

}
