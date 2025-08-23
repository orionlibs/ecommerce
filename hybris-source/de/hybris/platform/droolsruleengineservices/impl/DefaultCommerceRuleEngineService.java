package de.hybris.platform.droolsruleengineservices.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.droolsruleengineservices.agendafilter.AgendaFilterFactory;
import de.hybris.platform.droolsruleengineservices.agendafilter.impl.DefaultRuleAndRuleGroupExecutionTracker;
import de.hybris.platform.droolsruleengineservices.eventlisteners.AgendaEventListenerFactory;
import de.hybris.platform.droolsruleengineservices.eventlisteners.ProcessEventListenerFactory;
import de.hybris.platform.droolsruleengineservices.eventlisteners.RuleRuntimeEventListenerFactory;
import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationContext;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.exception.DroolsRuleLoopException;
import de.hybris.platform.ruleengine.init.InitializationFuture;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.providers.FactContextFactory;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.ruleengineservices.rao.providers.impl.FactContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.impl.RuleAndRuleGroupExecutionTracker;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.drools.core.event.DebugAgendaEventListener;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.rule.AgendaEventListener;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.kie.api.runtime.rule.AgendaFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCommerceRuleEngineService implements RuleEngineService
{
    private RuleEngineService platformRuleEngineService;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCommerceRuleEngineService.class);
    private AgendaFilterFactory agendaFilterFactory;
    private AgendaEventListenerFactory agendaEventListenerFactory;
    private ProcessEventListenerFactory processEventListenerFactory;
    private RuleRuntimeEventListenerFactory ruleRuntimeEventListenerFactory;
    private ConfigurationService configurationService;
    private FactContextFactory factContextFactory;
    private EngineRuleDao engineRuleDao;


    public void initialize(AbstractRulesModuleModel module, String deployedMvnVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        getPlatformRuleEngineService()
                        .initialize(module, deployedMvnVersion, propagateToOtherNodes, enableIncrementalUpdate, result);
    }


    public void initialize(AbstractRulesModuleModel module, String deployedMvnVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result, ExecutionContext executionContext)
    {
        getPlatformRuleEngineService()
                        .initialize(module, deployedMvnVersion, propagateToOtherNodes, enableIncrementalUpdate, result, executionContext);
    }


    public void initializeNonBlocking(AbstractRulesModuleModel module, String deployedReleaseIdVersion, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, RuleEngineActionResult result)
    {
        getPlatformRuleEngineService()
                        .initializeNonBlocking(module, deployedReleaseIdVersion, propagateToOtherNodes, enableIncrementalUpdate, result);
    }


    public InitializationFuture initialize(List<AbstractRulesModuleModel> modules, boolean propagateToOtherNodes, boolean enableIncrementalUpdate)
    {
        return getPlatformRuleEngineService().initialize(modules, propagateToOtherNodes, enableIncrementalUpdate);
    }


    public InitializationFuture initialize(List<AbstractRulesModuleModel> modules, boolean propagateToOtherNodes, boolean enableIncrementalUpdate, ExecutionContext executionContext)
    {
        return getPlatformRuleEngineService().initialize(modules, propagateToOtherNodes, enableIncrementalUpdate, executionContext);
    }


    public RuleEvaluationResult evaluate(RuleEvaluationContext context)
    {
        ServicesUtil.validateParameterNotNull(context, "context must not be null");
        AbstractRuleEngineContextModel abstractREContext = context.getRuleEngineContext();
        ServicesUtil.validateParameterNotNull(abstractREContext, "context.ruleEngineContext must not be null");
        Preconditions.checkState(abstractREContext instanceof DroolsRuleEngineContextModel, "ruleEngineContext %s is not a DroolsRuleEngineContext. %s is not supported.", abstractREContext
                        .getName(), abstractREContext
                        .getItemtype());
        logContextFacts(context);
        DroolsRuleEngineContextModel ruleEngineContext = (DroolsRuleEngineContextModel)abstractREContext;
        try
        {
            RuleEngineResultRAO rao = addRuleEngineResultRAO(context);
            addDroolsRuleExecutionTracker(context);
            AgendaFilter agendaFilter = getAgendaFilterFactory().createAgendaFilter(abstractREContext);
            context.setFilter(agendaFilter);
            Set<Object> eventListeners = getEventListners(ruleEngineContext);
            context.setEventListeners(eventListeners);
            RuleEvaluationResult result = getPlatformRuleEngineService().evaluate(context);
            result.setResult(rao);
            return result;
        }
        catch(DroolsRuleLoopException ex)
        {
            LOGGER.error(ex.getMessage());
            throw ex;
        }
        catch(Exception ex)
        {
            String errorMessage = String.format("Rule evaluation failed with message '%s' for facts: %s.", new Object[] {ex.getMessage(),
                            Arrays.toString((context.getFacts() != null) ? context.getFacts().toArray() : null)});
            LOGGER.error(errorMessage, ex);
            RuleEvaluationResult result = new RuleEvaluationResult();
            result.setEvaluationFailed(true);
            result.setErrorMessage(errorMessage);
            result.setFacts(context.getFacts());
            return result;
        }
    }


    public <T extends AbstractRuleEngineRuleModel> void deactivateRulesModuleEngineRules(String moduleName, Collection<T> engineRules)
    {
        getPlatformRuleEngineService().deactivateRulesModuleEngineRules(moduleName, engineRules);
    }


    protected void logContextFacts(RuleEvaluationContext context)
    {
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Rule evaluation triggered with the facts: {}",
                            (context.getFacts() == null) ? "[]" : context.getFacts().toString());
        }
    }


    protected Set<Object> getEventListners(DroolsRuleEngineContextModel ruleEngineContext)
    {
        Set<Object> eventListeners = new LinkedHashSet();
        Set<AgendaEventListener> listeners = getAgendaEventListenerFactory().createAgendaEventListeners((AbstractRuleEngineContextModel)ruleEngineContext);
        if(CollectionUtils.isNotEmpty(listeners))
        {
            eventListeners.addAll(listeners);
        }
        if(getRuleRuntimeEventListenerFactory() != null)
        {
            Set<RuleRuntimeEventListener> ruleRuntimeEventlisteners = getRuleRuntimeEventListenerFactory().createRuleRuntimeEventListeners((AbstractRuleEngineContextModel)ruleEngineContext);
            if(CollectionUtils.isNotEmpty(ruleRuntimeEventlisteners))
            {
                eventListeners.addAll(ruleRuntimeEventlisteners);
            }
        }
        if(getProcessEventListenerFactory() != null)
        {
            Set<ProcessEventListener> processEventListeners = getProcessEventListenerFactory().createProcessEventListeners((AbstractRuleEngineContextModel)ruleEngineContext);
            if(CollectionUtils.isNotEmpty(processEventListeners))
            {
                eventListeners.addAll(processEventListeners);
            }
        }
        if(LOGGER.isDebugEnabled())
        {
            eventListeners.add(new DebugRuleRuntimeEventListener());
            eventListeners.add(new DebugAgendaEventListener());
        }
        return eventListeners;
    }


    protected RuleEngineResultRAO addRuleEngineResultRAO(RuleEvaluationContext context)
    {
        RuleEngineResultRAO rao = null;
        if(context.getFacts() == null)
        {
            LinkedHashSet<Object> facts = new LinkedHashSet();
            context.setFacts(facts);
        }
        for(Object fact : context.getFacts())
        {
            if(fact instanceof RuleEngineResultRAO)
            {
                rao = (RuleEngineResultRAO)fact;
            }
        }
        if(rao == null)
        {
            rao = new RuleEngineResultRAO();
            rao.setActions(new LinkedHashSet());
            Set<Object> facts = new LinkedHashSet(context.getFacts());
            facts.add(rao);
            context.setFacts(facts);
        }
        return rao;
    }


    protected RuleAndRuleGroupExecutionTracker addDroolsRuleExecutionTracker(RuleEvaluationContext context)
    {
        DefaultRuleAndRuleGroupExecutionTracker tracker = new DefaultRuleAndRuleGroupExecutionTracker();
        context.getFacts().add(tracker);
        return (RuleAndRuleGroupExecutionTracker)tracker;
    }


    protected Set<Object> provideRAOs(FactContext factContext)
    {
        Set<Object> result = new HashSet();
        for(Object modelFact : factContext.getFacts())
        {
            for(RAOProvider raoProvider : factContext.getProviders(modelFact))
            {
                result.addAll(raoProvider.expandFactModel(modelFact));
            }
        }
        return result;
    }


    public List<RuleEngineActionResult> initializeAllRulesModules()
    {
        return getPlatformRuleEngineService().initializeAllRulesModules();
    }


    public List<RuleEngineActionResult> initializeAllRulesModules(boolean propagateToOtherNodes)
    {
        return getPlatformRuleEngineService().initializeAllRulesModules(propagateToOtherNodes);
    }


    public RuleEngineActionResult updateEngineRule(AbstractRuleEngineRuleModel ruleEngineRule, AbstractRulesModuleModel rulesModule)
    {
        return getPlatformRuleEngineService().updateEngineRule(ruleEngineRule, rulesModule);
    }


    public <T extends de.hybris.platform.ruleengine.model.DroolsRuleModel> Optional<InitializationFuture> archiveRules(Collection<T> engineRules)
    {
        return getPlatformRuleEngineService().archiveRules(engineRules);
    }


    public AbstractRuleEngineRuleModel getRuleForCodeAndModule(String code, String moduleName)
    {
        return getPlatformRuleEngineService().getRuleForCodeAndModule(code, moduleName);
    }


    public AbstractRuleEngineRuleModel getRuleForUuid(String uuid)
    {
        return getPlatformRuleEngineService().getRuleForUuid(uuid);
    }


    protected AgendaFilterFactory getAgendaFilterFactory()
    {
        return this.agendaFilterFactory;
    }


    public void setAgendaFilterFactory(AgendaFilterFactory agendaFilterFactory)
    {
        this.agendaFilterFactory = agendaFilterFactory;
    }


    protected AgendaEventListenerFactory getAgendaEventListenerFactory()
    {
        return this.agendaEventListenerFactory;
    }


    @Required
    public void setAgendaEventListenerFactory(AgendaEventListenerFactory agendaEventListenerFactory)
    {
        this.agendaEventListenerFactory = agendaEventListenerFactory;
    }


    protected ProcessEventListenerFactory getProcessEventListenerFactory()
    {
        return this.processEventListenerFactory;
    }


    public void setProcessEventListenerFactory(ProcessEventListenerFactory processEventListenerFactory)
    {
        this.processEventListenerFactory = processEventListenerFactory;
    }


    protected RuleRuntimeEventListenerFactory getRuleRuntimeEventListenerFactory()
    {
        return this.ruleRuntimeEventListenerFactory;
    }


    public void setRuleRuntimeEventListenerFactory(RuleRuntimeEventListenerFactory ruleRuntimeEventListenerFactory)
    {
        this.ruleRuntimeEventListenerFactory = ruleRuntimeEventListenerFactory;
    }


    protected RuleEngineService getPlatformRuleEngineService()
    {
        return this.platformRuleEngineService;
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


    @Required
    public void setPlatformRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.platformRuleEngineService = ruleEngineService;
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


    protected FactContextFactory getFactContextFactory()
    {
        return this.factContextFactory;
    }


    @Required
    public void setFactContextFactory(FactContextFactory factContextFactory)
    {
        this.factContextFactory = factContextFactory;
    }
}
