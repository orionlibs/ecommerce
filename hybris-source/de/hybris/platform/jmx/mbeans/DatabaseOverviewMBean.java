package de.hybris.platform.jmx.mbeans;

import java.util.Set;

public interface DatabaseOverviewMBean
{
    String getDatabaseName();


    String getDatabaseURL();


    String getDatabaseUser();


    String getDatabaseVersion();


    String getDriverVersion();


    String getID();


    String getJNDIName();


    Integer getLoginTimeout();


    int getMaxAllowedPhysicalOpen();


    int getMaxInUse();


    int getMaxPhysicalOpen();


    int getMaxPreparedParameterCount();


    long getMillisWaitedForConnection();


    int getNumInUse();


    int getNumPhysicalOpen();


    String getSchemaName();


    boolean isCanConnectToDataSource();


    String getTablePrefix();


    boolean isReadOnly();


    void resetStats();


    boolean isActive();


    Set<String> getAllDataSourceIDs();


    long getConnections();


    default Integer getReadOnlyDataSourceReadOnlyConnectionsNumber()
    {
        return null;
    }


    default Integer getReadOnlyDataSourceReadWriteConnectionsNumber()
    {
        return null;
    }
}
