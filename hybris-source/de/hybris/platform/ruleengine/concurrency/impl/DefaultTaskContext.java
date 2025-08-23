package de.hybris.platform.ruleengine.concurrency.impl;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.ruleengine.concurrency.RuleEngineSpliteratorStrategy;
import de.hybris.platform.ruleengine.concurrency.TaskContext;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.init.ConcurrentMapFactory;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.concurrent.ThreadFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTaskContext implements TaskContext
{
    private static final String WORKER_PRE_DESTROY_TIMEOUT = "ruleengine.task.predestroytimeout";
    private Tenant currentTenant;
    private ThreadFactory threadFactory;
    private ConfigurationService configurationService;
    private RulesModuleDao rulesModuleDao;
    private ConcurrentMapFactory concurrentMapFactory;
    private RuleEngineSpliteratorStrategy ruleEngineSpliteratorStrategy;


    public Tenant getCurrentTenant()
    {
        return this.currentTenant;
    }


    public ThreadFactory getThreadFactory()
    {
        return this.threadFactory;
    }


    public int getNumberOfThreads()
    {
        return getRuleEngineSpliteratorStrategy().getNumberOfThreads();
    }


    public Long getThreadTimeout()
    {
        return Long.valueOf(getConfigurationService().getConfiguration()
                        .getLong("ruleengine.task.predestroytimeout", 3600000L));
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
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
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


    protected RuleEngineSpliteratorStrategy getRuleEngineSpliteratorStrategy()
    {
        return this.ruleEngineSpliteratorStrategy;
    }


    @Required
    public void setRuleEngineSpliteratorStrategy(RuleEngineSpliteratorStrategy ruleEngineSpliteratorStrategy)
    {
        this.ruleEngineSpliteratorStrategy = ruleEngineSpliteratorStrategy;
    }
}
