package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.dao.RulesModuleDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengineservices.constants.RuleEngineServicesConstants;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.maintenance.RuleMaintenanceService;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.rule.services.RuleService;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ReInitializeRuleModulesTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(ReInitializeRuleModulesTask.class);
    private RuleEngineService ruleEngineService;
    private RuleService ruleService;
    private RulesModuleDao rulesModuleDao;
    private ConfigurationService configurationService;
    private RuleMaintenanceService ruleMaintenanceService;
    private RuleDao ruleDao;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Re-initializing active rule module");
        List<AbstractRuleModel> rulesToPublish = getRuleDao().findByVersionAndStatuses(RuleEngineServicesConstants.DEFAULT_RULE_VERSION, new RuleStatus[] {RuleStatus.INACTIVE});
        Objects.requireNonNull(SourceRuleModel.class);
        Objects.requireNonNull(SourceRuleModel.class);
        List<SourceRuleModel> sourceRulesToPublish = (List<SourceRuleModel>)rulesToPublish.stream().filter(SourceRuleModel.class::isInstance).map(SourceRuleModel.class::cast).collect(Collectors.toList());
        Map<RuleType, List<SourceRuleModel>> sourceRulesToPublishByType = (Map<RuleType, List<SourceRuleModel>>)sourceRulesToPublish.stream().collect(
                        Collectors.groupingBy(rule -> getRuleService().getEngineRuleTypeForRuleType(rule.getClass())));
        for(Map.Entry<RuleType, List<SourceRuleModel>> ruleToTypeEntry : sourceRulesToPublishByType.entrySet())
        {
            List<AbstractRulesModuleModel> activeModules = getRulesModuleDao().findActiveRulesModulesByRuleType(ruleToTypeEntry
                            .getKey());
            if(CollectionUtils.isNotEmpty(activeModules))
            {
                AbstractRulesModuleModel liveModule = activeModules.iterator().next();
                getRuleMaintenanceService().compileAndPublishRulesWithBlocking(sourceRulesToPublishByType
                                .get(ruleToTypeEntry.getKey()), liveModule
                                .getName(), true);
            }
        }
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected RuleMaintenanceService getRuleMaintenanceService()
    {
        return this.ruleMaintenanceService;
    }


    @Required
    public void setRuleMaintenanceService(RuleMaintenanceService ruleMaintenanceService)
    {
        this.ruleMaintenanceService = ruleMaintenanceService;
    }


    protected RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
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
}
