package de.hybris.platform.ruledefinitions.conditions;

import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.rao.CustomerSupportRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

public class RuleCustomerSupportConditionTranslator extends AbstractRuleConditionTranslator
{
    protected static final String ASSISTED_SERVICE_SESSION_ACTIVE_PARAM = "value";
    protected static final String CUSTOMER_SUPPORT_RAO_CUSTOMER_EMULATION_ACTIVE_ATTRIBUTE = "customerEmulationActive";


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        RuleParameterData assistedServiceSessionActiveParameter = (RuleParameterData)condition.getParameters().get("value");
        if(verifyAllPresent(new Object[] {assistedServiceSessionActiveParameter}))
        {
            Boolean assistedServiceSessionActive = (Boolean)assistedServiceSessionActiveParameter.getValue();
            if(verifyAllPresent(new Object[] {assistedServiceSessionActive}))
            {
                return (RuleIrCondition)RuleIrAttributeConditionBuilder.newAttributeConditionFor(context.generateVariable(CustomerSupportRAO.class))
                                .withAttribute("customerEmulationActive")
                                .withOperator(RuleIrAttributeOperator.EQUAL)
                                .withValue(assistedServiceSessionActive)
                                .build();
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }
}
