package org.shrigorevich.ml.domain.structures;

import org.shrigorevich.ml.db.models.StructBlockFull;

import java.util.List;
import java.util.Optional;

public interface Volumeable {
    int getVolumeId();
    Optional<StructBlockFull> getBrokenBlock();
    void updateVolume(List<StructBlockFull> blocks);
    void restoreBlock(int x, int y, int z);
    void restore();
}
