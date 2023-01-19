package org.shrigorevich.ml.domain.project;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.logging.log4j.LogManager;
import org.shrigorevich.ml.common.BaseContext;
import org.shrigorevich.ml.domain.project.contracts.ProjectContext;
import org.shrigorevich.ml.domain.project.models.StorageModel;
import org.shrigorevich.ml.domain.project.models.StorageModelImpl;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ProjectContextImpl extends BaseContext implements ProjectContext {
    public ProjectContextImpl(DataSource dataSource) {
        super(dataSource, LogManager.getLogger("ProjectContextImpl"));
    }

    @Override
    public StorageModel getStorage() {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            ResultSetHandler<StorageModel> h = new BeanHandler(StorageModelImpl.class);
            String sql = "SELECT deposit, resources FROM storage ORDER BY id asc limit 1";

            return run.query(sql, h);
        } catch (SQLException ex) {
            getLogger().error("Get storage: " + ex);
            return new StorageModelImpl();
        }
    }

    @Override
    public void updateResources(int amount) {
        try {
            QueryRunner run = new QueryRunner(getDataSource());
            String sql = String.format("UPDATE storage SET resources=%d", amount);
            run.update(sql);
        } catch (SQLException ex) {
            getLogger().error("Update resources: " + ex);
        }
    }
}
