package de.hybris.platform.core.system.query.impl;

public class MSSQLQueryProvider extends HSQLDBQueryProvider
{
    public MSSQLQueryProvider(String tableName)
    {
        super(tableName);
    }


    public String getQueryForTableCreate()
    {
        return "CREATE TABLE " + getTableName() + " ( id VARCHAR(255) NOT NULL, locked INTEGER NOT NULL, tenantId VARCHAR(255), clusterNode VARCHAR(255), lockdate DATETIME, process VARCHAR(255), instanceId BIGINT , CONSTRAINT PK_id_" +
                        getTableName() + " PRIMARY KEY ( id ) )";
    }


    public String getQueryForTransactionsIsolation()
    {
        return "SELECT \tis_read_committed_snapshot_on, \tsnapshot_isolation_state  FROM \tsys.databases  WHERE \tname = (SELECT DB_NAME() AS DataBaseName) ";
    }
}
