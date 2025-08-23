package de.hybris.platform.personalizationpromotions.translator;

import de.hybris.platform.personalizationpromotions.rao.CxPromotionActionResultRAO;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;

public class RuleCxPromotionActionResultTranslator implements RuleConditionTranslator
{
    private static final String ATTRIBUTE = "promotionId";


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        String cxPromotionActionResultVariable = context.generateVariable(CxPromotionActionResultRAO.class);
        RuleIrAttributeCondition irCondition = new RuleIrAttributeCondition();
        irCondition.setVariable(cxPromotionActionResultVariable);
        irCondition.setAttribute("promotionId");
        irCondition.setOperator(RuleIrAttributeOperator.EQUAL);
        irCondition.setValue(context.getRule().getCode());
        return (RuleIrCondition)irCondition;
    }
}
