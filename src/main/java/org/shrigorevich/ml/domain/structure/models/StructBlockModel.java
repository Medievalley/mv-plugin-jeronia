package org.shrigorevich.ml.domain.structure.models;

import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;

public interface StructBlockModel extends VolumeBlockModel {
    int getStructId();
    void setStructId(int structId);
    boolean isBroken();
    void setBroken(boolean broken);
    int getVolumeBlockId();
    void setVolumeBlockId(int volumeBlockId);
    void setTriggerDestruction(boolean triggerDestruction);
    boolean isTriggerDestruction();
    String getType();
    void setType(String type);
}
