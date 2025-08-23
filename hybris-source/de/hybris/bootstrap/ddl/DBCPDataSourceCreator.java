package de.hybris.bootstrap.ddl;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.log4j.Logger;

public class DBCPDataSourceCreator implements DataSourceCreator
{
    private static final Logger LOG = Logger.getLogger(DBCPDataSourceCreator.class.getName());


    public DataSource createDataSource(DatabaseSettings databaseSettings)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Connecting to [" + databaseSettings.getUser() + "]@" + databaseSettings.getUrl());
            }
            Properties dataSourceProperties = new Properties();
            String databaseUrl = adjustUrlForSqlserver(databaseSettings.getUrl());
            dataSourceProperties.setProperty("url", databaseUrl);
            dataSourceProperties.setProperty("driverClassName", databaseSettings.getDriverName());
            dataSourceProperties.setProperty("username", databaseSettings.getUser());
            dataSourceProperties.setProperty("password", databaseSettings.getPassword());
            return BasicDataSourceFactory.createDataSource(dataSourceProperties);
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Couldn't create data source", e);
        }
    }


    private String adjustUrlForSqlserver(String url)
    {
        String adjustedUrl = url;
        if(adjustedUrl.contains("sqlserver") && !adjustedUrl.contains("encrypt"))
        {
            adjustedUrl = adjustedUrl.concat(";encrypt=false");
        }
        return adjustedUrl;
    }
}
