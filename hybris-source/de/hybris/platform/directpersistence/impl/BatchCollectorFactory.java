package de.hybris.platform.directpersistence.impl;

import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.util.Config;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class BatchCollectorFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(BatchCollectorFactory.class);
    public static final String OPTIMISTIC_LOCK_ENABLED = "hjmp.throw.concurrent.modification.exceptions";
    public static final String ORACLE_BATCH_UPDATE_ENABLED = "direct.persistence.jdbc.oracle.batch.enabled";
    private boolean oracleDb;
    private boolean optimisticLockingEnabled;
    private JdbcTemplate jdbcTemplate;
    private int oracleBatchSize;
    private boolean oracleBatchUpdateDisabled = false;


    @PostConstruct
    public void checkDatabaseUsed()
    {
        this.optimisticLockingEnabled = Config.getBoolean("hjmp.throw.concurrent.modification.exceptions", false);
        this.oracleDb = Config.isOracleUsed();
        if(this.oracleDb)
        {
            enableOracleUpdateBatchingIfAvailable();
        }
    }


    protected void enableOracleUpdateBatchingIfAvailable()
    {
        try
        {
            Connection conn = this.jdbcTemplate.getDataSource().getConnection();
            try
            {
                DatabaseMetaData metaData = conn.getMetaData();
                int driverMajorVersion = metaData.getDriverMajorVersion();
                int driverMinorVersion = metaData.getDriverMinorVersion();
                LOG.debug("driver version: {}.{}", Integer.valueOf(driverMajorVersion), Integer.valueOf(driverMinorVersion));
                this.oracleBatchUpdateDisabled = (driverMajorVersion > 12 || (driverMajorVersion == 12 && driverMinorVersion >= 1));
                if(conn != null)
                {
                    conn.close();
                }
            }
            catch(Throwable throwable)
            {
                if(conn != null)
                {
                    try
                    {
                        conn.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            LOG.warn("error while determining the version of Oracle JDBC Driver", e);
        }
    }


    public BatchCollector createBatchCollector()
    {
        DefaultBatchCollector defaultBatchCollector;
        if(this.oracleDb && !this.oracleBatchUpdateDisabled)
        {
            OracleBatchCollector oracleBatchCollector = new OracleBatchCollector(this.jdbcTemplate, this.oracleBatchSize, this.optimisticLockingEnabled);
        }
        else
        {
            defaultBatchCollector = new DefaultBatchCollector();
        }
        return (BatchCollector)defaultBatchCollector;
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Required
    public void setOracleBatchSize(int oracleBatchSize)
    {
        this.oracleBatchSize = oracleBatchSize;
    }
}
