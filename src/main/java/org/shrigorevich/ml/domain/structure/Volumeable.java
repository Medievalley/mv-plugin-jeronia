package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;
import org.shrigorevich.ml.domain.structure.models.StructBlockModelImpl;

import java.util.List;

public interface Volumeable {
    int getVolumeId();
    void updateVolume(List<StructBlockModelImpl> blocks);
    void restoreBlock(StructBlockModel block);
    void restore();
    void applyVolume(int volumeId) throws IllegalArgumentException;
    List<StructBlockModel> getStructBlocks();
}
