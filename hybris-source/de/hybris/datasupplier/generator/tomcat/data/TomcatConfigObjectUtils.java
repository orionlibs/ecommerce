package de.hybris.datasupplier.generator.tomcat.data;

import java.util.Collection;
import java.util.Iterator;

public class TomcatConfigObjectUtils
{
    public static TomcatEngine findEngine(TomcatConfiguration tomcatConfiguration, String engineName)
    {
        if(tomcatConfiguration == null)
        {
            throw new IllegalArgumentException("tomcatConfiguration is null");
        }
        Tomcat tomcat = tomcatConfiguration.getTomcat();
        if(tomcat == null)
        {
            throw new IllegalArgumentException("Tomcat is null");
        }
        Collection servers = tomcat.getServers();
        if(servers == null)
        {
            return null;
        }
        Iterator<TomcatServer> serverIter = servers.iterator();
        while(serverIter.hasNext())
        {
            TomcatServer server = serverIter.next();
            Collection services = server.getServices();
            if(services == null)
            {
                continue;
            }
            Iterator<TomcatService> serviceIter = services.iterator();
            while(serviceIter.hasNext())
            {
                TomcatService service = serviceIter.next();
                TomcatEngine engine = service.getEngine();
                String name = engine.getName();
                if(name == null)
                {
                    throw new IllegalArgumentException("TomcatEngine.name is null");
                }
                if(engineName.equals(name))
                {
                    return engine;
                }
            }
        }
        return null;
    }


    public static String getTomcatVersion(String serverInfo)
    {
        int first = serverInfo.indexOf('/');
        if(first <= 0)
        {
            return null;
        }
        String serverInfoSubString = serverInfo.substring(first + 1);
        first = serverInfoSubString.indexOf('.');
        if(first <= 0)
        {
            return null;
        }
        int second = serverInfoSubString.indexOf(".", first + 1);
        return serverInfoSubString.substring(0, second);
    }


    public static String getEngineNameFromMappedResource70(String mappedResource)
    {
        int first = mappedResource.indexOf('.');
        if(first <= 0)
        {
            return null;
        }
        String managedResource = mappedResource.substring(0, first);
        first = mappedResource.indexOf('[');
        if(first <= 0)
        {
            return null;
        }
        return managedResource.substring(first + 1, managedResource.length() - 1);
    }
}
