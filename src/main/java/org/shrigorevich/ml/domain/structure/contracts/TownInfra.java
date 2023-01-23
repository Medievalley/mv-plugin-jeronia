package org.shrigorevich.ml.domain.structure.contracts;

public interface TownInfra extends Structure {
    int getPriority();
    int getVolumeId();
    Storage getStorage();
}
