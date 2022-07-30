package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.shrigorevich.ml.db.contexts.IStructureContext;
import org.shrigorevich.ml.domain.structure.models.StructBlockDB;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.LoreStructDB;

import java.util.List;
import java.util.Optional;

public class LoreStructImpl extends StructureImpl implements LoreStructure {
    private final int volumeId;
    private final String name;
    private final boolean destructible;
    private int destroyedPercent;
    private final IStructureContext context;

    public LoreStructImpl(LoreStructDB m, IStructureContext context) {
        super(m);
        this.volumeId = m.getVolumeId();
        this.name = m.getName();
        this.destructible = m.isDestructible();
        this.destroyedPercent = m.getDestroyedPercent();
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
    public Optional<StructBlockModel> getBrokenBlock() {
        return Optional.empty();
    }

    @Override
    public void updateVolume(List<StructBlockModel> blocks) {

    }

    @Override
    public void restoreBlock(int x, int y, int z) {
        Optional<StructBlockDB> structBlock = context.getStructBlock(x, y, z, getVolumeId(), getId());
        if (structBlock.isPresent()) {
            StructBlockDB sb = structBlock.get();
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
        context.restore(getId());
        List<StructBlockDB> structBlocks = context.getStructBlocks(getId());
        for(int i = 0; i < structBlocks.size(); i ++) {
            StructBlockDB sb = structBlocks.get(i);
            BlockData bd = Bukkit.createBlockData(sb.getBlockData());
            Block b = getWorld().getBlockAt(
                    sb.getX() + getX1(),
                    sb.getY() + getY1(),
                    sb.getZ() + getZ1());
            b.setBlockData(bd);
        }
    }
}
