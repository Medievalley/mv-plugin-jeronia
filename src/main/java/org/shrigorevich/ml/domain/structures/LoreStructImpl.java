package org.shrigorevich.ml.domain.structures;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.db.models.LoreStructModel;
import org.shrigorevich.ml.db.models.StructBlock;
import org.shrigorevich.ml.db.models.StructBlockFull;
import org.shrigorevich.ml.db.models.VolumeBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoreStructImpl extends BaseStructure implements LoreStructure {
    private int volumeId;
    private String name;
    private boolean destructible;
    private int destroyedPercent;
    private final IStructureContext context;

    public LoreStructImpl(LoreStructModel m, IStructureContext context) {
        super(m);
        this.volumeId = m.volumeId;
        this.name = m.name;
        this.destructible = m.destructible;
        this.destroyedPercent = m.destroyedPercent;
        this.context = context;
    }

    @Override
    public long getDestructionPercent() {
        return this.destroyedPercent;
    }

    public boolean isDestructible() {
        return destructible;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getVolumeId() {
        return this.volumeId;
    }

    @Override
    public Optional<StructBlockFull> getBrokenBlock() {
        return Optional.empty();
    }

    @Override
    public void updateVolume(List<StructBlockFull> blocks) {

    }

    @Override
    public void restoreBlock(int x, int y, int z) {
        Optional<StructBlockFull> structBlock = context.getStructBlock(x, y, z, getVolumeId(), getId());
        if (structBlock.isPresent()) {
            StructBlockFull sb = structBlock.get();
            BlockData bd = Bukkit.createBlockData(sb.getBlockData());
            Block b = getWorld().getBlockAt(
                    sb.getX() + getX1(),
                    sb.getY() + getY1(),
                    sb.getZ() + getZ1());
            b.setBlockData(bd);
        }
    }

    @Override
    public void restore() {
        List<StructBlockFull> structBlocks = context.getStructBlocks(getId());
        for(int i = 0; i < structBlocks.size(); i ++) {
            StructBlockFull sb = structBlocks.get(i);
            BlockData bd = Bukkit.createBlockData(sb.getBlockData());
            Block b = getWorld().getBlockAt(
                    sb.getX() + getX1(),
                    sb.getY() + getY1(),
                    sb.getZ() + getZ1());

            b.setBlockData(bd);
        }
    }
}
