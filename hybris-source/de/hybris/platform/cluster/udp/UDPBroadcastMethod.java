package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.AbstractBroadcastMethod;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.collections.YFastFIFOMap;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class UDPBroadcastMethod extends AbstractBroadcastMethod
{
    private static final Logger LOG = Logger.getLogger(UDPBroadcastMethod.class.getName());
    private MulticastSocket _multicastSocket;
    private String multicastaddress;
    private InetAddress multicastInetAddress;
    private String interfaceName;
    private InetAddress interfaceAddr;
    private String networkInterfaceName;
    private NetworkInterface networkInterface;
    private int port = -1;
    private int packetSize = -1;
    private final int cachesize = 1000;
    private YFastFIFOMap<String, List<RawMessage>> partMessageCache = null;
    private BroadcastService broadcastService = null;
    private long lastErrorWarnTime = 0L;
    private volatile boolean isStopped = false;
    private UDPServerThread serverThread;
    private UDPBroadcastMethodConfiguration cfg;


    public UDPBroadcastMethod()
    {
        setConfiguration((UDPBroadcastMethodConfiguration)new DefaultUDPBroadcastConfiguration());
    }


    public UDPBroadcastMethodConfiguration getConfiguration()
    {
        return this.cfg;
    }


    public void setConfiguration(UDPBroadcastMethodConfiguration cfg)
    {
        if(this.port != -1)
        {
            throw new IllegalStateException("cannot change configuration after method has been initialized");
        }
        this.cfg = cfg;
    }


    public InetAddress getMulticastAddress()
    {
        return (this._multicastSocket != null && this._multicastSocket.getInetAddress() != null) ? this._multicastSocket.getInetAddress() :
                        this.multicastInetAddress;
    }


    public int getMulticastPort()
    {
        return (this._multicastSocket != null && this._multicastSocket.getPort() > -1) ? this._multicastSocket.getPort() : this.port;
    }


    public InetAddress getInterfaceAddress()
    {
        try
        {
            return (this._multicastSocket != null) ? this._multicastSocket.getInterface() : this.interfaceAddr;
        }
        catch(SocketException e)
        {
            LOG.warn(e.getMessage(), e);
            return this.interfaceAddr;
        }
    }


    public NetworkInterface getNetworkInterfaceName()
    {
        try
        {
            return (this._multicastSocket != null) ? this._multicastSocket.getNetworkInterface() : this.networkInterface;
        }
        catch(SocketException e)
        {
            LOG.warn(e.getMessage(), e);
            return this.networkInterface;
        }
    }


    public Map<String, String> getSettings()
    {
        Map<String, String> ret = new LinkedHashMap<>(super.getSettings());
        ret.put(DefaultUDPBroadcastConfiguration.CFG_MULTICASTADDRESS, (getMulticastAddress() != null) ? getMulticastAddress().toString() : "n/a");
        ret.put(DefaultUDPBroadcastConfiguration.CFG_PORT, Integer.toString(getMulticastPort()));
        ret.put(DefaultUDPBroadcastConfiguration.CFG_INTERFACE, (getInterfaceAddress() != null) ? getInterfaceAddress().toString() : "n/a");
        ret.put(DefaultUDPBroadcastConfiguration.CFG_NETWORKINTERFACE, (getNetworkInterfaceName() != null) ? getNetworkInterfaceName().toString() : "n/a");
        return ret;
    }


    public void init(BroadcastService service)
    {
        this.broadcastService = service;
        readConfig();
        start();
    }


    public synchronized void send(RawMessage message)
    {
        if(message.mustSplit(this.packetSize))
        {
            for(RawMessage partmessage : message.split(this.packetSize))
            {
                byte[] toSend = partmessage.toRawByteArray();
                DatagramPacket datagramPacket = new DatagramPacket(toSend, toSend.length, this.multicastInetAddress, this.port);
                sendPacket(datagramPacket);
            }
        }
        else
        {
            byte[] toSend = message.toRawByteArray();
            DatagramPacket datagramPacket = new DatagramPacket(toSend, toSend.length, this.multicastInetAddress, this.port);
            sendPacket(datagramPacket);
        }
    }


    public void shutdown()
    {
        this.isStopped = true;
        if(this._multicastSocket != null && !this._multicastSocket.isClosed())
        {
            try
            {
                this._multicastSocket.close();
            }
            catch(Exception e)
            {
                LOG.warn("error closing multicast socket (error:" + e.getMessage() + ")", e);
            }
            finally
            {
                this._multicastSocket = null;
            }
        }
        this.interfaceAddr = null;
        this.multicastInetAddress = null;
        this.networkInterface = null;
    }


    protected void readConfig()
    {
        if(this.cfg == null)
        {
            throw new IllegalStateException("no configuration set");
        }
        UDPBroadcastMethodConfiguration cfg = getConfiguration();
        this.multicastaddress = cfg.getMulticastAddress();
        this.port = cfg.getPort();
        this.interfaceName = cfg.getInterfaceName();
        this.networkInterfaceName = cfg.getNetworkInterfaceName();
        this.packetSize = cfg.getPacketSize();
    }


    protected void start()
    {
        this._multicastSocket = startSocket();
        if(this._multicastSocket != null)
        {
            startServer();
        }
    }


    protected void startServer()
    {
        this.serverThread = new UDPServerThread(this);
        this.serverThread.start();
    }


    protected MulticastSocket startSocket()
    {
        MulticastSocket socket = null;
        try
        {
            this.multicastInetAddress = InetAddress.getByName(this.multicastaddress);
            socket = new MulticastSocket(this.port);
            if(!StringUtils.isEmpty(this.interfaceName))
            {
                try
                {
                    this.interfaceAddr = InetAddress.getByName(this.interfaceName);
                    socket.setInterface(this.interfaceAddr);
                }
                catch(UnknownHostException e)
                {
                    LOG.error("invalid upd interface address '" + this.interfaceName + "' (error:" + e.getMessage() + ")", e);
                }
                catch(SocketException e)
                {
                    LOG.error("cannot set upd interface address '" + this.interfaceName + "' (error:" + e.getMessage() + ")", e);
                }
            }
            if(!StringUtils.isEmpty(this.networkInterfaceName))
            {
                try
                {
                    this.networkInterface = NetworkInterface.getByName(this.networkInterfaceName);
                    socket.setNetworkInterface(this.networkInterface);
                }
                catch(SocketException e)
                {
                    LOG.error("invalid upd network interface '" + this.networkInterfaceName + "' (error:" + e.getMessage() + ")", e);
                }
            }
            socket.joinGroup(this.multicastInetAddress);
            if(LOG.isDebugEnabled())
            {
                synchronized(UDPBroadcastMethod.class)
                {
                    String displayName = socket.getNetworkInterface().getDisplayName();
                    if(displayName == null)
                    {
                        displayName = "<default>";
                    }
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("-------------------------------------------------------------------------");
                        LOG.debug("hybris UDP Multicast Processor configured with the following parameters: ");
                        LOG.debug("Interface: " + socket.getInterface() + ", NetworkInterface: " + displayName);
                        LOG.debug("Multicast address: " + this.multicastaddress + ":" + this.port);
                        LOG.debug("Cluster Island: " + Registry.getMasterTenant().getClusterIslandPK());
                        LOG.debug("Node ID: " + MasterTenant.getInstance().getClusterID() + "(dynamic:" +
                                        MasterTenant.getInstance().getDynamicClusterNodeID() + ")");
                        LOG.debug("-------------------------------------------------------------------------");
                    }
                }
            }
        }
        catch(IOException e)
        {
            LOG.error("error starting UPD broadcast method (error:" + e.getMessage() + ")", e);
        }
        return socket;
    }


    protected synchronized void sendPacket(DatagramPacket packet)
    {
        if(this._multicastSocket != null)
        {
            try
            {
                this._multicastSocket.send(packet);
            }
            catch(IOException e)
            {
                long timeStamp = System.currentTimeMillis();
                if(timeStamp - this.lastErrorWarnTime > 60000L)
                {
                    LOG.warn("there are UDP sender errors - please switch on DEBUG for " + LOG.getName(), e);
                    this.lastErrorWarnTime = timeStamp;
                }
            }
        }
        else
        {
            LOG.warn("udp broadcast not yet read - skipped packet");
        }
    }


    protected void processDatagramPacket(DatagramPacket packet)
    {
        RawMessage msg = new RawMessage(packet.getData(), packet.getOffset(), packet.getLength());
        DefaultBroadcastService dbs = (DefaultBroadcastService)this.broadcastService;
        if(dbs.accept(msg))
        {
            if(msg.getNumberOfPackets() > 1)
            {
                String key = "" + msg.getDynamicNodeID() + "-" + msg.getDynamicNodeID();
                if(this.partMessageCache == null)
                {
                    Objects.requireNonNull(this);
                    this.partMessageCache = new YFastFIFOMap(1000);
                }
                List<RawMessage> cachedList = (List<RawMessage>)this.partMessageCache.get(key);
                if(cachedList == null)
                {
                    List<RawMessage> messageList = new LinkedList<>();
                    messageList.add(msg);
                    this.partMessageCache.put(key, messageList);
                }
                else if(cachedList.size() == msg.getNumberOfPackets() - 1)
                {
                    this.partMessageCache.remove(key);
                    RawMessage joinedMessage = msg.join(cachedList);
                    joinedMessage.setRemoteAddress(packet.getAddress());
                    notifyMessgageReceived(joinedMessage);
                }
                else
                {
                    cachedList.add(msg);
                }
            }
            else
            {
                msg.setRemoteAddress(packet.getAddress());
                notifyMessgageReceived(msg);
            }
        }
    }
}
