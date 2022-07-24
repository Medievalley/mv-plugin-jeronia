package org.shrigorevich.ml.db.contexts;
import org.shrigorevich.ml.db.models.*;
import org.shrigorevich.ml.domain.callbacks.*;

import java.util.List;
import java.util.Optional;

public interface IStructureContext {

    int save(CreateStruct struct);
    Optional<GetStruct> getById(int id);
    void createVolume(Volume volume, List<VolumeBlock> volumeBlocks, ISaveVolumeCallback cb);
    List<VolumeBlock> getVolumeBlocks(int id);
    Optional<Volume> getVolumeById(int id);
    List<GetStruct> getStructures();
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
    Optional<StructBlockFull> getStructBlock(int x, int y, int z, int volumeId, int structId);
    void saveStructBlocks(List<StructBlock> blocks);
    List<StructBlockFull> getStructBlocks(int structId);
    int updateStructBlocksBrokenStatus(List<StructBlockFull> blocks);

}
