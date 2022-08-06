package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class NpcContextImpl extends Context implements NpcContext{


    public NpcContextImpl(Plugin plugin, DataSource dataSource) {
        super(plugin, dataSource);
    };

    @Override
    public int save(StructNpcDB npc) {
        try {

            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            String sql = String.format(
                    "INSERT INTO struct_npc (x, y, z, struct_id, name) VALUES (%d, %d, %d, %d, '%s') returning id",
                    npc.getX(), npc.getY(), npc.getZ(), npc.getStructId(), npc.getName());

            return run.insert(sql, h);

        } catch (SQLException ex) {
            getPlugin().getLogger().severe("StructContext. SaveStructure: " + ex);
            return 0;
        }
    }

    @Override
    public List<StructNpcDB> get() {
        return null;
    }

    @Override
    public Optional<StructNpcDB> get(int id) {
        return Optional.empty();
    }
}
