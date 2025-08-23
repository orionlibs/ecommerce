package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.calculation.EntriesDiscountDistributeStrategy;
import de.hybris.platform.ruleengineservices.enums.FixedDiscountDistributeStrategy;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleSubsetProductAction;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class RuleSubsetOrderEntryPercentageDiscountRAOAction extends AbstractRuleSubsetProductAction
{
    public static final String SELECT_CURRENCY_PARAM = "select_currency";
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";
    public static final String TARGET_CONTAINERS_PARAM = "target_containers";


    protected EntriesDiscountDistributeStrategy getEntriesDiscountDistributeStrategy(FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy)
    {
        throw new UnsupportedOperationException("There is no need to distribute discount!");
    }


    protected boolean performActionInternal(RuleActionContext context)
    {
        BigDecimal percentageDiscount = (BigDecimal)context.getParameter("value");
        String selectCurrency = (String)context.getParameter("select_currency");
        Map<String, BigDecimal> qualifyingProductsContainers = (Map<String, BigDecimal>)context.getParameter("qualifying_containers");
        List<String> targetProductsContainers = (List<String>)context.getParameter("target_containers");
        CartRAO cart = (CartRAO)context.getValue(CartRAO.class);
        if(!selectCurrency.equals(cart.getCurrencyIsoCode()))
        {
            return false;
        }
        for(Map.Entry<String, BigDecimal> entry : qualifyingProductsContainers.entrySet())
        {
            String containerId = entry.getKey();
            BigDecimal threshold = entry.getValue();
            if(!hasReachedContainerThreshold(context, containerId, threshold))
            {
                return false;
            }
        }
        Set<OrderEntryRAO> targetOrderEntries = (Set<OrderEntryRAO>)targetProductsContainers.stream().map(containerId -> getOrderEntries(context, containerId)).flatMap(Collection::stream).collect(Collectors.toSet());
        Set<OrderEntryRAO> qualifyOrderEntries = (Set<OrderEntryRAO>)qualifyingProductsContainers.keySet().stream().map(containerId -> getOrderEntries(context, containerId)).flatMap(Collection::stream).filter(ent -> !targetOrderEntries.contains(ent)).collect(Collectors.toSet());
        return performAction(context, qualifyOrderEntries, targetOrderEntries, percentageDiscount, selectCurrency);
    }


    protected boolean performAction(RuleActionContext context, Set<OrderEntryRAO> qualifyEntries, Set<OrderEntryRAO> targetEntries, BigDecimal discountValueForCartCurrency, String currencyISOCode)
    {
        boolean isPerformed = false;
        if(!targetEntries.isEmpty())
        {
            isPerformed = true;
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            List<DiscountRAO> discounts = addDiscountOnMultipleOrderEntryLevelAndConsume(context, (List)targetEntries
                            .stream().collect(Collectors.toUnmodifiableList()), false, discountValueForCartCurrency, currencyISOCode, null, result);
            if(CollectionUtils.isNotEmpty(qualifyEntries))
            {
                qualifyEntries.forEach(entry -> {
                    getConsumptionSupport().consumeOrderEntry(entry, discounts.isEmpty() ? null : discounts.get(0));
                    context.updateFacts(new Object[] {entry});
                });
            }
        }
        return isPerformed;
    }
}
