package org.shrigorevich.ml.db.contexts;
import org.bukkit.block.Block;
import org.shrigorevich.ml.db.models.*;
import org.shrigorevich.ml.domain.callbacks.*;

import java.util.List;
import java.util.Optional;

public interface IStructureContext {

    int save(CreateStructModel struct);
    Optional<GetStructModel> getById(int id);
    void saveStructVolume(Volume volume, List<VolumeBlock> volumeBlocks, ISaveVolumeCallback cb);
    List<VolumeBlock> getVolumeBlocks(int id);

    Optional<Volume> getVolumeById(int id);
    List<GetStructModel> getStructures();

    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
    Optional<VolumeBlock> getVolumeBlock(int x, int y, int z, int volumeId);
    int saveBrokenBlocks(List<BrokenBlock> brokenBlocks);
    long getVolumeNotAirBlocksNumber(int volumeId);
}
