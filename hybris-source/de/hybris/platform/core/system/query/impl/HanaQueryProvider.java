package de.hybris.platform.core.system.query.impl;

public class HanaQueryProvider extends HSQLDBQueryProvider
{
    public HanaQueryProvider(String tableName)
    {
        super(tableName);
    }


    public String getQueryForTableCreate()
    {
        return "CREATE TABLE " + getTableName() + " ( id NVARCHAR(255) NOT NULL, locked INTEGER NOT NULL, tenantId NVARCHAR(255), clusterNode NVARCHAR(255), lockdate TIMESTAMP, process NVARCHAR(255), instanceId BIGINT ,  PRIMARY KEY ( id ) )";
    }
}
