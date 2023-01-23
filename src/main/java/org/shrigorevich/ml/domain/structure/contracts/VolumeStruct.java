package org.shrigorevich.ml.domain.structure.contracts;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.StructBlockModelImpl;

import java.util.List;
import java.util.Optional;

public interface VolumeStruct extends Structure {
    int getVolumeId();
    void updateVolume(List<StructBlockModelImpl> blocks);
    void restoreBlock(StructBlockModel block);
    void restore();
    void applyVolume(int volumeId) throws IllegalArgumentException;
    List<StructBlockModel> getStructBlocks();
    Optional<StructBlockModel> getBlock(int x, int y, int z);
}
