package de.hybris.platform.core.system.query.impl;

public class OracleQueryProvider extends HSQLDBQueryProvider
{
    public OracleQueryProvider(String tableName)
    {
        super(tableName);
    }


    public String getQueryForTableCreate()
    {
        return "CREATE TABLE " + getTableName() + " ( id VARCHAR2(255) NOT NULL, locked NUMERIC(10) NOT NULL, tenantId VARCHAR2(255), clusterNode VARCHAR2(255), lockdate DATE, process VARCHAR2(255), instanceId INTEGER, CONSTRAINT PK_id_" +
                        getTableName() + " PRIMARY KEY ( id ) )";
    }
}
