package de.hybris.platform.cluster.jgroups;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class JGroupsPropertyConfigurer
{
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsPropertyConfigurer.class);
    private static final String SYSTEM_PROP_PREFIX = "hybris.jgroups.";
    public static final String BIND_ADDR_SYS_PROPERTY = "hybris.jgroups.bind_addr";
    public static final String BIND_PORT_SYS_PROPERTY = "hybris.jgroups.bind_port";
    public static final String MCAST_ADDRESS_SYS_PROPERTY = "hybris.jgroups.mcast_address";
    public static final String MCAST_PORT_SYS_PROPERTY = "hybris.jgroups.mcast_port";
    public static final String THREAD_POOL_MAX_THREAD_SYS_PROPERTY = "hybris.jgroups.thread_pool.max_threads";
    public static final String HYBRIS_TCP_IP_PROPERTY = "cluster.broadcast.method.jgroups.tcp.bind_addr";
    public static final String HYBRIS_TCP_PORT_PROPERTY = "cluster.broadcast.method.jgroups.tcp.bind_port";
    public static final String HYBRIS_MULTICAST_ADDR_PROPERTY = "cluster.broadcast.method.jgroups.udp.mcast_address";
    public static final String HYBRIS_MULTICAST_PORT_PROPERTY = "cluster.broadcast.method.jgroups.udp.mcast_port";
    public static final String HYBRIS_TCP_MAX_THREAD_PROPERTY = "cluster.broadcast.method.jgroups.tcp.thread_pool.max_threads";
    public static final String HYBRIS_UDP_MAX_THREAD_PROPERTY = "cluster.broadcast.method.jgroups.udp.thread_pool.max_threads";
    private static final String DEFAULT_TCP_IP = "127.0.0.1";
    private static final String DEFAULT_TCP_PORT = "7800";
    private static final String DEFAULT_MULTICAST_ADDR = "224.0.0.1";
    private static final String DEFAULT_MULTICAST_PORT = "45588";
    private static final String DEFAULT_MAX_THREAD = "100";


    public void configure(String configuration)
    {
        setStandardProperties();
        setConfigurationSpecificProperties(configuration);
    }


    private void setStandardProperties()
    {
        String bindAddr = getConfigValue("cluster.broadcast.method.jgroups.tcp.bind_addr", "127.0.0.1");
        String bindPort = getConfigValue("cluster.broadcast.method.jgroups.tcp.bind_port", "7800");
        String multicastAddr = getConfigValue("cluster.broadcast.method.jgroups.udp.mcast_address", "224.0.0.1");
        String multicastPort = getConfigValue("cluster.broadcast.method.jgroups.udp.mcast_port", "45588");
        String maxThread = getConfigValue("cluster.broadcast.method.jgroups.tcp.thread_pool.max_threads", "100");
        setProperty("hybris.jgroups.bind_addr", bindAddr);
        setProperty("hybris.jgroups.bind_port", bindPort);
        setProperty("hybris.jgroups.mcast_address", multicastAddr);
        setProperty("hybris.jgroups.mcast_port", multicastPort);
        setProperty("hybris.jgroups.thread_pool.max_threads", maxThread);
    }


    private void setConfigurationSpecificProperties(String configuration)
    {
        String configName = extractConfigName(configuration);
        Map<String, String> parametersMatching = getParametersMatchingSpecificConfig(configName);
        for(Map.Entry<String, String> entry : parametersMatching.entrySet())
        {
            String value = entry.getValue();
            String key = "hybris.jgroups." + (String)entry.getKey();
            if(StringUtils.isNotBlank(value))
            {
                setProperty(key, value);
            }
        }
    }


    private Map<String, String> getParametersMatchingSpecificConfig(String configName)
    {
        return Registry.getCurrentTenant().getConfig().getParametersMatching("cluster\\.conf\\." + configName + "\\.(.*)", true);
    }


    private String extractConfigName(String configuration)
    {
        return StringUtils.removeEnd(StringUtils.removeStart(configuration, "jgroups/"), ".xml");
    }


    protected void setProperty(String key, String value)
    {
        System.setProperty(key, value);
        LOG.debug("--- setting up {} to: {}", key, value);
    }


    private String getConfigValue(String key, String defaultValue)
    {
        ConfigIntf config = Registry.getCurrentTenant().getConfig();
        return config.getString(key, (defaultValue == null) ? "" : defaultValue);
    }
}
