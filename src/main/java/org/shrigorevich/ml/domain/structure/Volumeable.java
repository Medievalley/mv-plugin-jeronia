package org.shrigorevich.ml.domain.structure;

import org.shrigorevich.ml.domain.structure.models.StructBlockModel;

import java.util.List;
import java.util.Optional;

public interface Volumeable {
    int getVolumeId();
    Optional<StructBlockModel> getBrokenBlock();
    void updateVolume(List<StructBlockModel> blocks);
    void restoreBlock(int x, int y, int z);
    void restore();
}
