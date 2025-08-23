package de.hybris.platform.ruledefinitions.conditions;

import com.google.common.collect.Lists;
import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruledefinitions.conditions.builders.IrConditions;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrAttributeRelConditionBuilder;
import de.hybris.platform.ruledefinitions.conditions.builders.RuleIrGroupConditionBuilder;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExistsCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrLocalVariablesContainer;
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.List;
import java.util.Map;

public class RuleQualifyingProductsConditionTranslator extends AbstractRuleConditionTranslator
{
    public static final String PRODUCTS_OPERATOR_PARAM = "products_operator";


    public final RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData operatorParameter = conditionParameters.get("operator");
        RuleParameterData quantityParameter = conditionParameters.get("quantity");
        RuleParameterData productsOperatorParameter = conditionParameters.get("products_operator");
        RuleParameterData productsParameter = conditionParameters.get("products");
        if(verifyAllPresent(new Object[] {operatorParameter, quantityParameter, productsOperatorParameter, productsParameter}))
        {
            AmountOperator operator = (AmountOperator)operatorParameter.getValue();
            Integer quantity = (Integer)quantityParameter.getValue();
            CollectionOperator productsOperator = (CollectionOperator)productsOperatorParameter.getValue();
            List<String> products = (List<String>)productsParameter.getValue();
            if(verifyAllPresent(new Object[] {operator, quantity, productsOperator, products}))
            {
                return (RuleIrCondition)getQualifyingProductsCondition(context, operator, quantity, productsOperator, products);
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected RuleIrGroupCondition getQualifyingProductsCondition(RuleCompilerContext context, AmountOperator operator, Integer quantity, CollectionOperator productsOperator, List<String> products)
    {
        RuleIrGroupCondition irQualifyingProductsCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
        addQualifyingProductsCondition(context, operator, quantity, productsOperator, products, irQualifyingProductsCondition);
        return irQualifyingProductsCondition;
    }


    protected void addQualifyingProductsCondition(RuleCompilerContext context, AmountOperator operator, Integer quantity, CollectionOperator productsOperator, List<String> products, RuleIrGroupCondition irQualifyingProductsCondition)
    {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        List<RuleIrCondition> irConditions = Lists.newArrayList();
        RuleIrGroupCondition baseProductOrGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
        baseProductOrGroupCondition.getChildren().add(
                        RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("productCode")
                                        .withOperator(RuleIrAttributeOperator.IN).withValue(products).build());
        for(String product : products)
        {
            baseProductOrGroupCondition.getChildren().add(
                            RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("baseProductCodes")
                                            .withOperator(RuleIrAttributeOperator.CONTAINS).withValue(product).build());
        }
        irConditions.add(baseProductOrGroupCondition);
        irConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                        .withAttribute("quantity")
                        .withOperator(RuleIrAttributeOperator.valueOf(operator.name()))
                        .withValue(quantity)
                        .build());
        irConditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable)
                        .withAttribute("entries")
                        .withOperator(RuleIrAttributeOperator.CONTAINS)
                        .withTargetVariable(orderEntryRaoVariable)
                        .build());
        evaluateProductsOperator(context, operator, quantity, productsOperator, products, irQualifyingProductsCondition, irConditions, orderEntryRaoVariable);
    }


    protected void evaluateProductsOperator(RuleCompilerContext context, AmountOperator operator, Integer quantity, CollectionOperator productsOperator, List<String> products, RuleIrGroupCondition irQualifyingProductsCondition, List<RuleIrCondition> irConditions, String orderEntryRaoVariable)
    {
        if(CollectionOperator.CONTAINS_ANY.equals(productsOperator))
        {
            irConditions.addAll(getConsumptionSupport().newProductConsumedCondition(context, orderEntryRaoVariable));
        }
        if(CollectionOperator.NOT_CONTAINS.equals(productsOperator))
        {
            RuleIrNotCondition irNotProductCondition = new RuleIrNotCondition();
            irNotProductCondition.setChildren(irConditions);
            irQualifyingProductsCondition.getChildren().add(irNotProductCondition);
        }
        else
        {
            irQualifyingProductsCondition.getChildren().addAll(irConditions);
            if(CollectionOperator.CONTAINS_ALL.equals(productsOperator))
            {
                addContainsAllProductsConditions(context, operator, quantity, products, irQualifyingProductsCondition);
            }
        }
    }


    protected void addContainsAllProductsConditions(RuleCompilerContext context, AmountOperator operator, Integer quantity, List<String> products, RuleIrGroupCondition irQualifyingProductsCondition)
    {
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        for(String product : products)
        {
            RuleIrLocalVariablesContainer variablesContainer = context.createLocalContainer();
            String containsOrderEntryRaoVariable = context.generateLocalVariable(variablesContainer, OrderEntryRAO.class);
            List<RuleIrCondition> irConditions = Lists.newArrayList();
            irConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(containsOrderEntryRaoVariable)
                            .withAttribute("productCode")
                            .withOperator(RuleIrAttributeOperator.EQUAL)
                            .withValue(product)
                            .build());
            irConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(containsOrderEntryRaoVariable)
                            .withAttribute("quantity")
                            .withOperator(RuleIrAttributeOperator.valueOf(operator.name()))
                            .withValue(quantity)
                            .build());
            irConditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable)
                            .withAttribute("entries")
                            .withOperator(RuleIrAttributeOperator.CONTAINS)
                            .withTargetVariable(containsOrderEntryRaoVariable)
                            .build());
            irConditions.addAll(getConsumptionSupport().newProductConsumedCondition(context, containsOrderEntryRaoVariable));
            RuleIrExistsCondition irExistsProductCondition = new RuleIrExistsCondition();
            irExistsProductCondition.setVariablesContainer(variablesContainer);
            irExistsProductCondition.setChildren(irConditions);
            irQualifyingProductsCondition.getChildren().add(irExistsProductCondition);
        }
    }
}
