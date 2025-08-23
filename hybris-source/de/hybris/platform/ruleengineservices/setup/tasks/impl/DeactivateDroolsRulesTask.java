package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DeactivateDroolsRulesTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(DeactivateDroolsRulesTask.class);
    protected static final String FIND_QUALIFYING_RULES = "SELECT {engine_rule:Pk} FROM {AbstractRuleEngineRule as engine_rule JOIN AbstractRule as rule ON {rule:uuid}={engine_rule:uuid}} ";
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Deactivating existing drools rules");
        List<AbstractRuleEngineRuleModel> sourcedDroolsRules = getSourcedDroolsRules();
        List<AbstractRuleEngineRuleModel> modifiedRules = (List<AbstractRuleEngineRuleModel>)sourcedDroolsRules.stream().filter(isRuleValidToProcess()).map(this::deactivate).collect(Collectors.toList());
        getModelService().saveAll(modifiedRules);
    }


    protected Predicate<AbstractRuleEngineRuleModel> isRuleValidToProcess()
    {
        return rule -> (BooleanUtils.isTrue(rule.getActive()) && BooleanUtils.isTrue(rule.getCurrentVersion()));
    }


    protected List<AbstractRuleEngineRuleModel> getSourcedDroolsRules()
    {
        return getFlexibleSearchService()
                        .search("SELECT {engine_rule:Pk} FROM {AbstractRuleEngineRule as engine_rule JOIN AbstractRule as rule ON {rule:uuid}={engine_rule:uuid}} ").getResult();
    }


    protected AbstractRuleEngineRuleModel deactivate(AbstractRuleEngineRuleModel ruleEngineRule)
    {
        ruleEngineRule.setActive(Boolean.FALSE);
        ruleEngineRule.setCurrentVersion(Boolean.FALSE);
        return ruleEngineRule;
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


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
