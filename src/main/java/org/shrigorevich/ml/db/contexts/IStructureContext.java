package org.shrigorevich.ml.db.contexts;
import org.bukkit.Location;
import org.shrigorevich.ml.domain.callbacks.IFindOneCallback;
import org.shrigorevich.ml.domain.callbacks.IStructureCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;

public interface IStructureContext {

    void loadAll();
    void saveAsync(CreateStructModel struct, IStructureCallback cb);
    void getStructuresAsync(Location l, IFindOneCallback cb);
}
