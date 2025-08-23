package de.hybris.datasupplier.collectors.impl;

import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.net.URI;
import java.net.URISyntaxException;

public class HsqlCollector extends AbstractDatabaseCollector
{
    public boolean isApplicable(String url)
    {
        return "hsqldb".equals(DatabaseNameResolver.guessDatabaseNameFromURL(url));
    }


    public URI getHost(String url) throws URISyntaxException
    {
        return new URI("localhost");
    }


    public String getName(String url)
    {
        return "hsqldb";
    }
}
