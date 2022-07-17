package org.shrigorevich.ml.db.contexts;
import org.bukkit.Location;
import org.shrigorevich.ml.domain.callbacks.IFindOneCallback;
import org.shrigorevich.ml.domain.callbacks.IFindStructCallback;
import org.shrigorevich.ml.domain.callbacks.ISaveStructCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;

public interface IStructureContext {

    void loadAll();
    void saveAsync(CreateStructModel struct, ISaveStructCallback cb);
    void getStructuresAsync(Location l, IFindOneCallback cb);
    void getByIdAsync(int id, IFindStructCallback cb);
}
