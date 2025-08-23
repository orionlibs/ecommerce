package de.hybris.platform.ruleengine.concurrency;

import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public interface TaskExecutionFuture<T extends TaskResult>
{
    T getTaskResult();


    long getWorkerPreDestroyTimeout();


    void waitForTasksToFinish();


    default void waitForTasksToFinish(Set<Thread> workers)
    {
        if(CollectionUtils.isNotEmpty(workers))
        {
            workers.forEach(this::waitWhileWorkerIsRunning);
        }
    }


    default void waitWhileWorkerIsRunning(Thread worker)
    {
        if(Objects.nonNull(worker) && worker.isAlive())
        {
            try
            {
                worker.join(getWorkerPreDestroyTimeout());
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                throw new RuleEngineTaskExecutionException("Interrupted exception is caught during rules compilation and publishing:", e);
            }
        }
    }
}
