package de.hybris.platform.cluster;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.SingletonCreator;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class PingBroadcastHandler implements BroadcastMessageListener
{
    private static final Logger LOG = Logger.getLogger(PingBroadcastHandler.class);
    public static final int KIND_PING = 99;
    public static final String CFG_PING_INTERVAL = "cluster.ping.interval";
    public static final int PING_INTERVAL = 30;
    private final BroadcastService broadcastService;
    private volatile Timer pingTimer;
    private volatile TimerTask timerTask;
    private final Map<Long, NodeInfo> nodes;
    public static final String DELIMITER = "|";
    public static final String COMMAND_PING = "<PING>";
    public static final String COMMAND_PING_RESULT = "<PING_RESULT>";
    private static final long PING_WAIT_TIME = 5000L;
    private volatile long lastPingRequest = 0L;
    private final Pattern pingRequestRegExpPattern = Pattern.compile("<PING>");
    private final Pattern pingAnswerRegExpPattern = Pattern.compile("<PING_RESULT>(\\d+)\\|(\\d+)");
    private static final SingletonCreator.Creator<PingBroadcastHandler> SINGLETON_CREATOR = (SingletonCreator.Creator<PingBroadcastHandler>)new Object();


    public static PingBroadcastHandler getInstance()
    {
        return (PingBroadcastHandler)Registry.getNonTenantSingleton(SINGLETON_CREATOR);
    }


    public PingBroadcastHandler(BroadcastService broadcastService)
    {
        this.broadcastService = broadcastService;
        this.nodes = new ConcurrentHashMap<>();
        this.broadcastService.registerBroadcastListener(this, false);
        startPingTask(Registry.getMasterTenant().getConfig().getInt("cluster.ping.interval", 30));
    }


    public void destroy()
    {
        synchronized(this)
        {
            this.broadcastService.unregisterBroadcastListener(this);
            if(this.pingTimer != null)
            {
                this.pingTimer.cancel();
                this.pingTimer = null;
            }
            if(this.timerTask != null)
            {
                this.timerTask.cancel();
                this.timerTask = null;
            }
            this.nodes.clear();
        }
    }


    public boolean processMessage(RawMessage message)
    {
        return (message.getKind() == 99 && processPingMessage(message));
    }


    private void startPingTask(int intervalSec)
    {
        if(this.pingTimer != null)
        {
            throw new IllegalStateException("ping timer task already started");
        }
        if(intervalSec > 0)
        {
            this.pingTimer = new Timer("Ping Timer", true);
            Object object = new Object(this);
            try
            {
                this.pingTimer.schedule((TimerTask)object, 0L, (intervalSec * 1000));
            }
            catch(Exception e)
            {
                LOG.error("unknown error starting ping timer task - cannot discover other cluster nodes! (see stacktrace)", e);
            }
        }
    }


    public boolean isNodeAlive(int nodeID)
    {
        return this.nodes.containsKey(Integer.valueOf(nodeID));
    }


    public Collection<NodeInfo> getNodes()
    {
        long diff;
        if((diff = System.currentTimeMillis() - this.lastPingRequest) < 5000L)
        {
            try
            {
                Thread.sleep(5000L - diff);
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        return this.nodes.isEmpty() ? Collections.EMPTY_LIST : Collections.<NodeInfo>unmodifiableCollection(this.nodes.values());
    }


    protected void addNode(long dynamicNodeID, int remoteNodeID, String ipAddress, String methodName)
    {
        synchronized(this.nodes)
        {
            NodeInfo newOne = new NodeInfo(dynamicNodeID, remoteNodeID, ipAddress, methodName);
            this.nodes.put(Long.valueOf(dynamicNodeID), newOne);
            for(Map.Entry<Long, NodeInfo> e : this.nodes.entrySet())
            {
                NodeInfo other = e.getValue();
                if(other.getNodeID() == remoteNodeID && other.getDynamicNodeID() != dynamicNodeID)
                {
                    other.markAsDuplicate();
                    newOne.markAsDuplicate();
                }
            }
        }
    }


    public void pingNodes()
    {
        if(!RedeployUtilities.isShutdownInProgress())
        {
            try
            {
                this.nodes.clear();
                this.lastPingRequest = System.currentTimeMillis();
                sendPing();
            }
            catch(Exception e)
            {
                LOG.error("error sending cluster ping message (error:" + e.getMessage() + ")", e);
            }
        }
    }


    protected void sendPing()
    {
        this.broadcastService.send(new RawMessage(99, "<PING>"
                        .getBytes()));
    }


    protected void sendPingAnswer(long remoteDynamicNode)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<PING_RESULT>");
        stringBuilder.append(remoteDynamicNode).append("|");
        stringBuilder.append(getClusterNodeID());
        this.broadcastService.send(new RawMessage(99, stringBuilder
                        .toString().getBytes()));
    }


    protected boolean processPingMessage(RawMessage message)
    {
        String string = new String(message.getData());
        if(LOG.isDebugEnabled())
        {
            LOG.debug("process ping message:" + string);
        }
        try
        {
            Matcher pingMatcher = this.pingRequestRegExpPattern.matcher(string);
            if(pingMatcher.matches())
            {
                sendPingAnswer(message.getDynamicNodeID());
                return true;
            }
            Matcher pingAnswerMatcher = this.pingAnswerRegExpPattern.matcher(string);
            if(pingAnswerMatcher.matches())
            {
                long srcDynamicNodeID = Long.parseLong(pingAnswerMatcher.group(1));
                if(this.broadcastService.getDynamicClusterNodeID() == srcDynamicNodeID)
                {
                    addNode(message
                                                    .getDynamicNodeID(),
                                    Integer.parseInt(pingAnswerMatcher.group(2)),
                                    (message.getRemoteAddress() != null) ? message.getRemoteAddress().getHostAddress() : "n/a", message
                                                    .getBroadcastMethod());
                }
                return true;
            }
        }
        catch(Exception e)
        {
            LOG.error("unexpected error parsing PING message " + message + " (error:" + e.getMessage() + ")", e);
        }
        return false;
    }


    protected int getClusterNodeID()
    {
        return Registry.getMasterTenant().getClusterID();
    }


    protected long getClusterIslandPK()
    {
        return Registry.getMasterTenant().getClusterIslandPK();
    }
}
