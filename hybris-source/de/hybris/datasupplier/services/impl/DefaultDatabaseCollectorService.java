package de.hybris.datasupplier.services.impl;

import de.hybris.datasupplier.collectors.DatabaseCollector;
import de.hybris.datasupplier.services.DatabaseCollectorService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultDatabaseCollectorService implements DatabaseCollectorService
{
    private static final Logger LOG = Logger.getLogger(DefaultDatabaseCollectorService.class);
    private static final String HOST_PROPERTY = "datasupplier.database.host";
    private static final String FQDN_PROPERTY = "datasupplier.database.fqdn";
    private static final String DB_NAME_PROPERTY = "datasupplier.database.name";
    private static final String LOCALHOST = "localhost";
    private static final String LOCALHOST_IP = Config.getParameter("datasupplier.localhost.ip");
    private Properties databaseTypes;
    private Set<DatabaseCollector> collectors;


    public String getDatabaseHost()
    {
        String urlString = Config.getDatabaseURL();
        String hostPropertyValue = Config.getParameter("datasupplier.database.host");
        if(StringUtils.isEmpty(hostPropertyValue))
        {
            return extractHostName(urlString);
        }
        return hostPropertyValue;
    }


    public String getDatabaseIPAddress()
    {
        try
        {
            InetAddress address = InetAddress.getByName(getDatabaseHost());
            return address.getHostAddress();
        }
        catch(UnknownHostException e)
        {
            LOG.error(e);
            String ip = extractHostName(Config.getDatabaseURL());
            if(isIPNumber(ip))
            {
                return ip;
            }
            return null;
        }
    }


    public String getDatabaseName()
    {
        String dbNameValue = Config.getParameter("datasupplier.database.name");
        if(StringUtils.isEmpty(dbNameValue))
        {
            if(Config.getDatabaseURL().indexOf('&') < 0)
            {
                return extractDatabaseName(Config.getDatabaseURL());
            }
            return extractDatabaseName(Config.getDatabaseURL().substring(0, Config.getDatabaseURL().indexOf('&')));
        }
        return Config.getParameter("datasupplier.database.name");
    }


    public String getDatabaseFQDName()
    {
        String fqdnValue = Config.getParameter("datasupplier.database.fqdn");
        if(StringUtils.isEmpty(fqdnValue))
        {
            try
            {
                InetAddress address = InetAddress.getByName(getDatabaseHost());
                return address.getCanonicalHostName().toLowerCase();
            }
            catch(UnknownHostException e)
            {
                LOG.error(e);
                return "";
            }
        }
        return Config.getParameter("datasupplier.database.fqdn");
    }


    public String getDatabaseType()
    {
        String databaseType = this.databaseTypes.getProperty(DatabaseNameResolver.guessDatabaseNameFromURL(Config.getDatabaseURL()));
        if(StringUtils.isEmpty(databaseType))
        {
            return this.databaseTypes.getProperty("unspecified");
        }
        return databaseType;
    }


    public String extractDatabaseName(String url)
    {
        for(DatabaseCollector strategy : this.collectors)
        {
            if(strategy.isApplicable(url))
            {
                return strategy.getName(url);
            }
        }
        LOG.error("Unsupported database type by sldhybris.");
        return null;
    }


    public String extractHostName(String url)
    {
        try
        {
            for(DatabaseCollector strategy : this.collectors)
            {
                if(strategy.isApplicable(url))
                {
                    URI uri = strategy.getHost(url);
                    InetAddress address = InetAddress.getByName(uri.getHost());
                    String urlRet = address.getHostName();
                    if("localhost".equals(urlRet) || LOCALHOST_IP.equals(urlRet))
                    {
                        return InetAddress.getLocalHost().getHostName().toLowerCase();
                    }
                    return urlRet;
                }
            }
        }
        catch(URISyntaxException | UnknownHostException e)
        {
            LOG.error(e, e);
        }
        return null;
    }


    protected boolean isIPNumber(String url)
    {
        Pattern ipPattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
        Matcher ipMatcher = ipPattern.matcher(url);
        return ipMatcher.find();
    }


    public void setDatabaseTypes(Properties databaseTypes)
    {
        this.databaseTypes = databaseTypes;
    }


    public void setCollectors(Set<DatabaseCollector> collectors)
    {
        this.collectors = collectors;
    }
}
