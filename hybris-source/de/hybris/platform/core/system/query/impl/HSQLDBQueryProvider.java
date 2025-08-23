package de.hybris.platform.core.system.query.impl;

import de.hybris.platform.core.system.query.QueryProvider;
import de.hybris.platform.util.Config;

public class HSQLDBQueryProvider implements QueryProvider
{
    private final String tableName;


    public HSQLDBQueryProvider(String tableName)
    {
        this.tableName = tableName;
    }


    public String getQueryForTransactionsIsolation()
    {
        return "";
    }


    public String getQueryForLock()
    {
        return "UPDATE " + getTableName() + " SET locked=? , tenantId=?, clusterNode=?, lockdate=?, process=?, instanceId=? WHERE id=? AND locked=?";
    }


    public String getQueryForUnlock()
    {
        return "UPDATE " + getTableName() + " SET locked=? ,instanceId=? WHERE locked=? AND clusterNode=?";
    }


    public String getQueryForTableCreate()
    {
        boolean cachedTabled = Config.getBoolean("hsqldb.usecachedtables", false);
        return "CREATE " + (cachedTabled ? "CACHED " : "") + "TABLE " + getTableName() + " ( id VARCHAR(255) NOT NULL, locked INTEGER NOT NULL, tenantId VARCHAR(255), clusterNode VARCHAR(255), lockdate TIMESTAMP, process VARCHAR(255), instanceId BIGINT , CONSTRAINT PK_id_" +
                        getTableName() + " PRIMARY KEY ( id ) )";
    }


    public String getQueryForRowInsert()
    {
        return "INSERT INTO " + getTableName() + " ( id, locked ) VALUES ( ?, ? )";
    }


    public String getQueryForSelect()
    {
        return "SELECT tenantId,clusterNode,lockdate,locked,process,instanceId FROM " + getTableName() + " WHERE id=?";
    }


    public String getTableName()
    {
        return this.tableName;
    }
}
