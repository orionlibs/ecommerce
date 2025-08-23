package de.hybris.datasupplier.collectors.impl;

import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.net.URI;
import java.net.URISyntaxException;

public class SqlServerCollector extends AbstractDatabaseCollector
{
    public boolean isApplicable(String url)
    {
        return "sqlserver".equals(DatabaseNameResolver.guessDatabaseNameFromURL(url));
    }


    public URI getHost(String url) throws URISyntaxException
    {
        return new URI(matchAndReturn("sqlserver:[^:]*", url));
    }


    public String getName(String url)
    {
        String instanceName = matchAndReturn(";instanceName\\=[^;]*", url);
        if(instanceName != null)
        {
            return instanceName.replace(";instanceName=", "");
        }
        String databaseName = matchAndReturn(";databaseName\\=[^;]*", url);
        if(databaseName != null)
        {
            return databaseName.replace(";databaseName=", "");
        }
        return "Default";
    }
}
