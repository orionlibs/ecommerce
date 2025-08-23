package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RuleOrderEntryFixedPriceRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        if(context.getParameters().containsKey("is_discounted_price_included"))
        {
            Boolean discountedPriceIncluded = (Boolean)context.getParameter("is_discounted_price_included");
            return processWithCartTotalThreshold(context, discountedPriceIncluded.booleanValue());
        }
        return processWithoutCartTotalThreshold(context);
    }


    protected boolean processWithCartTotalThreshold(RuleActionContext context, boolean discountedPriceIncluded)
    {
        Set<OrderEntryRAO> orderEntries = context.getValues(OrderEntryRAO.class);
        Map<String, BigDecimal> values = (Map<String, BigDecimal>)context.getParameter("value");
        for(OrderEntryRAO orderEntryRao : orderEntries)
        {
            BigDecimal valueForCurrency = values.get(orderEntryRao.getCurrencyIsoCode());
            if(Objects.isNull(valueForCurrency))
            {
                break;
            }
            BigDecimal cartThreshold = (BigDecimal)((Map)context.getParameter("cart_threshold")).get(orderEntryRao.getCurrencyIsoCode());
            int consumableQuantity = getConsumptionSupport().getConsumableQuantity(orderEntryRao);
            if(consumableQuantity > 0)
            {
                Map<Integer, Integer> selectedMapOrderEntriesMap = Maps.newHashMap();
                selectedMapOrderEntriesMap.put(orderEntryRao.getEntryNumber(), Integer.valueOf(1));
                Set<OrderEntryRAO> selectedOrderEntryRaos = Sets.newHashSet();
                selectedOrderEntryRaos.add(orderEntryRao);
                BigDecimal total = context.getCartRao().getSubTotal();
                List<DiscountRAO> discounts = getRuleEngineCalculationService().addFixedPriceEntriesDiscount(context
                                .getCartRao(), selectedMapOrderEntriesMap, selectedOrderEntryRaos, valueForCurrency);
                if(isDiscountApplicable(discounts, orderEntryRao, cartThreshold, total, discountedPriceIncluded))
                {
                    addDiscount(context, orderEntryRao, 1, discounts.get(0));
                    return true;
                }
            }
        }
        return false;
    }


    protected boolean isDiscountApplicable(List<DiscountRAO> discounts, OrderEntryRAO orderEntryRao, BigDecimal cartThreshold, BigDecimal total, boolean discountedPriceIncluded)
    {
        Objects.requireNonNull(total);
        return ((Boolean)discounts.stream().findFirst().map(discount -> discountedPriceIncluded ? getDiscountedPrice(discount) : orderEntryRao.getPrice()).map(total::subtract)
                        .map(diff -> Boolean.valueOf((diff.compareTo(cartThreshold) >= 0)))
                        .orElse(Boolean.valueOf(false))).booleanValue();
    }


    protected BigDecimal getDiscountedPrice(DiscountRAO discount)
    {
        return discount.getValue().multiply(new BigDecimal(discount.getAppliedToQuantity()));
    }


    protected boolean processWithoutCartTotalThreshold(RuleActionContext context)
    {
        Set<OrderEntryRAO> orderEntries = context.getValues(OrderEntryRAO.class);
        Map<String, BigDecimal> values = (Map<String, BigDecimal>)context.getParameter("value");
        boolean isPerformed = false;
        for(OrderEntryRAO orderEntry : orderEntries)
        {
            BigDecimal valueForCurrency = values.get(orderEntry.getCurrencyIsoCode());
            if(Objects.nonNull(valueForCurrency))
            {
                isPerformed |= processOrderEntry(context, orderEntry, valueForCurrency);
            }
        }
        return isPerformed;
    }


    protected boolean processOrderEntry(RuleActionContext context, OrderEntryRAO orderEntryRao, BigDecimal valueForCurrency)
    {
        boolean isPerformed = false;
        int consumableQuantity = getConsumptionSupport().getConsumableQuantity(orderEntryRao);
        if(consumableQuantity > 0)
        {
            isPerformed = true;
            DiscountRAO discount = getRuleEngineCalculationService().addFixedPriceEntryDiscount(orderEntryRao, valueForCurrency);
            if(Objects.nonNull(discount))
            {
                addDiscount(context, orderEntryRao, discount);
            }
        }
        return isPerformed;
    }


    protected void addDiscount(RuleActionContext context, OrderEntryRAO orderEntryRao, DiscountRAO discount)
    {
        addDiscount(context, orderEntryRao, orderEntryRao.getQuantity(), discount);
    }


    protected void addDiscount(RuleActionContext context, OrderEntryRAO orderEntryRao, int quantity, DiscountRAO discount)
    {
        RuleEngineResultRAO result = context.getRuleEngineResultRao();
        result.getActions().add(discount);
        setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
        getConsumptionSupport().consumeOrderEntry(orderEntryRao, quantity, (AbstractRuleActionRAO)discount);
        context.scheduleForUpdate(new Object[] {orderEntryRao, orderEntryRao.getOrder(), result});
        context.insertFacts(new Object[] {discount});
        context.insertFacts(discount.getConsumedEntries());
    }
}
