package de.hybris.platform.ruledefinitions.conditions;

import com.google.common.collect.Lists;
import de.hybris.platform.core.enums.GroupType;
import de.hybris.platform.ruledefinitions.MembershipOperator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrNotConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryGroupRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RuleEntryGroupTypeConditionTranslator extends AbstractRuleConditionTranslator
{
    protected static final String GROUP_TYPES_PARAM = "groupTypes";
    protected static final String ORDER_ENTRY_RAO_ENTRY_GROUP_NUMBERS_ATTRIBUTE = "entryGroupNumbers";
    protected static final String ORDER_ENTRY_GROUP_RAO_ENTRY_GROUP_ID_ATTRIBUTE = "entryGroupId";
    protected static final String ORDER_ENTRY_GROUP_RAO_GROUP_TYPE_ATTRIBUTE = "groupType";


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData operatorParameter = conditionParameters.get("operator");
        RuleParameterData groupTypeParameter = conditionParameters.get("groupTypes");
        if(verifyAllPresent(new Object[] {operatorParameter, groupTypeParameter}))
        {
            MembershipOperator operator = (MembershipOperator)operatorParameter.getValue();
            List<GroupType> value = (List<GroupType>)groupTypeParameter.getValue();
            if(verifyAllPresent(new Object[] {operator, value}))
            {
                return getEntryGroupTypeConditions(context, operator, value);
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected RuleIrCondition getEntryGroupTypeConditions(RuleCompilerContext context, MembershipOperator operator, List<GroupType> value)
    {
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String orderEntryGroupRaoVariable = context.generateVariable(OrderEntryGroupRAO.class);
        List<RuleIrCondition> conditions = Lists.newArrayList();
        RuleIrTypeCondition orderEntryRAOTypeCondition = new RuleIrTypeCondition();
        orderEntryRAOTypeCondition.setVariable(orderEntryRaoVariable);
        conditions.add(orderEntryRAOTypeCondition);
        conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(orderEntryGroupRaoVariable)
                        .withAttribute("entryGroupId")
                        .withOperator(RuleIrAttributeOperator.MEMBER_OF)
                        .withTargetVariable(orderEntryRaoVariable)
                        .withTargetAttribute("entryGroupNumbers")
                        .build());
        conditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryGroupRaoVariable)
                        .withAttribute("groupType")
                        .withOperator(RuleIrAttributeOperator.IN)
                        .withValue(value)
                        .build());
        conditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable)
                        .withAttribute("entries")
                        .withOperator(RuleIrAttributeOperator.CONTAINS)
                        .withTargetVariable(orderEntryRaoVariable)
                        .build());
        return groupCondition(operator, conditions);
    }


    protected RuleIrCondition groupCondition(MembershipOperator operator, List<RuleIrCondition> conditions)
    {
        List<RuleIrCondition> childrenConditions = (operator == MembershipOperator.NOT_IN) ? (List)Collections.singletonList(RuleIrNotConditionBuilder.newNotCondition().withChildren(conditions).build()) : conditions;
        return (RuleIrCondition)RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).withChildren(childrenConditions).build();
    }
}
