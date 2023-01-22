package org.shrigorevich.ml.common;

public record CoordinatesImpl(int x, int y, int z) implements Coordinates {
    @Override
    public String getString() {
        return String.format("{x: %d, y: %d, z: %d}", x, y, z);
    }
}
