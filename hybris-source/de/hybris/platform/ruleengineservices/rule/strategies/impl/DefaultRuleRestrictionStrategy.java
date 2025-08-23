package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RulePublishRestriction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.List;

public class DefaultRuleRestrictionStrategy implements RulePublishRestriction
{
    private static final String RULE_PUBLISH_LIMITATION_ENABLED = "ruleengineservices.maximum.limitation.published.rules.enable";
    private static final String MAXIMUM_PUBLISHED_RULES = "ruleengineservices.maximum.published.rules";
    private static final int MAXIMUM_PUBLISHED_RULES_DEFAULT_VALUE = 100;
    private ConfigurationService configurationService;
    private EngineRuleDao engineRuleDao;


    public boolean isAllowedToPublish(String moduleName, int sourceRulesSize)
    {
        boolean isFeatureEnabled = getConfigurationService().getConfiguration()
                        .getBoolean("ruleengineservices.maximum.limitation.published.rules.enable", Boolean.FALSE).booleanValue();
        if(isFeatureEnabled)
        {
            List<AbstractRuleEngineRuleModel> publishedRules = getEngineRuleDao().getActiveRules(moduleName);
            return
                            (publishedRules.size() + sourceRulesSize <= getConfigurationService().getConfiguration().getInt("ruleengineservices.maximum.published.rules", 100));
        }
        return true;
    }


    public EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
