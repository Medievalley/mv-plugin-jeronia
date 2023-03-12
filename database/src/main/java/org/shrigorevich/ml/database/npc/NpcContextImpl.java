package org.shrigorevich.ml.database.npc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.Coords;
import org.shrigorevich.ml.database.BaseContext;
import org.shrigorevich.ml.state.npc.NpcContext;
import org.shrigorevich.ml.state.npc.models.StructNpcModel;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class NpcContextImpl extends BaseContext implements NpcContext {

    private final NpcQueryBuilder queryBuilder;

    public NpcContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("NpcContextImpl"));
        this.queryBuilder = new NpcQueryBuilder();
    }

    @Override
    public int save(String name, int roleId, int structId, Coords spawn, Coords work) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<Integer> h = new ScalarHandler<>();
            return run.insert(queryBuilder.saveNpc(name, roleId, structId, spawn, work), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while saving npc: %s", name));
        }
    }

    @Override
    public List<StructNpcModel> get() throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcModel>> h = new BeanListHandler<>(StructNpcModelImpl.class);
            return run.query(queryBuilder.getNpcList(), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while getting npc list");
        }
    }

    @Override
    public List<StructNpcModel> getByStructId(int structId) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<List<StructNpcModel>> h = new BeanListHandler<>(StructNpcModelImpl.class);
            return run.query(queryBuilder.getByStructId(structId), h);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while getting npc by struct id: %d", structId));
        }
    }

    @Override
    public Optional<StructNpcModel> get(int id) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StructNpcModel> h = new BeanHandler<>(StructNpcModelImpl.class);
            StructNpcModel s = run.query(queryBuilder.getById(id), h);
            return s == null ? Optional.empty() : Optional.of(s);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while getting npc by id: %d", id));
        }
    }
}

//SAVE
//            String workLocSql = String.format(
//                    "INSERT INTO location (x, y, z) VALUES (%d, %d, %d) returning id",
//                    npc.getWorkX(), npc.getWorkY(), npc.getWorkZ());
//            String spawnLocSql = String.format(
//                    "INSERT INTO location (x, y, z) VALUES (%d, %d, %d) returning id",
//                    npc.getSpawnX(), npc.getSpawnY(), npc.getSpawnZ());
//
//            int workLocId = run.insert(workLocSql, h);
//            int spawnLocId = run.insert(spawnLocSql, h);
//
//            String sql = String.format(
//                    "INSERT INTO struct_npc (name, role_id, struct_id, spawn, work) VALUES ('%s', %d, %d, %d, %d) returning id",
//                    npc.getName(), npc.getRoleId(), npc.getStructId(), spawnLocId, workLocId);
