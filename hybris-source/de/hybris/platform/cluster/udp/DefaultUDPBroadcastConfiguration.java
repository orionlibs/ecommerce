package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.DefaultBroadcastServiceConfiguration;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;

public class DefaultUDPBroadcastConfiguration implements UDPBroadcastMethodConfiguration
{
    public static final String DEFAULT_MULTICASTADDRESS = "230.0.0.1";
    public static final int DEFAULT_PACKETSIZE = 1000;
    public static final int DEFAULT_PORT = 7777;
    public static final String CFG_MULTICASTADDRESS = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "udp.multicastaddress";
    public static final String CFG_MULTICASTADDRESS_OLD = "cluster.multicastaddress";
    public static final String CFG_PORT = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "udp.port";
    public static final String CFG_PORT_OLD = "cluster.port";
    public static final String CFG_INTERFACE = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "udp.interface";
    public static final String CFG_INTERFACE_OLD = "cluster.interface";
    public static final String CFG_NETWORKINTERFACE = DefaultBroadcastServiceConfiguration.CFG_METHOD_PREFIX + "udp.networkinterface";
    public static final String CFG_NETWORKINTERFACE_OLD = "cluster.networkinterface";
    private String multicastAddress;
    private int port;
    private String interfaceName;
    private String networkInterfaceName;
    private int packetSize;


    public DefaultUDPBroadcastConfiguration()
    {
        ConfigIntf cfg = Registry.getMasterTenant().getConfig();
        this.multicastAddress = cfg.getString("cluster.multicastaddress", null);
        if(this.multicastAddress == null)
        {
            this.multicastAddress = cfg.getString(CFG_MULTICASTADDRESS, "230.0.0.1");
        }
        this.port = cfg.getInt("cluster.port", -1);
        if(this.port == -1)
        {
            this.port = cfg.getInt(CFG_PORT, 7777);
        }
        this.interfaceName = cfg.getString("cluster.interface", null);
        if(this.interfaceName == null)
        {
            this.interfaceName = cfg.getParameter(CFG_INTERFACE);
        }
        this.networkInterfaceName = cfg.getString("cluster.networkinterface", null);
        if(this.networkInterfaceName == null)
        {
            this.networkInterfaceName = cfg.getParameter(CFG_NETWORKINTERFACE);
        }
        this.packetSize = 1000;
    }


    public String getInterfaceName()
    {
        return this.interfaceName;
    }


    public void setInterfaceName(String name)
    {
        this.interfaceName = name;
    }


    public String getMulticastAddress()
    {
        return this.multicastAddress;
    }


    public void setMulticastAddress(String address)
    {
        this.multicastAddress = address;
    }


    public String getNetworkInterfaceName()
    {
        return this.networkInterfaceName;
    }


    public void setNetworkInterfaceName(String networkInterfaceName)
    {
        this.networkInterfaceName = networkInterfaceName;
    }


    public int getPort()
    {
        return this.port;
    }


    public void setPort(int port)
    {
        this.port = port;
    }


    public int getPacketSize()
    {
        return this.packetSize;
    }


    public void setPacketSize(int size)
    {
        this.packetSize = size;
    }
}
