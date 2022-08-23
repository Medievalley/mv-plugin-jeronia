package org.shrigorevich.ml.db.contexts;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.bukkit.plugin.Plugin;
import org.shrigorevich.ml.domain.npc.models.StructNpcDB;
import org.shrigorevich.ml.domain.npc.models.StructNpcModel;
import org.shrigorevich.ml.domain.structure.models.LoreStructDB;
import org.shrigorevich.ml.domain.structure.models.LoreStructModel;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
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
                    "INSERT INTO struct_npc (x, y, z, struct_id, role_id, name) VALUES (%d, %d, %d, %d, %d, '%s') returning id",
                    npc.getX(), npc.getY(), npc.getZ(), npc.getStructId(), npc.getRoleId(), npc.getName());

            return run.insert(sql, h);

        } catch (SQLException ex) {
            getPlugin().getLogger().severe("NpcContext. Save: " + ex);
            return 0;
        }
    }

    @Override
    public List<StructNpcDB> get() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcDB>> h = new BeanListHandler(StructNpcModel.class);
            String sql = "select n.id, x, y, z, struct_id as structId, n.alive, n.role_id as roleId, name, s.world from struct_npc n join struct s on s.id = n.struct_id";
            return run.query(sql, h);
        } catch (SQLException ex) {
            getPlugin().getLogger().severe("NpcContext. Get all: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public List<StructNpcDB> getByStructId(int structId) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcDB>> h = new BeanListHandler(StructNpcModel.class);
            String sql = String.format("select n.id, x, y, z, struct_id as structId, n.alive, n.role_id as roleId, name, s.world \n" +
                    "from struct_npc n join struct s on s.id = n.struct_id where struct_id = %d", structId);

            return run.query(sql, h);
        } catch (SQLException ex) {
            getPlugin().getLogger().severe("NpcContext. Get by structId: " + ex);
            return new ArrayList<>(0);
        }
    }

    @Override
    public Optional<StructNpcDB> get(int id) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructNpcDB> h = new BeanHandler(StructNpcModel.class);
            String sql = String.format("select n.id, x, y, z, struct_id as structid, name, s.world from struct_npc n join struct s on s.id = n.struct_id where n.id = %d", id);

            StructNpcDB s = run.query(sql, h);
            return s == null ? Optional.empty() : Optional.of(s);

        } catch (SQLException ex) {
            getPlugin().getLogger().severe("NpcContext. GetById: " + ex);
            return Optional.empty();
        }
    }
}
