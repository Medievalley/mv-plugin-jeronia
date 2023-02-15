package org.shrigorevich.ml.domain.structure;

import java.util.List;

public interface VolumeStruct extends Structure {
    int getVolumeId();
    List<StructBlock> getStructBlocks();
    List<StructBlock> getBrokenBlocks();
}
