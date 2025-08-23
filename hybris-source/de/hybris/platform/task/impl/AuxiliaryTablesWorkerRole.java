package de.hybris.platform.task.impl;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import de.hybris.platform.core.PK;
import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.BadSqlGrammarException;

public class AuxiliaryTablesWorkerRole implements InitializingBean
{
    private static final String POOLING_TIME_METRIC = MetricRegistry.name("pooling.time", new String[0]);
    private static final Logger LOGGER = LoggerFactory.getLogger(AuxiliaryTablesWorkerRole.class);
    private AuxiliaryTablesGatewayFactory gatewayFactory;
    private MetricRegistry metricRegistry;
    private final IntervalChecker nextRegistration = new IntervalChecker(AuxiliaryTablesBasedTaskProvider.Params.WORKER_REGISTRATION_INTERVAL);
    private volatile List<PK> tasksToDelete = createEmptyList(200);
    private Timer pollingTimer;


    public WorkerStateGateway.WorkerState registerAsWorker(TaskEngineParameters taskEngineParameters, RuntimeConfigHolder runtimeConfigHolder)
    {
        Set<String> myGroups = new HashSet<>(taskEngineParameters.getClusterGroupsForThisNode());
        Integer nodeId = Integer.valueOf(taskEngineParameters.getClusterNodeID());
        Integer noNode = Integer.valueOf(-1);
        boolean isNodeExclusiveModeEnabled = (!noNode.equals(nodeId) && taskEngineParameters.isExclusiveMode());
        WorkerStateGateway.WorkerState workerState = new WorkerStateGateway.WorkerState(nodeId.intValue(), Duration.ofNanos(0L), isNodeExclusiveModeEnabled, myGroups);
        if(this.nextRegistration.checkAndUpdateIfTrue(runtimeConfigHolder))
        {
            this.gatewayFactory.getWorkerStateGateway().registerAsWorker(workerState);
            deleteTasks(runtimeConfigHolder);
        }
        return workerState;
    }


    public Optional<WorkerStateGateway.WorkerRange> getWorkerRange(int nodeID)
    {
        Optional<List<WorkerStateGateway.WorkerRange>> result = this.gatewayFactory.getWorkerStateGateway().getWorkerRangeById(nodeID);
        return result.filter(CollectionUtils::isNotEmpty).map(workerRanges -> (WorkerStateGateway.WorkerRange)workerRanges.get(0));
    }


    public List<TasksProvider.VersionPK> getTasksForWorker(WorkerStateGateway.WorkerRange workerRange, WorkerStateGateway.WorkerState workerState, long maxItemsToSchedule, RuntimeConfigHolder configHolder)
    {
        Timer.Context pollTimer = this.pollingTimer.time();
        Duration lockDuration = (Duration)configHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.TASKS_LOCK_DURATION);
        List<TasksProvider.VersionPK> tasks = this.gatewayFactory.getTasksQueueGateway().getTasksForWorkerAndMarkForProcessing(workerRange, maxItemsToSchedule, workerState, lockDuration);
        long time = pollTimer.stop();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("{} (worker): retrieved from queue {} tasks to process, it took {}", new Object[] {Integer.valueOf(workerState.getNodeId()),
                            Integer.valueOf(tasks.size()),
                            DurationFormatUtils.formatDurationHMS(TimeUnit.NANOSECONDS.toMillis(time))});
        }
        return tasks;
    }


    public void deleteTasks(RuntimeConfigHolder runtimeConfigHolder)
    {
        deleteTask(null, true, runtimeConfigHolder);
    }


    public void deleteTask(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
        deleteTask(Objects.<PK>requireNonNull(taskPk), false, runtimeConfigHolder);
    }


    public void deleteTask(PK taskPk, boolean forceDbDelete, RuntimeConfigHolder runtimeConfigHolder)
    {
        int maxBatchSize = Math.max(((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)AuxiliaryTablesBasedTaskProvider.Params.WORKER_DELETE_TASKS_MAX_BATCH_SIZE)).intValue(), 1);
        if(taskPk != null)
        {
            this.tasksToDelete.add(taskPk);
        }
        if(this.tasksToDelete.size() > maxBatchSize || forceDbDelete)
        {
            List<PK> localCopy = new ArrayList<>(1);
            synchronized(this)
            {
                if(!this.tasksToDelete.isEmpty())
                {
                    localCopy = new ArrayList<>(this.tasksToDelete);
                    this.tasksToDelete = createEmptyList((int)Math.round(maxBatchSize * 1.25D));
                }
            }
            try
            {
                this.gatewayFactory.getTasksQueueGateway().deleteTasks(localCopy);
                LOGGER.debug("successfully deleted {} tasks", Integer.valueOf(localCopy.size()));
            }
            catch(Exception ex)
            {
                this.tasksToDelete.addAll(localCopy);
                LOGGER.warn("error while deleting tasks", ex);
                if(forceDbDelete && taskPk != null)
                {
                    deleteSingleTask(taskPk);
                }
            }
        }
    }


    private void deleteSingleTask(PK taskPk)
    {
        try
        {
            LOGGER.debug("deleting single task {}", taskPk);
            this.gatewayFactory.getTasksQueueGateway().deleteTasks(List.of(taskPk));
        }
        catch(Exception e)
        {
            LOGGER.error("error while deleting single task {}", taskPk, e);
        }
    }


    private static List<PK> createEmptyList(int initialCapacity)
    {
        return Collections.synchronizedList(new ArrayList<>(initialCapacity));
    }


    public void unregisterAsWorker(int nodeId)
    {
        try
        {
            this.gatewayFactory.getWorkerStateGateway().deleteWorkers(Collections.singletonList(Integer.valueOf(nodeId)));
        }
        catch(BadSqlGrammarException ex)
        {
            LOGGER.warn("error while removing worker", (Throwable)ex);
        }
    }


    @Required
    public void setGatewayFactory(AuxiliaryTablesGatewayFactory gatewayFactory)
    {
        this.gatewayFactory = gatewayFactory;
    }


    @Required
    public void setMetricRegistry(MetricRegistry metricRegistry)
    {
        this.metricRegistry = metricRegistry;
    }


    public void afterPropertiesSet()
    {
        this.pollingTimer = this.metricRegistry.timer(RuntimeConfigHolder.metricName(POOLING_TIME_METRIC));
    }
}
