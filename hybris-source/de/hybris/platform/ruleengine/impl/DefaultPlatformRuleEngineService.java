package de.hybris.platform.ruleengine.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleEngineCacheService;
import de.hybris.platform.ruleengine.concurrency.RuleEngineTaskProcessor;
import de.hybris.platform.ruleengine.concurrency.TaskExecutionFuture;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.drools.KieSessionHelper;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.event.KieModuleSwappingEvent;
import de.hybris.platform.ruleengine.event.RuleEngineInitializedEvent;
import de.hybris.platform.ruleengine.exception.DroolsInitializationException;
import de.hybris.platform.ruleengine.init.ConcurrentMapFactory;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.init.MultiFlag;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.init.RuleEngineContainerRegistry;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.init.tasks.PostRulesModuleSwappingTasksProvider;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.strategies.DroolsKIEBaseFinderStrategy;
import de.hybris.platform.ruleengine.util.EngineRulePreconditions;
import de.hybris.platform.ruleengine.util.EngineRulesRepository;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.appformer.maven.support.AFReleaseId;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.support.RetryTemplate;

public class DefaultPlatformRuleEngineService implements RuleEngineService
{
    public static final String MODULE_MVN_VERSION_NONE = "NONE";
    public static final String SWAPPING_IS_BLOCKING = "ruleengine.kiemodule.swapping.blocking";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlatformRuleEngineService.class);
    private static final String DROOLS_INITEXCEPTION_MESSAGE = "Kie container swapping is in progress, no rules updates are possible at this time";
    private EventService eventService;
    private ConfigurationService configurationService;
    private ModelService modelService;
    private DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy;
    private EngineRuleDao engineRuleDao;
    private RulesModuleDao rulesModuleDao;
    private RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper;
    private ConcurrentMapFactory concurrentMapFactory;
    private RuleEngineCacheService ruleEngineCacheService;
    private KieSessionHelper kieSessionHelper;
    private RuleEngineTaskProcessor<AbstractRuleEngineRuleModel, TaskResult> engineRulesPersistingTaskProcessor;
    private RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap;
    private RuleEngineContainerRegistry<ReleaseId, KieContainer> ruleEngineContainerRegistry;
    private EngineRulesRepository engineRulesRepository;
    private MultiFlag initialisationMultiFlag;
    private PostRulesModuleSwappingTasksProvider postRulesModuleSwappingTasksProvider;
    private Function<RuleEvaluationContext, Integer> maxRuleExecutionsFunction;
    private RetryTemplate ruleEnginePublishRetryTemplate;


    public RuleEvaluationResult evaluate(RuleEvaluationContext context)
    {
        if(LOGGER.isDebugEnabled() && Objects.nonNull(context.getFacts()))
        {
            LOGGER.debug("Rule evaluation triggered with the facts: {}", context.getFacts());
        }
        if(!isRuleEngineActive())
        {
            LOGGER.info("Ruleengine disabled, skipping rule evaluation");
            RuleEvaluationResult ruleEvaluationResult = new RuleEvaluationResult();
            ruleEvaluationResult.setEvaluationFailed(true);
            return ruleEvaluationResult;
        }
        RuleEvaluationResult result = new RuleEvaluationResult();
        KieContainer kContainer = null;
        try
        {
            getRuleEngineContainerRegistry().lockReadingRegistry();
            ReleaseId deployedReleaseId = getKieSessionHelper().getDeployedKieModuleReleaseId(context);
            kContainer = (KieContainer)getRuleEngineContainerRegistry().getActiveContainer(deployedReleaseId);
            int maxRetries = 10;
            int attempt = 0;
            while(Objects.isNull(kContainer) && 10 > attempt++)
            {
                LOGGER.warn("KieContainer with releaseId [{}] was not found. Trying to look up for closest matching version...", deployedReleaseId);
                ReleaseId tryDeployedReleaseId = getRuleEngineContainerRegistry().lookupForDeployedRelease(new String[] {deployedReleaseId.getGroupId(), deployedReleaseId.getArtifactId()}).orElse(null);
                if(tryDeployedReleaseId == null)
                {
                    LOGGER.error("Cannot complete the evaluation: rule engine was not initialized for releaseId [{}]", deployedReleaseId);
                    result.setEvaluationFailed(true);
                    result.setErrorMessage("Cannot complete the evaluation: rule engine was not initialized for releaseId " + deployedReleaseId);
                    return result;
                }
                LOGGER.info("Found KieContainer with releaseId [{}]", tryDeployedReleaseId);
                kContainer = (KieContainer)getRuleEngineContainerRegistry().getActiveContainer(tryDeployedReleaseId);
            }
        }
        finally
        {
            getRuleEngineContainerRegistry().unlockReadingRegistry();
        }
        AgendaFilter agendaFilter = (context.getFilter() instanceof AgendaFilter) ? (AgendaFilter)context.getFilter() : null;
        getRuleEngineCacheService().provideCachedEntities(context);
        List<Command> commands = Lists.newArrayList();
        commands.add(CommandFactory.newInsertElements(context.getFacts()));
        FireAllRulesCommand fireAllRulesCommand = Objects.nonNull(agendaFilter) ? new FireAllRulesCommand(agendaFilter) : new FireAllRulesCommand();
        fireAllRulesCommand.setMax(((Integer)getMaxRuleExecutionsFunction().apply(context)).intValue());
        LOGGER.debug("Adding command [{}]", fireAllRulesCommand);
        commands.add(fireAllRulesCommand);
        BatchExecutionCommand command = CommandFactory.newBatchExecution(commands);
        DroolsRuleEngineContextModel ruleEngineContext = (DroolsRuleEngineContextModel)context.getRuleEngineContext();
        DroolsSessionType sessionType = ruleEngineContext.getKieSession().getSessionType();
        Supplier<ExecutionResults> executionResultsSupplier = DroolsSessionType.STATEFUL.equals(sessionType) ? executionResultsSupplierWithStatefulSession(kContainer, command, context) : executionResultsSupplierWithStatelessSession(kContainer, command, context);
        result.setExecutionResult(executionResultsSupplier.get());
        return result;
    }


    protected Supplier<ExecutionResults> executionResultsSupplierWithStatefulSession(KieContainer kContainer, BatchExecutionCommand command, RuleEvaluationContext context)
    {
        return () -> {
            ExecutionResults executionResults;
            KieSession kieSession = (KieSession)getKieSessionHelper().initializeSession(KieSession.class, context, kContainer);
            LOGGER.debug("Executing KieSession.execute for releaseId [{}]", kContainer.getReleaseId());
            try
            {
                executionResults = (ExecutionResults)kieSession.execute((Command)command);
            }
            finally
            {
                ImmutableList.builder().addAll(kieSession.getFactHandles()).build().forEach(());
                LOGGER.debug("Disposing the session: {}", kieSession);
                kieSession.dispose();
            }
            return executionResults;
        };
    }


    protected Supplier<ExecutionResults> executionResultsSupplierWithStatelessSession(KieContainer kContainer, BatchExecutionCommand command, RuleEvaluationContext context)
    {
        return () -> {
            StatelessKieSession statelessKieSession = (StatelessKieSession)getKieSessionHelper().initializeSession(StatelessKieSession.class, context, kContainer);
            LOGGER.debug("Executing StatelessKieSession.execute for releaseId [{}]", kContainer.getReleaseId());
            return (ExecutionResults)statelessKieSession.execute((Command)command);
        };
    }


    public InitializationFuture initialize(List<AbstractRulesModuleModel> modules, boolean propagateToOtherNodes, boolean enableIncrementalUpdate)
    {
        return initialize(modules, propagateToOtherNodes, enableIncrementalUpdate, new ExecutionContext());
    }


    public InitializationFuture initialize(List<AbstractRulesModuleModel> modules, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, ExecutionContext executionContext)
    {
        Preconditions.checkArgument(Objects.nonNull(modules), "The provided modules list cannot be NULL here");
        InitializationFuture initializationFuture = InitializationFuture.of(this.ruleEngineKieModuleSwapper);
        for(AbstractRulesModuleModel module : modules)
        {
            RuleEngineActionResult result = new RuleEngineActionResult();
            result.setExecutionContext(executionContext);
            initialize(module, null, propagateToOtherNodes, enableIncrementalUpdate, result);
            initializationFuture.getResults().add(result);
        }
        return initializationFuture;
    }


    public void initializeNonBlocking(AbstractRulesModuleModel abstractModule, String deployedMvnVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        if(isRuleEngineActive())
        {
            Preconditions.checkArgument(Objects.nonNull(abstractModule), "module must not be null");
            Preconditions.checkState(abstractModule instanceof DroolsKIEModuleModel, "module %s is not a DroolsKIEModule. %s is not supported.", abstractModule
                            .getName(), abstractModule.getItemtype());
            LOGGER.debug("Drools Engine Service initialization for '{}' module triggered...", abstractModule.getName());
            DroolsKIEModuleModel moduleModel = (DroolsKIEModuleModel)abstractModule;
            Optional<ReleaseId> oldDeployedReleaseId = getRuleEngineKieModuleSwapper().getDeployedReleaseId(moduleModel, deployedMvnVersion);
            String oldVersion = oldDeployedReleaseId.<String>map(AFReleaseId::getVersion).orElse("NONE");
            result.setOldVersion(oldVersion);
            switchKieModule(moduleModel, (KieContainerListener)new Object(this, result, moduleModel, deployedMvnVersion, propagateToOtherNodes, oldDeployedReleaseId, oldVersion), propagateToOtherNodes, enableIncrementalUpdate, result,
                            getPostRulesModuleSwappingTasksProvider().getTasks(result));
        }
        else
        {
            populateRuleEngineActionResult(result,
                            String.format("cannot activate rules module as the rule engine is not active. To activate it set the property %s to true.", new Object[] {"ruleengine.engine.active"}), (abstractModule != null) ? abstractModule.getName() : null, false, MessageLevel.WARNING);
        }
    }


    public void initialize(AbstractRulesModuleModel abstractModule, String deployedMvnVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        initialize(abstractModule, deployedMvnVersion, propagateToOtherNodes, enableIncrementalUpdate, result, null);
    }


    public void initialize(AbstractRulesModuleModel abstractModule, String deployedMvnVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result, ExecutionContext executionContext)
    {
        if(Objects.nonNull(executionContext))
        {
            result.setExecutionContext(executionContext);
        }
        initializeNonBlocking(abstractModule, deployedMvnVersion, propagateToOtherNodes, enableIncrementalUpdate, result);
        if(isBlocking())
        {
            getRuleEngineKieModuleSwapper().waitForSwappingToFinish();
        }
    }


    public <T extends AbstractRuleEngineRuleModel> void deactivateRulesModuleEngineRules(String moduleName, Collection<T> engineRules)
    {
        if(CollectionUtils.isNotEmpty(engineRules))
        {
            List<AbstractRuleEngineRuleModel> engineRulesForModule = (List<AbstractRuleEngineRuleModel>)engineRules.stream().filter(r -> getEngineRulesRepository().checkEngineRuleDeployedForModule(r, moduleName)).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(engineRulesForModule))
            {
                AbstractRulesModuleModel moduleModel = getRulesModuleDao().findByName(moduleName);
                LOGGER.debug("Resetting the module version to [{}]", moduleModel.getVersion());
                AtomicLong initVal = new AtomicLong(moduleModel.getVersion().longValue() + 1L);
                TaskExecutionFuture<TaskResult> taskExecutionFuture = getEngineRulesPersistingTaskProcessor().execute(engineRulesForModule, listPartition -> {
                    Objects.requireNonNull(getModelService());
                    listPartition.forEach(getModelService()::refresh);
                    listPartition.forEach(());
                    Objects.requireNonNull(getModelService());
                    listPartition.forEach(getModelService()::save);
                });
                taskExecutionFuture.waitForTasksToFinish();
            }
        }
    }


    protected void doSwapKieContainers(KieContainer kieContainer, KIEModuleCacheBuilder cache, RuleEngineActionResult ruleEngineActionResult, DroolsKIEModuleModel module, String deployedReleaseIdVersion, boolean propagateToOtherNodes)
    {
        getRuleEngineContainerRegistry().lockWritingRegistry();
        try
        {
            getRuleEngineBootstrap().activateNewRuleEngineContainer(kieContainer, cache, ruleEngineActionResult, (AbstractRulesModuleModel)module, deployedReleaseIdVersion);
            if(propagateToOtherNodes)
            {
                notifyOtherNodes(ruleEngineActionResult);
            }
        }
        finally
        {
            getRuleEngineContainerRegistry().unlockWritingRegistry();
        }
        LOGGER.info("Swapping to a newly created kie container [{}] has finished successfully", kieContainer.getReleaseId());
    }


    protected boolean isBlocking()
    {
        return getConfigurationService().getConfiguration().getBoolean("ruleengine.kiemodule.swapping.blocking", false);
    }


    protected void retrySetFlagWhenSwitchKieModule(String moduleName)
    {
        if(!getInitializationMultiFlag().compareAndSet(moduleName, false, true))
        {
            LOGGER.warn("Exception occurred during Kie Module switching, Kie container swapping is still in progress. No updates are possible at this time, triggering retry logic");
            throw new IllegalStateException("Kie container swapping is in progress, no rules updates are possible at this time");
        }
    }


    protected void switchKieModule(DroolsKIEModuleModel module, KieContainerListener listener, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result, Collection<Supplier<Object>> chainOfPostTasks)
    {
        String moduleName = module.getName();
        RetryTemplate retryTemplate = getRuleEnginePublishRetryTemplate();
        Object object = new Object(this, moduleName, chainOfPostTasks, module, propagateToOtherNodes, result, listener, enableIncrementalUpdate);
        try
        {
            retryTemplate.execute((RetryCallback)object);
        }
        catch(IllegalStateException e)
        {
            LOGGER.error("Kie container swapping is in progress, no rules updates are possible at this time");
            throw new DroolsInitializationException("Kie container swapping is in progress, no rules updates are possible at this time");
        }
    }


    protected void notifyOtherNodes(RuleEngineActionResult result)
    {
        if(!result.isActionFailed())
        {
            LOGGER.info("Publishing event that Kie Container swap completed successfully for module [{}] and version [{}]", result
                            .getModuleName(), result
                            .getDeployedVersion());
            getEventService().publishEvent((AbstractEvent)new RuleEngineInitializedEvent(result.getModuleName(), result.getOldVersion()));
        }
    }


    protected void notifyOtherNodesAboutKieModuleSwapping(String moduleName, String deployedReleaseIdVersion)
    {
        LOGGER.info("Publishing event that Kie Container swap started for module [{}] and release ID [{}]", moduleName, deployedReleaseIdVersion);
        getEventService().publishEvent((AbstractEvent)new KieModuleSwappingEvent(moduleName, deployedReleaseIdVersion));
    }


    protected boolean isRuleEngineActive()
    {
        return getConfigurationService().getConfiguration().getBoolean("ruleengine.engine.active", true);
    }


    public List<RuleEngineActionResult> initializeAllRulesModules(boolean propagateToOtherNodes)
    {
        List<RuleEngineActionResult> results = Lists.newArrayList();
        if(isRuleEngineActive())
        {
            LOGGER.info("Starting rules module activation");
            InitializationFuture initializationFuture = initialize(getRulesModuleDao().findAll(), propagateToOtherNodes, false).waitForInitializationToFinish();
            results = initializationFuture.getResults();
        }
        else
        {
            results.add(createRuleEngineActionResult("cannot activate rules module as the rule engine is not active. To activate it set the property ruleengine.engine.active to true.", null, false, MessageLevel.WARNING));
        }
        return results;
    }


    public List<RuleEngineActionResult> initializeAllRulesModules()
    {
        return initializeAllRulesModules(true);
    }


    public RuleEngineActionResult updateEngineRule(@Nonnull AbstractRuleEngineRuleModel ruleEngineRule, @Nonnull AbstractRulesModuleModel rulesModule)
    {
        Preconditions.checkArgument(Objects.nonNull(ruleEngineRule), "ruleEngineRule argument must not be null");
        Preconditions.checkArgument(Objects.nonNull(rulesModule), "rulesModule argument must not be null");
        Preconditions.checkArgument(ruleEngineRule instanceof DroolsRuleModel, "ruleEngineRule must be an instance of DroolsRuleModel");
        Preconditions.checkArgument(rulesModule instanceof DroolsKIEModuleModel, "rulesModule must be an instance of DroolsKIEModuleModel");
        DroolsRuleModel droolsRule = (DroolsRuleModel)ruleEngineRule;
        DroolsKIEModuleModel droolsRulesModule = (DroolsKIEModuleModel)rulesModule;
        return addRuleToKieBase(droolsRule, droolsRulesModule);
    }


    private RuleEngineActionResult addRuleToKieBase(DroolsRuleModel droolsRule, DroolsKIEModuleModel droolsRulesModule)
    {
        RuleEngineActionResult result;
        DroolsKIEBaseModel kieBase = getDroolsKIEBaseFinderStrategy().getKIEBaseForKIEModule(droolsRulesModule);
        if(Objects.isNull(kieBase))
        {
            String message = String.format("Cannot update engine rule, given rules module with name: %s doesn't have any KIE base.", new Object[] {droolsRulesModule
                            .getName()});
            LOGGER.error(message);
            result = createRuleEngineActionResult(message, droolsRulesModule.getName(), false, MessageLevel.ERROR);
        }
        else
        {
            droolsRule.setKieBase(kieBase);
            getModelService().save(droolsRule);
            getModelService().refresh(kieBase);
            result = createRuleEngineActionResult(String.format("successfully updated rule with code: %s", new Object[] {droolsRule.getCode()}), droolsRulesModule
                            .getName(), true, MessageLevel.INFO);
        }
        return result;
    }


    protected Optional<RuleEngineActionResult> validateEngineRulesModule(AbstractRulesModuleModel rulesModule)
    {
        RuleEngineActionResult result = null;
        if(Objects.isNull(rulesModule))
        {
            String ERROR_MESSAGE = "Cannot update engine rule, given rule module is null";
            LOGGER.error("Cannot update engine rule, given rule module is null");
            result = createRuleEngineActionResult("Cannot update engine rule, given rule module is null", null, false, MessageLevel.ERROR);
        }
        else if(!(rulesModule instanceof DroolsKIEModuleModel))
        {
            String message = String.format("Cannot update engine rule, given rules module with name: %s is not DroolsKIEModuleModel, but %s.", new Object[] {rulesModule
                            .getName(), rulesModule.getItemtype()});
            LOGGER.error(message);
            result = createRuleEngineActionResult(message, null, false, MessageLevel.ERROR);
        }
        return Optional.ofNullable(result);
    }


    public <T extends DroolsRuleModel> Optional<InitializationFuture> archiveRules(@Nonnull Collection<T> engineRules)
    {
        if(CollectionUtils.isNotEmpty(engineRules))
        {
            EngineRulePreconditions.checkRulesHaveSameType(engineRules);
            engineRules.forEach(EngineRulePreconditions::checkRuleHasKieModule);
            Set<T> activeEngineRules = (Set<T>)engineRules.stream().filter(r -> (r.getCurrentVersion().booleanValue() && r.getActive().booleanValue())).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(activeEngineRules))
            {
                List<AbstractRulesModuleModel> modules = (List<AbstractRulesModuleModel>)activeEngineRules.stream().map(r -> r.getKieBase().getKieModule()).distinct().collect(Collectors.toList());
                for(DroolsRuleModel droolsRuleModel : activeEngineRules)
                {
                    droolsRuleModel.setActive(Boolean.FALSE);
                }
                getModelService().saveAll(activeEngineRules);
                return Optional.of(initialize(modules, true, true));
            }
        }
        return Optional.empty();
    }


    public AbstractRuleEngineRuleModel getRuleForCodeAndModule(String code, String moduleName)
    {
        return getEngineRuleDao().getRuleByCode(code, moduleName);
    }


    public AbstractRuleEngineRuleModel getRuleForUuid(String uuid)
    {
        return getEngineRuleDao().getRuleByUuid(uuid);
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected RuleEngineActionResult createRuleEngineActionResult(String message, String moduleName, boolean success, MessageLevel level)
    {
        RuleEngineActionResult result = new RuleEngineActionResult();
        populateRuleEngineActionResult(result, message, moduleName, success, level);
        return result;
    }


    protected void populateRuleEngineActionResult(RuleEngineActionResult result, String message, String moduleName, boolean success, MessageLevel level)
    {
        result.setActionFailed(!success);
        ResultItem item = new ResultItem();
        item.setMessage(message);
        item.setLevel(level);
        result.setResults(Collections.singleton(item));
        result.setModuleName(moduleName);
    }


    @PostConstruct
    public void setup()
    {
        this.initialisationMultiFlag = new MultiFlag(getConcurrentMapFactory());
        String dateFormat = getConfigurationService().getConfiguration().getString("drools.dateformat");
        if(StringUtils.isNotBlank(dateFormat))
        {
            System.setProperty("drools.dateformat", dateFormat);
        }
        getRuleEngineKieModuleSwapper().setUpKieServices();
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


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
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


    protected RuleEngineKieModuleSwapper getRuleEngineKieModuleSwapper()
    {
        return this.ruleEngineKieModuleSwapper;
    }


    @Required
    public void setRuleEngineKieModuleSwapper(RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper)
    {
        this.ruleEngineKieModuleSwapper = ruleEngineKieModuleSwapper;
    }


    protected DroolsKIEBaseFinderStrategy getDroolsKIEBaseFinderStrategy()
    {
        return this.droolsKIEBaseFinderStrategy;
    }


    @Required
    public void setDroolsKIEBaseFinderStrategy(DroolsKIEBaseFinderStrategy droolsKIEBaseFinderStrategy)
    {
        this.droolsKIEBaseFinderStrategy = droolsKIEBaseFinderStrategy;
    }


    protected RuleEngineCacheService getRuleEngineCacheService()
    {
        return this.ruleEngineCacheService;
    }


    @Required
    public void setRuleEngineCacheService(RuleEngineCacheService ruleEngineCacheService)
    {
        this.ruleEngineCacheService = ruleEngineCacheService;
    }


    protected ConcurrentMapFactory getConcurrentMapFactory()
    {
        return this.concurrentMapFactory;
    }


    @Required
    public void setConcurrentMapFactory(ConcurrentMapFactory concurrentMapFactory)
    {
        this.concurrentMapFactory = concurrentMapFactory;
    }


    protected MultiFlag getInitializationMultiFlag()
    {
        return this.initialisationMultiFlag;
    }


    protected KieSessionHelper getKieSessionHelper()
    {
        return this.kieSessionHelper;
    }


    @Required
    public void setKieSessionHelper(KieSessionHelper kieSessionHelper)
    {
        this.kieSessionHelper = kieSessionHelper;
    }


    protected RuleEngineTaskProcessor<AbstractRuleEngineRuleModel, TaskResult> getEngineRulesPersistingTaskProcessor()
    {
        return this.engineRulesPersistingTaskProcessor;
    }


    @Required
    public void setEngineRulesPersistingTaskProcessor(RuleEngineTaskProcessor<AbstractRuleEngineRuleModel, TaskResult> engineRulesPersistingTaskProcessor)
    {
        this.engineRulesPersistingTaskProcessor = engineRulesPersistingTaskProcessor;
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


    protected RuleEngineContainerRegistry<ReleaseId, KieContainer> getRuleEngineContainerRegistry()
    {
        return this.ruleEngineContainerRegistry;
    }


    @Required
    public void setRuleEngineContainerRegistry(RuleEngineContainerRegistry<ReleaseId, KieContainer> ruleEngineContainerRegistry)
    {
        this.ruleEngineContainerRegistry = ruleEngineContainerRegistry;
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


    protected PostRulesModuleSwappingTasksProvider getPostRulesModuleSwappingTasksProvider()
    {
        return this.postRulesModuleSwappingTasksProvider;
    }


    @Required
    public void setPostRulesModuleSwappingTasksProvider(PostRulesModuleSwappingTasksProvider postRulesModuleSwappingTasksProvider)
    {
        this.postRulesModuleSwappingTasksProvider = postRulesModuleSwappingTasksProvider;
    }


    protected Function<RuleEvaluationContext, Integer> getMaxRuleExecutionsFunction()
    {
        return this.maxRuleExecutionsFunction;
    }


    @Required
    public void setMaxRuleExecutionsFunction(Function<RuleEvaluationContext, Integer> maxRuleExecutionsFunction)
    {
        this.maxRuleExecutionsFunction = maxRuleExecutionsFunction;
    }


    public void setRuleEnginePublishRetryTemplate(RetryTemplate ruleEnginePublishRetryTemplate)
    {
        this.ruleEnginePublishRetryTemplate = ruleEnginePublishRetryTemplate;
    }


    protected RetryTemplate getRuleEnginePublishRetryTemplate()
    {
        return this.ruleEnginePublishRetryTemplate;
    }
}
