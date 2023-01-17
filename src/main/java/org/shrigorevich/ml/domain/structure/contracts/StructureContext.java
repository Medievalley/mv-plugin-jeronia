package org.shrigorevich.ml.domain.structure.contracts;
import org.shrigorevich.ml.domain.structure.models.*;
import org.shrigorevich.ml.domain.volume.models.VolumeBlockModel;
import org.shrigorevich.ml.domain.volume.models.VolumeModel;

import java.util.List;
import java.util.Optional;

public interface StructureContext {

    int save(StructModel struct);
    Optional<StructModel> getById(int id);
    int createVolume(VolumeModel volume, List<VolumeBlockModel> volumeBlocks);
    List<VolumeBlockModel> getVolumeBlocks(int id);
    Optional<VolumeModel> getVolumeById(int id);
    List<StructModel> getStructures();
    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
    Optional<StructBlockModel> getStructBlock(int x, int y, int z, int volumeId, int structId);
    void saveStructBlocks(List<StructBlockModel> blocks);
    List<StructBlockModel> getStructBlocks(int structId);
    int updateBlocksStatus(List<StructBlockModel> blocks, boolean isBroken);
    void restoreBlock(int id);
    void restore(int structId);
    void removeVolume(int structId);
    void updateStock(int structId, int stockSize);
}
