package de.hybris.platform.ruleengine.init.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.cache.KIEModuleCacheBuilder;
import de.hybris.platform.ruleengine.cache.RuleEngineCacheService;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.drools.KieModuleService;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;
import de.hybris.platform.ruleengine.init.RuleEngineBootstrap;
import de.hybris.platform.ruleengine.init.RuleEngineContainerRegistry;
import de.hybris.platform.ruleengine.init.RuleEngineKieModuleSwapper;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIESessionModel;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.drools.compiler.kie.builder.impl.KieContainerImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineBootstrap implements RuleEngineBootstrap<KieServices, KieContainer, DroolsKIEModuleModel>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleEngineBootstrap.class);
    private RulesModuleDao rulesModuleDao;
    private RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper;
    private RuleEngineCacheService ruleEngineCacheService;
    private RuleEngineContainerRegistry<ReleaseId, KieContainer> ruleEngineContainerRegistry;
    private KieModuleService kieModuleService;


    public KieServices getEngineServices()
    {
        return KieServices.get();
    }


    public RuleEngineActionResult startup(String moduleName)
    {
        Preconditions.checkArgument(Objects.nonNull(moduleName), "Module name should be provided");
        DroolsKIEModuleModel rulesModule = (DroolsKIEModuleModel)getRulesModuleDao().findByName(moduleName);
        RuleEngineActionResult result = new RuleEngineActionResult();
        result.setActionFailed(true);
        if(Objects.nonNull(rulesModule))
        {
            result.setActionFailed(false);
            String releaseIdAsString = getRuleEngineKieModuleSwapper().getReleaseId(rulesModule).toExternalForm();
            Optional<KieModule> restoredKieModule = getKieModuleService().loadKieModule(moduleName, releaseIdAsString);
            if(restoredKieModule.isPresent())
            {
                LOGGER.info("Restored KieModule for name {} and releaseId {}", moduleName, releaseIdAsString);
                KIEModuleCacheBuilder cache = getRuleEngineCacheService().createKIEModuleCacheBuilder(rulesModule);
                Collection<DroolsKIEBaseModel> kieBases = rulesModule.getKieBases();
                kieBases.forEach(kb -> getRuleEngineKieModuleSwapper().addRulesToCache(kb, cache));
                KieContainer kieContainer = getRuleEngineKieModuleSwapper().initializeNewKieContainer(rulesModule, restoredKieModule.get(), result);
                warmUpRuleEngineContainer(rulesModule, kieContainer);
                activateNewRuleEngineContainer(kieContainer, cache, result, rulesModule, null);
            }
            else
            {
                Pair<KieModule, KIEModuleCacheBuilder> kieModuleCacheBuilderPair = getRuleEngineKieModuleSwapper().createKieModule(rulesModule, result);
                KieContainer kieContainer = getRuleEngineKieModuleSwapper().initializeNewKieContainer(rulesModule, (KieModule)kieModuleCacheBuilderPair.getLeft(), result);
                warmUpRuleEngineContainer(rulesModule, kieContainer);
                activateNewRuleEngineContainer(kieContainer, (KIEModuleCacheBuilder)kieModuleCacheBuilderPair.getRight(), result, rulesModule, null);
            }
        }
        return result;
    }


    public void activateNewRuleEngineContainer(KieContainer kieContainer, KIEModuleCacheBuilder cache, RuleEngineActionResult ruleEngineActionResult, DroolsKIEModuleModel rulesModule, String deployedReleaseIdVersion)
    {
        LOGGER.debug("Activating Kie Container [{}] for module [{}] and deployed version [{}]", new Object[] {kieContainer, rulesModule.getName(), deployedReleaseIdVersion});
        ReleaseId releaseId = ((KieContainerImpl)kieContainer).getContainerReleaseId();
        Optional<ReleaseId> deployedReleaseId = getRuleEngineKieModuleSwapper().getDeployedReleaseId(rulesModule, deployedReleaseIdVersion);
        LOGGER.debug("Invoking module swapper process of Kie Module activation for module [{}]", rulesModule.getName());
        String deployedMvnVersion = getRuleEngineKieModuleSwapper().activateKieModule(rulesModule);
        LOGGER.info("The new rule module with deployedMvnVersion [{}] was activated successfully", rulesModule
                        .getDeployedMvnVersion());
        LOGGER.info("Swapping to a newly created kie container [{}]", releaseId);
        getRuleEngineContainerRegistry().setActiveContainer(releaseId, kieContainer);
        getRuleEngineCacheService().addToCache(cache);
        Objects.requireNonNull(getRuleEngineContainerRegistry());
        deployedReleaseId.filter(r -> !releaseId.getVersion().equals(r.getVersion())).ifPresent(getRuleEngineContainerRegistry()::removeActiveContainer);
        ruleEngineActionResult.setDeployedVersion(deployedMvnVersion);
    }


    public void warmUpRuleEngineContainer(DroolsKIEModuleModel rulesModule, KieContainer rulesContainer)
    {
        Preconditions.checkArgument(Objects.nonNull(rulesContainer), "rulesContainer should not be null");
        Collection<DroolsKIEBaseModel> kieBases = rulesModule.getKieBases();
        if(CollectionUtils.isNotEmpty(kieBases) && kieBases.size() == 1)
        {
            DroolsKIEBaseModel kieBase = kieBases.iterator().next();
            DroolsKIESessionModel defaultKIESession = kieBase.getDefaultKIESession();
            if(Objects.nonNull(defaultKIESession))
            {
                String kieSessionName = defaultKIESession.getName();
                if(DroolsSessionType.STATEFUL.equals(defaultKIESession.getSessionType()))
                {
                    LOGGER.debug("Initializing and disposing the session to optimize the tree...");
                    rulesContainer.newKieSession(kieSessionName).dispose();
                }
                else
                {
                    LOGGER.debug("Initializing the stateless session to optimize the tree...");
                    rulesContainer.newStatelessKieSession(kieSessionName);
                }
            }
        }
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


    protected RuleEngineKieModuleSwapper getRuleEngineKieModuleSwapper()
    {
        return this.ruleEngineKieModuleSwapper;
    }


    @Required
    public void setRuleEngineKieModuleSwapper(RuleEngineKieModuleSwapper ruleEngineKieModuleSwapper)
    {
        this.ruleEngineKieModuleSwapper = ruleEngineKieModuleSwapper;
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


    protected RuleEngineContainerRegistry<ReleaseId, KieContainer> getRuleEngineContainerRegistry()
    {
        return this.ruleEngineContainerRegistry;
    }


    @Required
    public void setRuleEngineContainerRegistry(RuleEngineContainerRegistry<ReleaseId, KieContainer> ruleEngineContainerRegistry)
    {
        this.ruleEngineContainerRegistry = ruleEngineContainerRegistry;
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
}
