package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.task.impl.gateways.WorkerStateGateway;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AuxiliaryTablesBasedTaskProvider implements TasksProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuxiliaryTablesBasedTaskProvider.class);
    public static final int VERSION = 4;
    private AuxiliaryTablesSchedulerRole auxiliaryTablesSchedulerRole;
    private AuxiliaryTablesWorkerRole auxiliaryTablesWorkerRole;


    public List<TasksProvider.VersionPK> getTasksToSchedule(RuntimeConfigHolder runtimeConfigHolder, TaskEngineParameters taskEngineParameters, int maxItemsToSchedule)
    {
        int nodeId = taskEngineParameters.getClusterNodeID();
        WorkerStateGateway.WorkerState workerState = this.auxiliaryTablesWorkerRole.registerAsWorker(taskEngineParameters, runtimeConfigHolder);
        Collection<TasksProvider.VersionPK> conditions = this.auxiliaryTablesSchedulerRole.tryToPerformSchedulerJob(runtimeConfigHolder, taskEngineParameters, maxItemsToSchedule);
        List<TasksProvider.VersionPK> tasksToSchedule = new ArrayList<>(conditions);
        Optional<WorkerStateGateway.WorkerRange> workerRange = this.auxiliaryTablesWorkerRole.getWorkerRange(nodeId);
        if(workerRange.isPresent())
        {
            List<TasksProvider.VersionPK> tasksForWorker = this.auxiliaryTablesWorkerRole.getTasksForWorker(workerRange.get(), workerState, maxItemsToSchedule, runtimeConfigHolder);
            tasksToSchedule.addAll(tasksForWorker);
        }
        else
        {
            LOGGER.debug("{} (worker): skipping as node is not active", Integer.valueOf(taskEngineParameters.getClusterNodeID()));
        }
        return tasksToSchedule;
    }


    public int getMaxItemsToSchedule(DefaultTaskService.TaskEngineRunningState runningState, RuntimeConfigHolder runtimeConfigHolder)
    {
        int baseValue = super.getMaxItemsToSchedule(runningState, runtimeConfigHolder);
        int multiplier = ((Integer)runtimeConfigHolder.getProperty((RuntimeConfigHolder.TaskEngineProperty)Params.WORKER_TASKS_COUNT_MULTIPLIER)).intValue();
        return Math.max(baseValue, baseValue * multiplier);
    }


    public void afterTaskEngineStop(int nodeId, RuntimeConfigHolder runtimeConfigHolder)
    {
        try
        {
            this.auxiliaryTablesWorkerRole.deleteTasks(runtimeConfigHolder);
        }
        catch(Exception e)
        {
            LOGGER.warn("exception during deleting tasks from queue while stopping task engine", e);
        }
        try
        {
            this.auxiliaryTablesWorkerRole.unregisterAsWorker(nodeId);
        }
        catch(Exception e)
        {
            LOGGER.warn("exception during unregistering worker while stopping task engine", e);
        }
    }


    public void afterTaskFinished(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
        this.auxiliaryTablesWorkerRole.deleteTask(taskPk, runtimeConfigHolder);
    }


    public void afterTaskUnlocked(PK taskPk, RuntimeConfigHolder runtimeConfigHolder)
    {
        this.auxiliaryTablesWorkerRole.deleteTask(taskPk, true, runtimeConfigHolder);
    }


    @Required
    public void setAuxiliaryTablesWorkerRole(AuxiliaryTablesWorkerRole auxiliaryTablesWorkerRole)
    {
        this.auxiliaryTablesWorkerRole = auxiliaryTablesWorkerRole;
    }


    @Required
    public void setAuxiliaryTablesSchedulerRole(AuxiliaryTablesSchedulerRole auxiliaryTablesSchedulerRole)
    {
        this.auxiliaryTablesSchedulerRole = auxiliaryTablesSchedulerRole;
    }
}
