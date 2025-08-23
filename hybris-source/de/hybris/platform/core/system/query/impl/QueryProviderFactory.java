package de.hybris.platform.core.system.query.impl;

import com.meterware.httpunit.UnsupportedActionException;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.system.query.QueryProvider;

public class QueryProviderFactory
{
    private final String databaseName;
    public static final String LOCK_TABLE = "SYSTEMINIT";
    static final String ID_COLUMN = "id";
    static final String LOCKED_COLUMN = "locked";
    static final String TENANT_COLUMN = "tenantId";
    static final String CLUSTER_COLUMN = "clusterNode";
    static final String STARTDATE_COLUMN = "lockdate";
    static final String PROCESS_COLUMN = "process";
    static final String UID_COLUMN = "instanceId";


    public QueryProviderFactory(String dataBasename)
    {
        this.databaseName = dataBasename;
    }


    protected String getLockTableName()
    {
        return Registry.getMasterTenant().getConfig().getParameter("db.tableprefix") + "SYSTEMINIT";
    }


    public QueryProvider getQueryProviderInstance()
    {
        if(this.databaseName == "hsqldb")
        {
            return (QueryProvider)new HSQLDBQueryProvider(getLockTableName());
        }
        if(this.databaseName == "mysql")
        {
            return (QueryProvider)new MySQLQueryProvider(getLockTableName());
        }
        if(this.databaseName == "sqlserver")
        {
            return (QueryProvider)new MSSQLQueryProvider(getLockTableName());
        }
        if(this.databaseName == "oracle")
        {
            return (QueryProvider)new OracleQueryProvider(getLockTableName());
        }
        if(this.databaseName == "sap")
        {
            return (QueryProvider)new HanaQueryProvider(getLockTableName());
        }
        if(this.databaseName == "postgresql")
        {
            return (QueryProvider)new PostgreSQLQueryProvider(getLockTableName());
        }
        throw new UnsupportedActionException(null);
    }
}
