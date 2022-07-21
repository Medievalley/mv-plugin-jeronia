package org.shrigorevich.ml.db.contexts;
import org.bukkit.block.Block;
import org.shrigorevich.ml.db.models.GetStructModel;
import org.shrigorevich.ml.db.models.Volume;
import org.shrigorevich.ml.db.models.VolumeBlock;
import org.shrigorevich.ml.domain.callbacks.*;
import org.shrigorevich.ml.db.models.CreateStructModel;

import java.util.List;
import java.util.Optional;

public interface IStructureContext {

    void save(CreateStructModel struct, ISaveStructCallback cb);
    Optional<GetStructModel> getById(int id);
    void saveStructVolume(Volume volume, List<Block> blockList, ISaveVolumeCallback cb);
    List<VolumeBlock> getVolumeBlocks(int id);

    Optional<Volume> getVolumeById(int id);
    List<GetStructModel> getStructures();

    /**
     * @param volumeId Identifier of volume assigned to structure
     * @param structId Structure identifier
     */
    void setStructVolume(int structId, int volumeId);
}
