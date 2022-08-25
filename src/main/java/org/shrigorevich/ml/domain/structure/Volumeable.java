package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructBlockDB;
import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;

public interface Volumeable {
    int getVolumeId();
    void updateVolume(List<StructBlockModel> blocks);
    void restoreBlock(int x, int y, int z);
    void restore();
    void applyVolume(int volumeId) throws IllegalArgumentException;
    List<StructBlockDB> getStructBlocks();
}
