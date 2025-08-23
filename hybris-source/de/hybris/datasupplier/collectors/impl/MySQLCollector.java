package de.hybris.datasupplier.collectors.impl;

import de.hybris.platform.util.jdbc.DatabaseNameResolver;

public class MySQLCollector extends AbstractDatabaseCollector
{
    public boolean isApplicable(String url)
    {
        return "mysql".equals(DatabaseNameResolver.guessDatabaseNameFromURL(url));
    }


    public String getName(String url)
    {
        String name = matchAndReturn("/[^//]*(\\?|$)", url);
        if(name == null)
        {
            return null;
        }
        return name.split("\\?")[0].replaceAll("/|\\?", "");
    }
}
