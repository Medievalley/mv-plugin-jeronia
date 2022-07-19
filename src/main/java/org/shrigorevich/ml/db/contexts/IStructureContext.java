package org.shrigorevich.ml.db.contexts;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.shrigorevich.ml.db.models.Volume;
import org.shrigorevich.ml.domain.callbacks.*;
import org.shrigorevich.ml.db.models.CreateStructModel;

import java.util.List;

public interface IStructureContext {

    void saveAsync(CreateStructModel struct, ISaveStructCallback cb);
    void getStructuresAsync(Location l, IFindOneCallback cb);
    void getByIdAsync(int id, IFindStructCallback cb);
    void saveStructVolumeAsync(Volume volume, List<Block> blockList, ISaveVolumeCallback cb);
    void getVolumeByIdAsync(int id, IGetVolumeCallback cb);
    void getStructures(IGetStructsCallback cb);
}
