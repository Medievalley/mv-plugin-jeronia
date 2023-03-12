package org.shrigorevich.ml.state.structures.models;

public interface StructBlockModel extends VolumeBlockModel {
    int getStructId();
    void setStructId(int structId);
    boolean isBroken();
    void setBroken(boolean broken);
    int getVolumeBlockId();
    void setVolumeBlockId(int volumeBlockId);
    void setTriggerDestruction(boolean triggerDestruction);
    boolean isTriggerDestruction();
    int getTypeId();
    void setTypeId(int typeId);
}
