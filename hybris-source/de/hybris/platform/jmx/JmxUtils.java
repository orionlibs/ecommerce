package de.hybris.platform.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class JmxUtils
{
    public static final String DEFAULT_JMX_DOMAIN = "hybris";
    public static final String TENANT_SUBFIX = " Tenant";
    public static final String MASTER_TENANT = "Master Tenant";
    public static JMXConnectorServer theServer;


    public static String normalizeTenantID(String id)
    {
        return id.endsWith("master") ? "Master Tenant" : (id + " Tenant");
    }


    public static void startJMXConnector(String urlAsString)
    {
        stopJMXConnector();
        int lastindex = urlAsString.indexOf("/jmxrmi");
        String first = urlAsString.substring(0, lastindex);
        int firstindex = first.lastIndexOf(':') + 1;
        String portStr = urlAsString.substring(firstindex, lastindex);
        int port = Integer.parseInt(portStr);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        Map<String, Object> env = new HashMap<>();
        env.put("com.sun.management.jmxremote.authenticate", "false");
        env.put("com.sun.management.jmxremote.ssl", "false");
        try
        {
            try
            {
                LocateRegistry.createRegistry(port);
            }
            catch(RemoteException e)
            {
                System.out.println("RMI Registry on port " + port + " could not be exported, perhaps it already exists..?");
            }
            JMXServiceURL url = new JMXServiceURL(urlAsString);
            theServer = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mbs);
            theServer.start();
        }
        catch(MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void stopJMXConnector()
    {
        try
        {
            if(theServer != null)
            {
                theServer.stop();
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
