package de.hybris.platform.ruleengineservices.maintenance.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.ruleengine.concurrency.SuspendResumeTaskManager;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.init.ConcurrentMapFactory;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilationContext;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationListener;

public class DefaultRuleCompilationContext implements RuleCompilationContext
{
    public static final String WORKER_PRE_DESTROY_TIMEOUT = "ruleengineservices.compiler.task.predestroytimeout";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleCompilationContext.class);
    private Tenant currentTenant;
    private ThreadFactory threadFactory;
    private RuleCompilerService ruleCompilerService;
    private ConfigurationService configurationService;
    private RuleEngineSpliteratorStrategy ruleEngineSpliteratorStrategy;
    private RulesModuleDao rulesModuleDao;
    private EventService eventService;
    private ConcurrentMapFactory concurrentMapFactory;
    private SuspendResumeTaskManager suspendResumeTaskManager;
    private Map<String, AtomicLong> ruleVersionForModules;
    private Map<String, RulesCompilationInProgressApplicationListener> listeners;


    public Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    public ThreadFactory getThreadFactory()
    {
        return this.threadFactory;
    }


    public RuleCompilerService getRuleCompilerService()
    {
        return this.ruleCompilerService;
    }


    public int getNumberOfThreads()
    {
        return getRuleEngineSpliteratorStrategy().getNumberOfThreads();
    }


    public Long getThreadTimeout()
    {
        return Long.valueOf(getConfigurationService().getConfiguration().getLong("ruleengineservices.compiler.task.predestroytimeout", 3600000L));
    }


    public AtomicLong resetRuleEngineRuleVersion(String moduleName)
    {
        AbstractRulesModuleModel moduleModel = getRulesModuleDao().findByName(moduleName);
        LOGGER.debug("Resetting the module version to [{}]", moduleModel.getVersion());
        AtomicLong initVal = new AtomicLong(moduleModel.getVersion().longValue() + 1L);
        if(Objects.nonNull(this.ruleVersionForModules.putIfAbsent(moduleName, initVal)))
        {
            this.ruleVersionForModules.replace(moduleName, initVal);
        }
        synchronized(this)
        {
            AtomicLong moduleVersion = this.ruleVersionForModules.get(moduleName);
            moduleVersion.set(moduleModel.getVersion().longValue() + 1L);
            return moduleVersion;
        }
    }


    public Long getNextRuleEngineRuleVersion(String moduleName)
    {
        LOGGER.debug("Getting next rule version for module [{}]", moduleName);
        AtomicLong moduleVersion = this.ruleVersionForModules.get(moduleName);
        if(Objects.isNull(moduleVersion))
        {
            moduleVersion = resetRuleEngineRuleVersion(moduleName);
        }
        return Long.valueOf(moduleVersion.getAndAdd(1L));
    }


    public synchronized void cleanup(String moduleName)
    {
        Preconditions.checkArgument(Objects.nonNull(moduleName), "Module name should be specified");
        synchronized(this)
        {
            RulesCompilationInProgressApplicationListener listener = this.listeners.get(moduleName);
            if(Objects.nonNull(listener))
            {
                this.listeners.remove(moduleName);
                getEventService().unregisterEventListener((ApplicationListener)listener);
            }
        }
    }


    public synchronized void registerCompilationListeners(String moduleName)
    {
        Preconditions.checkArgument(Objects.nonNull(moduleName), "Module name should be specified");
        synchronized(this)
        {
            RulesCompilationInProgressApplicationListener listener = this.listeners.get(moduleName);
            if(Objects.isNull(listener))
            {
                listener = new RulesCompilationInProgressApplicationListener(this, moduleName);
                getEventService().registerEventListener((ApplicationListener)listener);
                this.listeners.put(moduleName, listener);
            }
        }
    }


    @PostConstruct
    public void setUp()
    {
        this.ruleVersionForModules = getConcurrentMapFactory().createNew();
        this.listeners = getConcurrentMapFactory().createNew();
    }


    @Required
    public void setCurrentTenant(Tenant currentTenant)
    {
        this.currentTenant = currentTenant;
    }


    @Required
    public void setThreadFactory(ThreadFactory threadFactory)
    {
        this.threadFactory = threadFactory;
    }


    @Required
    public void setRuleCompilerService(RuleCompilerService ruleCompilerService)
    {
        this.ruleCompilerService = ruleCompilerService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected RuleEngineSpliteratorStrategy getRuleEngineSpliteratorStrategy()
    {
        return this.ruleEngineSpliteratorStrategy;
    }


    @Required
    public void setRuleEngineSpliteratorStrategy(RuleEngineSpliteratorStrategy ruleEngineSpliteratorStrategy)
    {
        this.ruleEngineSpliteratorStrategy = ruleEngineSpliteratorStrategy;
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


    protected EventService getEventService()
    {
        return this.eventService;
    }


    @Required
    public void setEventService(EventService eventService)
    {
        this.eventService = eventService;
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


    public SuspendResumeTaskManager getSuspendResumeTaskManager()
    {
        return this.suspendResumeTaskManager;
    }


    @Required
    public void setSuspendResumeTaskManager(SuspendResumeTaskManager suspendResumeTaskManager)
    {
        this.suspendResumeTaskManager = suspendResumeTaskManager;
    }
}
