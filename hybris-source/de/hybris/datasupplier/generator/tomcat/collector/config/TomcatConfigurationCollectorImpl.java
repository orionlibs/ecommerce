package de.hybris.datasupplier.generator.tomcat.collector.config;

import com.sap.sup.admin.sldsupplier.config.SLDSupplierConfiguration;
import com.sap.sup.admin.sldsupplier.data.java.JavaRuntime;
import com.sap.sup.admin.sldsupplier.error.InfrastructureException;
import de.hybris.datasupplier.generator.tomcat.collector.BaseTomcatCollectorImpl;
import de.hybris.datasupplier.generator.tomcat.data.Tomcat;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfigObjectUtils;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConfiguration;
import de.hybris.datasupplier.generator.tomcat.data.TomcatConnector;
import de.hybris.datasupplier.generator.tomcat.data.TomcatEngine;
import de.hybris.datasupplier.generator.tomcat.data.TomcatHost;
import de.hybris.datasupplier.generator.tomcat.data.TomcatServer;
import de.hybris.datasupplier.generator.tomcat.data.TomcatService;
import de.hybris.datasupplier.services.HybrisCollectorService;
import de.hybris.platform.core.Registry;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

public class TomcatConfigurationCollectorImpl extends BaseTomcatCollectorImpl
{
    private static final String MBEAN_SERVER_QUERY = "*:type=Server";
    private static final String MBEAN_HOST_QUERY_PRF = "*:type=Host,host=";
    private static final String MBEAN_ENGINE_QUERY = "*:type=Engine";
    private static final String STRING_FOUND = "Found [";
    private static final Logger LOG = Logger.getLogger(TomcatConfigurationCollectorImpl.class);
    private TomcatConfiguration tomcatConfiguration = new TomcatConfiguration();
    private String localHostName;
    private String ipAddress;
    private HybrisCollectorService hybrisCollectorService;


    public TomcatConfiguration getTomcatConfiguration()
    {
        return this.tomcatConfiguration;
    }


    public void setTomcatConfiguration(TomcatConfiguration tomcatConfiguration)
    {
        this.tomcatConfiguration = tomcatConfiguration;
    }


    public String getLocalHostName()
    {
        return this.localHostName;
    }


    public void setLocalHostName(String localHostName)
    {
        this.localHostName = localHostName;
    }


    public String getIpAddress()
    {
        return this.ipAddress;
    }


    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }


    public String toString()
    {
        return "TomcatConfigurationCollectorImpl";
    }


    public void collect()
    {
        try
        {
            startCollect();
            debugLogging("END collecting tomcat configuration");
        }
        catch(MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | ReflectionException | MBeanException | UnknownHostException e)
        {
            throw new InfrastructureException(e.getMessage(), e);
        }
    }


    protected void startCollect() throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException, UnknownHostException
    {
        debugLogging("START collecting tomcat configuration");
        if(this.localHostName == null)
        {
            this.localHostName = findLocalMachineName();
            setLocalHostName(this.localHostName);
        }
        if(this.ipAddress == null)
        {
            setIpAddress(findIpAddress());
        }
        MBeanServer mbeanServer = getServiceInitializer().getMBeanServer();
        Tomcat tomcat = this.tomcatConfiguration.getTomcat();
        ObjectName objnameForServers = new ObjectName("*:type=Server");
        Set<ObjectName> serverNames = mbeanServer.queryNames(objnameForServers, null);
        debugLogging("Found [" + serverNames.size() + "] Tomcat servers");
        Iterator<ObjectName> serverIter = serverNames.iterator();
        while(serverIter.hasNext())
        {
            ObjectName serverName = serverIter.next();
            TomcatServer tomcatServer = buildTomcatServer(mbeanServer, serverName);
            tomcat.addServer(tomcatServer);
        }
        tomcat.sortServers();
    }


    protected TomcatServer buildTomcatServer(MBeanServer mbeanServer, ObjectName serverName) throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        debugLogging("Building Tomcat server [" + serverName + "]");
        TomcatServer server = new TomcatServer();
        server.setObjectName(serverName);
        server.setHostName(this.localHostName);
        server.setIpAddress(this.ipAddress);
        SLDSupplierConfiguration configuration = getCollectorContext().getSLDSupplierConfiguration();
        String systemId = configuration.getSystemId();
        if(systemId != null)
        {
            debugLogging("final Assign System ID [" + systemId + "] final to the Apache final Tomcat system");
            server.setSystemId(systemId);
        }
        else
        {
            server.setSystemId(this.localHostName + "." + this.localHostName);
        }
        Object objServerInfo = mbeanServer.getAttribute(serverName, "serverInfo");
        if(objServerInfo != null)
        {
            String serverInfo = objServerInfo.toString();
            server.setServerInfo(objServerInfo.toString());
            String version = TomcatConfigObjectUtils.getTomcatVersion(serverInfo);
            server.setVersion(version);
        }
        else
        {
            debugLogging("Warning: Unexpected Tomcat configuration. Did not find server info for  [" + serverName + "]");
        }
        Object attrPort = mbeanServer.getAttribute(serverName, "port");
        if(attrPort != null)
        {
            server.setPort(attrPort.toString());
        }
        else
        {
            debugLogging("Warning: Unexpected Tomcat configuration. Did not find SHUTDOWN port for server [" + serverName + "]");
        }
        ObjectName[] serviceNames = (ObjectName[])mbeanServer.getAttribute(serverName, "serviceNames");
        if(serviceNames == null || serviceNames.length == 0)
        {
            debugLogging("Warning: Unexpected Tomcat configuration. Did not find any services for server [" + serverName + "]");
        }
        else
        {
            debugLogging("Found [" + serviceNames.length + "] service names for Tomcat server [" + serverName + "]");
            for(int i = 0; i < serviceNames.length; i++)
            {
                ObjectName serviceName = serviceNames[i];
                buildTomcatService(mbeanServer, server, serviceName);
            }
            server.sortServices();
        }
        server.setHybrisClusterID(getHybrisCollectorService().getClusterId());
        server.setHybrisClusterMode(getHybrisCollectorService().isClusterModeEnabled());
        server.setHybrisExtensionSet(getHybrisCollectorService().getExtensionsString());
        server.setHybrisPlatformHome(getHybrisCollectorService().getPlatformHomeDirectory());
        server.setHybrisBinPath(getHybrisCollectorService().getBinDirectory());
        server.setHybrisConfigPath(getHybrisCollectorService().getConfigDirectory());
        server.setHybrisDataPath(getHybrisCollectorService().getDataDirectory());
        server.setHybrisTempPath(getHybrisCollectorService().getTempDirectory());
        server.setHybrisLogPath(getHybrisCollectorService().getLogDirectory());
        server.setHybrisInstallPath(getHybrisCollectorService().getInstallDirectory());
        server.setHybrisServerLogPath(getHybrisCollectorService().getServerLogDirectory());
        buildJavaRuntime(mbeanServer, server, serverName);
        return server;
    }


    protected JavaRuntime buildJavaRuntime(MBeanServer mbeanServer, TomcatServer server, ObjectName serverName)
    {
        JavaRuntime javaRuntime = new JavaRuntime();
        server.setJavaRuntime(javaRuntime);
        Properties systemProps = System.getProperties();
        Set<Map.Entry<Object, Object>> propSet = systemProps.entrySet();
        Iterator<Map.Entry<Object, Object>> iter = propSet.iterator();
        while(iter.hasNext())
        {
            Map.Entry entry = iter.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            javaRuntime.setSystemProperty(key.toString(), StringEscapeUtils.escapeXml(value.toString()));
        }
        List<String> inputArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
        int vmArgCount = 0;
        if(inputArgs != null)
        {
            vmArgCount = javaRuntime.addVMArgument(inputArgs);
        }
        debugLogging("JavaRuntime for server  [" + serverName + "] found. Finished [" + systemProps.size() + "] system properties and [" + vmArgCount + "] VM arguments");
        return javaRuntime;
    }


    protected TomcatService buildTomcatService(MBeanServer mbeanServer, TomcatServer server, ObjectName serviceName) throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        debugLogging("Building Tomcat Service [" + serviceName + "]");
        TomcatService tomcatService = new TomcatService();
        tomcatService.setObjectName(serviceName);
        server.addService(tomcatService);
        Object attrServiceName = mbeanServer.getAttribute(serviceName, "name");
        if(attrServiceName != null)
        {
            tomcatService.setName(attrServiceName.toString());
        }
        ObjectName queryForEngine = new ObjectName("*:type=Engine");
        Set<ObjectName> engines = mbeanServer.queryNames(queryForEngine, null);
        debugLogging("Found [" + engines.size() + "] Tomcat engines");
        int engineCount = engines.size();
        if(engineCount == 0)
        {
            debugLogging("Warning:  Unexpected Tomcat configuration. found No Tomcat engines!");
        }
        else if(engineCount > 1)
        {
            debugLogging("Warning:  Unexpected Tomcat configuration. found [" + engines
                            .size() + "] >1 Tomcat engines!");
        }
        Iterator<ObjectName> engineIter = engines.iterator();
        if(engineIter.hasNext())
        {
            ObjectName engineName = engineIter.next();
            debugLogging("Found [" + engineName + "] Tomcat engine");
            buildTomcatEngine(mbeanServer, tomcatService, engineName);
        }
        ObjectName[] containerNames = (ObjectName[])mbeanServer.getAttribute(serviceName, "connectorNames");
        for(int i = 0; i < containerNames.length; i++)
        {
            ObjectName connectorName = containerNames[i];
            if(connectorName == null)
            {
                debugLogging("Warning:  Unexpected Tomcat configuration. TomcatConnector name is null");
            }
            else
            {
                debugLogging("Found Tomcat connector =  [" + connectorName + "]");
                buildTomcatConnector(mbeanServer, tomcatService, connectorName);
            }
        }
        return tomcatService;
    }


    protected TomcatConnector buildTomcatConnector(MBeanServer mbeanServer, TomcatService tomcatService, ObjectName connectorName) throws ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        TomcatConnector tomcatConnector = new TomcatConnector();
        tomcatConnector.setObjectName(connectorName);
        tomcatService.addConnector(tomcatConnector);
        Object attrPort = mbeanServer.getAttribute(connectorName, "port");
        debugLogging("Warning: Unexpected Tomcat configuration. TomcatConnector has no port!");
        if(attrPort != null)
        {
            tomcatConnector.setPort(attrPort.toString());
        }
        Object attrScheme = mbeanServer.getAttribute(connectorName, "scheme");
        debugLogging("Warning: Unexpected Tomcat configuration. TomcatConnector has no scheme!");
        if(attrScheme != null)
        {
            tomcatConnector.setScheme(attrScheme.toString());
        }
        Object attrProtocol = mbeanServer.getAttribute(connectorName, "protocol");
        debugLogging("Warning: Unexpected Tomcat configuration. TomcatConnector has no protocol!");
        if(attrProtocol != null)
        {
            tomcatConnector.setProtocol(attrProtocol.toString());
        }
        return tomcatConnector;
    }


    protected TomcatEngine buildTomcatEngine(MBeanServer mbeanServer, TomcatService tomcatService, ObjectName engineName) throws MalformedObjectNameException, ReflectionException, AttributeNotFoundException, MBeanException, InstanceNotFoundException
    {
        TomcatEngine tomcatEngine = new TomcatEngine();
        tomcatEngine.setObjectName(engineName);
        tomcatService.setEngine(tomcatEngine);
        Object attrBaseDir = System.getProperty("catalina.base");
        debugLogging("Warning: Unexpected Tomcat configuration. TomcatEngine has no base dir!");
        if(attrBaseDir != null)
        {
            tomcatEngine.setBaseDir(attrBaseDir.toString());
        }
        Object attrEngineName = mbeanServer.getAttribute(engineName, "name");
        debugLogging("Warning: Unexpected Tomcat configuration. TomcatEngine has no name!");
        if(attrEngineName != null)
        {
            tomcatEngine.setName(attrEngineName.toString());
        }
        Object attrDefaultHost = mbeanServer.getAttribute(engineName, "defaultHost");
        if(attrDefaultHost == null)
        {
            debugLogging("Warning:  Unexpected Tomcat configuration. No default tomcat host found!");
            return tomcatEngine;
        }
        String defaultHostName = attrDefaultHost.toString();
        tomcatEngine.setDefaultHost(defaultHostName);
        StringBuilder sb = new StringBuilder("*:type=Host,host=");
        sb.append(defaultHostName);
        ObjectName queryForHost = new ObjectName(sb.toString());
        Set<ObjectName> hosts = mbeanServer.queryNames(queryForHost, null);
        Iterator<ObjectName> hostIter = hosts.iterator();
        if(!hostIter.hasNext())
        {
            debugLogging("Warning: Unexpected Tomcat configuration. No TomcatHost found");
            return tomcatEngine;
        }
        ObjectName hostName = hostIter.next();
        debugLogging("Found [" + hostName + "] Tomcat default host object");
        TomcatHost tomcatHost = new TomcatHost();
        tomcatHost.setObjectName(hostName);
        attrBaseDir = System.getProperty("catalina.base");
        if(attrBaseDir != null)
        {
            tomcatHost.setBaseDir(attrBaseDir.toString());
        }
        if(attrBaseDir == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration. TomcatHost has no base dir!");
        }
        Object attrHostName = mbeanServer.getAttribute(hostName, "name");
        if(attrHostName != null)
        {
            tomcatHost.setName(attrHostName.toString());
        }
        if(attrHostName == null)
        {
            debugLogging("Warning: Unexpected Tomcat configuration. TomcatHost + final has no final host name!");
        }
        tomcatEngine.setDefaultTomcatHost(tomcatHost);
        return tomcatEngine;
    }


    protected String findLocalMachineName() throws UnknownHostException
    {
        InetAddress localMachine = InetAddress.getLocalHost();
        return localMachine.getCanonicalHostName().toLowerCase();
    }


    protected String findIpAddress() throws UnknownHostException
    {
        InetAddress localMachine = InetAddress.getLocalHost();
        return localMachine.getHostAddress().toLowerCase();
    }


    public void setHybrisCollectorService(HybrisCollectorService hybrisCollectorService)
    {
        this.hybrisCollectorService = hybrisCollectorService;
    }


    public HybrisCollectorService getHybrisCollectorService()
    {
        if(this.hybrisCollectorService == null)
        {
            this.hybrisCollectorService = (HybrisCollectorService)Registry.getApplicationContext().getBean("hybrisCollectorService", HybrisCollectorService.class);
        }
        return this.hybrisCollectorService;
    }


    private static void debugLogging(String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message);
        }
    }
}
