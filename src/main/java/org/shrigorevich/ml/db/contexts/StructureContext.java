package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.shrigorevich.ml.domain.callbacks.IFindOneCallback;
import org.shrigorevich.ml.domain.callbacks.IStructureCallback;
import org.shrigorevich.ml.db.models.CreateStructModel;
import org.shrigorevich.ml.db.models.GetStructModel;
import org.shrigorevich.ml.domain.models.IStructure;
import org.shrigorevich.ml.domain.models.Structure;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StructureContext implements IStructureContext {

    private final Plugin plugin;
    private final DataSource dataSource;
    private final BukkitScheduler scheduler;

    public StructureContext(Plugin plugin, DataSource dataSource) {
        this.plugin = plugin;
        this.dataSource = dataSource;
        this.scheduler = Bukkit.getScheduler();
    }

    public void loadAll() {

    }

    public void saveAsync(CreateStructModel st, IStructureCallback cb) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {

                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<GetStructModel> h = new BeanHandler(GetStructModel.class);
                GetStructModel m = run.insert(String.format(
                        "INSERT INTO structures (name, typeid, ownerid, destructible, world, x1, y1, z1, x2, y2, z2)\n" +
                        "VALUES ('%s', %d, %d, %b, '%s', %d, %d, %d, %d, %d, %d)\n" +
                        "RETURNING *",
                        st.name, st.typeId, st.ownerId, st.destructible, st.world, st.x1, st.y1, st.z1, st.x2, st.y2, st.z2), h);

                scheduler.runTask(plugin, () -> cb.structSaved(Optional.of(m),true, "Ok"));

            } catch (SQLException ex) {
                plugin.getLogger().severe(ex.toString());
                cb.structSaved(Optional.empty(), false, ex.toString());
            }
        });
    }

    // Write SQL query
    public void getStructuresAsync(Location l, IFindOneCallback cb) {
        scheduler.runTaskAsynchronously(plugin, () -> {
            try {
                QueryRunner run = new QueryRunner(dataSource);
                ResultSetHandler<List<Structure>> h = new BeanListHandler(Structure.class);
                List<? extends IStructure> structs = run.query(String.format("%s", 5), h);

                scheduler.runTask(plugin, () -> cb.onQueryDone(structs));

            } catch (SQLException ex) {
                plugin.getLogger().severe(ex.toString());
            }
        });
    }
}
