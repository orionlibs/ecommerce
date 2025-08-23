package de.hybris.platform.ruleengine.concurrency.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.ruleengine.concurrency.RuleEngineTaskProcessor;
import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import de.hybris.platform.ruleengine.concurrency.TaskExecutionFuture;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineTaskProcessor<I extends ItemModel> implements RuleEngineTaskProcessor<I, TaskResult>, RuleEngineSpliteratorStrategy
{
    private Tenant tenant;
    private ThreadFactory tenantAwareThreadFactory;
    private SuspendResumeTaskManager suspendResumeTaskManager;


    public TaskExecutionFuture<TaskResult> execute(List<I> items, Consumer<List<I>> taskConsumer)
    {
        return execute(items, taskConsumer, -1L);
    }


    public TaskExecutionFuture<TaskResult> execute(List<I> items, Consumer<List<I>> taskConsumer, long predestroyTimeout)
    {
        List<List<I>> partitionedItems = Lists.partition(items, RuleEngineSpliteratorStrategy.getPartitionSize(items.size(), getNumberOfThreads()));
        Set<Thread> workers = Sets.newHashSet();
        for(List<I> itemsPartition : partitionedItems)
        {
            workers.add(createAndStartNewWorker(itemsPartition, taskConsumer));
        }
        return (TaskExecutionFuture<TaskResult>)new DefaultTaskExecutionFuture(workers, predestroyTimeout);
    }


    public int getNumberOfThreads()
    {
        return Runtime.getRuntime().availableProcessors() + 1;
    }


    protected Thread createAndStartNewWorker(List<I> items, Consumer<List<I>> taskConsumer)
    {
        Thread asyncWorker;
        try
        {
            asyncWorker = getTenant().createAndRegisterBackgroundThread(() -> taskConsumer.accept(items), getTenantAwareThreadFactory());
            getSuspendResumeTaskManager().registerAsNonSuspendableTask(asyncWorker, "Generic rule engine task is in progress");
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Exception caught: ", e);
        }
        asyncWorker.start();
        return asyncWorker;
    }


    protected Tenant getTenant()
    {
        return this.tenant;
    }


    @Required
    public void setTenant(Tenant tenant)
    {
        this.tenant = tenant;
    }


    protected ThreadFactory getTenantAwareThreadFactory()
    {
        return this.tenantAwareThreadFactory;
    }


    @Required
    public void setTenantAwareThreadFactory(ThreadFactory tenantAwareThreadFactory)
    {
        this.tenantAwareThreadFactory = tenantAwareThreadFactory;
    }


    protected SuspendResumeTaskManager getSuspendResumeTaskManager()
    {
        return this.suspendResumeTaskManager;
    }


    @Required
    public void setSuspendResumeTaskManager(SuspendResumeTaskManager suspendResumeTaskManager)
    {
        this.suspendResumeTaskManager = suspendResumeTaskManager;
    }
}
