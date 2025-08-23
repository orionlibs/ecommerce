package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.platform.ruleengineservices.calculation.EntriesDiscountDistributeStrategy;
import de.hybris.platform.ruleengineservices.rao.EntriesDiscountDistributeStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractFixedDiscountDistributeStrategy implements EntriesDiscountDistributeStrategy
{
    private RAOConsumptionSupport consumptionSupport;
    private CurrencyUtils currencyUtils;


    public Map<Integer, BigDecimal> distributeDiscount(EntriesDiscountDistributeStrategyRPD strategy)
    {
        if(!strategy.isFixDiscount())
        {
            throw new IllegalArgumentException("The discount type is not fixed discount.");
        }
        return distributeDiscountOnOrderEntries(strategy.getTotalDiscount(), strategy.getOrderEntries(), strategy
                        .getCurrencyIsoCode());
    }


    protected BigDecimal getTotalUnconsumedPriceOfOrderEntry(OrderEntryRAO orderEntry)
    {
        BigDecimal unconsumedValue = BigDecimal.ZERO;
        if(!getConsumptionSupport().isConsumptionEnabled())
        {
            unconsumedValue = unconsumedValue.add(orderEntry.getTotalPrice());
        }
        else
        {
            BigDecimal consumedValue = BigDecimal.ZERO;
            Set<OrderEntryConsumedRAO> consumedInfos = getConsumptionSupport().getConsumedOrderEntryInfoForOrderEntry(orderEntry);
            for(OrderEntryConsumedRAO consumedInfo : consumedInfos)
            {
                consumedValue = consumedValue.add(consumedInfo.getAdjustedUnitPrice().multiply(BigDecimal.valueOf(consumedInfo.getQuantity())));
            }
            unconsumedValue = orderEntry.getTotalPrice().subtract(consumedValue);
        }
        return unconsumedValue;
    }


    protected abstract Map<Integer, BigDecimal> distributeDiscountOnOrderEntries(BigDecimal paramBigDecimal, List<OrderEntryRAO> paramList, String paramString);


    public RAOConsumptionSupport getConsumptionSupport()
    {
        return this.consumptionSupport;
    }


    public void setConsumptionSupport(RAOConsumptionSupport consumptionSupport)
    {
        this.consumptionSupport = consumptionSupport;
    }


    public CurrencyUtils getCurrencyUtils()
    {
        return this.currencyUtils;
    }


    public void setCurrencyUtils(CurrencyUtils currencyUtils)
    {
        this.currencyUtils = currencyUtils;
    }
}
