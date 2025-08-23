package de.hybris.platform.ruleengineservices.compiler.impl;

import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleActionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionsTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslator;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.services.RuleActionsService;
import de.hybris.platform.ruleengineservices.rule.services.RuleConditionsService;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueNormalizerStrategy;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourceRuleSourceCodeTranslator implements RuleSourceCodeTranslator
{
    private RuleConditionsService ruleConditionsService;
    private RuleActionsService ruleActionsService;
    private RuleConditionsTranslator ruleConditionsTranslator;
    private RuleActionsTranslator ruleActionsTranslator;
    private RuleCompilerProblemFactory ruleCompilerProblemFactory;
    private RuleParameterValueNormalizerStrategy ruleParameterValueNormalizerStrategy;


    public RuleIr translate(RuleCompilerContext context)
    {
        try
        {
            if(!(context.getRule() instanceof SourceRuleModel))
            {
                throw new RuleCompilerException("Rule is not of type SourceRule");
            }
            SourceRuleModel rule = (SourceRuleModel)context.getRule();
            RuleIr ruleIr = new RuleIr();
            ruleIr.setVariablesContainer(context.getVariablesGenerator().getRootContainer());
            List<RuleConditionData> ruleConditions = getRuleConditionsService().convertConditionsFromString(rule.getConditions(), context
                            .getConditionDefinitions());
            populateRuleParametersFromConditions(context, ruleConditions);
            addRuleConditionsToContext(context, ruleConditions);
            List<RuleActionData> ruleActions = getRuleActionsService().convertActionsFromString(rule.getActions(), context
                            .getActionDefinitions());
            populateRuleParametersFromActions(context, ruleActions);
            List<RuleIrCondition> ruleIrConditions = getRuleConditionsTranslator().translate(context, ruleConditions);
            ruleIr.setConditions(ruleIrConditions);
            List<RuleIrAction> ruleIrActions = getRuleActionsTranslator().translate(context, ruleActions);
            ruleIr.setActions(ruleIrActions);
            validate(context, ruleConditions, ruleActions);
            return ruleIr;
        }
        catch(RuleEngineServiceException e)
        {
            throw new RuleCompilerException(e);
        }
    }


    protected void addRuleConditionsToContext(RuleCompilerContext context, List<RuleConditionData> ruleConditions)
    {
        if(CollectionUtils.isEmpty(ruleConditions))
        {
            return;
        }
        for(RuleConditionData condition : ruleConditions)
        {
            context.getRuleConditions().add(condition);
            addRuleConditionsToContext(context, condition.getChildren());
        }
    }


    protected void populateRuleParametersFromConditions(RuleCompilerContext context, List<RuleConditionData> conditions)
    {
        if(CollectionUtils.isEmpty(conditions))
        {
            return;
        }
        for(RuleConditionData condition : conditions)
        {
            if(MapUtils.isNotEmpty(condition.getParameters()))
            {
                for(RuleParameterData parameter : condition.getParameters().values())
                {
                    normalizeRuleParameter(parameter);
                    context.getRuleParameters().add(parameter);
                }
            }
            populateRuleParametersFromConditions(context, condition.getChildren());
        }
    }


    protected void populateRuleParametersFromActions(RuleCompilerContext context, List<RuleActionData> actions)
    {
        if(CollectionUtils.isEmpty(actions))
        {
            return;
        }
        for(RuleActionData action : actions)
        {
            if(MapUtils.isNotEmpty(action.getParameters()))
            {
                for(RuleParameterData parameter : action.getParameters().values())
                {
                    normalizeRuleParameter(parameter);
                    context.getRuleParameters().add(parameter);
                }
            }
        }
    }


    protected void normalizeRuleParameter(RuleParameterData parameter)
    {
        parameter.setValue(getRuleParameterValueNormalizerStrategy().normalize(parameter.getValue(), parameter.getType()));
    }


    protected void validate(RuleCompilerContext context, List<RuleConditionData> conditions, List<RuleActionData> actions)
    {
        if(CollectionUtils.isEmpty(actions))
        {
            RuleCompilerProblem problem = getRuleCompilerProblemFactory().createProblem(RuleCompilerProblem.Severity.ERROR, "rule.compiler.error.actions.empty", new Object[0]);
            context.addProblem(problem);
            return;
        }
        getRuleConditionsTranslator().validate(context, conditions);
        getRuleActionsTranslator().validate(context, actions);
    }


    protected RuleConditionsService getRuleConditionsService()
    {
        return this.ruleConditionsService;
    }


    @Required
    public void setRuleConditionsService(RuleConditionsService ruleConditionsService)
    {
        this.ruleConditionsService = ruleConditionsService;
    }


    public RuleActionsService getRuleActionsService()
    {
        return this.ruleActionsService;
    }


    @Required
    public void setRuleActionsService(RuleActionsService ruleActionsService)
    {
        this.ruleActionsService = ruleActionsService;
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


    protected RuleActionsTranslator getRuleActionsTranslator()
    {
        return this.ruleActionsTranslator;
    }


    @Required
    public void setRuleActionsTranslator(RuleActionsTranslator ruleActionsTranslator)
    {
        this.ruleActionsTranslator = ruleActionsTranslator;
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


    protected RuleParameterValueNormalizerStrategy getRuleParameterValueNormalizerStrategy()
    {
        return this.ruleParameterValueNormalizerStrategy;
    }


    @Required
    public void setRuleParameterValueNormalizerStrategy(RuleParameterValueNormalizerStrategy ruleParameterValueNormalizerStrategy)
    {
        this.ruleParameterValueNormalizerStrategy = ruleParameterValueNormalizerStrategy;
    }
}
