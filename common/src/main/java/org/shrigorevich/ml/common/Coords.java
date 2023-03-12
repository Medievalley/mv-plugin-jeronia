package org.shrigorevich.ml.common;

public record Coords(int x, int y, int z) {

    public String getString() {
        return String.format("{x: %d, y: %d, z: %d}", x, y, z);
    }
}
