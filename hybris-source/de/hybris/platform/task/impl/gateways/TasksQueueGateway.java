package de.hybris.platform.task.impl.gateways;

import de.hybris.platform.core.PK;
import de.hybris.platform.task.impl.TasksProvider;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public interface TasksQueueGateway extends BaseGateway
{
    List<TasksProvider.VersionPK> getTasksForWorkerAndMarkForProcessing(WorkerStateGateway.WorkerRange paramWorkerRange, long paramLong, WorkerStateGateway.WorkerState paramWorkerState, Duration paramDuration);


    void clean(Duration paramDuration);


    long addTasks(String paramString1, String paramString2, Instant paramInstant, int paramInt1, int paramInt2);


    String defaultIfNull(String paramString, Integer paramInteger);


    String defaultIfNull(String paramString1, String paramString2);


    String getEmptyGroupValue();


    String getRangeSQLExpression(int paramInt1, int paramInt2);


    List<TasksCountResult> getTasksCount();


    List<TasksProvider.VersionPK> getConditionsToSchedule(String paramString, Instant paramInstant);


    String getUnlockTasksStatement();


    void deleteTasks(List<PK> paramList);


    void unlockTasksForWorkers(List<Integer> paramList);
}
