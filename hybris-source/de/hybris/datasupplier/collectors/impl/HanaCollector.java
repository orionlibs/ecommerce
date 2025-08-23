package de.hybris.datasupplier.collectors.impl;

import de.hybris.platform.util.jdbc.DatabaseNameResolver;

public class HanaCollector extends AbstractDatabaseCollector
{
    public static final String HANA = "sap".intern();


    public boolean isApplicable(String url)
    {
        return HANA.equals(DatabaseNameResolver.guessDatabaseNameFromURL(url));
    }


    public String getName(String url)
    {
        return url;
    }
}
