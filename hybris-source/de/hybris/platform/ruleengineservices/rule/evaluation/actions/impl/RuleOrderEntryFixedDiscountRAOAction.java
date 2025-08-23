package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class RuleOrderEntryFixedDiscountRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        Set<OrderEntryRAO> orderEntries = context.getValues(OrderEntryRAO.class);
        Map<String, BigDecimal> values = (Map<String, BigDecimal>)context.getParameter("value");
        boolean isPerformed = false;
        for(OrderEntryRAO orderEntry : orderEntries)
        {
            BigDecimal valueForCurrency = values.get(orderEntry.getCurrencyIsoCode());
            if(Objects.nonNull(valueForCurrency))
            {
                isPerformed |= performAction(context, orderEntry, valueForCurrency);
            }
        }
        return isPerformed;
    }


    protected boolean performAction(RuleActionContext context, OrderEntryRAO orderEntryRao, BigDecimal valueForCurrency)
    {
        boolean isPerformed = false;
        int consumableQuantity = getConsumptionSupport().getConsumableQuantity(orderEntryRao);
        if(consumableQuantity > 0)
        {
            isPerformed = true;
            DiscountRAO discount = getRuleEngineCalculationService().addOrderEntryLevelDiscount(orderEntryRao, true, valueForCurrency);
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
            getConsumptionSupport().consumeOrderEntry(orderEntryRao, consumableQuantity, (AbstractRuleActionRAO)discount);
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            result.getActions().add(discount);
            context.scheduleForUpdate(new Object[] {orderEntryRao, orderEntryRao.getOrder(), result});
            context.insertFacts(new Object[] {discount});
            context.insertFacts(discount.getConsumedEntries());
        }
        return isPerformed;
    }
}
