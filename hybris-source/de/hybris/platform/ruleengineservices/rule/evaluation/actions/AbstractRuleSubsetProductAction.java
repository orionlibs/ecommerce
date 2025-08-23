package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import de.hybris.platform.ruleengineservices.calculation.EntriesDiscountDistributeStrategy;
import de.hybris.platform.ruleengineservices.enums.FixedDiscountDistributeStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesDiscountDistributeStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractRuleSubsetProductAction extends AbstractRuleExecutableSupport
{
    private Map<FixedDiscountDistributeStrategy, EntriesDiscountDistributeStrategy> targetEntriesDiscountDistributeStrategy;


    protected Set<OrderEntryRAO> getOrderEntries(RuleActionContext context, String containerId)
    {
        return context.getValues(OrderEntryRAO.class, new String[] {containerId});
    }


    protected BigDecimal getTotalUnconsumePriceOfContainer(RuleActionContext context, String containerId)
    {
        BigDecimal containerProductsTotalValue = BigDecimal.ZERO;
        for(OrderEntryRAO orderEntry : getOrderEntries(context, containerId))
        {
            containerProductsTotalValue = containerProductsTotalValue.add(getTotalUnconsumedPriceOfOrderEntry(orderEntry));
        }
        return containerProductsTotalValue;
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


    protected boolean hasReachedContainerThreshold(RuleActionContext context, String containerId, BigDecimal threshold)
    {
        return (getTotalUnconsumePriceOfContainer(context, containerId).compareTo(threshold) >= 0);
    }


    protected List<DiscountRAO> addDiscountOnMultipleOrderEntryLevelAndConsume(RuleActionContext context, List<OrderEntryRAO> orderEntries, boolean absolute, BigDecimal discount, String currencyISOCode, FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy, RuleEngineResultRAO result)
    {
        List<DiscountRAO> discounts;
        Map<Integer, OrderEntryRAO> entries = (Map<Integer, OrderEntryRAO>)orderEntries.stream().collect(Collectors.toMap(OrderEntryRAO::getEntryNumber, entry -> entry));
        Map<Integer, Integer> fullOrderEntryMap = (Map<Integer, Integer>)entries.values().stream().collect(
                        Collectors.toMap(OrderEntryRAO::getEntryNumber, entry -> Integer.valueOf(getConsumptionSupport().getConsumableQuantity(entry))));
        if(absolute)
        {
            EntriesDiscountDistributeStrategyRPD distributeStrategyRPD = createDiscountDistributeStrategyRPD(orderEntries, discount, currencyISOCode, true, fixedDiscountDistributeStrategy);
            EntriesDiscountDistributeStrategy entriesDiscountDistributeStrategy = getEntriesDiscountDistributeStrategy(distributeStrategyRPD
                            .getFixedDiscountDistributeStrategy());
            Map<Integer, BigDecimal> discountPrices = entriesDiscountDistributeStrategy.distributeDiscount(distributeStrategyRPD);
            discounts = (List<DiscountRAO>)entries.values().stream().map(entry -> getRuleEngineCalculationService().addOrderEntryLevelDiscount(entry, absolute, (BigDecimal)discountPrices.get(entry.getEntryNumber()), false)).flatMap(Collection::stream).collect(Collectors.toList());
        }
        else
        {
            discounts = (List<DiscountRAO>)entries.values().stream().map(entry -> getRuleEngineCalculationService().addOrderEntryLevelDiscount(entry, absolute, discount, false)).flatMap(Collection::stream).collect(Collectors.toList());
        }
        for(DiscountRAO discountRAO : discounts)
        {
            OrderEntryRAO entry = (OrderEntryRAO)discountRAO.getAppliedToObject();
            getConsumptionSupport().consumeOrderEntry(entry, ((Integer)fullOrderEntryMap.get(entry.getEntryNumber())).intValue(), (AbstractRuleActionRAO)discountRAO);
            if(!mergeDiscounts(context, discountRAO, entry))
            {
                result.getActions().add(discountRAO);
                setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discountRAO});
                context.insertFacts(new Object[] {discountRAO});
                context.insertFacts(discountRAO.getConsumedEntries());
                discountRAO.getConsumedEntries().forEach(coe -> coe.setFiredRuleCode(discountRAO.getFiredRuleCode()));
            }
            context.scheduleForUpdate(new Object[] {discountRAO.getAppliedToObject()});
        }
        return discounts;
    }


    protected EntriesDiscountDistributeStrategyRPD createDiscountDistributeStrategyRPD(List<OrderEntryRAO> orderEntries, BigDecimal totalDiscount, String currencyISOCode, boolean fixDiscount, FixedDiscountDistributeStrategy fixedDiscountDistributeStrategy)
    {
        EntriesDiscountDistributeStrategyRPD entriesDiscountDistributeStrategyRPD = new EntriesDiscountDistributeStrategyRPD();
        entriesDiscountDistributeStrategyRPD.setOrderEntries(orderEntries);
        entriesDiscountDistributeStrategyRPD.setTotalDiscount(totalDiscount);
        entriesDiscountDistributeStrategyRPD.setFixDiscount(fixDiscount);
        entriesDiscountDistributeStrategyRPD.setCurrencyIsoCode(currencyISOCode);
        entriesDiscountDistributeStrategyRPD.setFixedDiscountDistributeStrategy(fixedDiscountDistributeStrategy);
        return entriesDiscountDistributeStrategyRPD;
    }


    public Map<FixedDiscountDistributeStrategy, EntriesDiscountDistributeStrategy> getTargetEntriesDiscountDistributeStrategy()
    {
        return this.targetEntriesDiscountDistributeStrategy;
    }


    public void setTargetEntriesDiscountDistributeStrategy(Map<FixedDiscountDistributeStrategy, EntriesDiscountDistributeStrategy> targetEntriesDiscountDistributeStrategy)
    {
        this.targetEntriesDiscountDistributeStrategy = targetEntriesDiscountDistributeStrategy;
    }


    protected abstract EntriesDiscountDistributeStrategy getEntriesDiscountDistributeStrategy(FixedDiscountDistributeStrategy paramFixedDiscountDistributeStrategy);
}
