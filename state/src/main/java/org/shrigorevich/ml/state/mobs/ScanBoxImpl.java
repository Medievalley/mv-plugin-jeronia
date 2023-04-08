package org.shrigorevich.ml.state.mobs;

import org.bukkit.Location;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.domain.mobs.ScanBox;


public class ScanBoxImpl implements ScanBox {

    private final Coords min;
    private final Coords max;
    public ScanBoxImpl(Location l, int offsetY, int radiusX, int radiusY, int radiusZ) {
        this.min = new Coords(
            l.getBlockX() - radiusX,
            l.getBlockY() - radiusY + offsetY,
            l.getBlockZ() - radiusZ
        );
        this.max = new Coords(
            l.getBlockX() + radiusX,
            l.getBlockY() + radiusY + offsetY,
            l.getBlockZ() + radiusZ
        );
    }

    @Override
    public Coords getMin() {
        return min;
    }

    @Override
    public Coords getMax() {
        return max;
    }
}
