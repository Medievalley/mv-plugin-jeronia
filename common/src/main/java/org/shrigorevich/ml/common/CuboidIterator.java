package org.shrigorevich.ml.common;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

public class CuboidIterator implements Iterator<Block> {
    private final Location center;
    private final int baseX, baseY, baseZ;
    private int x, y, z;
    private final int sizeX, sizeY, sizeZ;

    public CuboidIterator(Location center, int radius, int height) {
        this(center, height, radius, radius, radius);
    }

    public CuboidIterator(Location c, int height, int x, int y, int z) {
        this(c, c.getBlockX()+x, c.getBlockY()+y+height, c.getBlockZ()+z,
            c.getBlockX()-x, c.getBlockY()-y+height, c.getBlockZ()-z);
    }

    private CuboidIterator(Location c, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.center = c;
        this.baseX = x1;
        this.baseY = y1;
        this.baseZ = z1;
        this.sizeX = Math.abs(x2 - x1) + 1;
        this.sizeY = Math.abs(y2 - y1) + 1;
        this.sizeZ = Math.abs(z2 - z1) + 1;
        this.x = this.y = this.z = 0;
//        System.out.printf("Loc1: %d %d %d%n", x1, y1, z1);
//        System.out.printf("Loc2: %d %d %d%n", x2, y2, z2);
    }

    public boolean hasNext() {
        return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
    }

    public Block next() {
        Block b = this.center.getWorld().getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
        if (++x >= this.sizeX) {
            this.x = 0;
            if (++this.y >= this.sizeY) {
                this.y = 0;
                ++this.z;
            }
        }
        return b;
    }

    public void remove() {
    }
}