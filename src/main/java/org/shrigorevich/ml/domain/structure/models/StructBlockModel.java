package org.shrigorevich.ml.domain.structure.models;

public interface StructBlockModel extends VolumeBlockDB {
    int getStructId();
    void setStructId(int structId);
    boolean isBroken();
    void setBroken(boolean broken);
    int getVolumeBlockId();
    void setVolumeBlockId(int volumeBlockId);
    int getId();
    void setId(int id);
    void setTriggerDestruction(boolean triggerDestruction);
    boolean isTriggerDestruction();
}
