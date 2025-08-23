package de.hybris.platform.ruleengineservices.maintenance;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengine.concurrency.impl.DefaultTaskExecutionFuture;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleCompilerFuture extends DefaultTaskExecutionFuture
{
    private final List<List<RuleCompilerResult>> ruleCompilerResultsList;
    private final Long workerPreDestroyTimeout;


    public RuleCompilerFuture(Set<Thread> workers, Long workerPreDestroyTimeout)
    {
        super(workers);
        this.ruleCompilerResultsList = Lists.newArrayList();
        this.workerPreDestroyTimeout = workerPreDestroyTimeout;
    }


    public TaskResult getTaskResult()
    {
        waitForTasksToFinish();
        return (TaskResult)new RuleCompilerTaskResult(getRuleCompilerResults());
    }


    public long getWorkerPreDestroyTimeout()
    {
        return this.workerPreDestroyTimeout.longValue();
    }


    public void addRuleCompilerResults(List<RuleCompilerResult> ruleCompilerResults)
    {
        this.ruleCompilerResultsList.add(ruleCompilerResults);
    }


    protected List<RuleCompilerResult> getRuleCompilerResults()
    {
        return (List<RuleCompilerResult>)this.ruleCompilerResultsList.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }
}
