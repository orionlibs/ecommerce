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
import de.hybris.platform.ruleengineservices.compiler.RuleIrNotCondition;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class RuleQualifyingCategoriesConditionTranslator extends AbstractRuleConditionTranslator
{
    public static final String EXCLUDED_CATEGORIES_PARAM = "excluded_categories";
    public static final String EXCLUDED_PRODUCTS_PARAM = "excluded_products";


    public RuleIrCondition translate(RuleCompilerContext context, RuleConditionData condition, RuleConditionDefinitionData conditionDefinition)
    {
        Map<String, RuleParameterData> conditionParameters = condition.getParameters();
        RuleParameterData operatorParameter = conditionParameters.get("operator");
        RuleParameterData quantityParameter = conditionParameters.get("quantity");
        RuleParameterData categoriesOperatorParameter = conditionParameters.get("categories_operator");
        RuleParameterData categoriesParameter = conditionParameters.get("categories");
        RuleParameterData excludedCategoriesParameter = conditionParameters.get("excluded_categories");
        RuleParameterData excludedProductsParameter = conditionParameters.get("excluded_products");
        if(verifyAllPresent(new Object[] {operatorParameter, quantityParameter, categoriesOperatorParameter, categoriesParameter}))
        {
            AmountOperator operator = (AmountOperator)operatorParameter.getValue();
            Integer quantity = (Integer)quantityParameter.getValue();
            CollectionOperator categoriesOperator = (CollectionOperator)categoriesOperatorParameter.getValue();
            List<String> categories = (List<String>)categoriesParameter.getValue();
            if(verifyAllPresent(new Object[] {operator, quantity, categoriesOperator, categories}))
            {
                RuleIrGroupCondition irQualifyingCategoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
                addQualifyingCategoriesConditions(context, operator, quantity, categoriesOperator, categories, irQualifyingCategoriesCondition);
                if(!CollectionOperator.NOT_CONTAINS.equals(categoriesOperator))
                {
                    addExcludedProductsAndCategoriesConditions(context, excludedCategoriesParameter, excludedProductsParameter, irQualifyingCategoriesCondition);
                }
                return (RuleIrCondition)irQualifyingCategoriesCondition;
            }
        }
        return (RuleIrCondition)IrConditions.empty();
    }


    protected void addQualifyingCategoriesConditions(RuleCompilerContext context, AmountOperator operator, Integer quantity, CollectionOperator categoriesOperator, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition)
    {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        String cartRaoVariable = context.generateVariable(CartRAO.class);
        List<RuleIrCondition> irConditions = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(categories))
        {
            RuleIrGroupCondition categoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.OR).build();
            for(String category : categories)
            {
                categoriesCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                                .withAttribute("categoryCodes").withOperator(RuleIrAttributeOperator.CONTAINS)
                                .withValue(category).build());
            }
            irConditions.add(categoriesCondition);
        }
        irConditions.add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("quantity")
                        .withOperator(RuleIrAttributeOperator.valueOf(operator.name()))
                        .withValue(quantity).build());
        irConditions.add(RuleIrAttributeRelConditionBuilder.newAttributeRelationConditionFor(cartRaoVariable).withAttribute("entries")
                        .withOperator(RuleIrAttributeOperator.CONTAINS)
                        .withTargetVariable(orderEntryRaoVariable)
                        .build());
        irConditions.addAll(getConsumptionSupport().newProductConsumedCondition(context, orderEntryRaoVariable));
        evaluateCategoriesOperator(context, categoriesOperator, categories, irQualifyingCategoriesCondition, irConditions);
    }


    protected void evaluateCategoriesOperator(RuleCompilerContext context, CollectionOperator categoriesOperator, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition, List<RuleIrCondition> irConditions)
    {
        if(CollectionOperator.NOT_CONTAINS.equals(categoriesOperator))
        {
            RuleIrNotCondition irNotProductCondition = new RuleIrNotCondition();
            irNotProductCondition.setChildren(irConditions);
            irQualifyingCategoriesCondition.getChildren().add(irNotProductCondition);
        }
        else
        {
            irQualifyingCategoriesCondition.getChildren().addAll(irConditions);
            if(CollectionOperator.CONTAINS_ALL.equals(categoriesOperator))
            {
                addContainsAllCategoriesConditions(context, categories, irQualifyingCategoriesCondition);
            }
        }
    }


    protected void addContainsAllCategoriesConditions(RuleCompilerContext context, List<String> categories, RuleIrGroupCondition irQualifyingCategoriesCondition)
    {
        if(CollectionUtils.isNotEmpty(categories))
        {
            String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
            RuleIrGroupCondition categoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
            for(String category : categories)
            {
                categoriesCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                                .withAttribute("categoryCodes").withOperator(RuleIrAttributeOperator.CONTAINS)
                                .withValue(category).build());
            }
            RuleIrExistsCondition irExistsCategoryCondition = new RuleIrExistsCondition();
            irExistsCategoryCondition.setChildren(Arrays.asList(new RuleIrCondition[] {(RuleIrCondition)categoriesCondition}));
            irQualifyingCategoriesCondition.getChildren().add(irExistsCategoryCondition);
        }
    }


    protected void addExcludedProductsAndCategoriesConditions(RuleCompilerContext context, RuleParameterData excludedCategoriesParameter, RuleParameterData excludedProductsParameter, RuleIrGroupCondition irQualifyingCategoriesCondition)
    {
        String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
        if(excludedCategoriesParameter != null && CollectionUtils.isNotEmpty((Collection)excludedCategoriesParameter.getValue()))
        {
            RuleIrGroupCondition irExcludedCategoriesCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
            List<String> categories = (List<String>)excludedCategoriesParameter.getValue();
            for(String category : categories)
            {
                irExcludedCategoriesCondition.getChildren().add(RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable)
                                .withAttribute("categoryCodes")
                                .withOperator(RuleIrAttributeOperator.NOT_CONTAINS)
                                .withValue(category)
                                .build());
            }
            irQualifyingCategoriesCondition.getChildren().add(irExcludedCategoriesCondition);
        }
        if(excludedProductsParameter != null && CollectionUtils.isNotEmpty((Collection)excludedProductsParameter.getValue()))
        {
            RuleIrGroupCondition baseProductNotOrGroupCondition = RuleIrGroupConditionBuilder.newGroupConditionOf(RuleIrGroupOperator.AND).build();
            List<String> products = (List<String>)excludedProductsParameter.getValue();
            baseProductNotOrGroupCondition.getChildren().add(
                            RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("productCode")
                                            .withOperator(RuleIrAttributeOperator.NOT_IN).withValue(products).build());
            for(String product : products)
            {
                baseProductNotOrGroupCondition.getChildren().add(
                                RuleIrAttributeConditionBuilder.newAttributeConditionFor(orderEntryRaoVariable).withAttribute("baseProductCodes")
                                                .withOperator(RuleIrAttributeOperator.NOT_CONTAINS).withValue(product).build());
            }
            irQualifyingCategoriesCondition.getChildren().add(baseProductNotOrGroupCondition);
        }
    }
}
