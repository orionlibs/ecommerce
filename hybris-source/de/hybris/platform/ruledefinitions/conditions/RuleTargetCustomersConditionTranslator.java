package de.hybris.platform.ruledefinitions.conditions;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrNotConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeRelCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import de.hybris.platform.ruleengineservices.rao.UserRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleTargetCustomersConditionTranslator extends AbstractRuleConditionTranslator
{
    public static final String CUSTOMER_GROUPS_OPERATOR_PARAM = "customer_groups_operator";
    public static final String CUSTOMER_GROUPS_PARAM = "customer_groups";
    public static final String CUSTOMERS_PARAM = "customers";
    public static final String EXCLUDED_CUSTOMER_GROUPS_PARAM = "excluded_customer_groups";
    public static final String EXCLUDED_USERS_PARAM = "excluded_customers";
    public static final String USER_GROUP_RAO_ID_ATTRIBUTE = "id";
    public static final String USER_RAO_ID_ATTRIBUTE = "id";
    public static final String USER_RAO_PK_ATTRIBUTE = "pk";
    public static final String USER_RAO_GROUPS_ATTRIBUTE = "groups";
    public static final String CART_RAO_USER_ATTRIBUTE = "user";
    private boolean usePk;


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData customerGroupsOperatorParameter = conditionParameters.get("customer_groups_operator");
        RuleParameterData customerGroupsParameter = conditionParameters.get("customer_groups");
        RuleParameterData customersParameter = conditionParameters.get("customers");
        RuleParameterData excludedCustomerGroupsParameter = conditionParameters.get("excluded_customer_groups");
        RuleParameterData excludedCustomersParameter = conditionParameters.get("excluded_customers");
        if(verifyAnyPresent(new Object[] {customerGroupsParameter, customersParameter}))
        {
            CollectionOperator customerGroupsOperator = (CollectionOperator)customerGroupsOperatorParameter.getValue();
            List<String> customerGroups = Objects.isNull(customerGroupsParameter) ? Collections.<String>emptyList() : (List<String>)customerGroupsParameter.getValue();
            List<String> customers = Objects.isNull(customersParameter) ? Collections.<String>emptyList() : (List<String>)customersParameter.getValue();
            if(verifyAllPresent(new Object[] {customerGroupsOperator}) && verifyAnyPresent(new Object[] {customerGroups, customers}))
            {
                RuleIrGroupCondition irTargetCustomersCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
                addTargetCustomersConditions(context, customerGroupsOperator, customerGroups, customers, irTargetCustomersCondition);
                if(!CollectionOperator.NOT_CONTAINS.equals(customerGroupsOperator))
                {
                    addExcludedCustomersAndCustomerGroupsConditions(context, excludedCustomerGroupsParameter, excludedCustomersParameter, irTargetCustomersCondition);
                }
                return (RuleIrCondition)irTargetCustomersCondition;
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected void addTargetCustomersConditions(RuleCompilerContext context, CollectionOperator customerGroupsOperator, List<String> customerGroups, List<String> customers, RuleIrGroupCondition irTargetCustomersCondition)
    {
        String userRaoVariable = context.generateVariable(UserRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        List<RuleIrCondition> irConditions = Lists.newArrayList();
        RuleIrTypeCondition irUserCondition = new RuleIrTypeCondition();
        irUserCondition.setVariable(userRaoVariable);
        RuleIrAttributeRelCondition irCartUserRel = RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable).withAttribute("user").withOperator(RuleIrAttributeOperator.EQUAL).withTargetVariable(userRaoVariable).build();
        irConditions.add(irUserCondition);
        irConditions.add(irCartUserRel);
        RuleIrGroupCondition irCustomerGroupsCondition = getCustomerGroupConditions(context, customerGroupsOperator, customerGroups);
        RuleIrAttributeCondition irCustomersCondition = getCustomerConditions(context, customers);
        if(verifyAllPresent(new Object[] {irCustomerGroupsCondition, irCustomersCondition}))
        {
            RuleIrGroupCondition userAndUserGroupGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
            RuleIrTypeCondition irUserGroupCondition = new RuleIrTypeCondition();
            irUserGroupCondition.setVariable(context.generateVariable(UserGroupRAO.class));
            userAndUserGroupGroupCondition.setChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)irCustomersCondition, (RuleIrCondition)irUserGroupCondition}));
            RuleIrGroupCondition groupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
            groupCondition.setChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)irCustomerGroupsCondition, (RuleIrCondition)userAndUserGroupGroupCondition}));
            irConditions.add(groupCondition);
        }
        else if(Objects.nonNull(irCustomerGroupsCondition))
        {
            irConditions.add(irCustomerGroupsCondition);
        }
        else if(Objects.nonNull(irCustomersCondition))
        {
            irConditions.add(irCustomersCondition);
        }
        if(CollectionOperator.NOT_CONTAINS.equals(customerGroupsOperator))
        {
            irTargetCustomersCondition.getChildren().add(RuleIrNotConditionBuilder.newNotCondition().withChildren(irConditions).build());
        }
        else
        {
            irTargetCustomersCondition.getChildren().addAll(irConditions);
        }
    }


    protected RuleIrGroupCondition getCustomerGroupConditions(RuleCompilerContext context, CollectionOperator customerGroupsOperator, List<String> customerGroups)
    {
        RuleIrGroupCondition irCustomerGroupsCondition = null;
        if(CollectionUtils.isNotEmpty(customerGroups))
        {
            String userRaoVariable = context.generateVariable(UserRAO.class);
            String userGroupRaoVariable = context.generateVariable(UserGroupRAO.class);
            List<RuleIrCondition> irCustomerGroupsConditions = Lists.newArrayList();
            RuleIrAttributeCondition irUserGroupCondition = RuleIrAttributeConditionBuilder.newAttributeConditionFor(userGroupRaoVariable).withAttribute("id").withOperator(RuleIrAttributeOperator.IN).withValue(customerGroups).build();
            RuleIrAttributeRelCondition irUserUserGroupRel = RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(userRaoVariable).withAttribute("groups").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(userGroupRaoVariable).build();
            irCustomerGroupsConditions.add(irUserGroupCondition);
            irCustomerGroupsConditions.add(irUserUserGroupRel);
            addContainsAllCustomerGroupConditions(context, customerGroupsOperator, customerGroups, irCustomerGroupsConditions);
            irCustomerGroupsCondition = new RuleIrGroupCondition();
            irCustomerGroupsCondition.setOperator(RuleIrGroupOperator.AND);
            irCustomerGroupsCondition.setChildren(irCustomerGroupsConditions);
        }
        return irCustomerGroupsCondition;
    }


    protected void addContainsAllCustomerGroupConditions(RuleCompilerContext context, CollectionOperator customerGroupsOperator, List<String> customerGroups, List<RuleIrCondition> irCustomerGroupsConditions)
    {
        if(CollectionOperator.CONTAINS_ALL.equals(customerGroupsOperator))
        {
            String userRaoVariable = context.generateVariable(UserRAO.class);
            for(String customerGroup : customerGroups)
            {
                RuleIrLocalVariablesContainer variablesContainer = context.createLocalContainer();
                String containsUserGroupRaoVariable = context.generateLocalVariable(variablesContainer, UserGroupRAO.class);
                RuleIrAttributeCondition irContainsUserGroupCondition = RuleIrAttributeConditionBuilder.newAttributeConditionFor(containsUserGroupRaoVariable).withAttribute("id").withOperator(RuleIrAttributeOperator.EQUAL).withValue(customerGroup).build();
                RuleIrAttributeRelCondition irContainsUserUserGroupRel = RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(userRaoVariable).withAttribute("groups").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(containsUserGroupRaoVariable).build();
                RuleIrExistsCondition irContainsCustomerGroupsCondition = new RuleIrExistsCondition();
                irContainsCustomerGroupsCondition.setVariablesContainer(variablesContainer);
                irContainsCustomerGroupsCondition
                                .setChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)irContainsUserGroupCondition, (RuleIrCondition)irContainsUserUserGroupRel}));
                irCustomerGroupsConditions.add(irContainsCustomerGroupsCondition);
            }
        }
    }


    protected RuleIrAttributeCondition getCustomerConditions(RuleCompilerContext context, List<String> customers)
    {
        RuleIrAttributeCondition irCustomersCondition = null;
        if(CollectionUtils.isNotEmpty(customers))
        {
            irCustomersCondition = RuleIrAttributeConditionBuilder.newAttributeConditionFor(context.generateVariable(UserRAO.class)).withAttribute(getUserRAOAttribute()).withOperator(RuleIrAttributeOperator.IN).withValue(customers).build();
        }
        return irCustomersCondition;
    }


    protected void addExcludedCustomersAndCustomerGroupsConditions(RuleCompilerContext context, RuleParameterData excludedCustomerGroupsParameter, RuleParameterData excludedCustomersParameter, RuleIrGroupCondition irTargetCustomersCondition)
    {
        String userRaoVariable = context.generateVariable(UserRAO.class);
        if(verifyAllPresent(new Object[] {excludedCustomerGroupsParameter, excludedCustomerGroupsParameter}) && CollectionUtils.isNotEmpty((Collection)excludedCustomerGroupsParameter
                        .getValue()))
        {
            RuleIrLocalVariablesContainer variablesContainer = context.createLocalContainer();
            String excludedUserGroupRaoVariable = context.generateLocalVariable(variablesContainer, UserGroupRAO.class);
            RuleIrAttributeCondition irExcludedUserGroupCondition = RuleIrAttributeConditionBuilder.newAttributeConditionFor(excludedUserGroupRaoVariable).withAttribute("id").withOperator(RuleIrAttributeOperator.IN).withValue(excludedCustomerGroupsParameter.getValue()).build();
            RuleIrAttributeRelCondition irExcludedUserUserGroupRel = RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(userRaoVariable).withAttribute("groups").withOperator(RuleIrAttributeOperator.CONTAINS).withTargetVariable(excludedUserGroupRaoVariable).build();
            RuleIrNotCondition irExcludedCustomerGroupsCondition = RuleIrNotConditionBuilder.newNotCondition().withVariablesContainer(variablesContainer).withChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)irExcludedUserGroupCondition, (RuleIrCondition)irExcludedUserUserGroupRel}))
                            .build();
            irTargetCustomersCondition.getChildren().add(irExcludedCustomerGroupsCondition);
        }
        if(verifyAllPresent(new Object[] {excludedCustomersParameter, excludedCustomersParameter.getValue()}))
        {
            irTargetCustomersCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(userRaoVariable)
                            .withAttribute(getUserRAOAttribute())
                            .withOperator(RuleIrAttributeOperator.NOT_IN)
                            .withValue(excludedCustomersParameter.getValue())
                            .build());
        }
    }


    protected String getUserRAOAttribute()
    {
        return isUsePk() ? "pk" : "id";
    }


    protected boolean isUsePk()
    {
        return this.usePk;
    }


    @Required
    public void setUsePk(boolean usePk)
    {
        this.usePk = usePk;
    }
}
