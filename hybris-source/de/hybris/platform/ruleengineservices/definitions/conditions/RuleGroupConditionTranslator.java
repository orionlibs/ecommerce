package de.hybris.platform.ruleengineservices.definitions.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleGroupConditionTranslator implements RuleConditionTranslator, RuleConditionValidator
{
    public static final String OPERATOR_PARAM = "operator";
    private RuleConditionsTranslator ruleConditionsTranslator;


    public RuleConditionsTranslator getRuleConditionsTranslator()
    {
        return this.ruleConditionsTranslator;
    }


    @Required
    public void setRuleConditionsTranslator(RuleConditionsTranslator ruleConditionsTranslator)
    {
        this.ruleConditionsTranslator = ruleConditionsTranslator;
    }


    public void validate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        if(CollectionUtils.isNotEmpty(condition.getChildren()))
        {
            this.ruleConditionsTranslator.validate(context, condition.getChildren());
        }
    }


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        RuleGroupOperator operator = null;
        RuleParameterData operatorParameter = (RuleParameterData)condition.getParameters().get("operator");
        if(operatorParameter != null && operatorParameter.getValue() != null)
        {
            operator = (RuleGroupOperator)operatorParameter.getValue();
        }
        else
        {
            operator = RuleGroupOperator.AND;
        }
        RuleIrGroupOperator irOperator = RuleIrGroupOperator.valueOf(operator.name());
        List<RuleIrCondition> irChildren = this.ruleConditionsTranslator.translate(context, condition.getChildren());
        RuleIrGroupCondition irGroupCondition = new RuleIrGroupCondition();
        irGroupCondition.setOperator(irOperator);
        irGroupCondition.setChildren(irChildren);
        return (RuleIrCondition)irGroupCondition;
    }
}
