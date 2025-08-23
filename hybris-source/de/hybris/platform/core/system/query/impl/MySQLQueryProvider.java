package de.hybris.platform.core.system.query.impl;

import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;

public class MySQLQueryProvider extends HSQLDBQueryProvider
{
    public MySQLQueryProvider(String tableName)
    {
        super(tableName);
    }


    public String getQueryForTableCreate()
    {
        StringBuilder stringBuilder = new StringBuilder();
        String tableType = Config.getParameter("mysql.tabletype");
        if(StringUtils.isNotBlank(tableType))
        {
            stringBuilder.append(" ENGINE ").append(tableType);
        }
        String optionalDefs = Config.getParameter("mysql.optional.tabledefs");
        if(StringUtils.isNotBlank(optionalDefs))
        {
            stringBuilder.append(" ").append(optionalDefs);
        }
        return "CREATE TABLE " + getTableName() + " ( id VARCHAR(255) NOT NULL, locked INTEGER NOT NULL, tenantId VARCHAR(255), clusterNode VARCHAR(255), lockdate TIMESTAMP, process VARCHAR(255), instanceId BIGINT, CONSTRAINT PK_id_" +
                        getTableName() + " PRIMARY KEY ( id ) )" + stringBuilder
                        .toString();
    }
}
