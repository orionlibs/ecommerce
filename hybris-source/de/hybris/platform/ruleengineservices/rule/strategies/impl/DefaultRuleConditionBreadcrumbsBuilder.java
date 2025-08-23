package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.definitions.conditions.RuleGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConditionBreadcrumbsBuilder;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleConditionBreadcrumbsBuilder extends AbstractRuleBreadcrumbsBuilder implements RuleConditionBreadcrumbsBuilder
{
    protected static final String GROUP_CONDITION_DEFINITION_ID = "y_group";
    protected static final String GROUP_CONDITION_OPERATOR_PARAM = "operator";
    protected static final String GROUP_CONDITION_OPERATOR_TYPE = "Enum(de.hybris.platform.ruleengineservices.definitions.conditions.RuleGroupOperator)";
    protected static final RuleGroupOperator DEFAULT_GROUP_OPERATOR = RuleGroupOperator.AND;
    protected static final String PARENT_CONDITION_CLASS_PREFIX = "rule-parent-condition rule-parent-condition-";
    private I18NService i18NService;


    public String buildConditionBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return buildBreadcrumbs(conditions, conditionDefinitions, false);
    }


    public String buildStyledConditionBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions)
    {
        return buildBreadcrumbs(conditions, conditionDefinitions, true);
    }


    protected String buildBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions, boolean styled)
    {
        ServicesUtil.validateParameterNotNull(conditions, "conditions cannot be null");
        ServicesUtil.validateParameterNotNull(conditionDefinitions, "condition definitions cannot be null");
        StringBuilder breadcrumbBuilder = new StringBuilder();
        Locale locale = this.i18NService.getCurrentLocale();
        String separator = buildSeparator(null, conditionDefinitions, locale, styled);
        buildBreadcrumbsHelper(conditions, conditionDefinitions, styled, "", "", separator, true, locale, breadcrumbBuilder);
        return breadcrumbBuilder.toString();
    }


    protected void buildBreadcrumbsHelper(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions, boolean styled, String prefix, String suffix, String separator, boolean isRootParent, Locale locale, StringBuilder breadcrumbBuilder)
    {
        if(CollectionUtils.isEmpty(conditions))
        {
            return;
        }
        List<RuleConditionBreadcrumbData> conditionBreadcrumbs = extractConditionBreadcrumbs(conditions, conditionDefinitions, locale, styled);
        int conditionBreadcrumbsSize = conditionBreadcrumbs.size();
        boolean requiresPrefixAndSuffix = (isRootParent || conditionBreadcrumbsSize != 1);
        if(requiresPrefixAndSuffix)
        {
            breadcrumbBuilder.append(prefix);
        }
        int index = 0;
        for(RuleConditionBreadcrumbData conditionBreadcrumb : conditionBreadcrumbs)
        {
            RuleConditionData condition = conditionBreadcrumb.getCondition();
            RuleConditionDefinitionData conditionDefinition = conditionBreadcrumb.getConditionDefinition();
            String breadcrumb = conditionBreadcrumb.getBreadcrumb();
            boolean isGroupCondition = conditionBreadcrumb.isGroupCondition();
            if(index != 0)
            {
                breadcrumbBuilder.append(separator);
            }
            if(BooleanUtils.isTrue(conditionDefinition.getAllowsChildren()))
            {
                String styleClass = "rule-parent-condition rule-parent-condition-" + condition.getDefinitionId();
                String childrenSeparator = buildSeparator(condition, conditionDefinitions, locale, styled);
                if(isGroupCondition && isRootParent && conditionBreadcrumbsSize == 1)
                {
                    String childrenPrefix = "";
                    String childrenSuffix = "";
                    buildBreadcrumbsHelper(condition.getChildren(), conditionDefinitions, styled, "", "", childrenSeparator, false, locale, breadcrumbBuilder);
                }
                else if(isGroupCondition)
                {
                    String childrenPrefix = decorateValue("(", styleClass, styled);
                    String childrenSuffix = decorateValue(")", styleClass, styled);
                    buildBreadcrumbsHelper(condition.getChildren(), conditionDefinitions, styled, childrenPrefix, childrenSuffix, childrenSeparator, false, locale, breadcrumbBuilder);
                }
                else
                {
                    String childrenPrefix = decorateValue(breadcrumb + " (", styleClass, styled);
                    String childrenSuffix = decorateValue(")", styleClass, styled);
                    buildBreadcrumbsHelper(condition.getChildren(), conditionDefinitions, styled, childrenPrefix, childrenSuffix, childrenSeparator, true, locale, breadcrumbBuilder);
                }
            }
            else
            {
                breadcrumbBuilder.append(breadcrumb);
            }
            index++;
        }
        if(requiresPrefixAndSuffix)
        {
            breadcrumbBuilder.append(suffix);
        }
    }


    protected List<RuleConditionBreadcrumbData> extractConditionBreadcrumbs(List<RuleConditionData> conditions, Map<String, RuleConditionDefinitionData> conditionDefinitions, Locale locale, boolean styled)
    {
        List<RuleConditionBreadcrumbData> conditionBreadcrumbs = new ArrayList<>();
        for(RuleConditionData condition : conditions)
        {
            RuleConditionDefinitionData conditionDefinition = conditionDefinitions.get(condition.getDefinitionId());
            if(conditionDefinition == null)
            {
                throw new RuleEngineServiceException("No condition definition found for id " + condition.getDefinitionId());
            }
            boolean decorated = (styled && BooleanUtils.isFalse(conditionDefinition.getAllowsChildren()));
            String breadcrumb = formatBreadcrumb(conditionDefinition.getBreadcrumb(), condition.getParameters(), locale, styled, decorated);
            boolean isGroupCondition = isGroupCondition(condition);
            if(StringUtils.isNotBlank(breadcrumb) || isGroupCondition)
            {
                RuleConditionBreadcrumbData conditionBreadcrumb = new RuleConditionBreadcrumbData();
                conditionBreadcrumb.setCondition(condition);
                conditionBreadcrumb.setConditionDefinition(conditionDefinition);
                conditionBreadcrumb.setBreadcrumb(breadcrumb);
                conditionBreadcrumb.setGroupCondition(isGroupCondition);
                conditionBreadcrumbs.add(conditionBreadcrumb);
            }
        }
        return conditionBreadcrumbs;
    }


    protected String buildSeparator(RuleConditionData parentCondition, Map<String, RuleConditionDefinitionData> conditionDefinitions, Locale locale, boolean styled)
    {
        String separator;
        RuleConditionDefinitionData groupConditionDefinition = conditionDefinitions.get("y_group");
        if(isGroupCondition(parentCondition))
        {
            separator = formatBreadcrumb(groupConditionDefinition.getBreadcrumb(), parentCondition.getParameters(), locale, false, true);
        }
        else
        {
            RuleParameterData operatorParameter = new RuleParameterData();
            operatorParameter.setValue(DEFAULT_GROUP_OPERATOR);
            operatorParameter.setType("Enum(de.hybris.platform.ruleengineservices.definitions.conditions.RuleGroupOperator)");
            Map<String, RuleParameterData> parameters = Collections.singletonMap("operator", operatorParameter);
            separator = formatBreadcrumb(groupConditionDefinition.getBreadcrumb(), parameters, locale, false, true);
        }
        String styleClass = "rule-parent-condition rule-parent-condition-y_group";
        return decorateValue(" " + separator + " ", "rule-parent-condition rule-parent-condition-y_group", styled);
    }


    protected boolean isGroupCondition(RuleConditionData condition)
    {
        if(condition == null)
        {
            return false;
        }
        return "y_group".equals(condition.getDefinitionId());
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
