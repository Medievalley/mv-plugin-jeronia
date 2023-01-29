package org.shrigorevich.ml.domain.project;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.project.models.StorageModel;
import org.shrigorevich.ml.domain.structure.StructureType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Optional;

public class ProjectContextImpl extends BaseContext implements ProjectContext {
    public ProjectContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("ProjectContextImpl"));
    }

    @Override
    public Optional<StorageModel> getStorage() throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StorageModel> h = new BeanHandler<>(StorageModelImpl.class);
            StorageModel storage = run.query(String.format("SELECT id, resources, deposit from struct WHERE type_id=%d", StructureType.MAIN.getTypeId()), h);
            return storage == null ? Optional.empty() : Optional.of(storage);
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception("Error while getting storage from main struct");
        }
    }

    @Override
    public void updateResources(int structId, int amount) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(String.format("UPDATE struct SET resources=%d WHERE id=%d", amount, structId));
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while updating storage(%d) resources by %d", structId, amount));
        }
    }

    @Override
    public void updateDeposit(int structId, int amount) throws Exception {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            run.update(String.format("UPDATE struct SET deposit=%d WHERE id=%d", amount, structId));
        } catch (SQLException ex) {
            getLogger().error(ex.getMessage());
            throw new Exception(String.format("Error while updating storage(%d) deposit by %d", structId, amount));
        }
    }
}
