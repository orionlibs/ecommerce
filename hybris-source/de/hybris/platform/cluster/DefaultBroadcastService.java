package de.hybris.platform.cluster;

import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.collections.YFastFIFOMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class DefaultBroadcastService implements BroadcastService
{
    private static final Logger LOG = Logger.getLogger(DefaultBroadcastService.class);
    public static final int CUR_VERSION = 67174656;
    private static final SingletonCreator.Creator<DefaultBroadcastService> SINGLETON_CREATOR = (SingletonCreator.Creator<DefaultBroadcastService>)new Object();
    private final long dynamicNodeID;
    private long clusterIslandID;
    private int clusterNodeID;
    private final boolean clusteringEnabled;
    private volatile boolean methodsInitialized;
    private volatile List<MethodWrapper> methods;


    public static DefaultBroadcastService getInstance()
    {
        return (DefaultBroadcastService)Registry.getNonTenantSingleton(SINGLETON_CREATOR);
    }


    private final List<BroadcastMessageListener> listeners = new CopyOnWriteArrayList<>();
    private final List<BroadcastMessageListener> localMessageListeners = new CopyOnWriteArrayList<>();
    private final Map messageKeyMap;
    private final boolean sendAsynchonouslyFlag;
    private final long sendAsyncTimeout;
    private final long sendAsyncWaitTime;
    private final ExecutorService messageSendingExecutors;
    private final AtomicInteger messageCounter = new AtomicInteger();
    private final BroadcastServiceConfiguration cfg;


    public DefaultBroadcastService(BroadcastServiceConfiguration cfg)
    {
        this.cfg = cfg;
        this.dynamicNodeID = cfg.getDynamicNodeID();
        this.clusterNodeID = cfg.getConfiguredNodeID();
        this.clusterIslandID = cfg.getClusterIslandID();
        this.clusteringEnabled = cfg.enableClusterMode();
        this.sendAsynchonouslyFlag = (cfg.enableClusterMode() && cfg.enableAsynchonousSending());
        if(this.sendAsynchonouslyFlag)
        {
            this.sendAsyncTimeout = cfg.getMessageSendingMaxWait();
            this.sendAsyncWaitTime = cfg.getMessageSendingWaitDelay();
            this.messageSendingExecutors = createMessageSendingExecutorService();
        }
        else
        {
            this.sendAsyncTimeout = -1L;
            this.sendAsyncWaitTime = -1L;
            this.messageSendingExecutors = null;
        }
        this.messageKeyMap = (Map)new YFastFIFOMap(cfg.getMessageKeyCacheSize());
        getMethods();
        if(LOG.isInfoEnabled())
        {
            LOG.info(getStartupInfo());
        }
    }


    protected String getStartupInfo()
    {
        StringBuilder stringBuilder = new StringBuilder("Started message broadcast service:\n");
        stringBuilder.append("clustering (enabled:").append(this.clusteringEnabled);
        if(this.clusteringEnabled)
        {
            stringBuilder.append(",islandID:").append(
                            (this.clusterIslandID == -1L) ? "<loaded from tenant>" : Long.toString(this.clusterIslandID));
            stringBuilder.append(",nodeID:").append(this.clusterNodeID);
            stringBuilder.append(",dynamicNodeID:").append(this.dynamicNodeID);
        }
        stringBuilder.append(")\n");
        if(this.sendAsynchonouslyFlag)
        {
            stringBuilder.append("sending asynchronously (queueSize:").append(this.cfg.getMessageSendingQueueSize());
            stringBuilder.append(",fair:").append(this.cfg.getMessageSendingQueueFairness());
            stringBuilder.append(",workers:").append(this.cfg.getSenderMinThreads()).append('/').append(this.cfg.getSenderMaxThreads());
            stringBuilder.append(",keep-alive:").append(this.cfg.getSenderThreadsKeepAlive()).append("ms");
            stringBuilder.append(",sendWaitDelay:").append(this.sendAsyncWaitTime).append("ms");
            stringBuilder.append(",sendTimeout:").append(this.sendAsyncTimeout).append("ms");
            stringBuilder.append(")\n");
        }
        else
        {
            stringBuilder.append("sending synchronously - no queue\n");
        }
        stringBuilder.append("methods ").append(getMethods());
        return stringBuilder.toString();
    }


    protected void updateNodeIDsFromDatabase()
    {
        MasterTenant masterTenant = Registry.getMasterTenant();
        if(LOG.isInfoEnabled())
        {
            if(this.clusterIslandID != masterTenant.getClusterIslandPK())
            {
                LOG.info("updating cluster island ID " + this.clusterIslandID + "->" + masterTenant.getClusterIslandPK());
            }
            if(this.clusterNodeID != masterTenant.getClusterID())
            {
                LOG.info("updating cluster node ID " + this.clusterNodeID + "->" + masterTenant.getClusterID());
            }
        }
        this.clusterIslandID = masterTenant.getClusterIslandPK();
        this.clusterNodeID = masterTenant.getClusterID();
    }


    protected ExecutorService createMessageSendingExecutorService()
    {
        return new ThreadPoolExecutor(this.cfg
                        .getSenderMinThreads(), this.cfg.getSenderMaxThreads(), this.cfg
                        .getSenderThreadsKeepAlive(), TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(this.cfg
                        .getMessageSendingQueueSize(), this.cfg
                        .getMessageSendingQueueFairness()), (ThreadFactory)new MyThreadFactory("BroadcastSender"));
    }


    protected ExecutorService getMessageSendingExecutorService()
    {
        return this.messageSendingExecutors;
    }


    public long getDynamicClusterNodeID()
    {
        return this.dynamicNodeID;
    }


    public Set<String> getBroadcastMethodNames()
    {
        List<MethodWrapper> methods = getMethods();
        if(methods.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<String> ret = new LinkedHashSet<>(methods.size());
        for(MethodWrapper m : methods)
        {
            ret.add(m.getName());
        }
        return ret;
    }


    public BroadcastMethod getBroadcastMethod(String name)
    {
        for(MethodWrapper m : getMethods())
        {
            if(name.equalsIgnoreCase(m.getName()))
            {
                return m.getMethod();
            }
        }
        return null;
    }


    protected List<MethodWrapper> getMethods()
    {
        List<MethodWrapper> ret = this.methods;
        if(ret == null)
        {
            synchronized(this)
            {
                ret = this.methods;
                if(ret == null)
                {
                    ret = Collections.unmodifiableList(loadMethods());
                    this.methods = ret;
                }
            }
        }
        return ret;
    }


    public void initMethods()
    {
        if(!this.methodsInitialized)
        {
            synchronized(this)
            {
                if(!this.methodsInitialized)
                {
                    this.methodsInitialized = true;
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("lazy initialization of broadcast methods");
                    }
                    List<MethodWrapper> failed = new ArrayList<>();
                    for(MethodWrapper m : getMethods())
                    {
                        try
                        {
                            m.initialize(this);
                        }
                        catch(Exception e)
                        {
                            LOG.error("error initializing broadcast method " + m + " (error:" + e
                                            .getMessage() + ") - removing method", e);
                            failed.add(m);
                        }
                    }
                    if(!failed.isEmpty())
                    {
                        List<MethodWrapper> newOne = new ArrayList<>(this.methods);
                        newOne.removeAll(failed);
                        if(newOne.isEmpty())
                        {
                            newOne.addAll(loadLoopbackMethods());
                            for(MethodWrapper m : newOne)
                            {
                                try
                                {
                                    m.initialize(this);
                                }
                                catch(Exception e)
                                {
                                    LOG.error("error initializing loopback broadcast method " + m + " (error:" + e.getMessage() + ") - removing method", e);
                                    newOne.remove(m);
                                }
                            }
                        }
                        this.methods = Collections.unmodifiableList(newOne);
                    }
                    for(MethodWrapper wr : this.methods)
                    {
                        wr.getMethod().registerProcessor((BroadcastMessageListener)new MethodBroadcastListener(this, wr.getName()));
                    }
                }
            }
        }
    }


    protected void startPingHandler()
    {
        if(this.cfg.startPingOnTenantStartup())
        {
            PingBroadcastHandler.getInstance();
        }
    }


    protected List<MethodWrapper> loadMethods()
    {
        List<MethodWrapper> methods = null;
        if(this.clusteringEnabled)
        {
            methods = loadConfiguredMethods();
            if(methods.isEmpty())
            {
                methods = loadLoopbackMethods();
            }
        }
        else
        {
            methods = loadLoopbackMethods();
        }
        return methods;
    }


    protected List<MethodWrapper> loadLoopbackMethods()
    {
        MethodWrapper methodWrapper = new MethodWrapper("<loopback>", (BroadcastMethod)new LoopBackBroadcastMethod());
        return Collections.singletonList(methodWrapper);
    }


    protected List<MethodWrapper> loadConfiguredMethods()
    {
        List<MethodWrapper> tmp = new ArrayList<>();
        for(Map.Entry<String, Class<?>> cfgMethod : (Iterable<Map.Entry<String, Class<?>>>)this.cfg.getMethods().entrySet())
        {
            MethodWrapper methodWrapper = createMethod(cfgMethod.getKey(), cfgMethod.getValue());
            if(methodWrapper != null)
            {
                tmp.add(methodWrapper);
            }
        }
        return tmp;
    }


    protected MethodWrapper createMethod(String name, Class<BroadcastMethod> clazz)
    {
        try
        {
            return new MethodWrapper(name, clazz.newInstance());
        }
        catch(Exception e)
        {
            LOG.error("Invalid messaging mode definition " + name + " -> " + clazz + " ( error: " + e.getMessage() + ")", e);
            return null;
        }
    }


    public void registerBroadcastListener(BroadcastMessageListener listener, boolean remoteMessagesOnly)
    {
        if(listener == null)
        {
            throw new NullPointerException("listener was null");
        }
        if(!remoteMessagesOnly)
        {
            if(!this.localMessageListeners.contains(listener))
            {
                this.localMessageListeners.add(listener);
            }
        }
        if(!this.listeners.contains(listener))
        {
            this.listeners.add(listener);
        }
    }


    public void unregisterBroadcastListener(BroadcastMessageListener listener)
    {
        if(listener == null)
        {
            throw new NullPointerException("listener was null");
        }
        this.listeners.remove(listener);
        this.localMessageListeners.remove(listener);
    }


    public long getClusterIslandPK()
    {
        return this.clusterIslandID;
    }


    public int getClusterNodeID()
    {
        return this.clusterNodeID;
    }


    public boolean isClusteringEnabled()
    {
        return this.clusteringEnabled;
    }


    public void addTransportData(RawMessage message)
    {
        message.setSenderTransportData(67174656, getClusterIslandPK(), getDynamicClusterNodeID(), this.messageCounter
                        .getAndIncrement());
    }


    public void send(RawMessage message)
    {
        addTransportData(message);
        Tenant currentTenant = Registry.getCurrentTenantNoFallback();
        for(MethodWrapper m : getMethods())
        {
            if(m.isInitialized())
            {
                if(this.sendAsynchonouslyFlag)
                {
                    try
                    {
                        sendAsnychronously(m, message, currentTenant, getMessageSendingExecutorService());
                        continue;
                    }
                    catch(RejectedExecutionException e)
                    {
                        LOG.error("could not place message " + message + " into queue after " + this.sendAsyncTimeout + " ms - this message is not being broadcasted!", e);
                        break;
                    }
                }
                sendSynchronously(m, message);
                continue;
            }
            LOG.warn("broadcast method " + m + " is not initialized yet - cannot send message " + message);
        }
    }


    protected void sendSynchronously(MethodWrapper methodWrapper, RawMessage message)
    {
        try
        {
            methodWrapper.getMethod().send(message);
        }
        catch(Exception e)
        {
            LOG.error("error trying to send message " + message + " using " + methodWrapper + " (error: " + e.getMessage() + ")", e);
        }
    }


    protected void sendAsnychronously(MethodWrapper methodWrapper, RawMessage message, Tenant currentTenant, ExecutorService executorService) throws RejectedExecutionException
    {
        MethodMessageSender sender = new MethodMessageSender(methodWrapper, message, currentTenant);
        boolean success = false;
        long retryStartTime = -1L;
        do
        {
            try
            {
                executorService.execute((Runnable)sender);
                success = true;
            }
            catch(RejectedExecutionException e)
            {
                if(retryStartTime == -1L)
                {
                    retryStartTime = System.currentTimeMillis();
                }
                else if(System.currentTimeMillis() - retryStartTime >= this.sendAsyncTimeout)
                {
                    throw e;
                }
                try
                {
                    Thread.sleep(this.sendAsyncWaitTime);
                }
                catch(InterruptedException interruptedException)
                {
                }
            }
        }
        while(!success);
    }


    public boolean accept(RawMessage message)
    {
        if(message.getVersion() == -1)
        {
            LOG.error("Received RawMessage with undefined message version! " + message.toString());
        }
        if(getClusterIslandPK() == -1L)
        {
            if(message.getClusterIslandPK() != -1L ||
                            getDynamicClusterNodeID() != message.getDynamicNodeID())
            {
                return false;
            }
        }
        else if(getClusterIslandPK() != message.getClusterIslandPK())
        {
            if(message.getClusterIslandPK() == -1L)
            {
                LOG.error("Received RawMessage with undefined message version! " + message.toString());
            }
            return false;
        }
        if(message.getVersion() != 67174656)
        {
            return false;
        }
        return true;
    }


    protected boolean isDuplicateMessage(RawMessage message)
    {
        if(this.messageKeyMap.put(message.getMessageKey(), Boolean.TRUE) == null)
        {
            return false;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("skipped duplicate message " + message);
        }
        return true;
    }


    protected boolean isLocal(RawMessage message)
    {
        return (getDynamicClusterNodeID() == message.getDynamicNodeID());
    }


    protected void processMessageFromMethod(RawMessage message, String methodName)
    {
        if(accept(message) && !isDuplicateMessage(message))
        {
            message.setBroadcastMethod(methodName);
            if(isLocal(message))
            {
                for(BroadcastMessageListener listener : this.localMessageListeners)
                {
                    if(listener.processMessage(message))
                    {
                        break;
                    }
                }
            }
            else
            {
                for(BroadcastMessageListener listener : this.listeners)
                {
                    if(listener.processMessage(message))
                    {
                        break;
                    }
                }
            }
        }
    }


    public void destroy()
    {
        this.listeners.clear();
        this.localMessageListeners.clear();
        if(getMessageSendingExecutorService() != null)
        {
            getMessageSendingExecutorService().shutdownNow();
        }
        if(this.methods != null)
        {
            synchronized(this)
            {
                if(this.methods != null)
                {
                    for(MethodWrapper wrapper : this.methods)
                    {
                        wrapper.shutdown();
                    }
                    this.methods = null;
                }
            }
        }
    }
}
