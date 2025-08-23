package de.hybris.platform.ruleengine.init;

import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengine.concurrency.impl.DefaultTaskExecutionFuture;
import java.util.List;
import java.util.Set;
import org.kie.api.builder.KieBuilder;

public class RulePublishingFuture extends DefaultTaskExecutionFuture
{
    private final List<RuleEngineActionResult> rulePublishingResults;
    private final List<KieBuilder> partialKieBuilders;
    private final long workerPreDestroyTimeout;


    public RulePublishingFuture(Set<Thread> workers, List<RuleEngineActionResult> rulePublishingResults, List<KieBuilder> partialKieBuilders, long workerPreDestroyTimeout)
    {
        super(workers);
        this.rulePublishingResults = rulePublishingResults;
        this.workerPreDestroyTimeout = workerPreDestroyTimeout;
        this.partialKieBuilders = partialKieBuilders;
    }


    public List<KieBuilder> getPartialKieBuilders()
    {
        return this.partialKieBuilders;
    }


    public TaskResult getTaskResult()
    {
        waitForTasksToFinish();
        return (TaskResult)new RuleDeploymentTaskResult(this.rulePublishingResults);
    }


    public long getWorkerPreDestroyTimeout()
    {
        return this.workerPreDestroyTimeout;
    }
}
