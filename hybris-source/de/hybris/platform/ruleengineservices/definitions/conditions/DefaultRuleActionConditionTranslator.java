package de.hybris.platform.ruleengineservices.definitions.conditions;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleActionConditionTranslator implements RuleConditionTranslator
{
    public static final String FIRED_RULE_CODE_ATTRIBUTE = "firedRuleCode";
    public static final String RULE_PARAM = "rule";
    public static final String ALLOWED_PARAM = "allowed";
    private RuleConditionsTranslator ruleConditionsTranslator;
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Preconditions.checkNotNull(context, "Rule Compiler Context is not expected to be NULL here");
        Preconditions.checkNotNull(condition, "Rule Condition Data is not expected to be NULL here");
        RuleParameterData ruleParameter = (RuleParameterData)condition.getParameters().get("rule");
        RuleParameterData allowedParameter = (RuleParameterData)condition.getParameters().get("allowed");
        if(ruleParameter == null || allowedParameter == null)
        {
            return (RuleIrCondition)new RuleIrEmptyCondition();
        }
        String referencedRuleCode = (String)ruleParameter.getValue();
        Boolean referencedRuleActionAllowed = (Boolean)allowedParameter.getValue();
        if(referencedRuleCode == null || referencedRuleActionAllowed == null)
        {
            return (RuleIrCondition)new RuleIrEmptyCondition();
        }
        return translate(context, referencedRuleCode, referencedRuleActionAllowed);
    }


    protected RuleIrCondition translate(RuleCompilerContext context, String referencedRuleCode, Boolean referencedRuleActionAllowed)
    {
        RuleIrAttributeCondition irReferencedRuleActionCondition = new RuleIrAttributeCondition();
        irReferencedRuleActionCondition.setAttribute("firedRuleCode");
        irReferencedRuleActionCondition.setOperator(RuleIrAttributeOperator.EQUAL);
        irReferencedRuleActionCondition.setValue(referencedRuleCode);
        if(referencedRuleActionAllowed.booleanValue())
        {
            String str = context.generateVariable(AbstractRuleActionRAO.class);
            irReferencedRuleActionCondition.setVariable(str);
            return (RuleIrCondition)irReferencedRuleActionCondition;
        }
        RuleIrLocalVariablesContainer variablesContainer = context.createLocalContainer();
        String ruleActionRaoVariable = context.generateLocalVariable(variablesContainer, AbstractRuleActionRAO.class);
        irReferencedRuleActionCondition.setVariable(ruleActionRaoVariable);
        RuleIrNotCondition irNotReferencedRuleActionCondition = new RuleIrNotCondition();
        irNotReferencedRuleActionCondition.setVariablesContainer(variablesContainer);
        irNotReferencedRuleActionCondition.setChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)irReferencedRuleActionCondition}));
        return (RuleIrCondition)irNotReferencedRuleActionCondition;
    }


    protected RuleConditionsTranslator getRuleConditionsTranslator()
    {
        return this.ruleConditionsTranslator;
    }


    @Required
    public void setRuleConditionsTranslator(RuleConditionsTranslator ruleConditionsTranslator)
    {
        this.ruleConditionsTranslator = ruleConditionsTranslator;
    }


    protected RuleCompilerProblemFactory getRuleCompilerProblemFactory()
    {
        return this.ruleCompilerProblemFactory;
    }


    @Required
    public void setRuleCompilerProblemFactory(RuleCompilerProblemFactory ruleCompilerProblemFactory)
    {
        this.ruleCompilerProblemFactory = ruleCompilerProblemFactory;
    }
}
