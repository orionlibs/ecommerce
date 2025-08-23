package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.constants.RuleEngineServicesConstants;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class UpdateSourceRuleVersionTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(UpdateSourceRuleVersionTask.class);
    private ModelService modelService;
    private RuleDao ruleDao;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Updating Source Rules version");
        List<AbstractRuleModel> allRules = getRuleDao().findAllRules();
        allRules.stream().forEach(this::changeVersion);
        getModelService().saveAll(allRules);
    }


    protected void changeVersion(AbstractRuleModel rule)
    {
        if(rule.getVersion() == null)
        {
            rule.setVersion(RuleEngineServicesConstants.DEFAULT_RULE_VERSION);
        }
    }


    public RuleDao getRuleDao()
    {
        return this.ruleDao;
    }


    @Required
    public void setRuleDao(RuleDao ruleDao)
    {
        this.ruleDao = ruleDao;
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
}
