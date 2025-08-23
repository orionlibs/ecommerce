package de.hybris.platform.servicelayer.tenant;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.numberseries.SerialNumberGenerator;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadFactory;

public class MockTenant implements Tenant
{
    private final String tenantId;


    public void resetTenantRestartMarker()
    {
        throw new UnsupportedOperationException();
    }


    public MockTenant(String tenantId)
    {
        this.tenantId = tenantId;
    }


    public HybrisDataSource getDataSource(String className)
    {
        throw new UnsupportedOperationException();
    }


    public Cache getCache()
    {
        throw new UnsupportedOperationException();
    }


    public ConfigIntf getConfig()
    {
        throw new UnsupportedOperationException();
    }


    public HybrisDataSource getDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public InvalidationManager getInvalidationManager()
    {
        throw new UnsupportedOperationException();
    }


    public JaloConnection getJaloConnection()
    {
        throw new UnsupportedOperationException();
    }


    public PersistenceManager getPersistenceManager()
    {
        throw new UnsupportedOperationException();
    }


    public PersistencePool getPersistencePool()
    {
        throw new UnsupportedOperationException();
    }


    public SerialNumberGenerator getSerialNumberGenerator()
    {
        throw new UnsupportedOperationException();
    }


    public SingletonCreator getSingletonCreator()
    {
        throw new UnsupportedOperationException();
    }


    public SystemEJB getSystemEJB()
    {
        throw new UnsupportedOperationException();
    }


    public String getTenantID()
    {
        return this.tenantId;
    }


    public List<String> getTenantSpecificExtensionNames()
    {
        throw new UnsupportedOperationException();
    }


    public Locale getTenantSpecificLocale()
    {
        throw new UnsupportedOperationException();
    }


    public TimeZone getTenantSpecificTimeZone()
    {
        throw new UnsupportedOperationException();
    }


    public ThreadPool getThreadPool()
    {
        throw new UnsupportedOperationException();
    }


    public ThreadPool getWorkersThreadPool()
    {
        throw new UnsupportedOperationException();
    }


    public int hashCode()
    {
        return this.tenantId.hashCode();
    }


    public boolean equals(Object object)
    {
        if(object == null || !object.getClass().equals(getClass()))
        {
            return false;
        }
        return this.tenantId.equals(((MockTenant)object).tenantId);
    }


    public JaloSession getActiveSession()
    {
        throw new UnsupportedOperationException();
    }


    public void deactivateSlaveDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public String activateSlaveDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public void activateSlaveDataSource(String id)
    {
        throw new UnsupportedOperationException();
    }


    public HybrisDataSource getMasterDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public boolean isSlaveDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public boolean isAlternativeMasterDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public Collection<HybrisDataSource> getAllSlaveDataSources()
    {
        throw new UnsupportedOperationException();
    }


    public Set<String> getAllDataSourceIDs()
    {
        throw new UnsupportedOperationException();
    }


    public void forceMasterDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public boolean isForceMaster()
    {
        throw new UnsupportedOperationException();
    }


    public void activateAlternativeMasterDataSource(String id)
    {
        throw new UnsupportedOperationException();
    }


    public void deactivateAlternativeDataSource()
    {
        throw new UnsupportedOperationException();
    }


    public Set<String> getAllAlternativeMasterDataSourceIDs()
    {
        throw new UnsupportedOperationException();
    }


    public Collection<HybrisDataSource> getAllAlternativeMasterDataSources()
    {
        throw new UnsupportedOperationException();
    }


    public Set<String> getAllSlaveDataSourceIDs()
    {
        throw new UnsupportedOperationException();
    }


    public Thread createAndRegisterBackgroundThread(Runnable payload, ThreadFactory factory)
    {
        throw new UnsupportedOperationException();
    }
}
