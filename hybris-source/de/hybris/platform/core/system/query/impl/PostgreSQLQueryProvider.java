package de.hybris.platform.core.system.query.impl;

public class PostgreSQLQueryProvider extends HSQLDBQueryProvider
{
    public PostgreSQLQueryProvider(String tableName)
    {
        super(tableName);
    }


    public String getQueryForTableCreate()
    {
        return "CREATE TABLE " + getTableName() + " ( id VARCHAR(255) NOT NULL, locked INTEGER NOT NULL, tenantId VARCHAR(255), clusterNode VARCHAR(255), lockdate TIMESTAMP, process VARCHAR(255), instanceId BIGINT , CONSTRAINT PK_id_" +
                        getTableName() + " PRIMARY KEY ( id ) )";
    }
}
