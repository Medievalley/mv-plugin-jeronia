package org.shrigorevich.ml.db.contexts;
import org.shrigorevich.ml.domain.callbacks.*;
import org.shrigorevich.ml.domain.structure.models.LoreStructDB;
import org.shrigorevich.ml.domain.structure.models.StructBlockDB;
import org.shrigorevich.ml.domain.structure.models.VolumeBlockDB;
import org.shrigorevich.ml.domain.structure.models.VolumeDB;

import java.util.List;
import java.util.Optional;

public interface StructureContext {

    int save(LoreStructDB struct);
    Optional<LoreStructDB> getById(int id);
    void createVolume(VolumeDB volume, List<VolumeBlockDB> volumeBlocks, ISaveVolumeCallback cb);
    List<VolumeBlockDB> getVolumeBlocks(int id);
    Optional<VolumeDB> getVolumeById(int id);
    List<LoreStructDB> getStructures();
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
    Optional<StructBlockDB> getStructBlock(int x, int y, int z, int volumeId, int structId);
    void saveStructBlocks(List<StructBlockDB> blocks);
    List<StructBlockDB> getStructBlocks(int structId);
    int updateStructBlocksBrokenStatus(List<StructBlockDB> blocks);
    void delete(int structId);
    void restore(int structId);
    void removeVolume(int structId);
}
