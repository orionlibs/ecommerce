package de.hybris.platform.ruleengine.concurrency.impl;

import de.hybris.platform.ruleengine.concurrency.TaskExecutionFuture;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import java.util.Set;

public class DefaultTaskExecutionFuture implements TaskExecutionFuture<TaskResult>
{
    private static final long DEFAULT_PRE_DESTROY_TOUT = 1000L;
    private Set<Thread> workers;
    private long predestroyTimeout;


    public DefaultTaskExecutionFuture(Set<Thread> workers)
    {
        this(workers, -1L);
    }


    public DefaultTaskExecutionFuture(Set<Thread> workers, long predestroyTimeout)
    {
        this.workers = workers;
        this.predestroyTimeout = predestroyTimeout;
    }


    public TaskResult getTaskResult()
    {
        if(this.workers.stream().anyMatch(Thread::isAlive))
        {
            return () -> TaskResult.State.IN_PROGRESS;
        }
        return () -> TaskResult.State.SUCCESS;
    }


    public long getWorkerPreDestroyTimeout()
    {
        if(this.predestroyTimeout == -1L)
        {
            return 1000L;
        }
        return this.predestroyTimeout;
    }


    public void waitForTasksToFinish()
    {
        waitForTasksToFinish(this.workers);
    }
}
