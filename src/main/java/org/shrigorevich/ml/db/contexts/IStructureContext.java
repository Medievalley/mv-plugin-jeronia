package org.shrigorevich.ml.db.contexts;
import org.bukkit.Location;
import org.shrigorevich.ml.db.callbacks.ICreateOneCallback;
import org.shrigorevich.ml.db.callbacks.IFindOneCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;

public interface IStructureContext {

    void loadAll();
    void saveAsync(CreateStructModel struct, ICreateOneCallback cb);
    void getStructuresAsync(Location l, IFindOneCallback cb);
}
