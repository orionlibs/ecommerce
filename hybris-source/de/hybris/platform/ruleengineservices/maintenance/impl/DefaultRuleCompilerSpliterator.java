package de.hybris.platform.ruleengineservices.maintenance.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerFuture;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerSpliterator;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRuleCompilerSpliterator<T extends SourceRuleModel> implements RuleCompilerSpliterator<T>
{
    private static final String BASE_WORKER_NAME = "RuleCompiler";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleCompilerSpliterator.class);
    private static final int DEFAULT_NUMBER_OF_THREADS = 1;
    private RuleCompilationContext ruleCompilationContext;


    private DefaultRuleCompilerSpliterator(RuleCompilationContext ruleCompilationContext)
    {
        this.ruleCompilationContext = ruleCompilationContext;
    }


    static <T extends SourceRuleModel> RuleCompilerSpliterator<T> withCompilationContext(@Nonnull RuleCompilationContext ruleCompilationContext)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleCompilationContext), "Valid RuleCompilationContext must be provided");
        return new DefaultRuleCompilerSpliterator<>(ruleCompilationContext);
    }


    public RuleCompilerResult compileSingleRule(T rule, String moduleName)
    {
        RuleCompilerService ruleCompilerService = this.ruleCompilationContext.getRuleCompilerService();
        Preconditions.checkArgument(Objects.nonNull(ruleCompilerService), "RuleCompilerService must be provided as part of RuleCompilationContext");
        this.ruleCompilationContext.resetRuleEngineRuleVersion(moduleName);
        return ruleCompilerService.compile(this.ruleCompilationContext, (AbstractRuleModel)rule, moduleName);
    }


    public RuleCompilerFuture compileRulesAsync(List<T> rules, String moduleName)
    {
        Set<Thread> workers = Sets.newHashSet();
        RuleCompilerFuture ruleCompilerFuture = createNewRuleCompilerFuture(workers);
        this.ruleCompilationContext.resetRuleEngineRuleVersion(moduleName);
        List<List<T>> splitRulesChunks = splitRules(rules);
        for(List<T> rulesChunk : splitRulesChunks)
        {
            List<RuleCompilerResult> ruleCompilerResults = Lists.newArrayList();
            ruleCompilerFuture.addRuleCompilerResults(ruleCompilerResults);
            workers.add(createNewWorker(rulesChunk, moduleName, ruleCompilerResults));
        }
        startWorkers(workers);
        return ruleCompilerFuture;
    }


    protected List<List<T>> splitRules(List<T> rules)
    {
        int numberOfThreads = this.ruleCompilationContext.getNumberOfThreads();
        if(numberOfThreads <= 0)
        {
            LOGGER.warn("Valid maximum number of threads (>0) must be provided as part of RuleCompilationContext. It was [{}]. The default value [{}] will be used",
                            Integer.valueOf(numberOfThreads), Integer.valueOf(1));
            numberOfThreads = 1;
        }
        int partitionSize = RuleEngineSpliteratorStrategy.getPartitionSize(rules.size(), numberOfThreads);
        if(partitionSize == 0)
        {
            return Lists.emptyList();
        }
        return Lists.partition(rules, partitionSize);
    }


    protected Thread createNewWorker(List<T> rules, String moduleName, List<RuleCompilerResult> ruleCompilerResults)
    {
        Tenant currentTenant = this.ruleCompilationContext.getCurrentTenant();
        ThreadFactory tenantAwareThreadFactory = this.ruleCompilationContext.getThreadFactory();
        RuleCompilerService ruleCompilerService = this.ruleCompilationContext.getRuleCompilerService();
        Preconditions.checkArgument(Objects.nonNull(currentTenant), "Current Tenant must be provided as part of RuleCompilationContext");
        Preconditions.checkArgument(Objects.nonNull(tenantAwareThreadFactory), "ThreadFactory must be provided as part of RuleCompilationContext");
        Preconditions.checkArgument(Objects.nonNull(ruleCompilerService), "RuleCompilerService must be provided as part of RuleCompilationContext");
        return currentTenant
                        .createAndRegisterBackgroundThread(JobProvider.getJob(this.ruleCompilationContext, rules, moduleName, ruleCompilerResults), tenantAwareThreadFactory);
    }


    protected void startWorkers(Set<Thread> workers)
    {
        if(CollectionUtils.isNotEmpty(workers))
        {
            for(Thread worker : workers)
            {
                String threadName = "RuleCompiler-" + worker.getName();
                worker.setName(threadName);
                this.ruleCompilationContext.getSuspendResumeTaskManager()
                                .registerAsNonSuspendableTask(worker,
                                                String.format("Rule engine rules compilation task %s is currently in progress", new Object[] {worker.getName()}));
                worker.start();
            }
        }
    }


    protected RuleCompilerFuture createNewRuleCompilerFuture(Set<Thread> workers)
    {
        Long preDestroyTimeout = this.ruleCompilationContext.getThreadTimeout();
        Preconditions.checkArgument((Objects.nonNull(preDestroyTimeout) && preDestroyTimeout.longValue() > 0L), "Valid pre-destroy timeout (>0) must be provided as part of RuleCompilationContext: [" + preDestroyTimeout + "]");
        return new RuleCompilerFuture(workers, preDestroyTimeout);
    }
}
