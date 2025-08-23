package de.hybris.platform.ruleengine.init.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.InitializeMode;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleEngineCacheService;
import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import de.hybris.platform.ruleengine.concurrency.TaskResult;
import de.hybris.platform.ruleengine.constants.RuleEngineConstants;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.drools.KieModuleService;
import de.hybris.platform.ruleengine.exception.DroolsInitializationException;
import de.hybris.platform.ruleengine.impl.KieContainerListener;
import de.hybris.platform.ruleengine.init.ConcurrentMapFactory;
import de.hybris.platform.ruleengine.init.ContentMatchRulesFilter;
import de.hybris.platform.ruleengine.init.IncrementalRuleEngineUpdateStrategy;
import de.hybris.platform.ruleengine.init.RuleDeploymentTaskResult;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.init.RulePublishingFuture;
import de.hybris.platform.ruleengine.init.RulePublishingSpliterator;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengine.util.RuleEngineUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieFileSystemImpl;
import org.drools.compiler.kie.builder.impl.MemoryKieModule;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.fest.util.Collections;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.builder.IncrementalResults;
import org.kie.internal.builder.InternalKieBuilder;
import org.kie.internal.builder.KieBuilderSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineKieModuleSwapper implements RuleEngineKieModuleSwapper
{
    public static final String WORKER_PRE_DESTROY_TIMEOUT = "ruleengine.kiemodule.swapping.predestroytimeout";
    private static final String BASE_WORKER_NAME = "RuleEngine-module-swapping";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineKieModuleSwapper.class);
    private long workerPreDestroyTimeout;
    private ConfigurationService configurationService;
    private KieServices kieServices;
    private Tenant currentTenant;
    private ThreadFactory tenantAwareThreadFactory;
    private ModelService modelService;
    private ConcurrentMapFactory concurrentMapFactory;
    private RulesModuleDao rulesModuleDao;
    private RuleEngineCacheService ruleEngineCacheService;
    private RulePublishingSpliterator rulePublishingSpliterator;
    private ContentMatchRulesFilter contentMatchRulesFilter;
    private IncrementalRuleEngineUpdateStrategy incrementalRuleEngineUpdateStrategy;
    private RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel> ruleEngineBootstrap;
    private SuspendResumeTaskManager suspendResumeTaskManager;
    private KieModuleService kieModuleService;
    private EngineRuleDao engineRuleDao;
    private Map<String, Set<Thread>> asyncWorkers;


    public List<Object> switchKieModule(DroolsKIEModuleModel module, KieContainerListener listener, LinkedList<Supplier<Object>> postTaskList, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        LOGGER.debug("Starting swapping of Kie Module [{}] in {}incremental mode", module.getName(),
                        enableIncrementalUpdate ? "non-" : "");
        List<Object> resultsAccumulator = Lists.newArrayList();
        try
        {
            LOGGER.debug("Invoking initialization of a new module for [{}]", module.getName());
            initializeNewModule(module, listener, enableIncrementalUpdate, result);
        }
        finally
        {
            postTaskList.forEach(pt -> resultsAccumulator.add(pt.get()));
        }
        return resultsAccumulator;
    }


    public void switchKieModuleAsync(String moduleName, KieContainerListener listener, List<Object> resultsAccumulator, Supplier<Object> resetFlagSupplier, List<Supplier<Object>> postTaskList, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        waitForSwappingToFinish(moduleName);
        LOGGER.info("Starting swapping for rule module [{}]", moduleName);
        Thread asyncWorker = getCurrentTenant().createAndRegisterBackgroundThread(switchKieModuleRunnableTask(moduleName, listener, resultsAccumulator, resetFlagSupplier, postTaskList, enableIncrementalUpdate, result),
                        getTenantAwareThreadFactory());
        asyncWorker.setName(getNextWorkerName());
        if(getSuspendResumeTaskManager().isSystemRunning())
        {
            asyncWorker.start();
            registerWorker(moduleName, asyncWorker);
        }
        else
        {
            resetFlagSupplier.get();
            LOGGER.info("Cannot proceed with module swapping since the system is not in running state");
        }
    }


    public void waitForSwappingToFinish()
    {
        this.asyncWorkers.entrySet().stream().flatMap(e -> ((Set)e.getValue()).stream()).forEach(this::waitWhileWorkerIsRunning);
    }


    protected void waitForSwappingToFinish(String moduleName)
    {
        this.asyncWorkers.entrySet().stream().filter(e -> ((String)e.getKey()).equals(moduleName)).flatMap(e -> ((Set)e.getValue()).stream())
                        .forEach(this::waitWhileWorkerIsRunning);
    }


    protected String getNextWorkerName()
    {
        long nextActiveOrder = 0L;
        if(MapUtils.isNotEmpty(this.asyncWorkers))
        {
            nextActiveOrder = this.asyncWorkers.entrySet().stream().flatMap(e -> ((Set)e.getValue()).stream()).filter(w -> (Objects.nonNull(w) && w.isAlive())).count();
        }
        return "RuleEngine-module-swapping-" + nextActiveOrder;
    }


    protected void waitWhileWorkerIsRunning(Thread worker)
    {
        if(Objects.nonNull(worker) && worker.isAlive())
        {
            try
            {
                LOGGER.debug("Waiting for a currently running async worker to finish the job...");
                worker.join(getWorkerPreDestroyTimeout());
            }
            catch(InterruptedException e)
            {
                Thread.currentThread().interrupt();
                LOGGER.error("Interrupted exception is caught during async Kie container swap: {}", e);
            }
        }
    }


    public void beforeDestroy()
    {
        waitForSwappingToFinish();
    }


    public void writeKModuleXML(KieModuleModel module, KieFileSystem kfs)
    {
        LOGGER.debug("Writing Kie Module [{}] to XML", module);
        kfs.writeKModuleXML(module.toXML());
    }


    public void writePomXML(DroolsKIEModuleModel module, KieFileSystem kfs)
    {
        ReleaseId releaseId = getReleaseId(module);
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Writing POM for releaseId: {}", releaseId.toExternalForm());
        }
        kfs.generateAndWritePomXML(releaseId);
    }


    public ReleaseId getReleaseId(DroolsKIEModuleModel module)
    {
        String moduleVersion = RuleEngineUtils.getDeployedRulesModuleVersion(module);
        return getKieServices().newReleaseId(module.getMvnGroupId(), module.getMvnArtifactId(), moduleVersion);
    }


    public Pair<KieModule, KIEModuleCacheBuilder> createKieModule(DroolsKIEModuleModel module, RuleEngineActionResult result)
    {
        ServicesUtil.validateParameterNotNull(module, "module must not be null");
        LOGGER.debug("Starting creation of a Kie Module for module [{}]", module.getName());
        KIEModuleCacheBuilder cache = getRuleEngineCacheService().createKIEModuleCacheBuilder(module);
        Collection<DroolsKIEBaseModel> kieBases = module.getKieBases();
        LOGGER.debug("Module [{}] contains [{}] kie bases", module.getName(), Integer.valueOf(CollectionUtils.size(kieBases)));
        ServicesUtil.validateParameterNotNull(kieBases, "kieBases in the module must not be null");
        LOGGER.debug("Creating new Kie Module Model");
        KieModuleModel kieModuleModel = getKieServices().newKieModuleModel();
        LOGGER.debug("Successfully created new Kie Module [{}]", kieModuleModel);
        KieFileSystem kfs = getKieServices().newKieFileSystem();
        kieBases.forEach(base -> addKieBase(kieModuleModel, kfs, base, cache));
        writeKModuleXML(kieModuleModel, kfs);
        writePomXML(module, kfs);
        KieBuilder kieBuilder = getKieServices().newKieBuilder(kfs);
        kieBuilder.buildAll();
        if(kieBuilder.getResults().hasMessages(new Message.Level[] {Message.Level.ERROR, Message.Level.WARNING, Message.Level.INFO}))
        {
            for(Message message : kieBuilder.getResults().getMessages())
            {
                LOGGER.error("{} {} {}", new Object[] {message.getLevel(), message.getText(), message.getPath()});
                ResultItem item = addNewResultItemOf(result, RuleEngineKieModuleSwapper.convertLevel(message.getLevel()), message.getText());
                item.setLine(message.getLine());
                item.setPath(message.getPath());
                if(Message.Level.ERROR.equals(message.getLevel()))
                {
                    result.setActionFailed(true);
                }
            }
            if(kieBuilder.getResults().hasMessages(new Message.Level[] {Message.Level.ERROR}))
            {
                throw new DroolsInitializationException(result.getResults(), "Drools rule engine initialization failed");
            }
        }
        return Pair.of(kieBuilder.getKieModule(), cache);
    }


    protected Pair<KieModule, KIEModuleCacheBuilder> createKieModule(DroolsKIEModuleModel module, RuleEngineActionResult result, boolean enableIncrementalUpdate)
    {
        LOGGER.debug("Starting creation of a Kie Module for module [{}]", module.getName());
        ServicesUtil.validateParameterNotNull(module, "module must not be null");
        Collection<DroolsKIEBaseModel> kieBases = module.getKieBases();
        ServicesUtil.validateParameterNotNull(kieBases, "kieBases in the module must not be null");
        LOGGER.debug("Module [{}] contains [{}] kie bases", module.getName(), Integer.valueOf(CollectionUtils.size(kieBases)));
        LOGGER.debug("Creating new Kie Module Model");
        KieModuleModel kieModuleModel = getKieServices().newKieModuleModel();
        LOGGER.debug("Successfully created new Kie Module [{}]", kieModuleModel);
        kieBases.forEach(base -> addKieBase(kieModuleModel, base));
        LOGGER.debug("Creating KIEModuleCacheBuilder for modules [{}]", module.getName());
        KIEModuleCacheBuilder cache = getRuleEngineCacheService().createKIEModuleCacheBuilder(module);
        LOGGER.debug("Obtaining new ReleaseId for module [{}]", module.getName());
        ReleaseId newReleaseId = getReleaseId(module);
        LOGGER.debug("New ReleaseId obtained: [{}]", newReleaseId);
        Optional<ReleaseId> deployedReleaseId = getDeployedReleaseId(module, null);
        if(enableIncrementalUpdate && deployedReleaseId.isPresent())
        {
            ReleaseId currentReleaseId = deployedReleaseId.get();
            KieModule deployedKieModule = getKieServices().getRepository().getKieModule(currentReleaseId);
            LOGGER.debug("Deployed KieModule for ReleaseId [{}] : [{}]", currentReleaseId, deployedKieModule);
            if(Objects.nonNull(deployedKieModule))
            {
                List<Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>> rulesToUpdateList = (List<Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>>)kieBases.stream().map(kBase -> prepareIncrementalUpdate(currentReleaseId, kBase)).filter(Optional::isPresent)
                                .map(Optional::get).collect(Collectors.toList());
                LOGGER.debug("Rules to deploy [{}]", Integer.valueOf(CollectionUtils.size(rulesToUpdateList)));
                List<DroolsRuleModel> rulesToAdd = (List<DroolsRuleModel>)rulesToUpdateList.stream().flatMap(u -> ((Collection)u.getLeft()).stream()).collect(Collectors.toList());
                List<DroolsRuleModel> rulesToRemove = (List<DroolsRuleModel>)rulesToUpdateList.stream().flatMap(u -> ((Collection)u.getRight()).stream()).collect(Collectors.toList());
                if(getIncrementalRuleEngineUpdateStrategy()
                                .shouldUpdateIncrementally(currentReleaseId, module.getName(), rulesToAdd, rulesToRemove))
                {
                    MemoryKieModule memoryKieModule = cloneForIncrementalCompilation((MemoryKieModule)deployedKieModule, newReleaseId, kieModuleModel);
                    List<AbstractRuleEngineRuleModel> activeRules = getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)module);
                    kieBases.forEach(kBase -> {
                        Objects.requireNonNull(cache);
                        activeRules.forEach(cache::processRule);
                    });
                    rulesToUpdateList
                                    .forEach(u -> deployRulesIncrementally(newReleaseId, kieModuleModel, cloneKieModule, (Collection<DroolsRuleModel>)u.getLeft(), (Collection<DroolsRuleModel>)u.getRight(), result));
                    KieModule kieModule1 = mergePartialKieModules(newReleaseId, kieModuleModel, (KieModule)memoryKieModule);
                    return Pair.of(kieModule1, cache);
                }
            }
        }
        List<KieBuilder> kieBuilders = (List<KieBuilder>)kieBases.stream().flatMap(base -> deployRules(module, kieModuleModel, base, cache).stream()).collect(Collectors.toList());
        KieModule kieModule = mergePartialKieModules(newReleaseId, kieModuleModel, kieBuilders);
        return Pair.of(kieModule, cache);
    }


    protected void copyChanges(MemoryKieModule trgKieModule, MemoryKieModule srcKieModule)
    {
        MemoryFileSystem trgMemoryFileSystem = trgKieModule.getMemoryFileSystem();
        MemoryFileSystem srcMemoryFileSystem = srcKieModule.getMemoryFileSystem();
        Map<String, byte[]> compiledCode = srcMemoryFileSystem.getMap();
        for(Map.Entry<String, byte[]> entry : compiledCode.entrySet())
        {
            String path = entry.getKey();
            if(!path.startsWith(RuleEngineConstants.DROOLS_BASE_PATH))
            {
                String resoursePath = RuleEngineConstants.DROOLS_BASE_PATH + RuleEngineConstants.DROOLS_BASE_PATH;
                String normalizedRulePath = RuleEngineUtils.getNormalizedRulePath(resoursePath);
                if(resoursePath.endsWith(".drl") && trgMemoryFileSystem.existsFile(normalizedRulePath))
                {
                    LOGGER.debug("Removing file: {}", normalizedRulePath);
                    trgMemoryFileSystem.remove(normalizedRulePath);
                }
                trgMemoryFileSystem.write(path, entry.getValue());
            }
        }
        trgMemoryFileSystem.mark();
    }


    protected MemoryKieModule cloneForIncrementalCompilation(MemoryKieModule origKieModule, ReleaseId releaseId, KieModuleModel kModuleModel)
    {
        MemoryFileSystem newMfs = new MemoryFileSystem();
        MemoryKieModule clone = new MemoryKieModule(releaseId, kModuleModel, newMfs);
        MemoryFileSystem origMemoryFileSystem = origKieModule.getMemoryFileSystem();
        Map<String, byte[]> fileContents = origMemoryFileSystem.getMap();
        for(Map.Entry<String, byte[]> entry : fileContents.entrySet())
        {
            newMfs.write(entry.getKey(), entry.getValue());
        }
        clone.mark();
        for(InternalKieModule dep : origKieModule.getKieDependencies().values())
        {
            clone.addKieDependency(dep);
        }
        for(KieBaseModel kBaseModel : origKieModule.getKieModuleModel().getKieBaseModels().values())
        {
            clone.cacheKnowledgeBuilderForKieBase(kBaseModel.getName(), origKieModule
                            .getKnowledgeBuilderForKieBase(kBaseModel.getName()));
        }
        clone.setPomModel(origKieModule.getPomModel());
        for(InternalKieModule dependency : origKieModule.getKieDependencies().values())
        {
            clone.addKieDependency(dependency);
        }
        clone.setUnresolvedDependencies(origKieModule.getUnresolvedDependencies());
        return clone;
    }


    protected Optional<Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>> prepareIncrementalUpdate(ReleaseId releaseId, DroolsKIEBaseModel kieBase)
    {
        LOGGER.debug("Prepare for incremental update for KieBase [{}] and ReleaseId [{}]", kieBase.getName(), releaseId);
        List<AbstractRuleEngineRuleModel> rules = getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)kieBase.getKieModule());
        Long newModuleVersion = kieBase.getKieModule().getVersion();
        LOGGER.debug("New module version: [{}]", newModuleVersion);
        Optional<Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>>> rulesToDeploy = Optional.empty();
        if(CollectionUtils.isNotEmpty(rules))
        {
            List<String> ruleUuids = (List<String>)rules.stream().filter(this::isRuleValid).map(AbstractRuleEngineRuleModel::getUuid).collect(Collectors.toList());
            if(!ruleUuids.isEmpty())
            {
                Pair<Collection<DroolsRuleModel>, Collection<DroolsRuleModel>> matchingRules = getContentMatchRulesFilter().apply(ruleUuids, newModuleVersion);
                return Optional.of(matchingRules);
            }
        }
        return rulesToDeploy;
    }


    protected void deployRulesIncrementally(ReleaseId releaseId, KieModuleModel kieModuleModel, KieModule kieModule, Collection<DroolsRuleModel> rulesToAdd, Collection<DroolsRuleModel> rulesToRemove, RuleEngineActionResult result)
    {
        if(CollectionUtils.isNotEmpty(rulesToRemove))
        {
            LOGGER.debug("Rules to remove: {}", Integer.valueOf(rulesToRemove.size()));
            deleteRulesFromKieModule((MemoryKieModule)kieModule, rulesToRemove);
        }
        if(CollectionUtils.isNotEmpty(rulesToAdd))
        {
            MemoryFileSystem memoryFileSystem = ((MemoryKieModule)kieModule).getMemoryFileSystem();
            KieFileSystemImpl kieFileSystemImpl = new KieFileSystemImpl(memoryFileSystem);
            writeKModuleXML(kieModuleModel, (KieFileSystem)kieFileSystemImpl);
            kieFileSystemImpl.generateAndWritePomXML(releaseId);
            KieBuilder kieBuilder = getKieServices().newKieBuilder((KieFileSystem)kieFileSystemImpl);
            String[] ruleToAddPaths = getRulePaths(rulesToAdd);
            if(ArrayUtils.isNotEmpty((Object[])ruleToAddPaths))
            {
                LOGGER.debug("Rules to add: {}", Integer.valueOf(rulesToAdd.size()));
                writeRulesToKieFileSystem((KieFileSystem)kieFileSystemImpl, rulesToAdd);
                KieBuilderSet kieBuilderSet = ((InternalKieBuilder)kieBuilder).createFileSet(ruleToAddPaths);
                Results kieBuilderResults = kieBuilder.getResults();
                List<Message> kieBuilderMessages = kieBuilderResults.getMessages(new Message.Level[] {Message.Level.ERROR});
                verifyErrors(result, kieBuilderMessages);
                IncrementalResults incrementalResults = kieBuilderSet.build();
                List<Message> messages = incrementalResults.getAddedMessages();
                verifyErrors(result, messages);
            }
            KieModule incrementalKieModule = kieBuilder.getKieModule();
            copyChanges((MemoryKieModule)kieModule, (MemoryKieModule)incrementalKieModule);
        }
    }


    protected void verifyErrors(RuleEngineActionResult result, List<Message> messages)
    {
        messages.stream().filter(m -> m.getLevel().equals(Message.Level.ERROR))
                        .forEach(m -> addNewResultItemOf(result, MessageLevel.ERROR, m.getText()));
        if(messages.stream().anyMatch(m -> m.getLevel().equals(Message.Level.ERROR)))
        {
            throw new DroolsInitializationException(result.getResults(), "Drools rule engine initialization failed");
        }
    }


    protected KieModule mergePartialKieModules(ReleaseId releaseId, KieModuleModel kieModuleModel, KieModule partialKieModule)
    {
        LOGGER.debug("Merging partial Kie Module [{}] with Kie Module [{}] for ReleaseId [{}]", new Object[] {partialKieModule, kieModuleModel, releaseId});
        MemoryFileSystem mainMemoryFileSystem = new MemoryFileSystem();
        MemoryKieModule memoryKieModule = new MemoryKieModule(releaseId, kieModuleModel, mainMemoryFileSystem);
        mergeFileSystemToKieModule((MemoryKieModule)partialKieModule, mainMemoryFileSystem);
        mainMemoryFileSystem.mark();
        LOGGER.debug("Main KIE module contains [{}] files", Integer.valueOf(mainMemoryFileSystem.getFileNames().size()));
        return (KieModule)memoryKieModule;
    }


    protected void mergeFileSystemToKieModule(MemoryKieModule partialKieModule, MemoryFileSystem mainMemoryFileSystem)
    {
        MemoryFileSystem partialMemoryFileSystem = partialKieModule.getMemoryFileSystem();
        Map<String, byte[]> fileContents = partialMemoryFileSystem.getMap();
        for(Map.Entry<String, byte[]> entry : fileContents.entrySet())
        {
            mainMemoryFileSystem.write(entry.getKey(), entry.getValue());
        }
    }


    protected KieModule mergePartialKieModules(ReleaseId releaseId, KieModuleModel kieModuleModel, List<KieBuilder> kieBuilders)
    {
        LOGGER.debug("Merging Kie Module [{} for ReleaseId [{}]", kieModuleModel, releaseId);
        MemoryFileSystem mainMemoryFileSystem = new MemoryFileSystem();
        MemoryKieModule memoryKieModule = new MemoryKieModule(releaseId, kieModuleModel, mainMemoryFileSystem);
        if(CollectionUtils.isNotEmpty(kieBuilders))
        {
            for(KieBuilder kieBuilder : kieBuilders)
            {
                KieModule partialKieModule = kieBuilder.getKieModule();
                mergeFileSystemToKieModule((MemoryKieModule)partialKieModule, mainMemoryFileSystem);
            }
        }
        mainMemoryFileSystem.mark();
        LOGGER.debug("Main KIE module contains [{}] files", Integer.valueOf(mainMemoryFileSystem.getFileNames().size()));
        return (KieModule)memoryKieModule;
    }


    public void addKieBase(KieModuleModel module, KieFileSystem kfs, DroolsKIEBaseModel base, KIEModuleCacheBuilder cache)
    {
        addKieBase(module, base);
        addRules(kfs, base, cache);
    }


    public void addKieBase(KieModuleModel module, DroolsKIEBaseModel base)
    {
        KieBaseModel kieBaseModel = module.newKieBaseModel(base.getName());
        kieBaseModel.setEqualsBehavior(RuleEngineKieModuleSwapper.getEqualityBehaviorOption(base.getEqualityBehavior()));
        kieBaseModel.setEventProcessingMode(RuleEngineKieModuleSwapper.getEventProcessingOption(base.getEventProcessingMode()));
        if(base.equals(base.getKieModule().getDefaultKIEBase()))
        {
            kieBaseModel.setDefault(true);
        }
        base.getKieSessions().forEach(session -> addKieSession(kieBaseModel, session));
    }


    public void addKieSession(KieBaseModel base, DroolsKIESessionModel session)
    {
        KieSessionModel kieSession = base.newKieSessionModel(session.getName());
        DroolsKIEBaseModel kieBase = session.getKieBase();
        DroolsKIESessionModel defaultKIESession = kieBase.getDefaultKIESession();
        if(Objects.nonNull(defaultKIESession))
        {
            PK pk = defaultKIESession.getPk();
            if(Objects.nonNull(pk))
            {
                kieSession.setDefault(pk.equals(session.getPk()));
            }
        }
        KieSessionModel.KieSessionType sessionType = RuleEngineKieModuleSwapper.getSessionType(session.getSessionType());
        kieSession.setType(sessionType);
    }


    public String activateKieModule(DroolsKIEModuleModel module)
    {
        LOGGER.debug("Activating Kie Module [{}]", module.getName());
        getModelService().refresh(module);
        String releaseIdVersion = getReleaseId(module).getVersion();
        module.setDeployedMvnVersion(releaseIdVersion);
        LOGGER.debug("Setting deployed mvn version [{}] for module [{}]", releaseIdVersion, module.getName());
        getModelService().save(module);
        return releaseIdVersion;
    }


    public boolean removeKieModuleIfPresent(ReleaseId releaseId, RuleEngineActionResult result)
    {
        LOGGER.info("Removing old Kie module [{}]", releaseId);
        boolean moduleRemoved = false;
        KieModule kieModule = getKieServices().getRepository().getKieModule(releaseId);
        if(Objects.nonNull(kieModule) && !isInitialEngineStartup(releaseId, result.getDeployedVersion()))
        {
            getKieServices().getRepository().removeKieModule(releaseId);
            moduleRemoved = true;
        }
        LOGGER.debug("Kie module [{}] has {} been removed", releaseId, moduleRemoved ? "" : "not");
        return moduleRemoved;
    }


    public boolean removeOldKieModuleIfPresent(RuleEngineActionResult result)
    {
        boolean moduleRemoved = false;
        LOGGER.info("Removing old Kie module with name {}", result.getModuleName());
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(result.getModuleName());
        if(Objects.nonNull(rulesModule))
        {
            ReleaseIdImpl releaseIdImpl = new ReleaseIdImpl(rulesModule.getMvnGroupId(), rulesModule.getMvnArtifactId(), result.getOldVersion());
            moduleRemoved = removeKieModuleIfPresent((ReleaseId)releaseIdImpl, result);
        }
        LOGGER.debug("Kie module has {} been removed", moduleRemoved ? "" : "not");
        return moduleRemoved;
    }


    public void addRules(KieFileSystem kfs, DroolsKIEBaseModel base, KIEModuleCacheBuilder cache)
    {
        LOGGER.debug("Drools Engine Service addRules triggered...");
        Set<DroolsRuleModel> rules = filterByBiggestVersion(getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)base.getKieModule()));
        writeRulesToKieFileSystem(kfs, rules);
        Objects.requireNonNull(cache);
        rules.stream().filter(this::isRuleValid).forEach(cache::processRule);
    }


    protected <R extends D, D extends AbstractRuleEngineRuleModel> Set<R> filterByBiggestVersion(Collection<D> rulesForVersion)
    {
        Set<R> rules = Sets.newHashSet();
        if(CollectionUtils.isNotEmpty(rulesForVersion))
        {
            Map<String, List<R>> twinRulesMap = (Map<String, List<R>>)rulesForVersion.stream().map(r -> r).collect(Collectors.groupingBy(AbstractRuleEngineRuleModel::getCode));
            twinRulesMap.values().stream().map(Collection::stream).forEach(l -> {
                Objects.requireNonNull(rules);
                l.max(Comparator.comparing(AbstractRuleEngineRuleModel::getVersion)).ifPresent(rules::add);
            });
        }
        return rules;
    }


    protected boolean isRuleValid(AbstractRuleEngineRuleModel rule)
    {
        return (Objects.nonNull(rule.getRuleContent()) && BooleanUtils.isTrue(rule.getActive()));
    }


    protected void writeRulesToKieFileSystem(KieFileSystem kfs, Collection<DroolsRuleModel> rules)
    {
        for(DroolsRuleModel rule : rules)
        {
            if(isRuleValid((AbstractRuleEngineRuleModel)rule))
            {
                String rulePath = RuleEngineUtils.getRulePath(rule);
                String drl = rule.getRuleContent();
                LOGGER.debug("{} {}", rule.getCode(), rulePath);
                LOGGER.debug("{}", drl);
                kfs.write(rulePath, drl);
            }
            if(BooleanUtils.isNotTrue(rule.getActive()))
            {
                LOGGER.debug("ignoring rule {}. Rule is not active.", rule.getCode());
                continue;
            }
            if(Objects.isNull(rule.getRuleContent()))
            {
                LOGGER.warn("ignoring rule {}. No ruleContent set!", rule.getCode());
            }
        }
    }


    protected void deleteRulesFromKieModule(MemoryKieModule kieModule, Collection<DroolsRuleModel> rules)
    {
        LOGGER.debug("Delete rules [{}] from Kie Module [{}]", Integer.valueOf(rules.size()), kieModule);
        String[] rulePaths = getRulePaths(rules);
        if(ArrayUtils.isNotEmpty((Object[])rulePaths))
        {
            MemoryFileSystem memoryFileSystem = kieModule.getMemoryFileSystem();
            Arrays.<String>stream(rulePaths).map(RuleEngineUtils::stripDroolsMainResources).forEach(p -> deleteFileIfExists(memoryFileSystem, p));
        }
    }


    private static void deleteFileIfExists(MemoryFileSystem mfs, String path)
    {
        if(mfs.existsFile(path))
        {
            mfs.remove(path);
        }
    }


    private String[] getRulePaths(Collection<DroolsRuleModel> rules)
    {
        return (String[])((List)rules.stream().filter(this::isRuleValid).map(RuleEngineUtils::getRulePath).collect(Collectors.toList()))
                        .toArray((Object[])new String[0]);
    }


    public Optional<ReleaseId> getDeployedReleaseId(DroolsKIEModuleModel module, String deployedMvnVersion)
    {
        LOGGER.debug("Getting deployed ReleaseId for module [{}] and mvn version [{}]", module.getName(), deployedMvnVersion);
        String deployedReleaseIdVersion = deployedMvnVersion;
        DroolsKIEModuleModel localModule = module;
        if(Objects.isNull(deployedReleaseIdVersion))
        {
            localModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(module.getName());
            if(Objects.nonNull(localModule))
            {
                deployedReleaseIdVersion = localModule.getDeployedMvnVersion();
            }
        }
        Optional<ReleaseId> deployedReleaseId = Optional.empty();
        if(Objects.nonNull(getKieServices()) && Objects.nonNull(deployedReleaseIdVersion))
        {
            deployedReleaseId = Optional.of(getKieServices()
                            .newReleaseId(localModule.getMvnGroupId(), localModule.getMvnArtifactId(), deployedReleaseIdVersion));
        }
        LOGGER.debug("Obtained deployed ReleaseId: [{}]", deployedReleaseId.orElse(null));
        return deployedReleaseId;
    }


    public void setUpKieServices()
    {
        if(Objects.isNull(getKieServices()))
        {
            this.kieServices = (KieServices)getRuleEngineBootstrap().getEngineServices();
        }
    }


    @PostConstruct
    public void setup()
    {
        this.workerPreDestroyTimeout = this.configurationService.getConfiguration().getLong("ruleengine.kiemodule.swapping.predestroytimeout", 3600000L);
        this.asyncWorkers = getConcurrentMapFactory().createNew();
        setUpKieServices();
    }


    protected List<KieBuilder> deployRules(DroolsKIEModuleModel module, KieModuleModel kieModuleModel, DroolsKIEBaseModel kieBase, KIEModuleCacheBuilder cache)
    {
        List<String> rulesUuids = (List<String>)getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)module).stream().map(AbstractRuleEngineRuleModel::getUuid).collect(Collectors.toList());
        RulePublishingFuture rulePublishingFuture = getRulePublishingSpliterator().publishRulesAsync(kieModuleModel, getReleaseId(module), rulesUuids, cache);
        RuleDeploymentTaskResult ruleDeploymentResult = (RuleDeploymentTaskResult)rulePublishingFuture.getTaskResult();
        if(ruleDeploymentResult.getState().equals(TaskResult.State.FAILURE))
        {
            throw new DroolsInitializationException((Collection)ruleDeploymentResult
                            .getRulePublishingResults().stream()
                            .filter(result -> CollectionUtils.isNotEmpty(result.getResults()))
                            .flatMap(result -> result.getResults().stream())
                            .collect(Collectors.toList()), "Initialization of rule engine failed during the deployment phase: ");
        }
        return rulePublishingFuture.getPartialKieBuilders();
    }


    public KieContainer initializeNewKieContainer(DroolsKIEModuleModel module, KieModule kieModule, RuleEngineActionResult result)
    {
        LOGGER.debug("Starting initialization of a new Kie Container for module [{}]", module.getName());
        ReleaseId releaseId = getReleaseId(module);
        result.setModuleName(module.getName());
        KieRepository kieRepository = getKieServices().getRepository();
        if(LOGGER.isInfoEnabled())
        {
            LOGGER.info("Drools Engine Service initialization for '{}' module in tenant '{}' finished. ReleaseId of the new Kie Module: '{}'", new Object[] {module
                            .getName(), module.getTenantId(), kieModule.getReleaseId().toExternalForm()});
        }
        kieRepository.addKieModule(kieModule);
        KieContainer kieContainer = getKieServices().newKieContainer(releaseId);
        getRuleEngineBootstrap().warmUpRuleEngineContainer((AbstractRulesModuleModel)module, kieContainer);
        result.setDeployedVersion(releaseId.getVersion());
        LOGGER.debug("New container has been initialized successfully: [{}]", releaseId.getVersion());
        return kieContainer;
    }


    protected void initializeNewModule(DroolsKIEModuleModel module, KieContainerListener listener, boolean enableIncrementalUpdates, RuleEngineActionResult result)
    {
        try
        {
            LOGGER.debug("Starting initialization of module [{}]", module);
            InitializeMode initializeMode = getInitializeMode(result.getExecutionContext());
            boolean isRestoredKieModule = (initializeMode == InitializeMode.RESTORE && restoreKieModule(module, listener, result));
            if(!isRestoredKieModule)
            {
                newKieModule(module, listener, enableIncrementalUpdates, result);
            }
        }
        catch(DroolsInitializationException e)
        {
            LOGGER.error("DroolsInitializationException occurred {}", (Throwable)e);
            result.setResults(e.getResults());
            completeWithFailure(getReleaseId(module), result, listener);
        }
        catch(RuntimeException e)
        {
            LOGGER.error("Drools Engine Service initialization Exception occurred {}", e);
            addNewResultItemOf(result, MessageLevel.ERROR, e.getLocalizedMessage());
            completeWithFailure(getReleaseId(module), result, listener);
        }
    }


    protected InitializeMode getInitializeMode(ExecutionContext executionContext)
    {
        return (Objects.nonNull(executionContext) && Objects.nonNull(executionContext.getInitializeMode())) ? executionContext.getInitializeMode() : InitializeMode.NEW;
    }


    protected boolean restoreKieModule(DroolsKIEModuleModel module, KieContainerListener listener, RuleEngineActionResult result)
    {
        Optional<KieModule> restoredKieModule = getKieModuleService().loadKieModule(module.getName(), getReleaseId(module).toExternalForm());
        if(restoredKieModule.isPresent())
        {
            LOGGER.debug("Serialized module [{}] is found and will be restored", module);
            KIEModuleCacheBuilder cache = getRuleEngineCacheService().createKIEModuleCacheBuilder(module);
            Collection<DroolsKIEBaseModel> kieBases = module.getKieBases();
            kieBases.forEach(kb -> addRulesToCache(kb, cache));
            KieContainer kieContainer = initializeNewKieContainer(module, restoredKieModule.get(), result);
            listener.onSuccess(kieContainer, cache);
            return true;
        }
        return false;
    }


    protected void newKieModule(DroolsKIEModuleModel module, KieContainerListener listener, boolean enableIncrementalUpdates, RuleEngineActionResult result)
    {
        InitializeMode initializeMode = getInitializeMode(result.getExecutionContext());
        Pair<KieModule, KIEModuleCacheBuilder> moduleCacheBuilderPair = createKieModule(module, result, enableIncrementalUpdates);
        KieModule newKieModule = (KieModule)moduleCacheBuilderPair.getLeft();
        KIEModuleCacheBuilder cache = (KIEModuleCacheBuilder)moduleCacheBuilderPair.getRight();
        KieContainer kieContainer = initializeNewKieContainer(module, newKieModule, result);
        if(initializeMode == InitializeMode.NEW)
        {
            getKieModuleService().saveKieModule(module.getName(), getReleaseId(module).toExternalForm(), newKieModule);
            if(LOGGER.isInfoEnabled())
            {
                LOGGER.info("KieModule {} / {} has been serialized", module.getName(), getReleaseId(module).toExternalForm());
            }
        }
        listener.onSuccess(kieContainer, cache);
    }


    public void addRulesToCache(DroolsKIEBaseModel base, KIEModuleCacheBuilder cache)
    {
        LOGGER.debug("Drools Engine Service addRulesToCache triggered...");
        Set<DroolsRuleModel> rules = filterByBiggestVersion(getEngineRuleDao().getActiveRules((AbstractRulesModuleModel)base.getKieModule()));
        Objects.requireNonNull(cache);
        rules.stream().filter(this::isRuleValid).forEach(cache::processRule);
    }


    protected void completeWithFailure(ReleaseId releaseId, RuleEngineActionResult result, KieContainerListener listener)
    {
        KieRepository kieRepository = getKieServices().getRepository();
        KieModule corruptedKieModule = kieRepository.getKieModule(releaseId);
        if(Objects.nonNull(corruptedKieModule))
        {
            kieRepository.removeKieModule(releaseId);
        }
        result.setActionFailed(true);
        listener.onFailure(result);
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


    protected void registerWorker(String moduleName, Thread worker)
    {
        ImmutableSet immutableSet;
        getSuspendResumeTaskManager().registerAsNonSuspendableTask(worker, "Rule engine module deployment is in progress");
        Set<Thread> workersForModule = this.asyncWorkers.get(moduleName);
        if(Objects.isNull(workersForModule))
        {
            immutableSet = ImmutableSet.of(worker);
        }
        else
        {
            Set<Thread> aliveWorkers = (Set<Thread>)workersForModule.stream().filter(Thread::isAlive).collect(Collectors.toSet());
            aliveWorkers.add(worker);
            immutableSet = ImmutableSet.copyOf(aliveWorkers);
        }
        this.asyncWorkers.put(moduleName, immutableSet);
    }


    protected Runnable switchKieModuleRunnableTask(String moduleName, KieContainerListener listener, List<Object> resultsAccumulator, Supplier<Object> resetFlagSupplier, List<Supplier<Object>> postTaskList, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        Preconditions.checkArgument(Objects.nonNull(resultsAccumulator), "Results accumulator must be initialized upfront");
        return () -> {
            result.setModuleName(moduleName);
            try
            {
                DroolsKIEModuleModel module = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
                resultsAccumulator.addAll(switchKieModule(module, listener, (LinkedList<Supplier<Object>>)postTaskList, enableIncrementalUpdate, result));
                return;
            }
            catch(Exception e)
            {
                onSwapFailed(e, result, resetFlagSupplier);
                result.setActionFailed(true);
                listener.onFailure(result);
                return;
            }
        };
    }


    protected Object onSwapFailed(Throwable t, RuleEngineActionResult result, Supplier<Object> resetFlagSupplier)
    {
        LOGGER.error("Exception caught: {}", t);
        addNewResultItemOf(result, MessageLevel.ERROR, t.getLocalizedMessage());
        if(Objects.nonNull(resetFlagSupplier))
        {
            return resetFlagSupplier.get();
        }
        return null;
    }


    protected boolean isInitialEngineStartup(ReleaseId releaseId, String newDeployedMvnVersion)
    {
        return releaseId.getVersion().equals(newDeployedMvnVersion);
    }


    protected KieServices getKieServices()
    {
        return this.kieServices;
    }


    protected void setKieServices(KieServices kieServices)
    {
        this.kieServices = kieServices;
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


    protected Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    @Required
    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
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


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
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


    protected RulesModuleDao getRulesModuleDao()
    {
        return this.rulesModuleDao;
    }


    @Required
    public void setRulesModuleDao(RulesModuleDao rulesModuleDao)
    {
        this.rulesModuleDao = rulesModuleDao;
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


    protected RulePublishingSpliterator getRulePublishingSpliterator()
    {
        return this.rulePublishingSpliterator;
    }


    @Required
    public void setRulePublishingSpliterator(RulePublishingSpliterator rulePublishingSpliterator)
    {
        this.rulePublishingSpliterator = rulePublishingSpliterator;
    }


    protected ContentMatchRulesFilter getContentMatchRulesFilter()
    {
        return this.contentMatchRulesFilter;
    }


    @Required
    public void setContentMatchRulesFilter(ContentMatchRulesFilter contentMatchRulesFilter)
    {
        this.contentMatchRulesFilter = contentMatchRulesFilter;
    }


    protected IncrementalRuleEngineUpdateStrategy getIncrementalRuleEngineUpdateStrategy()
    {
        return this.incrementalRuleEngineUpdateStrategy;
    }


    @Required
    public void setIncrementalRuleEngineUpdateStrategy(IncrementalRuleEngineUpdateStrategy incrementalRuleEngineUpdateStrategy)
    {
        this.incrementalRuleEngineUpdateStrategy = incrementalRuleEngineUpdateStrategy;
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


    protected long getWorkerPreDestroyTimeout()
    {
        return this.workerPreDestroyTimeout;
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


    protected KieModuleService getKieModuleService()
    {
        return this.kieModuleService;
    }


    @Required
    public void setKieModuleService(KieModuleService kieModuleService)
    {
        this.kieModuleService = kieModuleService;
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
}
