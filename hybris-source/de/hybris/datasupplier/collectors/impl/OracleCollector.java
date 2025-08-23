package de.hybris.datasupplier.collectors.impl;

import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class OracleCollector extends AbstractDatabaseCollector
{
    public boolean isApplicable(String url)
    {
        return "oracle".equals(DatabaseNameResolver.guessDatabaseNameFromURL(url));
    }


    public URI getHost(String url) throws URISyntaxException
    {
        String urlString = matchAndReturn("host\\=[^)]*", url);
        if(urlString != null)
        {
            urlString = urlString.replaceFirst("Host=", "");
        }
        else
        {
            urlString = matchAndReturn("@[^:]*", url);
        }
        if(urlString != null)
        {
            urlString = "oracle://" + urlString.replaceFirst("@", "").replaceFirst("//", "");
        }
        return new URI(urlString);
    }


    public String getName(String url)
    {
        String serviceName = matchAndReturn("service_name\\=[^)]*", url);
        if(serviceName != null)
        {
            return Pattern.compile("service_name\\=", 2).matcher(serviceName).replaceFirst("");
        }
        String sid = matchAndReturn("sid\\=[^)]*", url);
        if(sid != null)
        {
            return Pattern.compile("sid\\=", 2).matcher(sid).replaceFirst("");
        }
        serviceName = matchAndReturn("[^/]/[^/]+", url);
        if(serviceName != null)
        {
            return serviceName.split("/")[1];
        }
        sid = matchAndReturn("@.*:.*:\\S+:?", url);
        if(sid != null)
        {
            return sid.split(":")[2];
        }
        return null;
    }
}
