package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class RuleOrderEntryPercentageDiscountRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        boolean isPerformed = false;
        Optional<BigDecimal> amount = extractAmountForCurrency(context, context.getParameter("value"));
        if(amount.isPresent())
        {
            Set<OrderEntryRAO> orderEntries = context.getValues(OrderEntryRAO.class);
            if(CollectionUtils.isNotEmpty(orderEntries))
            {
                for(OrderEntryRAO orderEntryRAO : orderEntries)
                {
                    isPerformed |= processOrderEntry(context, orderEntryRAO, amount.get());
                }
            }
        }
        return isPerformed;
    }


    protected boolean processOrderEntry(RuleActionContext context, OrderEntryRAO orderEntryRao, BigDecimal value)
    {
        boolean isPerformed = false;
        int consumableQuantity = getConsumptionSupport().getConsumableQuantity(orderEntryRao);
        if(consumableQuantity > 0)
        {
            DiscountRAO discount = getRuleEngineCalculationService().addOrderEntryLevelDiscount(orderEntryRao, false, value);
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
            getConsumptionSupport().consumeOrderEntry(orderEntryRao, consumableQuantity, (AbstractRuleActionRAO)discount);
            RuleEngineResultRAO result = (RuleEngineResultRAO)context.getValue(RuleEngineResultRAO.class);
            result.getActions().add(discount);
            context.scheduleForUpdate(new Object[] {orderEntryRao, orderEntryRao.getOrder(), result});
            context.insertFacts(new Object[] {discount});
            context.insertFacts(discount.getConsumedEntries());
            isPerformed = true;
        }
        return isPerformed;
    }
}
