package de.hybris.datasupplier.collectors.impl;

import de.hybris.datasupplier.collectors.DatabaseCollector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractDatabaseCollector implements DatabaseCollector
{
    public URI getHost(String url) throws URISyntaxException
    {
        return new URI(url.replaceFirst("jdbc:", ""));
    }


    protected String matchAndReturn(String regex, String url)
    {
        Pattern sqlServerPattern = Pattern.compile(regex, 2);
        Matcher sqlServerMatcher = sqlServerPattern.matcher(url);
        if(sqlServerMatcher.find())
        {
            return sqlServerMatcher.group();
        }
        return null;
    }
}
