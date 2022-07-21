package org.shrigorevich.ml.domain.models;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.shrigorevich.ml.db.models.GetStructModel;
import org.shrigorevich.ml.domain.enums.StructureType;

public class Structure implements IStructure {

    private final int id;
    private final String name;
    private final String owner;
    private final String world;
    private final int volumeId;
    private final StructureType type;
    private final boolean destructible;
    private final int x1, y1, z1;
    private final int x2, y2, z2;

    public Structure(GetStructModel m) throws IllegalArgumentException {

        this.type = parseType(m.getTypeId());
        if(type == null) throw new IllegalArgumentException(
                String.format("Unable to parse struct type: %d. StructId: %d", m.getTypeId(), m.getId()));

        this.id = m.getId();
        this.world = m.getWorld();
        this.name = m.getName();
        this.owner = m.getOwner();
        this.destructible = m.isDestructible();
        this.x1 = Math.min(m.getX1(), m.getX2());
        this.x2 = Math.max(m.getX1(), m.getX2());
        this.y1 = Math.min(m.getY1(), m.getY2());
        this.y2 = Math.max(m.getY1(), m.getY2());
        this.z1 = Math.min(m.getZ1(), m.getZ2());
        this.z2 = Math.max(m.getZ1(), m.getZ2());
        this.volumeId = m.getVolumeId();
    }

    public StructureType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public String getName() { return this.name; }

    public String getOwner() { return this.owner; }

    public boolean isDestructible() { return this.destructible; }

    public World getWorld() {
        World world = Bukkit.getWorld(this.world);
        if (world == null) throw new IllegalStateException("World '" + this.world + "' is not loaded");
        return world;
    }

    public Location getLowerNE() {
        return new Location(this.getWorld(), this.x1, this.y1, this.z1);
    }

    public Location getUpperSW() {
        return new Location(this.getWorld(), this.x2, this.y2, this.z2);
    }

    /**
     * Get the blocks in the Cuboid.
     *
     * @return The blocks in the Cuboid
     */
    public List<Block> getBlocks() {
        Iterator<Block> blockI = this.iterator();
        List<Block> copy = new ArrayList<Block>();
        while (blockI.hasNext())
            copy.add(blockI.next());
        return copy;
    }

    /**
     * Get the the centre of the Cuboid.
     *
     * @return Location at the centre of the Cuboid
     */
    public Location getCenter() {
        int x1 = this.getUpperX() + 1;
        int y1 = this.getUpperY() + 1;
        int z1 = this.getUpperZ() + 1;
        return new Location(this.getWorld(), this.getLowerX() + (x1 - this.getLowerX()) / 2.0, this.getLowerY() + (y1 - this.getLowerY()) / 2.0, this.getLowerZ() + (z1 - this.getLowerZ()) / 2.0);
    }

    /**
     * Get the size of this Cuboid along the X axis
     *
     * @return  Size of Cuboid along the X axis
     */
    public int getSizeX() {
        return (this.x2 - this.x1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Y axis
     *
     * @return  Size of Cuboid along the Y axis
     */
    public int getSizeY() {
        return (this.y2 - this.y1) + 1;
    }

    /**
     * Get the size of this Cuboid along the Z axis
     *
     * @return  Size of Cuboid along the Z axis
     */
    public int getSizeZ() {
        return (this.z2 - this.z1) + 1;
    }

    /**
     * Get the minimum X co-ordinate of this Cuboid
     *
     * @return  the minimum X co-ordinate
     */
    public int getLowerX() {
        return this.x1;
    }

    /**
     * Get the minimum Y co-ordinate of this Cuboid
     *
     * @return  the minimum Y co-ordinate
     */
    public int getLowerY() {
        return this.y1;
    }

    /**
     * Get the minimum Z co-ordinate of this Cuboid
     *
     * @return  the minimum Z co-ordinate
     */
    public int getLowerZ() {
        return this.z1;
    }

    /**
     * Get the maximum X co-ordinate of this Cuboid
     *
     * @return  the maximum X co-ordinate
     */
    public int getUpperX() {
        return this.x2;
    }

    /**
     * Get the maximum Y co-ordinate of this Cuboid
     *
     * @return  the maximum Y co-ordinate
     */
    public int getUpperY() {
        return this.y2;
    }

    /**
     * Get the maximum Z co-ordinate of this Cuboid
     *
     * @return  the maximum Z co-ordinate
     */
    public int getUpperZ() {
        return this.z2;
    }

    /**
     * Get the Blocks at the eight corners of the Cuboid.
     *
     * @return array of Block objects representing the Cuboid corners
     */
    public Block[] corners() {
        Block[] res = new Block[8];
        World w = this.getWorld();
        res[0] = w.getBlockAt(this.x1, this.y1, this.z1);
        res[1] = w.getBlockAt(this.x1, this.y1, this.z2);
        res[2] = w.getBlockAt(this.x1, this.y2, this.z1);
        res[3] = w.getBlockAt(this.x1, this.y2, this.z2);
        res[4] = w.getBlockAt(this.x2, this.y1, this.z1);
        res[5] = w.getBlockAt(this.x2, this.y1, this.z2);
        res[6] = w.getBlockAt(this.x2, this.y2, this.z1);
        res[7] = w.getBlockAt(this.x2, this.y2, this.z2);
        return res;
    }

//    public Structure expand(CuboidDirection dir, int amount) {
//        switch (dir) {
//            case North:
//                return new Structure(this.world, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
//            case South:
//                return new Structure(this.world, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
//            case East:
//                return new Structure(this.world, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
//            case West:
//                return new Structure(this.world, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
//            case Down:
//                return new Structure(this.world, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
//            case Up:
//                return new Structure(this.world, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
//            default:
//                throw new IllegalArgumentException("Invalid direction " + dir);
//        }
//    }

//    public Structure shift(CuboidDirection dir, int amount) {
//        return expand(dir, amount).expand(dir.opposite(), -amount);
//    }

    public boolean contains(int x, int y, int z) {
        return x >= this.x1 && x <= this.x2 && y >= this.y1 && y <= this.y2 && z >= this.z1 && z <= this.z2;
    }
    public boolean contains(Block b) {
        return this.contains(b.getLocation());
    }
    public boolean contains(Location l) {
        if (!this.world.equals(l.getWorld().getName())) return false;
        return this.contains(l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    /**
     * Get a block relative to the lower NE point of the Cuboid.
     *
     * @param x - The X co-ordinate
     * @param y - The Y co-ordinate
     * @param z - The Z co-ordinate
     * @return The block at the given position
     */
    public Block getRelativeBlock(int x, int y, int z) {
        return this.getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
    }

    /**
     * Get a list of the chunks which are fully or partially contained in this cuboid.
     *
     * @return A list of Chunk objects
     */
    public List<Chunk> getChunks() {
        List<Chunk> res = new ArrayList<Chunk>();

        World w = this.getWorld();
        int x1 = this.getLowerX() & ~0xf;
        int x2 = this.getUpperX() & ~0xf;
        int z1 = this.getLowerZ() & ~0xf;
        int z2 = this.getUpperZ() & ~0xf;
        for (int x = x1; x <= x2; x += 16) {
            for (int z = z1; z <= z2; z += 16) {
                res.add(w.getChunkAt(x >> 4, z >> 4));
            }
        }
        return res;
    }

    public Iterator<Block> iterator() {
        return new CuboidIterator(this.getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
    }

    public class CuboidIterator implements Iterator<Block> {
        private World w;
        private int baseX, baseY, baseZ;
        private int x, y, z;
        private int sizeX, sizeY, sizeZ;

        public CuboidIterator(World w, int x1, int y1, int z1, int x2, int y2, int z2) {
            this.w = w;
            this.baseX = x1;
            this.baseY = y1;
            this.baseZ = z1;
            this.sizeX = Math.abs(x2 - x1) + 1;
            this.sizeY = Math.abs(y2 - y1) + 1;
            this.sizeZ = Math.abs(z2 - z1) + 1;
            this.x = this.y = this.z = 0;
        }

        public boolean hasNext() {
            return this.x < this.sizeX && this.y < this.sizeY && this.z < this.sizeZ;
        }

        public Block next() {
            Block b = this.w.getBlockAt(this.baseX + this.x, this.baseY + this.y, this.baseZ + this.z);
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

    public enum CuboidDirection {
        North, East, South, West, Up, Down, Horizontal, Vertical, Both, Unknown;

        public CuboidDirection opposite() {
            switch (this) {
                case North:
                    return South;
                case East:
                    return West;
                case South:
                    return North;
                case West:
                    return East;
                case Horizontal:
                    return Vertical;
                case Vertical:
                    return Horizontal;
                case Up:
                    return Down;
                case Down:
                    return Up;
                case Both:
                    return Both;
                default:
                    return Unknown;
            }
        }

    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getZ1() {
        return z1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public int getZ2() {
        return z2;
    }

    private StructureType parseType(int typeId) {
        for(StructureType st : StructureType.values()) {
            if (st.getTypeId() == typeId) {
                return st;
            }
        }
        return null;
    }
}