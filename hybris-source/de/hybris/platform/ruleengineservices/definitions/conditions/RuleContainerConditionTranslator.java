package de.hybris.platform.ruleengineservices.definitions.conditions;

import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionValidator;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrEmptyCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleContainerConditionTranslator implements RuleConditionTranslator, RuleConditionValidator
{
    public static final String ID_PARAM = "id";
    public static final String NO_CHILDREN = "rule.validation.error.container.children.notexist";
    private RuleConditionsTranslator ruleConditionsTranslator;
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;


    public void validate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        if(CollectionUtils.isNotEmpty(condition.getChildren()))
        {
            this.ruleConditionsTranslator.validate(context, condition.getChildren());
        }
        else
        {
            context.addProblem(this.ruleCompilerProblemFactory.createProblem(RuleCompilerProblem.Severity.ERROR, "rule.validation.error.container.children.notexist", new Object[] {conditionDefinition
                            .getName()}));
        }
    }


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        RuleParameterData idParameter = (RuleParameterData)condition.getParameters().get("id");
        if(idParameter == null)
        {
            return (RuleIrCondition)new RuleIrEmptyCondition();
        }
        String id = (String)idParameter.getValue();
        if(id == null)
        {
            return (RuleIrCondition)new RuleIrEmptyCondition();
        }
        RuleIrVariablesGenerator variablesGenerator = context.getVariablesGenerator();
        try
        {
            variablesGenerator.createContainer(id);
            RuleIrGroupOperator irOperator = RuleIrGroupOperator.AND;
            List<RuleIrCondition> irChildren = this.ruleConditionsTranslator.translate(context, condition.getChildren());
            RuleIrGroupCondition irGroupCondition = new RuleIrGroupCondition();
            irGroupCondition.setOperator(irOperator);
            irGroupCondition.setChildren(irChildren);
            return (RuleIrCondition)irGroupCondition;
        }
        finally
        {
            variablesGenerator.closeContainer();
        }
    }


    public RuleConditionsTranslator getRuleConditionsTranslator()
    {
        return this.ruleConditionsTranslator;
    }


    @Required
    public void setRuleConditionsTranslator(RuleConditionsTranslator ruleConditionsTranslator)
    {
        this.ruleConditionsTranslator = ruleConditionsTranslator;
    }


    public RuleCompilerProblemFactory getRuleCompilerProblemFactory()
    {
        return this.ruleCompilerProblemFactory;
    }


    @Required
    public void setRuleCompilerProblemFactory(RuleCompilerProblemFactory ruleCompilerProblemFactory)
    {
        this.ruleCompilerProblemFactory = ruleCompilerProblemFactory;
    }
}
