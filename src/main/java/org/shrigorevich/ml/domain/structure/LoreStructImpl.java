package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Villager;
import org.shrigorevich.ml.Ml;
import org.shrigorevich.ml.db.contexts.StructureContext;
import org.shrigorevich.ml.domain.structure.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoreStructImpl extends StructureImpl implements LoreStructure {
    private int volumeId;
    private final String name;
    private final boolean destructible;
    private int destroyedPercent;
    private final StructureContext context;
    private Villager laborer;
    private int foodStock; //TODO: store to database and load with rest data

    public LoreStructImpl(LoreStructDB m, StructureContext context) {
        super(m);
        this.volumeId = m.getVolumeId();
        this.name = m.getName();
        this.destructible = m.isDestructible();
        this.foodStock = m.getStock();
        this.context = context;

        if (m.getBlocks() > 0 && m.getBrokenBlocks() > 0) {
            this.destroyedPercent = m.getBrokenBlocks() * 100 / m.getBlocks();
        } else {
            destroyedPercent = 0;
        }
    }

    @Override
    public long getDestructionPercent() {
        return this.destroyedPercent;
    }

    public void setDestroyedPercent(int destroyedPercent) {
        this.destroyedPercent = destroyedPercent;
        System.out.printf("Destroyed percent updated: %d%%%n", destroyedPercent);
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

    @Override
    public void applyVolume(int volumeId) throws IllegalArgumentException {
        Optional<VolumeDB> volume = context.getVolumeById(volumeId);
        if (!volume.isPresent()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

        if(!isSizeEqual(volume.get()))
            throw new IllegalArgumentException("Structure and volume sizes are not equal");

        context.removeVolume(getId());
        context.setStructVolume(getId(), volumeId);

        List<VolumeBlockDB> volumeBlocks = context.getVolumeBlocks(volumeId);
        List<StructBlockDB> structBlocks = new ArrayList<>();
        for(int i = 0; i < volumeBlocks.size(); i ++) {
            VolumeBlockDB vb = volumeBlocks.get(i);
            structBlocks.add(new StructBlockModel(getId(), vb.getId(), true));
        }
        context.saveStructBlocks(structBlocks);
        this.volumeId = volumeId;
        this.restore();
    }

    @Override
    public int getFoodStock() {
        return foodStock;
    }

    @Override
    public void updateFoodStock(int foodAmount) {
        foodStock+=foodAmount;
        context.updateStock(getId(), foodStock);
    }

    @Override
    public Optional<Villager> getLaborer() {
        return laborer == null ? Optional.empty() : Optional.of(laborer);
    }

    @Override
    public void setLaborer(Villager e) {
        this.laborer = e;
    }

    private boolean isSizeEqual(VolumeDB volume) {
        return this.getSizeX() == volume.getSizeX() && this.getSizeY() == volume.getSizeY() &&
                this.getSizeZ() == volume.getSizeZ();
    }
}
