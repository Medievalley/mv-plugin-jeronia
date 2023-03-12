package org.shrigorevich.ml.state.structures;

import org.shrigorevich.ml.domain.structures.BlockType;
import org.shrigorevich.ml.domain.structures.StructBlock;
import org.shrigorevich.ml.state.structures.models.StructModel;

import java.util.List;
import java.util.stream.Collectors;

public class AbodeStructImpl extends VolumeStructImpl implements ExAbodeStructure {

    private final String name;
    private final List<StructBlock> spawnBlocks;

    public AbodeStructImpl(StructModel model, List<StructBlock> blocks) {
        super(model, blocks);
        this.name = model.getName();
        this.spawnBlocks = blocks.stream()
            .filter(b -> b.getType() == BlockType.ABODE_SPAWN).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<StructBlock> getSpawnBlocks() {
        return spawnBlocks;
    }

    @Override
    public void addSpawnBlock(StructBlock block) {
        spawnBlocks.add(block);
    }
}
