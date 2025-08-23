package de.hybris.platform.cluster;

import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultBroadcastServiceConfiguration implements BroadcastServiceConfiguration
{
    private static final Logger LOG = Logger.getLogger(DefaultBroadcastService.class);
    public static String CFG_METHODS = "cluster.broadcast.methods";
    public static String CFG_METHOD_PREFIX = "cluster.broadcast.method.";
    public static String CFG_SENDER_THREADS_MIN = "cluster.broadcast.senderthreads.min";
    public static String CFG_SENDER_THREADS_MAX = "cluster.broadcast.senderthreads.max";
    public static String CFG_SENDER_THREADS_KEEPALIVE = "cluster.broadcast.senderthreads.keepalive";
    public static String CFG_SENDER_THREADS_QUEUESIZE = "cluster.broadcast.senderthreads.queuesize";
    public static String CFG_SENDER_THREADS_QUEUE_FAIRNESS = "cluster.broadcast.senderthreads.queue.fair";
    public static String CFG_SENDER_THREADS_MAXWAIT = "cluster.broadcast.senderthreads.maxwait";
    public static String CFG_SENDER_THREADS_WAITDELAY = "cluster.broadcast.senderthreads.waitdelay";
    public static String CFG_SENDER_THREADS_DISABLE = "cluster.broadcast.senderthreads.disable";
    public static String CFG_START_PING_ON_TENANT_STARTUP = "cluster.ping.load.on.startup";
    public static String CFG_MESSAGE_KEYCACHE_SIZE = "cluster.broadcast.keycache.size";
    public static String DEFAULT_METHODS = "udp";
    public static final long MESSAGE_SENDING_TIMEOUT = 60L;
    public static final int MESSAGE_QUEUE_WAIT_TIME = 100;
    public static final int MESSAGE_QUEUE_SIZE = 1000;
    public static final int MESSAGE_THREADS_MIN = 1;
    public static final int MESSAGE_THREADS_MAX = 10;
    public static final int MESSAGE_THREADS_KEEPALIVE = 10;
    private long dynamicID;
    private int configuredID;
    private long clusterIslandID;
    private boolean clusterMode;
    private boolean asynchronousSending;
    private long sendingMaxWait;
    private int sendingWaitDelay;
    private int senderMinThreads;
    private int senderMaxThreads;
    private int senderKeepAlive;
    private int sendingQueueSize;
    private boolean sendingQueueFair;
    private final boolean startPingOnTenant;
    private int messageKeyCacheSize;
    private Map<String, Class> methods;


    public DefaultBroadcastServiceConfiguration()
    {
        MasterTenant masterTenant = Registry.getMasterTenant();
        this.dynamicID = PK.createUUIDPK(0).getLongValue();
        this.configuredID = masterTenant.getClusterID();
        this.clusterIslandID = masterTenant.getClusterIslandPK();
        this.clusterMode = masterTenant.isClusteringEnabled();
        ConfigIntf cfg = masterTenant.getConfig();
        this.asynchronousSending = !cfg.getBoolean(CFG_SENDER_THREADS_DISABLE, false);
        this.sendingMaxWait = 1000L * cfg.getLong(CFG_SENDER_THREADS_MAXWAIT, 60L);
        this.sendingWaitDelay = cfg.getInt(CFG_SENDER_THREADS_WAITDELAY, 100);
        this.senderMinThreads = cfg.getInt(CFG_SENDER_THREADS_MIN, 1);
        this.senderMaxThreads = cfg.getInt(CFG_SENDER_THREADS_MAX, 10);
        this.senderKeepAlive = 1000 * cfg.getInt(CFG_SENDER_THREADS_KEEPALIVE, 10);
        this.sendingQueueSize = cfg.getInt(CFG_SENDER_THREADS_QUEUESIZE, 1000);
        this.sendingQueueFair = cfg.getBoolean(CFG_SENDER_THREADS_QUEUE_FAIRNESS, false);
        this.messageKeyCacheSize = cfg.getInt(CFG_MESSAGE_KEYCACHE_SIZE, 100);
        this.startPingOnTenant = cfg.getBoolean(CFG_START_PING_ON_TENANT_STARTUP, true);
        this.methods = Collections.unmodifiableMap((Map)loadMethods(cfg));
    }


    protected Map<String, Class> loadMethods(ConfigIntf cfg)
    {
        Map<String, Class<?>> ret = new LinkedHashMap<>();
        for(String method : cfg.getString(CFG_METHODS, DEFAULT_METHODS).split("[,; ]"))
        {
            Class<?> clazz;
            String className = cfg.getParameter(CFG_METHOD_PREFIX + CFG_METHOD_PREFIX);
            try
            {
                clazz = Class.forName(className);
            }
            catch(Exception e)
            {
                LOG.error("Invalid messaging mode definition " + method + " -> " + className + " ( error: " + e
                                .getMessage() + ")", e);
            }
            if(clazz != null)
            {
                ret.put(method, clazz);
            }
        }
        return ret;
    }


    public boolean enableAsynchonousSending()
    {
        return this.asynchronousSending;
    }


    public void setAsynchonousSending(boolean enabled)
    {
        this.asynchronousSending = enabled;
    }


    public boolean enableClusterMode()
    {
        return this.clusterMode;
    }


    public void setClusterMode(boolean enabled)
    {
        this.clusterMode = enabled;
    }


    public int getConfiguredNodeID()
    {
        return this.configuredID;
    }


    public void setConfiguredNodeID(int id)
    {
        this.configuredID = id;
    }


    public long getClusterIslandID()
    {
        return this.clusterIslandID;
    }


    public void setClusterIslandID(long id)
    {
        this.clusterIslandID = id;
    }


    public long getDynamicNodeID()
    {
        return this.dynamicID;
    }


    public void setDynamicNodeID(long id)
    {
        this.dynamicID = id;
    }


    public int getMessageKeyCacheSize()
    {
        return this.messageKeyCacheSize;
    }


    public void setMessageKeyCacheSize(int size)
    {
        this.messageKeyCacheSize = size;
    }


    public long getMessageSendingMaxWait()
    {
        return this.sendingMaxWait;
    }


    public void setMessageSendingMaxWait(long milliseconds)
    {
        this.sendingMaxWait = milliseconds;
    }


    public int getMessageSendingQueueSize()
    {
        return this.sendingQueueSize;
    }


    public void setMessageSendingQueueSize(int size)
    {
        this.sendingQueueSize = size;
    }


    public boolean getMessageSendingQueueFairness()
    {
        return this.sendingQueueFair;
    }


    public void setMessageSendingQueueFairness(boolean fair)
    {
        this.sendingQueueFair = fair;
    }


    public int getMessageSendingWaitDelay()
    {
        return this.sendingWaitDelay;
    }


    public void setMessageSendingWaitDelay(int milliseconds)
    {
        this.sendingWaitDelay = milliseconds;
    }


    public Map<String, Class> getMethods()
    {
        return this.methods;
    }


    public void setMethods(Map<String, Class<?>> methodClassMap)
    {
        this.methods = Collections.unmodifiableMap(methodClassMap);
    }


    public int getSenderMaxThreads()
    {
        return this.senderMaxThreads;
    }


    public void setSenderMaxThreads(int maxThreads)
    {
        this.senderMaxThreads = maxThreads;
    }


    public int getSenderMinThreads()
    {
        return this.senderMinThreads;
    }


    public void setSenderMinThreads(int minThreads)
    {
        this.senderMinThreads = minThreads;
    }


    public int getSenderThreadsKeepAlive()
    {
        return this.senderKeepAlive;
    }


    public void setSenderThreadsKeepAlive(int milliseconds)
    {
        this.senderKeepAlive = milliseconds;
    }


    public boolean startPingOnTenantStartup()
    {
        return this.startPingOnTenant;
    }
}
