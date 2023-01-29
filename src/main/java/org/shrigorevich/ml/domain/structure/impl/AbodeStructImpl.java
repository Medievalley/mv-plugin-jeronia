package org.shrigorevich.ml.domain.structure.impl;

import org.shrigorevich.ml.domain.structure.AbodeStructure;
import org.shrigorevich.ml.domain.structure.BlockType;
import org.shrigorevich.ml.domain.structure.StructBlock;
import org.shrigorevich.ml.domain.structure.models.StructModel;

import java.util.List;

public class AbodeStructImpl extends StructureImpl implements AbodeStructure {

    private final String name;
    private final List<StructBlock> spawnBlocks;

    public AbodeStructImpl(StructModel model, List<StructBlock> blocks) {
        super(model);
        this.name = model.getName();
        this.spawnBlocks = blocks.stream().filter(b -> b.getType() == BlockType.ABODE_SPAWN).toList();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<StructBlock> getSpawnBlocks() {
        return spawnBlocks;
    }
}
