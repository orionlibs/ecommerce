package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import com.google.common.collect.Sets;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RAOConsumptionSupport
{
    boolean isConsumptionEnabled();


    void trackConsumedProducts(RuleActionContext paramRuleActionContext);


    OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO paramOrderEntryRAO, int paramInt, BigDecimal paramBigDecimal, AbstractRuleActionRAO paramAbstractRuleActionRAO);


    OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO paramOrderEntryRAO, AbstractRuleActionRAO paramAbstractRuleActionRAO);


    OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO paramOrderEntryRAO, int paramInt, AbstractRuleActionRAO paramAbstractRuleActionRAO);


    <T extends AbstractRuleActionRAO> void consumeOrderEntries(RuleActionContext paramRuleActionContext, Collection<EntriesSelectionStrategyRPD> paramCollection, T paramT);


    Set<OrderEntryConsumedRAO> consumeOrderEntries(Set<OrderEntryRAO> paramSet, Map<Integer, Integer> paramMap, AbstractRuleActionRAO paramAbstractRuleActionRAO);


    int getConsumedQuantityForOrderEntry(OrderEntryRAO paramOrderEntryRAO);


    Map<Integer, Integer> getSelectedOrderEntryQuantities(RuleActionContext paramRuleActionContext, Collection<EntriesSelectionStrategyRPD> paramCollection);


    Set<OrderEntryRAO> getSelectedOrderEntryRaos(Collection<EntriesSelectionStrategyRPD> paramCollection, Map<Integer, Integer> paramMap);


    OrderEntryConsumedRAO createOrderEntryConsumedRAO(OrderEntryRAO paramOrderEntryRAO, int paramInt, BigDecimal paramBigDecimal);


    void updateActionRAOWithConsumed(AbstractRuleActionRAO paramAbstractRuleActionRAO, OrderEntryConsumedRAO paramOrderEntryConsumedRAO);


    void mergeOrderEntryConsumed(OrderEntryConsumedRAO paramOrderEntryConsumedRAO1, OrderEntryConsumedRAO paramOrderEntryConsumedRAO2);


    boolean hasEnoughQuantity(RuleActionContext paramRuleActionContext, Collection<EntriesSelectionStrategyRPD> paramCollection);


    Map<Integer, Integer> getEligibleEntryQuantities(Collection<EntriesSelectionStrategyRPD> paramCollection);


    int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> paramCollection);


    int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> paramCollection, int paramInt);


    default int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, RuleActionContext context)
    {
        return -1;
    }


    default int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, RuleActionContext context, int maxCount)
    {
        return -1;
    }


    int getConsumableQuantity(OrderEntryRAO paramOrderEntryRAO);


    default Set<OrderEntryConsumedRAO> getConsumedOrderEntryInfoForOrderEntry(OrderEntryRAO orderEntryRao)
    {
        return Sets.newHashSet();
    }
}
