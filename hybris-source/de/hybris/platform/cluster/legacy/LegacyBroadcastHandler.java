package de.hybris.platform.cluster.legacy;

import de.hybris.platform.cluster.BroadcastMessageListener;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.SingletonCreator;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.threadpool.PoolableThread;
import de.hybris.platform.util.threadpool.ThreadPool;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

public class LegacyBroadcastHandler implements BroadcastMessageListener
{
    private static final Logger LOG = Logger.getLogger(LegacyBroadcastHandler.class.getName());
    public static final String CFG_MAXTHREADS_OLD = "cluster.udpserver.maxthreads";
    public static final String CFG_MAXTHREADS = "cluster.broadcast.worker.maxthreads";
    public static final int MAXTHREADS = 32;
    public static final int IDLETHREADS = 10;
    private static final SingletonCreator.Creator<LegacyBroadcastHandler> SINGLETON_CREATOR = (SingletonCreator.Creator<LegacyBroadcastHandler>)new Object();
    private final BroadcastService broadcastService;


    public static LegacyBroadcastHandler getInstance()
    {
        return (LegacyBroadcastHandler)Registry.getNonTenantSingleton(SINGLETON_CREATOR);
    }


    private final CopyOnWriteArrayList<BinaryBroadcastListener> binaryListeners = new CopyOnWriteArrayList<>();
    private final ConcurrentMap<String, MessageBroadcastListener> listeners = new ConcurrentHashMap<>(100, 0.75F, 4);
    private final ThreadPool workerThreadPool;
    public static final int KIND_CUSTOMEVENT_BINARY = 3;
    public static final int KIND_CUSTOMEVENT = 2;


    LegacyBroadcastHandler(BroadcastService broadcastService)
    {
        this.broadcastService = broadcastService;
        this.workerThreadPool = createWorkerThreadPool();
        this.broadcastService.registerBroadcastListener(this, false);
    }


    public boolean processMessage(RawMessage message)
    {
        if(3 == message.getKind())
        {
            notifyMessageReceived(message, message.getData());
            return true;
        }
        if(2 == message.getKind())
        {
            notifyMessageReceived(new String(message.getData()));
            return true;
        }
        return false;
    }


    public void sendBinaryCustomPacket(byte[] customData)
    {
        this.broadcastService.send(new RawMessage(3, customData));
    }


    public void sendCustomPacket(String prefix, String msg)
    {
        this.broadcastService.send(new RawMessage(2, ("[" + prefix + "]" + msg).getBytes()));
    }


    public void registerBinaryListener(BinaryBroadcastListener listener)
    {
        this.binaryListeners.addIfAbsent(listener);
    }


    public void unregisterBinaryListener(BinaryBroadcastListener listener)
    {
        this.binaryListeners.remove(listener);
    }


    public void unregisterMessageListener(String prefix)
    {
        this.listeners.remove(adjustPrefix(prefix));
    }


    public void registerMessageListener(String prefix, MessageBroadcastListener messageBroadcastListener)
    {
        this.listeners.put(adjustPrefix(prefix), messageBroadcastListener);
    }


    protected String adjustPrefix(String prefix)
    {
        return "[" + prefix + "]";
    }


    protected void destroy()
    {
        this.broadcastService.unregisterBroadcastListener(this);
        this.workerThreadPool.close();
        this.binaryListeners.clear();
        this.listeners.clear();
    }


    protected void notifyMessageReceived(RawMessage message, byte[] data)
    {
        for(BinaryBroadcastListener b : this.binaryListeners)
        {
            try
            {
                PoolableThread poolableThread = this.workerThreadPool.borrowThread();
                poolableThread.execute((Runnable)new NotifyMessageReceivedRunnable(this, b, data));
            }
            catch(NoSuchElementException e)
            {
                LOG.warn("Unable to get worker from pool - a binary broadcast could not be processed by " + b, e);
            }
        }
    }


    protected void notifyMessageReceived(String string)
    {
        for(Map.Entry<String, MessageBroadcastListener> e : this.listeners.entrySet())
        {
            String adjustedPrefix = e.getKey();
            if(string.startsWith(adjustedPrefix))
            {
                if(((MessageBroadcastListener)e.getValue()).processPacket(string.substring(adjustedPrefix.length())))
                {
                    break;
                }
            }
        }
    }


    private ThreadPool createWorkerThreadPool()
    {
        ConfigIntf cfg = Registry.getMasterTenant().getConfig();
        int poolSize = cfg.getInt("cluster.udpserver.maxthreads", -1);
        if(poolSize == -1)
        {
            poolSize = cfg.getInt("cluster.broadcast.worker.maxthreads", 32);
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("create binary message processor worker pool (size:" + poolSize + ")");
        }
        ThreadPool ret = new ThreadPool(null, poolSize);
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxActive = poolSize;
        config.maxIdle = poolSize;
        config.minIdle = Math.min(10, poolSize);
        config.maxWait = 10000L;
        config.whenExhaustedAction = 1;
        config.testOnBorrow = true;
        config.testOnReturn = true;
        config.timeBetweenEvictionRunsMillis = -1L;
        ret.setConfig(config);
        return ret;
    }
}
