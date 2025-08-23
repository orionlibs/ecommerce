package de.hybris.platform.jmx.mbeans.impl;

import de.hybris.platform.jalo.flexiblesearch.internal.ReadOnlyConditionsHelper;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jmx.mbeans.DatabaseOverviewMBean;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description = "Gives an overview of the data source of the current tenant.")
public class DatabaseOverviewMBeanImpl extends AbstractJMXMBean implements DatabaseOverviewMBean
{
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper = new ReadOnlyConditionsHelper();


    protected HybrisDataSource getCurrentTenantMasterDataSource()
    {
        return (HybrisDataSource)(new Object(this))
                        .getResult();
    }


    protected Optional<HybrisDataSource> getCurrentTenantReadOnlyDataSource()
    {
        return (Optional<HybrisDataSource>)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "Name of the database.")
    public String getDatabaseName()
    {
        return getCurrentTenantMasterDataSource().getDatabaseName();
    }


    @ManagedAttribute(description = "URL of the database.")
    public String getDatabaseURL()
    {
        return getCurrentTenantMasterDataSource().getDatabaseURL();
    }


    @ManagedAttribute(description = "The used database user.")
    public String getDatabaseUser()
    {
        return getCurrentTenantMasterDataSource().getDatabaseUser();
    }


    @ManagedAttribute(description = "The database version.")
    public String getDatabaseVersion()
    {
        return getCurrentTenantMasterDataSource().getDatabaseVersion();
    }


    @ManagedAttribute(description = "The database driver version.")
    public String getDriverVersion()
    {
        return getCurrentTenantMasterDataSource().getDriverVersion();
    }


    @ManagedAttribute(description = "ID of the current data source.")
    public String getID()
    {
        return getCurrentTenantMasterDataSource().getID();
    }


    @ManagedAttribute(description = "The Java Naming and Directory Interface name.")
    public String getJNDIName()
    {
        return getCurrentTenantMasterDataSource().getJNDIName();
    }


    @ManagedAttribute(description = "Timeout for login. The zero value denotes the default system timeout if exists. Otherwise it means, that there is no timeout.")
    public Integer getLoginTimeout()
    {
        try
        {
            return Integer.valueOf(getCurrentTenantMasterDataSource().getLoginTimeout());
        }
        catch(UnsupportedOperationException u)
        {
            return Integer.valueOf(0);
        }
        catch(SQLException e)
        {
            return Integer.valueOf(0);
        }
    }


    @ManagedAttribute(description = "The maximum number of objects that can be allocated by the connection pool.")
    public int getMaxAllowedPhysicalOpen()
    {
        return getCurrentTenantMasterDataSource().getMaxAllowedPhysicalOpen();
    }


    @ManagedAttribute(description = "The maximum number of connections that may be used.")
    public int getMaxInUse()
    {
        return getCurrentTenantMasterDataSource().getMaxInUse();
    }


    @ManagedAttribute(description = "The maximum number of physical SQL connections.")
    public int getMaxPhysicalOpen()
    {
        return getCurrentTenantMasterDataSource().getMaxPhysicalOpen();
    }


    @ManagedAttribute(description = "The maximum number of parameters within one prepared statement. If no limit exists, the value is -1.")
    public int getMaxPreparedParameterCount()
    {
        return getCurrentTenantMasterDataSource().getMaxPreparedParameterCount();
    }


    @ManagedAttribute(description = "Time in milliseconds that was needed to get a connection from the pool.")
    public long getMillisWaitedForConnection()
    {
        return getCurrentTenantMasterDataSource().getMillisWaitedForConnection();
    }


    @ManagedAttribute(description = "The number of instances currently borrowed from the pool.")
    public int getNumInUse()
    {
        return getCurrentTenantMasterDataSource().getNumInUse();
    }


    @ManagedAttribute(description = "The number of currently open JDBC connections.")
    public int getNumPhysicalOpen()
    {
        return getCurrentTenantMasterDataSource().getNumPhysicalOpen();
    }


    @ManagedAttribute(description = "The schema name of the database.")
    public String getSchemaName()
    {
        return getCurrentTenantMasterDataSource().getSchemaName();
    }


    @ManagedAttribute(description = "Is set to true if a connection to the pool is possible.")
    public boolean isCanConnectToDataSource()
    {
        return !getCurrentTenantMasterDataSource().cannotConnect();
    }


    @ManagedAttribute(description = "The table prefix for all tables.")
    public String getTablePrefix()
    {
        return getCurrentTenantMasterDataSource().getTablePrefix();
    }


    @ManagedAttribute(description = "Is set to true if the data source is in read-only mode.")
    public boolean isReadOnly()
    {
        return getCurrentTenantMasterDataSource().isReadOnly();
    }


    @ManagedOperation(description = "Reset values for MillisWaitedForConnection, MaxInUse and TotalGets.")
    public void resetStats()
    {
        getCurrentTenantMasterDataSource().resetStats();
    }


    @ManagedAttribute(description = "Is set to true if the current data source is active.")
    public boolean isActive()
    {
        return ((Boolean)(new Object(this))
                        .getResult()).booleanValue();
    }


    @ManagedAttribute(description = "A list of IDs of all available data source.")
    public Set<String> getAllDataSourceIDs()
    {
        return (Set<String>)(new Object(this))
                        .getResult();
    }


    @ManagedAttribute(description = "The total number of connections.")
    public long getConnections()
    {
        return getCurrentTenantMasterDataSource().totalGets();
    }


    @ManagedAttribute(description = "The total number of read-only connections.")
    public Integer getReadOnlyDataSourceReadOnlyConnectionsNumber()
    {
        Optional<HybrisDataSource> hybrisDataSource = getCurrentTenantReadOnlyDataSource();
        return hybrisDataSource.<Integer>map(HybrisDataSource::getNumReadOnlyOpen).orElse(null);
    }


    @ManagedAttribute(description = "The total number of read-write connections from read-only data source.")
    public Integer getReadOnlyDataSourceReadWriteConnectionsNumber()
    {
        Optional<HybrisDataSource> hybrisDataSource = getCurrentTenantReadOnlyDataSource();
        return hybrisDataSource.<Integer>map(dataSource -> {
            int numPhysicalOpenConnections = dataSource.getConnectionPool().getNumPhysicalOpen();
            int numReadOnlyOpenConnections = dataSource.getConnectionPool().getNumReadOnlyOpen();
            return Integer.valueOf(numPhysicalOpenConnections - numReadOnlyOpenConnections);
        }).orElse(null);
    }
}
