package org.shrigorevich.ml.domain.callbacks;

import org.shrigorevich.ml.db.models.VolumeBlock;

import java.util.List;

public interface IGetVolumeCallback {
    void volumeFound(boolean res, List<VolumeBlock> blocks);
}
