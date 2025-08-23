package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.constants.RuleEngineServicesConstants;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class LinkSourceRulesTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(LinkSourceRulesTask.class);
    private EngineRuleDao engineRuleDao;
    private RuleDao ruleDao;
    private ModelService modelService;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Linking source rules with engine rules");
        List<AbstractRuleEngineRuleModel> linkedEngineRules = (List<AbstractRuleEngineRuleModel>)selectRules(new RuleStatus[] {RuleStatus.INACTIVE}).map(this::updateMapping).filter(Objects::nonNull).collect(Collectors.toList());
        getModelService().saveAll(linkedEngineRules);
    }


    protected AbstractRuleEngineRuleModel updateMapping(SourceRuleModel rule)
    {
        AbstractRuleEngineRuleModel ruleEngineRule = getEngineRuleDao().getRuleByUuid(rule.getUuid());
        if(null != ruleEngineRule)
        {
            ruleEngineRule.setSourceRule((AbstractRuleModel)rule);
        }
        return ruleEngineRule;
    }


    protected Stream<SourceRuleModel> selectRules(RuleStatus... statuses)
    {
        return getRuleDao().findByVersionAndStatuses(RuleEngineServicesConstants.DEFAULT_RULE_VERSION, statuses).stream();
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


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
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
}
