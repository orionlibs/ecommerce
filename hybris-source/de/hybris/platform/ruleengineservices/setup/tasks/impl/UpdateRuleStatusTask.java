package de.hybris.platform.ruleengineservices.setup.tasks.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.setup.tasks.MigrationTask;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class UpdateRuleStatusTask implements MigrationTask
{
    private static final Logger LOG = LoggerFactory.getLogger(UpdateRuleStatusTask.class);
    protected static final String SELECT_RULES_BY_STATUS = "SELECT {Pk} FROM {AbstractRule} WHERE {status} = ?status AND {version} is null";
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;


    public void execute(SystemSetupContext systemSetupContext)
    {
        LOG.info("Task - Updating Source Rules status");
        List<AbstractRuleModel> updatedRules = Lists.newArrayList();
        updatedRules.addAll(changeStatus(RuleStatus.PUBLISHED, RuleStatus.INACTIVE));
        updatedRules.addAll(changeStatus(RuleStatus.MODIFIED, RuleStatus.UNPUBLISHED));
        getModelService().saveAll(updatedRules);
    }


    protected List<AbstractRuleModel> changeStatus(RuleStatus fromStatus, RuleStatus toStatus)
    {
        return (List<AbstractRuleModel>)selectRulesByStatus(fromStatus).map(rule -> changeStatus((AbstractRuleModel)rule, toStatus)).collect(Collectors.toList());
    }


    protected AbstractRuleModel changeStatus(AbstractRuleModel rule, RuleStatus status)
    {
        rule.setStatus(status);
        return rule;
    }


    protected Stream<SourceRuleModel> selectRulesByStatus(RuleStatus status)
    {
        ImmutableMap immutableMap = ImmutableMap.of("status", status);
        return getFlexibleSearchService().search("SELECT {Pk} FROM {AbstractRule} WHERE {status} = ?status AND {version} is null", (Map)immutableMap).getResult().stream();
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
