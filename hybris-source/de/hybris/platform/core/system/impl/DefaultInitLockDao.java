package de.hybris.platform.core.system.impl;

import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.system.InitializationLockDao;
import de.hybris.platform.core.system.InitializationLockInfo;
import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.core.system.query.impl.QueryProviderFactory;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.sql.Timestamp;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DefaultInitLockDao implements InitializationLockDao
{
    private static final int SINGLE_ROW_COUNT = 1;
    private static final int NO_ROWS_COUNT = 0;
    private static final int UNDEFINED_ROW_COUNT = -1;
    private static final int MAX_WAIT_TIME = 10000;
    private static final int SLEEP_BETWEEN_RETRY = 100;
    private static final Logger LOG = Logger.getLogger(DefaultInitLockDao.class.getName());
    private static final Integer UNLOCK_VALUE = Integer.valueOf(0);
    private static final Integer LOCK_VALUE = Integer.valueOf(1);
    private static final String GLOBAL_ID = "globalID";
    private static final transient Long uniqueInstanceIdentifier = Long.valueOf(PK.createUUIDPK(0).getLongValue());
    private static final long serialVersionUID = -1782834225628918483L;
    private final QueryProvider queryProvider;


    public DefaultInitLockDao()
    {
        this(null);
    }


    public DefaultInitLockDao(QueryProvider queryProvider)
    {
        this.queryProvider = initQueryProviderIfNeeded(queryProvider);
    }


    protected HybrisDataSource getInitializedMasterDataSource()
    {
        HybrisDataSource masterTenantDataSource = Registry.getMasterTenant().getMasterDataSource();
        assertDataSourceValid(masterTenantDataSource);
        return masterTenantDataSource;
    }


    private void assertDataSourceValid(HybrisDataSource masterTenantDataSource)
    {
        if(masterTenantDataSource.cannotConnect() || masterTenantDataSource.getConnectionPool().isPoolClosed())
        {
            throw new IllegalStateException("Given master data source " + masterTenantDataSource + " seemed to be invalid (cannot connnect = " + masterTenantDataSource
                            .cannotConnect() + " ), (pool closed = " + masterTenantDataSource
                            .getConnectionPool().isPoolClosed() + ")");
        }
    }


    private QueryProvider initQueryProviderIfNeeded(QueryProvider givenqueryProvider)
    {
        if(givenqueryProvider == null)
        {
            return (new QueryProviderFactory(getInitializedMasterDataSource().getDatabaseName())).getQueryProviderInstance();
        }
        return givenqueryProvider;
    }


    public InitializationLockInfo readLockInfo()
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)getInitializedMasterDataSource());
        try
        {
            return (InitializationLockInfo)template.queryForObject(this.queryProvider
                            .getQueryForSelect(), new Object[] {"globalID"}, (RowMapper)new Object(this));
        }
        catch(DataAccessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't read lock info :" + e.getMessage(), (Throwable)e);
            }
            return null;
        }
    }


    public boolean lockRow(Tenant forTenant, String processName)
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)getInitializedMasterDataSource());
        int updateCount = lockRowInternal(forTenant, processName, template);
        if(updateCount == 1)
        {
            return true;
        }
        if(updateCount == 0)
        {
            return false;
        }
        throw new IllegalStateException("unexpected update count " + updateCount);
    }


    private int lockRowInternal(Tenant forTenant, String processName, JdbcTemplate template)
    {
        int updateCount = -1;
        long maxTime = System.currentTimeMillis() + 10000L;
        try
        {
            do
            {
                try
                {
                    updateCount = template.update(this.queryProvider
                                    .getQueryForLock(), new Object[] {LOCK_VALUE, forTenant
                                    .getTenantID(),
                                    String.valueOf(((AbstractTenant)forTenant).getClusterID()), new Timestamp(
                                    System.currentTimeMillis()), processName, uniqueInstanceIdentifier, "globalID", UNLOCK_VALUE});
                }
                catch(ConcurrencyFailureException e)
                {
                    LOG.warn(".");
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e);
                    }
                }
                finally
                {
                    waitIfNotFetched(updateCount);
                }
            }
            while(updateCount == -1 && System.currentTimeMillis() < maxTime);
        }
        catch(InterruptedException e)
        {
            Thread.currentThread().interrupt();
            LOG.warn("Thread interrupted while waiting for next lock aquisition , " + e.getMessage());
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
            updateCount = 0;
        }
        return updateCount;
    }


    private void waitIfNotFetched(int updateCount) throws InterruptedException
    {
        if(updateCount == -1)
        {
            Thread.sleep(100L);
        }
    }


    public boolean releaseRow(Tenant forTenant)
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)getInitializedMasterDataSource());
        try
        {
            return (template.update(this.queryProvider
                            .getQueryForUnlock(), new Object[] {UNLOCK_VALUE, null, LOCK_VALUE,
                            String.valueOf(((AbstractTenant)forTenant).getClusterID())}) == 1);
        }
        catch(DataAccessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't update a row with query " + this.queryProvider.getQueryForUnlock() + " , " + e.getMessage(), (Throwable)e);
            }
            return false;
        }
    }


    public boolean insertRow()
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)getInitializedMasterDataSource());
        try
        {
            return (template.update(this.queryProvider
                            .getQueryForRowInsert(), new Object[] {"globalID", UNLOCK_VALUE}) == 1);
        }
        catch(DuplicateKeyException e)
        {
            LOG.trace(e);
            return true;
        }
        catch(DataAccessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't insert a row with query " + this.queryProvider.getQueryForRowInsert() + " , " + e.getMessage(), (Throwable)e);
            }
            return false;
        }
    }


    public void createTable()
    {
        JdbcTemplate template = new JdbcTemplate((DataSource)getInitializedMasterDataSource());
        try
        {
            template.update(this.queryProvider
                            .getQueryForTableCreate());
        }
        catch(DataAccessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Can't create a  lock table :" + e.getMessage(), (Throwable)e);
            }
        }
    }


    public long getUniqueInstanceIdentifier()
    {
        return uniqueInstanceIdentifier.longValue();
    }
}
