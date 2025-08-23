package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.AbstractBroadcastMethod;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.collections.YFastFIFOMap;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public class UnicastBroadcastMethod extends AbstractBroadcastMethod
{
    private static final Logger LOG = Logger.getLogger(UnicastBroadcastMethod.class.getName());
    public static final int KIND_NODES = 100;
    private final int cachesize = 1000;
    private YFastFIFOMap<String, List<RawMessage>> partMessageCache = null;
    private BroadcastService broadcastService = null;
    private DatagramSocket _socket;
    private InetSocketAddress serverUnresolvedAddress;
    private InetSocketAddress serverAddress;
    private int packetSize = -1;
    private int sendNodesIntervalSeconds = -1;
    private final Collection<InetSocketAddress> currentClusterAddresses = new CopyOnWriteArrayList<>();
    private UnicastBroadcastMethodConfiguration cfg;
    private long lastErrorWarnTime = 0L;
    private volatile UnicastServerThread serverThread;
    private volatile SyncNodesThread syncNodesThread;
    private final ConfigIntf.ConfigChangeListener CHANGE_LISTENER;


    public UnicastBroadcastMethodConfiguration getConfiguration()
    {
        return this.cfg;
    }


    public void setConfiguration(UnicastBroadcastMethodConfiguration cfg)
    {
        if(this._socket != null)
        {
            throw new IllegalStateException("cannot change configuration after method has been initialized");
        }
        this.cfg = cfg;
    }


    public InetSocketAddress getServerAddress()
    {
        return (this.serverAddress == null) ? this.serverUnresolvedAddress : this.serverAddress;
    }


    public Map<String, String> getSettings()
    {
        Map<String, String> ret = new LinkedHashMap<>(super.getSettings());
        ret.put(DefaultUnicastBroadcastConfiguration.CFG_SERVERADDRESS, (getServerAddress() != null) ? getServerAddress().toString() : "n/a");
        ret.put(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, String.valueOf(getAddresses()));
        ret.put(DefaultUnicastBroadcastConfiguration.CFG_SYNC_INTERVAL, String.valueOf(this.sendNodesIntervalSeconds));
        return ret;
    }


    public void init(BroadcastService service)
    {
        this.broadcastService = service;
        readConfig();
        registerConfigChangeListener();
        start();
    }


    public UnicastBroadcastMethod()
    {
        this.CHANGE_LISTENER = (ConfigIntf.ConfigChangeListener)new Object(this);
        setConfiguration((UnicastBroadcastMethodConfiguration)new DefaultUnicastBroadcastConfiguration(getConfig()));
    }


    protected ConfigIntf getConfig()
    {
        return Registry.getMasterTenant().getConfig();
    }


    protected void registerConfigChangeListener()
    {
        getConfig().registerConfigChangeListener(this.CHANGE_LISTENER);
    }


    protected void removeConfigChangeListener()
    {
        getConfig().unregisterConfigChangeListener(this.CHANGE_LISTENER);
    }


    private void mergeClusterNodesToConfig(Collection<InetSocketAddress> unresolvedOtherNodes)
    {
        Set<InetSocketAddress> unresolvedConfigNodes = readNodesFromConfig();
        if(unresolvedConfigNodes.addAll(unresolvedOtherNodes))
        {
            String newNodesString = DefaultUnicastBroadcastConfiguration.getClusterNodesAsString(unresolvedConfigNodes);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("changing cluster nodes on new nodes received. new nodes are " + newNodesString + ".");
            }
            getConfig().setParameter(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES, newNodesString);
        }
    }


    private void setClusterNodesFromConfig()
    {
        Collection<InetSocketAddress> nodesFromConfig = readNodesFromConfig();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("setting unicast cluster nodes to " + nodesFromConfig + " after config update");
        }
        setClusterNodes(nodesFromConfig);
    }


    private Set<InetSocketAddress> readNodesFromConfig()
    {
        String clusterNodeList = getConfig().getParameter(DefaultUnicastBroadcastConfiguration.CFG_CLUSTERNODES);
        return new LinkedHashSet<>(DefaultUnicastBroadcastConfiguration.parseClusterNodes(clusterNodeList,
                        getServerAddress().getPort()));
    }


    private void setClusterNodes(Collection<InetSocketAddress> unresolvedAddresses)
    {
        ResolvedAddressSet resolvedNewAddresses = (unresolvedAddresses != null) ? new ResolvedAddressSet(resolveAddresses(unresolvedAddresses)) : new ResolvedAddressSet();
        if(this.serverAddress != null)
        {
            resolvedNewAddresses.add(this.serverAddress);
        }
        if(this.currentClusterAddresses.isEmpty())
        {
            this.currentClusterAddresses.addAll((Collection<? extends InetSocketAddress>)resolvedNewAddresses);
            if(LOG.isInfoEnabled())
            {
                LOG.info("unicast nodes updated: added " + resolvedNewAddresses.size() + "; got these nodes now: " + this.currentClusterAddresses);
            }
        }
        else
        {
            Set<InetSocketAddress> toAdd = new LinkedHashSet<>((Collection<? extends InetSocketAddress>)resolvedNewAddresses);
            toAdd.removeAll(this.currentClusterAddresses);
            Set<InetSocketAddress> toRemove = new LinkedHashSet<>(this.currentClusterAddresses);
            toRemove.removeAll((Collection<?>)resolvedNewAddresses);
            if(!toAdd.isEmpty())
            {
                this.currentClusterAddresses.addAll(toAdd);
            }
            if(!toRemove.isEmpty())
            {
                this.currentClusterAddresses.removeAll(toRemove);
            }
            if(LOG.isInfoEnabled())
            {
                LOG.info("unicast nodes updated: added " + toAdd.size() + " and removed " + toRemove.size() + "; got these nodes now: " + this.currentClusterAddresses);
            }
        }
    }


    public synchronized void send(RawMessage message)
    {
        send(message, false);
    }


    private void send(RawMessage message, boolean excludeLocal)
    {
        if(message.mustSplit(this.packetSize))
        {
            for(RawMessage partmessage : message.split(this.packetSize))
            {
                send(partmessage.toRawByteArray(), excludeLocal);
            }
        }
        else
        {
            send(message.toRawByteArray(), excludeLocal);
        }
    }


    public boolean isSyncNodesEnabled()
    {
        return (this.sendNodesIntervalSeconds >= 0);
    }


    private boolean isHavingOtherNodes()
    {
        return (getAddresses().size() > 1 || !getAddresses().contains(this.serverAddress));
    }


    private void sendMyNodesSyncMessagesIfEnabled()
    {
        if(isSyncNodesEnabled() && isHavingOtherNodes())
        {
            Set<InetSocketAddress> toSend = readNodesFromConfig();
            toSend.add(this.serverUnresolvedAddress);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("synchronizing own unicast nodes with the cluster. sending " + toSend);
            }
            RawMessage message = new RawMessage(100, DefaultUnicastBroadcastConfiguration.getClusterNodesAsString(toSend).getBytes());
            ((DefaultBroadcastService)this.broadcastService).addTransportData(message);
            send(message, true);
        }
    }


    private void processSyncNodesMessage(String msg)
    {
        if(isSyncNodesEnabled())
        {
            mergeClusterNodesToConfig(DefaultUnicastBroadcastConfiguration.parseClusterNodes(msg, -1));
        }
    }


    public Collection<InetSocketAddress> getClusterNodes()
    {
        return Collections.unmodifiableCollection(getAddresses());
    }


    private Collection<InetSocketAddress> getAddresses()
    {
        return this.currentClusterAddresses;
    }


    private void send(byte[] toSend, boolean excludeLocal)
    {
        for(InetSocketAddress a : getAddresses())
        {
            if(!excludeLocal || !isOwnAddress(a))
            {
                DatagramPacket datagramPacket = new DatagramPacket(toSend, toSend.length, a.getAddress(), a.getPort());
                sendPacket(datagramPacket);
                continue;
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("exclude local: not send sending " + toSend.length + " bytes to " + a);
            }
        }
    }


    protected boolean isOwnAddress(InetSocketAddress nodeAddress)
    {
        return (this.serverAddress != null && this.serverAddress.equals(nodeAddress));
    }


    public void shutdown()
    {
        removeConfigChangeListener();
        if(this.syncNodesThread != null)
        {
            this.syncNodesThread.interrupt();
            this.syncNodesThread = null;
        }
        if(this.serverThread != null)
        {
            this.serverThread.stopServer();
            this.serverThread = null;
        }
        if(this._socket != null && !this._socket.isClosed())
        {
            try
            {
                this._socket.close();
            }
            catch(Exception e)
            {
                LOG.warn("error closing multicast socket (error:" + e.getMessage() + ")", e);
            }
            finally
            {
                this._socket = null;
            }
        }
        this.serverUnresolvedAddress = null;
        this.serverAddress = null;
    }


    protected void readConfig()
    {
        if(this.cfg == null)
        {
            throw new IllegalStateException("no configuration set");
        }
        UnicastBroadcastMethodConfiguration cfg = getConfiguration();
        this.serverUnresolvedAddress = cfg.getServerAddress();
        this.packetSize = cfg.getPacketSize();
        this.sendNodesIntervalSeconds = cfg.getSyncNodesIntervalSeconds();
        setClusterNodes(cfg.getClusterNodes());
    }


    protected Set<InetSocketAddress> resolveAddresses(Collection<InetSocketAddress> unresolvedAddresses)
    {
        Set<InetSocketAddress> resolvedOnes = new LinkedHashSet<>();
        for(InetSocketAddress unresolvedAddress : unresolvedAddresses)
        {
            InetSocketAddress resolvedOne = resolveAddress(unresolvedAddress);
            if(resolvedOne != null)
            {
                resolvedOnes.add(resolvedOne);
            }
        }
        return resolvedOnes;
    }


    protected InetSocketAddress resolveAddress(InetSocketAddress unresolvedAddress)
    {
        if(unresolvedAddress.isUnresolved())
        {
            InetSocketAddress ret = null;
            try
            {
                ret = new InetSocketAddress(InetAddress.getByName(unresolvedAddress.getHostName()), unresolvedAddress.getPort());
            }
            catch(Exception e)
            {
                LOG.warn("cannot resolve cluster node address " + unresolvedAddress + " due to " + e
                                .getMessage() + " - ignoring it", e);
            }
            return (ret == null || ret.isUnresolved()) ? null : ret;
        }
        return unresolvedAddress;
    }


    protected void start()
    {
        this._socket = startSocket();
        if(this._socket != null)
        {
            startServer();
            Thread.yield();
            sendMyNodesSyncMessagesIfEnabled();
            if(isSyncNodesEnabled())
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("starting synchronizing unicast cluster nodes with " + this.sendNodesIntervalSeconds + " seconds interval");
                }
                this.syncNodesThread = new SyncNodesThread(this, this.sendNodesIntervalSeconds);
                this.syncNodesThread.start();
            }
        }
    }


    protected void startServer()
    {
        this.serverThread = new UnicastServerThread(this, this._socket, this.packetSize);
        this.serverThread.start();
    }


    protected DatagramSocket startSocket()
    {
        DatagramSocket socket = null;
        try
        {
            this.serverAddress = new InetSocketAddress(this.serverUnresolvedAddress.getHostName(), this.serverUnresolvedAddress.getPort());
            socket = createDatagramSocket(this.serverAddress);
            if(!this.currentClusterAddresses.contains(this.serverAddress))
            {
                this.currentClusterAddresses.add(this.serverAddress);
            }
        }
        catch(IOException e)
        {
            LOG.error("error starting Unicast broadcast socket using " + this.serverUnresolvedAddress + " (error:" + e
                            .getMessage() + ")", e);
        }
        return socket;
    }


    protected synchronized void sendPacket(DatagramPacket packet)
    {
        if(this._socket != null)
        {
            try
            {
                this._socket.send(packet);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("sent " + packet.getLength() + " bytes to " + packet.getAddress() + ":" + packet.getPort());
                }
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


    private DatagramSocket createDatagramSocket(InetSocketAddress socketAddress) throws IOException
    {
        DatagramSocket socket = new DatagramSocket(socketAddress);
        if(LOG.isDebugEnabled())
        {
            synchronized(UnicastBroadcastMethod.class)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("-------------------------------------------------------------------------");
                    LOG.debug("hybris Unicast Broadcast Method configured with the following parameters: ");
                    LOG.debug("Server address: " + socketAddress);
                    LOG.debug("Address: " + socketAddress);
                    LOG.debug("Cluster Island: " + Registry.getMasterTenant().getClusterIslandPK());
                    LOG.debug("Node ID: " + MasterTenant.getInstance().getClusterID() + "(dynamic:" +
                                    MasterTenant.getInstance().getDynamicClusterNodeID() + ")");
                    LOG.debug("-------------------------------------------------------------------------");
                }
            }
        }
        return socket;
    }


    protected void notifyMessgageReceived(RawMessage message)
    {
        if(message.getKind() == 100)
        {
            String msg = new String(message.getData());
            if(LOG.isDebugEnabled())
            {
                LOG.debug("received sync nodes message " + message + " -> " + msg);
            }
            processSyncNodesMessage(msg);
        }
        else
        {
            super.notifyMessgageReceived(message);
        }
    }


    protected boolean accept(RawMessage msg)
    {
        DefaultBroadcastService dbs = (DefaultBroadcastService)this.broadcastService;
        return (dbs == null || dbs.accept(msg));
    }


    protected void processDatagramPacket(DatagramPacket packet)
    {
        RawMessage msg = new RawMessage(packet.getData(), packet.getOffset(), packet.getLength());
        if(accept(msg))
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
