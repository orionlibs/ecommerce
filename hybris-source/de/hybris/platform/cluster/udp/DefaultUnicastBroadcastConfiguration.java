package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.DefaultBroadcastServiceConfiguration;
import de.hybris.platform.util.config.ConfigIntf;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultUnicastBroadcastConfiguration implements UnicastBroadcastMethodConfiguration
{
    private static final Logger LOG = Logger.getLogger(DefaultUnicastBroadcastConfiguration.class.getName());
    public static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    public static final int DEFAULT_PACKETSIZE = 1000;
    public static final int DEFAULT_PORT = 7777;
    public static final String CFG_SERVERADDRESS = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "unicast.serveraddress";
    public static final String CFG_PORT = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "unicast.port";
    public static final String CFG_CLUSTERNODES = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "unicast.clusternodes";
    public static final String CFG_SYNC_INTERVAL = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "unicast.sync.nodes.interval";
    private InetSocketAddress serverAddress;
    private List<InetSocketAddress> clusterNodes;
    private int packetSize;
    private int syncNodesIntervalSeconds = -1;


    public DefaultUnicastBroadcastConfiguration()
    {
        this.packetSize = 1000;
        this.syncNodesIntervalSeconds = -1;
        this.serverAddress = InetSocketAddress.createUnresolved("127.0.0.1", 7777);
        this.clusterNodes = Collections.EMPTY_LIST;
    }


    public DefaultUnicastBroadcastConfiguration(ConfigIntf cfg)
    {
        int port = cfg.getInt(CFG_PORT, 7777);
        this.serverAddress = InetSocketAddress.createUnresolved(cfg.getString(CFG_SERVERADDRESS, "127.0.0.1"), port);
        this.packetSize = 1000;
        this.syncNodesIntervalSeconds = cfg.getInt(CFG_SYNC_INTERVAL, -1);
        this.clusterNodes = parseClusterNodes(cfg.getParameter(CFG_CLUSTERNODES), port);
    }


    public static String getClusterNodesAsString(Collection<InetSocketAddress> toSend)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int index = 0;
        for(InetSocketAddress a : toSend)
        {
            if(index > 0)
            {
                stringBuilder.append(';');
            }
            index++;
            stringBuilder.append(a);
        }
        return stringBuilder.toString();
    }


    public static List<InetSocketAddress> parseClusterNodes(String config, int defaultPort)
    {
        List<InetSocketAddress> ret = new ArrayList<>();
        if(StringUtils.isNotBlank(config))
        {
            for(String s : StringUtils.split(config, ",;|"))
            {
                InetSocketAddress addr = parseClusterNode(s, defaultPort);
                if(addr != null)
                {
                    ret.add(addr);
                }
            }
        }
        return ret;
    }


    private static InetSocketAddress parseClusterNode(String address, int defaultPort)
    {
        if(StringUtils.isNotBlank(address))
        {
            String host;
            int port, portPos = address.indexOf(':');
            if(portPos >= 0)
            {
                host = address.substring(0, portPos).trim();
                port = parsePort(address, address.substring(portPos + 1), defaultPort);
            }
            else
            {
                host = address.trim();
                port = defaultPort;
            }
            int slashPos = host.indexOf('/');
            if(slashPos >= 0)
            {
                host = host.substring(0, slashPos);
                if(StringUtils.isBlank(host))
                {
                    host = host.substring(slashPos + 1);
                }
            }
            return InetSocketAddress.createUnresolved(host, port);
        }
        return null;
    }


    private static int parsePort(String addressInfo, String portStr, int defaultPort)
    {
        try
        {
            return Integer.parseInt(portStr.trim());
        }
        catch(NumberFormatException e)
        {
            LOG.warn("cannot parse port number in cluster node address " + addressInfo + " due to " + e.getMessage() + " - using default port " + defaultPort);
            if(defaultPort >= 0)
            {
                return defaultPort;
            }
            throw new IllegalArgumentException("illegal address " + addressInfo + " - no port specified and no default port defined!");
        }
    }


    public InetSocketAddress getServerAddress()
    {
        return this.serverAddress;
    }


    public void setServerAddress(InetSocketAddress address)
    {
        this.serverAddress = address;
    }


    public int getPacketSize()
    {
        return this.packetSize;
    }


    public void setPacketSize(int size)
    {
        this.packetSize = size;
    }


    public List<InetSocketAddress> getClusterNodes()
    {
        return this.clusterNodes;
    }


    public void setClusterNodes(List<InetSocketAddress> nodes)
    {
        this.clusterNodes = nodes;
    }


    public void setSyncNodesIntervalSeconds(int seconds)
    {
        this.syncNodesIntervalSeconds = seconds;
    }


    public int getSyncNodesIntervalSeconds()
    {
        return this.syncNodesIntervalSeconds;
    }
}
