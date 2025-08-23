package de.hybris.platform.ruleengine.event;

import de.hybris.platform.ruleengine.ExecutionContext;
import de.hybris.platform.ruleengine.InitializeMode;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class OnRuleEngineInitializedEventListener extends AbstractEventListener<RuleEngineInitializedEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(OnRuleEngineInitializedEventListener.class);
    private RuleEngineService platformRuleEngineService;
    private RulesModuleDao rulesModuleDao;
    private ConfigurationService configurationService;


    protected void onEvent(RuleEngineInitializedEvent ruleEngineInitializedEvent)
    {
        if(getConfigurationService().getConfiguration().getBoolean("ruleengine.engine.active", true))
        {
            String rulesModuleName = ruleEngineInitializedEvent.getRulesModuleName();
            LOG.info("RuleEngineInitializedEvent received. Beginning initialization of the rule engine for being restored module [{}]", rulesModuleName);
            AbstractRulesModuleModel rulesModel = getRulesModuleDao().findByName(rulesModuleName);
            RuleEngineActionResult result = new RuleEngineActionResult();
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.setInitializeMode(InitializeMode.RESTORE);
            getPlatformRuleEngineService().initialize(rulesModel, ruleEngineInitializedEvent.getDeployedReleaseIdVersion(), false, false, result, executionContext);
        }
    }


    protected RuleEngineService getPlatformRuleEngineService()
    {
        return this.platformRuleEngineService;
    }


    @Required
    public void setPlatformRuleEngineService(RuleEngineService ruleEngineService)
    {
        this.platformRuleEngineService = ruleEngineService;
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
