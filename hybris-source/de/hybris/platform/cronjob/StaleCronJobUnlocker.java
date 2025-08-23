package de.hybris.platform.cronjob;

import de.hybris.platform.cluster.ClusterNodeInfo;
import de.hybris.platform.cluster.ClusterNodeManagementService;
import de.hybris.platform.cluster.DefaultClusterNodeManagementService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.suspend.SystemIsSuspendedException;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.util.Config;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaleCronJobUnlocker extends RegistrableThread
{
    public static final String PROPERTY_CRONJOB_UNLOCKER_ACTIVE = "cronjob.unlocker.active";
    public static final String PROPERTY_CRONJOB_UNLOCKER_INTERVAL = "cronjob.unlocker.interval.ms";
    public static final String PROPERTY_CRONJOB_STALE_NODE_INTERVAL = "cronjob.unlocker.stale.node.interval.ms";
    public static final String PROPERTY_CRONJOB_STALE_NODE_CUTOFF_INTERVAL = "cronjob.unlocker.stale.node.cutoff.interval.seconds";
    public static final String PROPERTY_CRONJOB_UNLOCK_LIMIT = "cronjob.unlocker.cronJobs.unlockLimit";
    private static final Logger LOGGER = LoggerFactory.getLogger(StaleCronJobUnlocker.class);
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final Tenant tenant;
    private final long defaultCheckIntervalMs;
    private final long defaultStaleNodeIntervalMs;
    private final int defaultMaxCronJobsToUnlock = 200;


    public StaleCronJobUnlocker(Tenant tenant)
    {
        this(StaleCronJobUnlocker.class.getSimpleName() + "-" + StaleCronJobUnlocker.class.getSimpleName(), tenant);
    }


    public StaleCronJobUnlocker(String threadName, Tenant tenant)
    {
        super(threadName);
        this.tenant = tenant;
        ClusterNodeManagementService clusterNodeManagementService = DefaultClusterNodeManagementService.getInstance();
        this.defaultCheckIntervalMs = clusterNodeManagementService.getUpdateInterval();
        this.defaultStaleNodeIntervalMs = clusterNodeManagementService.getStaleNodeTimeout();
    }


    protected void internalRun()
    {
        try
        {
            Registry.setCurrentTenant(this.tenant);
            do
            {
                try
                {
                    Thread.sleep(getCheckIntervalMs().toMillis());
                }
                catch(InterruptedException e)
                {
                    Thread.currentThread().interrupt();
                    break;
                }
                if(!shouldUnlockStaleCronJobs())
                {
                    continue;
                }
                unlockStaleCronJobs();
            }
            while(!this.stopped.get());
        }
        finally
        {
            Registry.unsetCurrentTenant();
        }
    }


    protected boolean shouldUnlockStaleCronJobs()
    {
        return (!this.stopped.get() && this.tenant.getConfig().getBoolean("cronjob.unlocker.active", false));
    }


    protected Duration getCheckIntervalMs()
    {
        return Duration.ofMillis(Math.max(Config.getLong("cronjob.unlocker.interval.ms", this.defaultCheckIntervalMs), 1000L));
    }


    private void unlockStaleCronJobs()
    {
        try
        {
            Instant now = Instant.ofEpochMilli(System.currentTimeMillis());
            Duration staleThreshold = getStaleNodeThresholdInterval();
            Instant staleNodeTsThreshold = getStaleNodeTsThreshold(now, staleThreshold);
            Instant staleNodeTsCutoff = getStaleNodeTsCutoff(staleNodeTsThreshold);
            Predicate<ClusterNodeInfo> staleNodePredicate = getStaleNodePredicate(staleNodeTsThreshold, staleNodeTsCutoff);
            Collection<ClusterNodeInfo> allNodes = getAllNodes();
            LOGGER.debug("Trying to unlock stale cronjobs. Looking for nodes with last ping between {} and {}", staleNodeTsCutoff, staleNodeTsThreshold);
            List<Integer> staleNodeIds = getStaleNodeIds(allNodes, staleNodePredicate);
            List<Integer> allNodeIds = getNodeIds(allNodes);
            unlockCronJobsForNodeIds(staleNodeIds, allNodeIds);
            unlockCronJobsFromLocalInstance();
        }
        catch(SystemIsSuspendedException e)
        {
            LOGGER.info("System is SUSPENDED. Unlocking cronjobs is paused.");
        }
        catch(Exception ex)
        {
            LOGGER.warn("Error occurred while unlocking stale cronjobs", ex);
        }
    }


    protected void unlockCronJobsFromLocalInstance()
    {
        int maxCronJobsToCheck = getMaxCronJobsCountToBeUnlocked();
        CronJobManager.getInstance().getStrandedCronJobsRegistry().checkStrandedItems(maxCronJobsToCheck);
    }


    protected Collection<ClusterNodeInfo> getAllNodes()
    {
        return DefaultClusterNodeManagementService.getInstance().findAllNodes();
    }


    protected List<Integer> getNodeIds(Collection<ClusterNodeInfo> nodes)
    {
        return (List<Integer>)((Collection)Objects.<Collection>requireNonNull(nodes, "nodes can't be null"))
                        .stream()
                        .map(ClusterNodeInfo::getId)
                        .collect(Collectors.toList());
    }


    protected Duration getStaleNodeThresholdInterval()
    {
        long aLong = Config.getLong("cronjob.unlocker.stale.node.interval.ms", this.defaultStaleNodeIntervalMs);
        return Duration.ofMillis((aLong > 0L) ? aLong : this.defaultStaleNodeIntervalMs);
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected void unlockCronJobsForNodeIds(List<Integer> staleNodes)
    {
        unlockCronJobsForNodeIds(staleNodes, Set.of());
    }


    protected void unlockCronJobsForNodeIds(Collection<Integer> staleNodes, Collection<Integer> allNodes)
    {
        LOGGER.debug("Unlocking cronjobs for nodes {}", staleNodes);
        CronJobManager.getInstance()
                        .abortRunningCronJobsForClusterNodes(Set.copyOf(staleNodes), Set.copyOf(allNodes),
                                        getMaxCronJobsCountToBeUnlocked());
    }


    protected List<Integer> getStaleNodeIds(Collection<ClusterNodeInfo> allNodes, Predicate<ClusterNodeInfo> staleNodePredicate)
    {
        return (List<Integer>)((Collection<ClusterNodeInfo>)Objects.<Collection<ClusterNodeInfo>>requireNonNull(allNodes, "allNodes can't be null")).stream()
                        .filter(staleNodePredicate)
                        .map(ClusterNodeInfo::getId)
                        .collect(Collectors.toList());
    }


    @Deprecated(since = "2011", forRemoval = true)
    protected List<Integer> getStaleNodeIds(Predicate<ClusterNodeInfo> staleNodePredicate)
    {
        Collection<ClusterNodeInfo> allNodes = getAllNodes();
        return getStaleNodeIds(allNodes, staleNodePredicate);
    }


    protected int getMaxCronJobsCountToBeUnlocked()
    {
        return Config.getInt("cronjob.unlocker.cronJobs.unlockLimit", 200);
    }


    protected Instant getStaleNodeTsThreshold(Instant now, Duration staleThreshold)
    {
        return now.minus(staleThreshold);
    }


    protected Instant getStaleNodeTsCutoff(Instant staleNodeThreshold)
    {
        Instant staleNodePingTsCutoff;
        long cutoffInterval = Math.max(this.tenant.getConfig().getLong("cronjob.unlocker.stale.node.cutoff.interval.seconds", 600L), 0L);
        if(cutoffInterval > 0L)
        {
            staleNodePingTsCutoff = staleNodeThreshold.minus(cutoffInterval, ChronoUnit.SECONDS);
        }
        else
        {
            staleNodePingTsCutoff = Instant.EPOCH;
        }
        return staleNodePingTsCutoff;
    }


    protected Predicate<ClusterNodeInfo> getStaleNodePredicate(Instant staleNodePingTsThreshold, Instant staleNodePingTsCutoff)
    {
        return clusterNodeInfo -> {
            Instant instant = Instant.ofEpochMilli(clusterNodeInfo.getLastPingTS());
            return (instant.isBefore(staleNodePingTsThreshold) && instant.isAfter(staleNodePingTsCutoff));
        };
    }


    public void stopUpdatingAndFinish(long timeoutMillis)
    {
        this.stopped.set(true);
        try
        {
            join(timeoutMillis);
        }
        catch(InterruptedException e)
        {
            LOGGER.warn("Stopping the thread has been interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
