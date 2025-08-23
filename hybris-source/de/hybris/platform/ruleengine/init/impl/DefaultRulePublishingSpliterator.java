package de.hybris.platform.ruleengine.init.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import de.hybris.platform.ruleengine.concurrency.TaskContext;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.init.RulePublishingFuture;
import de.hybris.platform.ruleengine.init.RulePublishingSpliterator;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.RuleEngineUtils;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadFactory;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.assertj.core.util.Lists;
import org.drools.compiler.kie.builder.impl.ResultsImpl;
import org.fest.util.Collections;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRulePublishingSpliterator implements RulePublishingSpliterator
{
    private static final String BASE_WORKER_NAME = "RulePublisher";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRulePublishingSpliterator.class);
    private KieServices kieServices;
    private TaskContext taskContext;
    private EngineRuleDao engineRuleDao;
    private RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap;
    private SuspendResumeTaskManager suspendResumeTaskManager;


    public RulePublishingFuture publishRulesAsync(KieModuleModel kieModuleModel, ReleaseId containerReleaseId, List<String> ruleUuids, KIEModuleCacheBuilder cache)
    {
        List<KieBuilder> kieBuilders = new CopyOnWriteArrayList<>();
        List<List<String>> partitionOfRulesUuids = splitListByThreads(ruleUuids, getTaskContext().getNumberOfThreads());
        Set<Thread> builderWorkers = Sets.newHashSet();
        List<RuleEngineActionResult> ruleEngineActionResults = Lists.newCopyOnWriteArrayList();
        for(List<String> ruleUuidsChunk : partitionOfRulesUuids)
        {
            builderWorkers.add(createNewWorker(kieBuilders, kieModuleModel, containerReleaseId, ruleUuidsChunk, ruleEngineActionResults, cache));
        }
        startWorkers(builderWorkers);
        return new RulePublishingFuture(builderWorkers, ruleEngineActionResults, kieBuilders, getTaskContext().getThreadTimeout().longValue());
    }


    @PostConstruct
    protected void setUp()
    {
        this.kieServices = (KieServices)getRuleEngineBootstrap().getEngineServices();
    }


    public <T> List<List<T>> splitListByThreads(List<T> list, int numberOfThreads)
    {
        Preconditions.checkArgument((numberOfThreads > 0), "Valid maximum number of threads (>0) must be provided");
        int partitionSize = RuleEngineSpliteratorStrategy.getPartitionSize(list.size(), numberOfThreads);
        if(partitionSize == 0)
        {
            return Lists.emptyList();
        }
        return Lists.partition(list, partitionSize);
    }


    protected Thread createNewWorker(List<KieBuilder> kieBuilders, KieModuleModel kieModuleModel, ReleaseId releaseId, List<String> ruleUuids, List<RuleEngineActionResult> ruleEngineActionResults, KIEModuleCacheBuilder cache)
    {
        Tenant currentTenant = getTaskContext().getCurrentTenant();
        ThreadFactory tenantAwareThreadFactory = getTaskContext().getThreadFactory();
        Preconditions.checkArgument(Objects.nonNull(currentTenant), "Current Tenant must be provided as part of TaskContext");
        Preconditions.checkArgument(Objects.nonNull(tenantAwareThreadFactory), "ThreadFactory must be provided as part of TaskContext");
        return currentTenant.createAndRegisterBackgroundThread(() -> ruleEngineActionResults.add(addRulesBuilder(kieBuilders, kieModuleModel, releaseId, ruleUuids, cache)), tenantAwareThreadFactory);
    }


    protected void startWorkers(Set<Thread> workers)
    {
        if(CollectionUtils.isNotEmpty(workers))
        {
            for(Thread worker : workers)
            {
                worker.setName("RulePublisher-" + worker.getName());
                getSuspendResumeTaskManager().registerAsNonSuspendableTask(worker,
                                String.format("Rule engine rules publishing task %s is currently in progress", new Object[] {worker.getName()}));
                worker.start();
            }
        }
    }


    protected RuleEngineActionResult addRulesBuilder(List<KieBuilder> kieBuilders, KieModuleModel kieModuleModel, ReleaseId releaseId, List<String> ruleUuids, KIEModuleCacheBuilder cache)
    {
        Collection<DroolsRuleModel> droolRules = getEngineRuleDao().getRulesByUuids(ruleUuids);
        KieFileSystem partialKieFileSystem = getKieServices().newKieFileSystem();
        writeKModuleXML(kieModuleModel, partialKieFileSystem);
        writePomXML(releaseId, partialKieFileSystem);
        KieBuilder partialKieBuilder = getKieServices().newKieBuilder(partialKieFileSystem);
        for(DroolsRuleModel rule : droolRules)
        {
            if(Objects.nonNull(rule.getRuleContent()) && BooleanUtils.isTrue(rule.getActive()) && BooleanUtils.isTrue(rule.getCurrentVersion()))
            {
                cache.processRule((AbstractRuleEngineRuleModel)rule);
                try
                {
                    partialKieFileSystem.write(RuleEngineUtils.getRulePath(rule), rule.getRuleContent());
                }
                catch(Exception e)
                {
                    return createNewResult(createKieBuilderErrorResult(rule, e));
                }
            }
            if(Objects.isNull(rule.getRuleContent()))
            {
                LOGGER.warn("ignoring rule {}. No ruleContent set!", rule.getCode());
                continue;
            }
            if(BooleanUtils.isNotTrue(rule.getActive()) || BooleanUtils.isNotTrue(rule.getCurrentVersion()))
            {
                LOGGER.debug("ignoring rule {}. Rule is not active.", rule.getCode());
            }
        }
        partialKieBuilder.buildAll();
        kieBuilders.add(partialKieBuilder);
        return createNewResult(partialKieBuilder.getResults());
    }


    protected Results createKieBuilderErrorResult(DroolsRuleModel rule, Exception e)
    {
        ResultsImpl results = new ResultsImpl();
        results.addMessage(Message.Level.ERROR, rule.getCode(), "exception encountered during writing of kie file system:" + e
                        .getMessage());
        return (Results)results;
    }


    protected void writeKModuleXML(KieModuleModel module, KieFileSystem kfs)
    {
        kfs.writeKModuleXML(module.toXML());
    }


    public void writePomXML(ReleaseId releaseId, KieFileSystem kfs)
    {
        kfs.generateAndWritePomXML(releaseId);
    }


    protected RuleEngineActionResult createNewResult(Results results)
    {
        RuleEngineActionResult ruleEngineActionResult = new RuleEngineActionResult();
        for(Message message : results.getMessages())
        {
            LOGGER.error("{} {} {}", new Object[] {message.getLevel(), message.getText(), message.getPath()});
            ResultItem item = addNewResultItemOf(ruleEngineActionResult, RuleEngineKieModuleSwapper.convertLevel(message.getLevel()), message.getText());
            item.setLine(message.getLine());
            item.setPath(message.getPath());
        }
        if(results.hasMessages(new Message.Level[] {Message.Level.ERROR}))
        {
            ruleEngineActionResult.setActionFailed(true);
        }
        return ruleEngineActionResult;
    }


    protected ResultItem addNewResultItemOf(RuleEngineActionResult result, MessageLevel messageLevel, String message)
    {
        ResultItem resultItem = new ResultItem();
        resultItem.setLevel(messageLevel);
        resultItem.setMessage(message);
        if(Collections.isEmpty(result.getResults()))
        {
            result.setResults(Lists.newCopyOnWriteArrayList());
        }
        result.getResults().add(resultItem);
        return resultItem;
    }


    protected KieServices getKieServices()
    {
        return this.kieServices;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    protected TaskContext getTaskContext()
    {
        return this.taskContext;
    }


    @Required
    public void setTaskContext(TaskContext taskContext)
    {
        this.taskContext = taskContext;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> getRuleEngineBootstrap()
    {
        return this.ruleEngineBootstrap;
    }


    @Required
    public void setRuleEngineBootstrap(RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap)
    {
        this.ruleEngineBootstrap = ruleEngineBootstrap;
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
