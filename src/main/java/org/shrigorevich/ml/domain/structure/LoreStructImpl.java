package org.shrigorevich.ml.domain.structure;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Villager;
import org.shrigorevich.ml.domain.structure.contracts.LoreStructure;
import org.shrigorevich.ml.domain.structure.contracts.StructureContext;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoreStructImpl extends StructureImpl implements LoreStructure {
    private int volumeId;
    private final String name;
    private final boolean destructible;
    private final StructureContext context;
    private Villager laborer;
    private final int priority;
    private int foodStock;

    public LoreStructImpl(LoreStructModel m, StructureContext context) {
        super(m);
        this.volumeId = m.getVolumeId();
        this.name = m.getName();
        this.destructible = m.isDestructible();
        this.foodStock = m.getStock();
        this.context = context;
        this.priority = m.getPriority();
    }

    public boolean isDestructible() {
        return destructible;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int getVolumeId() {
        return this.volumeId;
    }

    @Override
    public void updateVolume(List<StructBlockModelImpl> blocks) {

    } //TODO: replace class to interface

    @Override
    public void restoreBlock(StructBlockModel block) {
        context.restoreBlock(block.getId());
        BlockData bd = Bukkit.createBlockData(block.getBlockData());
        Block b = getWorld().getBlockAt(block.getX(), block.getY(), block.getZ());
        b.setBlockData(bd);
    }

    @Override
    public void restore() {
        context.restore(getId());
        List<StructBlockModel> structBlocks = context.getStructBlocks(getId());
        for (StructBlockModel sb : structBlocks) {
            BlockData bd = Bukkit.createBlockData(sb.getBlockData());
            Block b = getWorld().getBlockAt(sb.getX(), sb.getY(), sb.getZ());
            b.setBlockData(bd);
        }
    }

    @Override
    public void applyVolume(int volumeId) throws IllegalArgumentException {
        Optional<VolumeModel> volume = context.getVolumeById(volumeId);
        if (!volume.isPresent()) throw new IllegalArgumentException(String.format("Volume %d not found", volumeId));

        if(!isSizeEqual(volume.get()))
            throw new IllegalArgumentException("Structure and volume sizes are not equal");

        context.removeVolume(getId());
        context.setStructVolume(getId(), volumeId);

        List<VolumeBlockModel> volumeBlocks = context.getVolumeBlocks(volumeId);
        List<StructBlockModel> structBlocks = new ArrayList<>();
        for(int i = 0; i < volumeBlocks.size(); i ++) {
            VolumeBlockModel vb = volumeBlocks.get(i);
            structBlocks.add(new StructBlockModelImpl(getId(), vb.getId(), true));
        }
        context.saveStructBlocks(structBlocks);
        this.volumeId = volumeId;
        this.restore();
    }

    @Override
    public List<StructBlockModel> getStructBlocks() {
        return context.getStructBlocks(getId());
    }

    @Override
    public Optional<StructBlockModel> getBlock(int x, int y, int z) {
        return context.getStructBlock(x, y, z, volumeId, getId());
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

    @Override
    public int getPriority() {
        return priority;
    }

    private boolean isSizeEqual(VolumeModel volume) {
        return this.getSizeX() == volume.getSizeX() && this.getSizeY() == volume.getSizeY() &&
                this.getSizeZ() == volume.getSizeZ();
    }
}
