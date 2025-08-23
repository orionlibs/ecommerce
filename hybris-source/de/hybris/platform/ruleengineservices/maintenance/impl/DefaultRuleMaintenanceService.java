package de.hybris.platform.ruleengineservices.maintenance.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.concurrency.GuardStatus;
import de.hybris.platform.ruleengine.concurrency.GuardedSuspension;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.exception.RuleEngineRuntimeException;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.RulesModuleResolver;
import de.hybris.platform.ruleengine.util.EngineRulePreconditions;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.ruleengine.util.RuleMappings;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContextProvider;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerFuture;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerSpliterator;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerTaskResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleMaintenanceService;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.rule.strategies.RulePublishRestriction;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleMaintenanceService implements RuleMaintenanceService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleMaintenanceService.class);
    private ModelService modelService;
    private RuleEngineService ruleEngineService;
    private RuleService ruleService;
    private RulesModuleResolver rulesModuleResolver;
    private RulesModuleDao rulesModuleDao;
    private EngineRuleDao engineRuleDao;
    private RuleCompilationContextProvider ruleCompilationContextProvider;
    private GuardedSuspension<String> rulesCompilationGuardedSuspension;
    private EngineRulesRepository engineRulesRepository;
    private EventService eventService;
    private RulePublishRestriction rulePublishRestriction;


    public <T extends SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRulesWithBlocking(List<T> rules, String moduleName, boolean enableIncrementalUpdate)
    {
        return compileAndPublishRules(rules, moduleName, enableIncrementalUpdate, true);
    }


    public <T extends SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRulesWithBlocking(List<T> rules, String moduleName, boolean enableIncrementalUpdate, CronJobProgressTracker cronJobProgressTracker)
    {
        return compileAndPublishRules(rules, moduleName, enableIncrementalUpdate, cronJobProgressTracker, true);
    }


    public <T extends SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRules(List<T> rules, String moduleName, boolean enableIncrementalUpdate)
    {
        return compileAndPublishRules(rules, moduleName, enableIncrementalUpdate, false);
    }


    public RuleCompilerPublisherResult initializeModule(String moduleName, boolean enableIncrementalUpdate)
    {
        DroolsKIEModuleModel module = getRulesModuleByName(moduleName);
        InitializationFuture initializationFuture = getRuleEngineService().initialize(Lists.newArrayList((Object[])new AbstractRulesModuleModel[] {(AbstractRulesModuleModel)module}, ), true, enableIncrementalUpdate);
        List<RuleEngineActionResult> ruleEngineActionResults = initializationFuture.waitForInitializationToFinish().getResults();
        RuleCompilerPublisherResult.Result result = ruleEngineActionResults.stream().filter(RuleEngineActionResult::isActionFailed).findAny().map(r -> RuleCompilerPublisherResult.Result.PUBLISHER_ERROR).orElse(RuleCompilerPublisherResult.Result.SUCCESS);
        return (RuleCompilerPublisherResult)new DefaultRuleCompilerPublisherResult(result, null, ruleEngineActionResults);
    }


    public RuleCompilerPublisherResult initializeAllModules(boolean enableIncrementalUpdate)
    {
        List<RuleEngineActionResult> ruleEngineActionResults = getRuleEngineService().initializeAllRulesModules();
        RuleCompilerPublisherResult.Result result = ruleEngineActionResults.stream().filter(RuleEngineActionResult::isActionFailed).findAny().map(r -> RuleCompilerPublisherResult.Result.PUBLISHER_ERROR).orElse(RuleCompilerPublisherResult.Result.SUCCESS);
        return (RuleCompilerPublisherResult)new DefaultRuleCompilerPublisherResult(result, null, ruleEngineActionResults);
    }


    public <T extends SourceRuleModel> Optional<RuleCompilerPublisherResult> undeployRules(List<T> rules, String moduleName)
    {
        if(CollectionUtils.isNotEmpty(rules))
        {
            Objects.requireNonNull(DroolsRuleModel.class);
            Set<DroolsRuleModel> engineRules = (Set<DroolsRuleModel>)rules.stream().filter(s -> CollectionUtils.isNotEmpty(s.getEngineRules())).flatMap(s -> s.getEngineRules().stream()).filter(r -> getEngineRulesRepository().checkEngineRuleDeployedForModule(r, moduleName))
                            .map(DroolsRuleModel.class::cast).collect(Collectors.toSet());
            EngineRulePreconditions.checkRulesHaveSameType(engineRules);
            if(CollectionUtils.isNotEmpty(engineRules))
            {
                RuleCompilerPublisherResult.Result result = RuleCompilerPublisherResult.Result.SUCCESS;
                Optional<InitializationFuture> initializationFuture = getRuleEngineService().archiveRules(engineRules);
                if(initializationFuture.isPresent())
                {
                    List<RuleEngineActionResult> results = ((InitializationFuture)initializationFuture.get()).waitForInitializationToFinish().getResults();
                    result = results.stream().filter(RuleEngineActionResult::isActionFailed).findAny().map(r -> RuleCompilerPublisherResult.Result.PUBLISHER_ERROR).orElse(result);
                    return (Optional)Optional.of(new DefaultRuleCompilerPublisherResult(result, Collections.emptyList(), results));
                }
            }
        }
        return Optional.empty();
    }


    public Optional<RuleCompilerPublisherResult> synchronizeModules(String sourceModuleName, String targetModuleName)
    {
        Preconditions.checkArgument(Objects.nonNull(sourceModuleName), "The source module name should be provided");
        Preconditions.checkArgument(Objects.nonNull(targetModuleName), "The target module name should be provided");
        DroolsKIEModuleModel sourceModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(sourceModuleName);
        DroolsKIEModuleModel targetModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(targetModuleName);
        if(!sourceModule.getRuleType().equals(targetModule.getRuleType()))
        {
            throw new RuleEngineRuntimeException(String.format("Cannot synchronize modules with different rule types [%s -> %s]", new Object[] {sourceModule
                            .getRuleType(), targetModule.getRuleType()}));
        }
        Map<? extends SourceRuleModel, DroolsRuleModel> deployedRulesForSourceModule = getDeployedRules(sourceModule);
        Map<? extends SourceRuleModel, DroolsRuleModel> deployedRulesForTargetModule = getDeployedRules(targetModule);
        Map<String, ? extends SourceRuleModel> sourceRulesDeployedForSourceModule = getSourceRulesByName(deployedRulesForSourceModule
                        .keySet());
        Map<String, ? extends SourceRuleModel> sourceRulesDeployedForTargetModule = getSourceRulesByName(deployedRulesForTargetModule
                        .keySet());
        List<? extends SourceRuleModel> rulesToDeploy = Collections.emptyList();
        List<AbstractRuleModel> rulesToRemove = Collections.emptyList();
        if(MapUtils.isNotEmpty(sourceRulesDeployedForTargetModule))
        {
            if(MapUtils.isNotEmpty(sourceRulesDeployedForSourceModule))
            {
                rulesToDeploy = (List<? extends SourceRuleModel>)sourceRulesDeployedForSourceModule.values().stream().filter(r -> !isSourceRuleDeployed(sourceRulesDeployedForTargetModule, r)).collect(Collectors.toList());
                rulesToRemove = (List<AbstractRuleModel>)sourceRulesDeployedForTargetModule.values().stream().filter(r -> !isSourceRuleDeployed(sourceRulesDeployedForSourceModule, r)).collect(Collectors.toList());
            }
            else
            {
                rulesToRemove = new ArrayList<>((Collection)sourceRulesDeployedForTargetModule.values());
            }
        }
        else if(MapUtils.isNotEmpty(sourceRulesDeployedForSourceModule))
        {
            rulesToDeploy = new ArrayList<>(sourceRulesDeployedForSourceModule.values());
        }
        if(CollectionUtils.isNotEmpty(rulesToDeploy) || CollectionUtils.isNotEmpty(rulesToRemove))
        {
            Objects.requireNonNull(deployedRulesForTargetModule);
            getRuleEngineService().deactivateRulesModuleEngineRules(targetModuleName, (Collection)rulesToRemove.stream().map(deployedRulesForTargetModule::get).collect(Collectors.toList()));
            return Optional.ofNullable(compileAndPublishRules(rulesToDeploy, targetModule.getName(), true, false));
        }
        return Optional.empty();
    }


    protected <S extends SourceRuleModel> boolean isSourceRuleDeployed(Map<String, S> deployedRulesMap, SourceRuleModel sourceRule)
    {
        return (deployedRulesMap.containsKey(sourceRule.getCode()) && ((SourceRuleModel)deployedRulesMap
                        .get(sourceRule.getCode())).getVersion().equals(sourceRule.getVersion()));
    }


    protected <S extends SourceRuleModel> Map<String, S> getSourceRulesByName(Collection<S> sourceRules)
    {
        return (Map<String, S>)sourceRules.stream().collect(Collectors.toConcurrentMap(AbstractRuleModel::getCode, Function.identity()));
    }


    protected <S extends SourceRuleModel> Map<S, DroolsRuleModel> getDeployedRules(DroolsKIEModuleModel module)
    {
        Map<S, List<DroolsRuleModel>> deployedRules = (Map<S, List<DroolsRuleModel>>)module.getKieBases().stream().filter(b -> CollectionUtils.isNotEmpty(b.getRules())).flatMap(b -> b.getRules().stream()).parallel()
                        .filter(r -> getEngineRulesRepository().checkEngineRuleDeployedForModule((AbstractRuleEngineRuleModel)r, RuleMappings.moduleName(r))).collect(Collectors.groupingBy(r -> (SourceRuleModel)r.getSourceRule()));
        return (Map<S, DroolsRuleModel>)deployedRules.entrySet().stream().parallel().filter(deployedRule -> CollectionUtils.isNotEmpty((Collection)deployedRule.getValue()))
                        .collect(Collectors.toConcurrentMap(Map.Entry::getKey, deployedRule -> (DroolsRuleModel)((List<DroolsRuleModel>)deployedRule.getValue()).iterator().next()));
    }


    protected <T extends SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRules(List<T> rules, String moduleName, boolean enableIncrementalUpdate, boolean blocking)
    {
        return compileAndPublishRules(rules, moduleName, enableIncrementalUpdate, null, blocking);
    }


    protected <T extends SourceRuleModel> RuleCompilerPublisherResult compileAndPublishRules(List<T> rules, String moduleName, boolean enableIncrementalUpdate, CronJobProgressTracker cronJobProgressTracker, boolean blocking)
    {
        ServicesUtil.validateParameterNotNull(rules, "Rules list cannot be null");
        if(!getRulePublishRestriction().isAllowedToPublish(moduleName, rules.size()))
        {
            throw new RuleEngineServiceException("Can not publish promotion rules as the active promotions limit is reached");
        }
        DroolsKIEModuleModel module = getRulesModuleByName(moduleName);
        RuleCompilerTaskResult ruleCompilerTaskResult = compileRules(rules, moduleName);
        RuleCompilerPublisherResult.Result result = ruleCompilerTaskResult.getState().equals(TaskResult.State.SUCCESS) ? RuleCompilerPublisherResult.Result.SUCCESS : RuleCompilerPublisherResult.Result.COMPILER_ERROR;
        List<RuleEngineActionResult> publisherResults = null;
        if(!RuleCompilerPublisherResult.Result.COMPILER_ERROR.equals(result))
        {
            updateCronJobTracker(cronJobProgressTracker, rules.size(), rules.size() / 2);
            InitializationFuture initializationFuture = getRuleEngineService().initialize(Collections.singletonList(module), true, enableIncrementalUpdate,
                            createExecutionContext(ruleCompilerTaskResult));
            if(blocking)
            {
                initializationFuture.waitForInitializationToFinish();
                updateCronJobTracker(cronJobProgressTracker, rules.size(), rules.size());
            }
            publisherResults = initializationFuture.getResults();
            result = publisherResults.stream().filter(RuleEngineActionResult::isActionFailed).findAny().map(r -> RuleCompilerPublisherResult.Result.PUBLISHER_ERROR).orElse(result);
        }
        else
        {
            LOG.error("The rule compilation finished with errors");
            logCompilerErrors(ruleCompilerTaskResult);
            onCompileErrorCleanup(ruleCompilerTaskResult, moduleName);
            updateCronJobTracker(cronJobProgressTracker, rules.size(), rules.size());
        }
        return (RuleCompilerPublisherResult)new DefaultRuleCompilerPublisherResult(result, ruleCompilerTaskResult.getRuleCompilerResults(), publisherResults);
    }


    protected void onCompileErrorCleanup(RuleCompilerTaskResult result, String moduleName)
    {
        Set<AbstractRuleEngineRuleModel> engineRules = (Set<AbstractRuleEngineRuleModel>)result.getRuleCompilerResults().stream().filter(r -> RuleCompilerResult.Result.SUCCESS.equals(r.getResult()))
                        .map(r -> getEngineRuleDao().getActiveRuleByCodeAndMaxVersion(r.getRuleCode(), moduleName, r.getRuleVersion())).filter(Objects::nonNull).collect(Collectors.toSet());
        getModelService().removeAll(engineRules);
    }


    protected ExecutionContext createExecutionContext(RuleCompilerTaskResult ruleCompilerTaskResult)
    {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.setRuleVersions((Map)ruleCompilerTaskResult.getRuleCompilerResults().stream()
                        .collect(Collectors.toMap(RuleCompilerResult::getRuleCode, RuleCompilerResult::getRuleVersion)));
        return executionContext;
    }


    protected void updateCronJobTracker(CronJobProgressTracker tracker, int itemsTotal, int itemsProcessed)
    {
        if(Objects.nonNull(tracker))
        {
            if(itemsTotal > 0 && itemsProcessed >= 0)
            {
                tracker.setProgress(Double.valueOf(100.0D * itemsProcessed / itemsTotal));
            }
            else if(itemsTotal == 0)
            {
                tracker.setProgress(Double.valueOf(100.0D));
            }
        }
    }


    protected <T extends SourceRuleModel> RuleCompilerTaskResult compileRules(List<T> rules, String moduleName)
    {
        RuleCompilerTaskResult ruleCompilerTaskResult;
        ServicesUtil.validateParameterNotNull(rules, "Rules list cannot be null");
        GuardStatus guardStatus = getRulesCompilationGuardedSuspension().checkPreconditions(moduleName);
        if(GuardStatus.Type.NO_GO.equals(guardStatus.getType()))
        {
            throw new IllegalStateException("The compilation of rules is currently in progress for rules module [" + moduleName + "]");
        }
        RuleCompilationContext ruleCompilationContext = null;
        try
        {
            ruleCompilationContext = getRuleCompilationContextProvider().getRuleCompilationContext();
            ruleCompilationContext.registerCompilationListeners(moduleName);
            RuleCompilerSpliterator<T> ruleCompilerSpliterator = DefaultRuleCompilerSpliterator.withCompilationContext(ruleCompilationContext);
            RuleCompilerFuture ruleCompilerFuture = ruleCompilerSpliterator.compileRulesAsync(rules, moduleName);
            ruleCompilerTaskResult = (RuleCompilerTaskResult)ruleCompilerFuture.getTaskResult();
        }
        finally
        {
            if(Objects.nonNull(ruleCompilationContext))
            {
                ruleCompilationContext.cleanup(moduleName);
            }
        }
        return ruleCompilerTaskResult;
    }


    protected DroolsKIEModuleModel getRulesModuleByName(String moduleName)
    {
        try
        {
            AbstractRulesModuleModel module = getRulesModuleDao().findByName(moduleName);
            if(!(module instanceof DroolsKIEModuleModel))
            {
                throw new RuleEngineServiceException("No DroolsKIEModuleModel instance was found for the name [" + moduleName + "]");
            }
            return (DroolsKIEModuleModel)module;
        }
        catch(ModelNotFoundException e)
        {
            throw new RuleEngineServiceException("No RulesModuleModel was found for the name [" + moduleName + "]");
        }
    }


    protected void logCompilerErrors(RuleCompilerTaskResult ruleCompilerTaskResult)
    {
        if(LOG.isDebugEnabled() && CollectionUtils.isNotEmpty(ruleCompilerTaskResult.getRuleCompilerResults()))
        {
            LOG.debug("rule compilation errors:");
            for(RuleCompilerResult rcr : ruleCompilerTaskResult.getRuleCompilerResults())
            {
                if(CollectionUtils.isEmpty(rcr.getProblems()))
                {
                    continue;
                }
                LOG.debug("rule[version]: result  -  {}[{}]: {}", new Object[] {rcr.getRuleCode(), Long.valueOf(rcr.getRuleVersion()), rcr.getResult()});
                List<RuleCompilerProblem> problems = (List<RuleCompilerProblem>)rcr.getProblems().stream().filter(Objects::nonNull).collect(Collectors.toList());
                for(RuleCompilerProblem rcp : problems)
                {
                    LOG.debug("              {} {}", rcp.getSeverity(), rcp.getMessage());
                }
            }
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RuleEngineService getRuleEngineService()
    {
        return this.ruleEngineService;
    }


    @Required
    public void setRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.ruleEngineService = ruleEngineService;
    }


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
    }


    protected RuleService getRuleService()
    {
        return this.ruleService;
    }


    @Required
    public void setRuleService(RuleService ruleService)
    {
        this.ruleService = ruleService;
    }


    protected RuleCompilationContextProvider getRuleCompilationContextProvider()
    {
        return this.ruleCompilationContextProvider;
    }


    @Required
    public void setRuleCompilationContextProvider(RuleCompilationContextProvider ruleCompilationContextProvider)
    {
        this.ruleCompilationContextProvider = ruleCompilationContextProvider;
    }


    protected RulesModuleResolver getRulesModuleResolver()
    {
        return this.rulesModuleResolver;
    }


    @Required
    public void setRulesModuleResolver(RulesModuleResolver rulesModuleResolver)
    {
        this.rulesModuleResolver = rulesModuleResolver;
    }


    protected GuardedSuspension<String> getRulesCompilationGuardedSuspension()
    {
        return this.rulesCompilationGuardedSuspension;
    }


    @Required
    public void setRulesCompilationGuardedSuspension(GuardedSuspension<String> rulesCompilationGuardedSuspension)
    {
        this.rulesCompilationGuardedSuspension = rulesCompilationGuardedSuspension;
    }


    protected EngineRulesRepository getEngineRulesRepository()
    {
        return this.engineRulesRepository;
    }


    @Required
    public void setEngineRulesRepository(EngineRulesRepository engineRulesRepository)
    {
        this.engineRulesRepository = engineRulesRepository;
    }


    protected EventService getEventService()
    {
        return this.eventService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    public void setRulePublishRestriction(RulePublishRestriction rulePublishRestriction)
    {
        this.rulePublishRestriction = rulePublishRestriction;
    }


    protected RulePublishRestriction getRulePublishRestriction()
    {
        return this.rulePublishRestriction;
    }
}
