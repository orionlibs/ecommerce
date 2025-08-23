package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.hybris.platform.ruleengineservices.calculation.EntriesSelectionStrategy;
import de.hybris.platform.ruleengineservices.calculation.PriceAdjustmentStrategy;
import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.SwitchService;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryConsumedRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOLookupService;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRAOConsumptionSupport implements RAOConsumptionSupport
{
    private RAOLookupService raoLookupService;
    private Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> entriesSelectionStrategies;
    private Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> qualifyingEntriesSelectionStrategies;
    private CurrencyUtils currencyUtils;
    private SwitchService switchService;
    private PriceAdjustmentStrategy<OrderEntryRAO> priceAdjustmentStrategy;


    public boolean isConsumptionEnabled()
    {
        return getSwitchService().isEnabled(Switch.CONSUMPTION);
    }


    public void trackConsumedProducts(RuleActionContext context)
    {
        if(!isConsumptionEnabled())
        {
            return;
        }
        List<OrderEntryRAO> orderEntryRAOs = getRaoLookupService().lookupRAOObjectsByType(OrderEntryRAO.class, context, new java.util.function.Predicate[0]);
        if(CollectionUtils.isNotEmpty(orderEntryRAOs))
        {
            for(OrderEntryRAO orderEntry : orderEntryRAOs)
            {
                int availableQuantityInOrderEntry = getConsumableQuantity(orderEntry);
                if(availableQuantityInOrderEntry != orderEntry.getAvailableQuantity())
                {
                    orderEntry.setAvailableQuantity(availableQuantityInOrderEntry);
                    context.scheduleForUpdate(new Object[] {orderEntry});
                }
            }
        }
    }


    public OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO orderEntryRAO, int quantity, BigDecimal discountValue, AbstractRuleActionRAO actionRAO)
    {
        OrderEntryConsumedRAO orderEntryConsumedRAO = createOrderEntryConsumedRAO(orderEntryRAO, quantity, discountValue);
        updateActionRAOWithConsumed(actionRAO, orderEntryConsumedRAO);
        return orderEntryConsumedRAO;
    }


    public OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO orderEntryRAO, AbstractRuleActionRAO actionRAO)
    {
        return consumeOrderEntry(orderEntryRAO, orderEntryRAO.getQuantity(), adjustUnitPrice(orderEntryRAO), actionRAO);
    }


    public OrderEntryConsumedRAO consumeOrderEntry(OrderEntryRAO orderEntryRAO, int quantity, AbstractRuleActionRAO actionRAO)
    {
        return consumeOrderEntry(orderEntryRAO, quantity, adjustUnitPrice(orderEntryRAO, quantity), actionRAO);
    }


    public <T extends AbstractRuleActionRAO> void consumeOrderEntries(RuleActionContext context, Collection<EntriesSelectionStrategyRPD> strategies, T actionRAO)
    {
        Map<Integer, Integer> selectedOrderEntryMap = getSelectedOrderEntryQuantities(context, strategies);
        Set<OrderEntryRAO> selectedOrderEntryRaos = getSelectedOrderEntryRaos(strategies, selectedOrderEntryMap);
        consumeOrderEntries(selectedOrderEntryRaos, selectedOrderEntryMap, (AbstractRuleActionRAO)actionRAO);
    }


    public Set<OrderEntryConsumedRAO> consumeOrderEntries(Set<OrderEntryRAO> selectedEntries, Map<Integer, Integer> selectedEntriesMap, AbstractRuleActionRAO actionRAO)
    {
        Set<OrderEntryConsumedRAO> result = Sets.newLinkedHashSet();
        for(OrderEntryRAO selectedEntry : selectedEntries)
        {
            result.add(consumeOrderEntry(selectedEntry, ((Integer)selectedEntriesMap.get(selectedEntry.getEntryNumber())).intValue(), BigDecimal.ZERO, actionRAO));
        }
        return result;
    }


    public int getConsumedQuantityForOrderEntry(OrderEntryRAO orderEntryRao)
    {
        if(!isConsumptionEnabled())
        {
            return 0;
        }
        Set<OrderEntryRAO> entries = orderEntryRao.getOrder().getEntries();
        if(CollectionUtils.isNotEmpty(entries))
        {
            Set<AbstractRuleActionRAO> allActions = (Set<AbstractRuleActionRAO>)entries.stream().filter(e -> CollectionUtils.isNotEmpty(e.getActions())).flatMap(e -> e.getActions().stream()).collect(Collectors.toSet());
            return getConsumedQuantityForOrderEntry(orderEntryRao, allActions);
        }
        Set<AbstractRuleActionRAO> actions = orderEntryRao.getActions();
        if(CollectionUtils.isNotEmpty(actions))
        {
            return getConsumedQuantityForOrderEntry(orderEntryRao, actions);
        }
        return 0;
    }


    public Map<Integer, Integer> getSelectedOrderEntryQuantities(RuleActionContext context, Collection<EntriesSelectionStrategyRPD> strategies)
    {
        Map<Integer, Integer> result = Maps.newHashMap();
        for(EntriesSelectionStrategyRPD strategy : strategies)
        {
            Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> strategyBeans = strategy.isTargetOfAction() ? getEntriesSelectionStrategies() : getQualifyingEntriesSelectionStrategies();
            if(!strategyBeans.containsKey(strategy.getSelectionStrategy()))
            {
                throw new IllegalStateException(String.format("UnitForBundleSelector Strategy with identifier '%s' not defined", new Object[] {strategy
                                .getSelectionStrategy()}));
            }
            List<OrderEntryRAO> orderEntriesForStrategy = strategy.getOrderEntries();
            Map<Integer, Integer> consumableQtyByOrderEntry = Maps.newHashMap();
            for(OrderEntryRAO orderEntryRAO : orderEntriesForStrategy)
            {
                int consumableQuantity = getConsumableQuantity(orderEntryRAO);
                consumableQtyByOrderEntry.put(orderEntryRAO.getEntryNumber(), Integer.valueOf(consumableQuantity));
            }
            Map<Integer, Integer> consumptionByOrderEntryMap = ((EntriesSelectionStrategy)strategyBeans.get(strategy.getSelectionStrategy())).pickup(strategy, consumableQtyByOrderEntry);
            result.putAll(consumptionByOrderEntryMap);
        }
        return result;
    }


    public Set<OrderEntryRAO> getSelectedOrderEntryRaos(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, Map<Integer, Integer> selectedOrderEntryMap)
    {
        Set<OrderEntryRAO> orderEntryRAOS = (Set<OrderEntryRAO>)selectionStrategyRPDs.stream().flatMap(selectionStrategy -> selectionStrategy.getOrderEntries().stream()).filter(orderEntry -> selectedOrderEntryMap.containsKey(orderEntry.getEntryNumber())).collect(Collectors.toSet());
        orderEntryRAOS = (Set<OrderEntryRAO>)orderEntryRAOS.stream().filter(e -> (getConsumableQuantity(e) > 0)).collect(Collectors.toSet());
        return orderEntryRAOS;
    }


    public OrderEntryConsumedRAO createOrderEntryConsumedRAO(OrderEntryRAO orderEntryRAO, int quantity, BigDecimal discountValue)
    {
        OrderEntryConsumedRAO orderEntryConsumed = new OrderEntryConsumedRAO();
        orderEntryConsumed.setOrderEntry(orderEntryRAO);
        orderEntryConsumed.setQuantity(quantity);
        BigDecimal unitPrice = orderEntryRAO.getPrice();
        BigDecimal adjustedUnitPrice = unitPrice.subtract(discountValue);
        BigDecimal roundedAdjustedUnitPrice = getCurrencyUtils().applyRounding(adjustedUnitPrice, orderEntryRAO
                        .getCurrencyIsoCode());
        orderEntryConsumed.setAdjustedUnitPrice(roundedAdjustedUnitPrice);
        return orderEntryConsumed;
    }


    public void updateActionRAOWithConsumed(AbstractRuleActionRAO actionRAO, OrderEntryConsumedRAO orderEntryConsumedRAO)
    {
        if(actionRAO != null)
        {
            Set<OrderEntryConsumedRAO> consumedEntries = actionRAO.getConsumedEntries();
            if(Objects.isNull(consumedEntries))
            {
                consumedEntries = Sets.newLinkedHashSet();
                actionRAO.setConsumedEntries(consumedEntries);
            }
            Integer entryNumber = orderEntryConsumedRAO.getOrderEntry().getEntryNumber();
            String firedRuleCode = actionRAO.getFiredRuleCode();
            Optional<OrderEntryConsumedRAO> existingOrderEntryConsumedRAO = consumedEntries.stream().filter(e -> (e.getOrderEntry().getEntryNumber().equals(entryNumber) && e.getFiredRuleCode().equals(firedRuleCode))).findFirst();
            orderEntryConsumedRAO.setFiredRuleCode(firedRuleCode);
            if(existingOrderEntryConsumedRAO.isPresent())
            {
                mergeOrderEntryConsumed(existingOrderEntryConsumedRAO.get(), orderEntryConsumedRAO);
            }
            else
            {
                consumedEntries.add(orderEntryConsumedRAO);
            }
        }
    }


    public void mergeOrderEntryConsumed(OrderEntryConsumedRAO consumedTarget, OrderEntryConsumedRAO consumedSource)
    {
        consumedTarget.setQuantity(consumedTarget.getQuantity() + consumedSource.getQuantity());
        consumedSource.setQuantity(consumedTarget.getQuantity());
    }


    public boolean hasEnoughQuantity(RuleActionContext context, Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs)
    {
        return hasEnoughQuantity(selectionStrategyRPDs);
    }


    protected boolean hasEnoughQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs)
    {
        Map<Integer, Integer> entriesToBeConsumedMap = getEligibleEntryQuantities(selectionStrategyRPDs);
        return selectionStrategyRPDs.stream().flatMap(s -> s.getOrderEntries().stream()).noneMatch(e -> {
            int consumableQuantity = getConsumableQuantity(e);
            return (((Integer)entriesToBeConsumedMap.getOrDefault(e.getEntryNumber(), (V)Integer.valueOf(0))).intValue() > consumableQuantity);
        });
    }


    public Map<Integer, Integer> getEligibleEntryQuantities(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs)
    {
        Map<Integer, Integer> entryQuantities = (Map<Integer, Integer>)selectionStrategyRPDs.stream().map(EntriesSelectionStrategyRPD::getOrderEntries).flatMap(Collection::stream).collect(Collectors.toMap(OrderEntryRAO::getEntryNumber, this::getConsumableQuantity, (a, b) -> b));
        Map<Integer, Integer> entriesToBeConsumedMap = Maps.newHashMap();
        selectionStrategyRPDs.stream().filter(EntriesSelectionStrategyRPD::isTargetOfAction)
                        .forEach(targetRPD -> putEligibleEntryQuantities(entryQuantities, entriesToBeConsumedMap, targetRPD));
        selectionStrategyRPDs.stream().filter(rpd -> !rpd.isTargetOfAction())
                        .forEach(qualifyRPD -> putEligibleEntryQuantities(entryQuantities, entriesToBeConsumedMap, qualifyRPD));
        return entriesToBeConsumedMap;
    }


    public int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs)
    {
        return adjustStrategyQuantity(selectionStrategyRPDs, -1);
    }


    public int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, int maxCount)
    {
        return adjustStrategyQuantity(selectionStrategyRPDs, null, maxCount);
    }


    public int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, RuleActionContext context)
    {
        return adjustStrategyQuantity(selectionStrategyRPDs, context, -1);
    }


    public int adjustStrategyQuantity(Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, RuleActionContext context, int maxCount)
    {
        if(isConsumptionEnabled())
        {
            return -1;
        }
        int lo = 1;
        int hi = ((Integer)selectionStrategyRPDs.stream().map(this::getAdjustMultiplierForStrategyRPD).min(Integer::compareTo).orElse(Integer.valueOf(1))).intValue() + 1;
        while(hi - lo > 1)
        {
            int mid = (lo + hi) / 2;
            if(!adjustMultiplierValid(context, selectionStrategyRPDs, mid))
            {
                hi = mid;
                continue;
            }
            lo = mid;
        }
        int finalCnt = (maxCount > 0) ? Math.min(maxCount, lo) : lo;
        selectionStrategyRPDs.forEach(s -> s.setQuantity(s.getQuantity() * finalCnt));
        return finalCnt;
    }


    protected int getAdjustMultiplierForStrategyRPD(EntriesSelectionStrategyRPD strategyRPD)
    {
        int entryQtySum = ((Integer)strategyRPD.getOrderEntries().stream().map(this::getConsumableQuantity).reduce(Integer.valueOf(0), Integer::sum)).intValue();
        int rpdQty = Math.max(1, strategyRPD.getQuantity());
        return Math.max(entryQtySum / rpdQty, 1);
    }


    protected boolean adjustMultiplierValid(RuleActionContext context, Collection<EntriesSelectionStrategyRPD> selectionStrategyRPDs, int adjustMultiplier)
    {
        List<EntriesSelectionStrategyRPD> cloneRPDs = (List<EntriesSelectionStrategyRPD>)selectionStrategyRPDs.stream().map(rpd -> saveSourceRPDWithAdjustMultiplierAsNew(rpd, adjustMultiplier)).collect(Collectors.toUnmodifiableList());
        return hasEnoughQuantity(context, cloneRPDs);
    }


    protected EntriesSelectionStrategyRPD saveSourceRPDWithAdjustMultiplierAsNew(EntriesSelectionStrategyRPD source, int adjustMultiplier)
    {
        EntriesSelectionStrategyRPD rpd = new EntriesSelectionStrategyRPD();
        int quantity = source.getQuantity() * adjustMultiplier;
        rpd.setQuantity(quantity);
        rpd.setOrderEntries(source.getOrderEntries());
        rpd.setSelectionStrategy(source.getSelectionStrategy());
        rpd.setTargetOfAction(source.isTargetOfAction());
        return rpd;
    }


    public int getConsumableQuantity(OrderEntryRAO orderEntryRao)
    {
        return orderEntryRao.getQuantity() - getConsumedQuantityForOrderEntry(orderEntryRao);
    }


    public Set<OrderEntryConsumedRAO> getConsumedOrderEntryInfoForOrderEntry(OrderEntryRAO orderEntryRao)
    {
        Set<OrderEntryRAO> entries = orderEntryRao.getOrder().getEntries();
        if(CollectionUtils.isNotEmpty(entries))
        {
            Set<AbstractRuleActionRAO> allActions = (Set<AbstractRuleActionRAO>)entries.stream().filter(e -> CollectionUtils.isNotEmpty(e.getActions())).flatMap(e -> e.getActions().stream()).collect(Collectors.toSet());
            return getConsumedOrderEntryInfoForOrderEntry(orderEntryRao, allActions);
        }
        Set<AbstractRuleActionRAO> actions = orderEntryRao.getActions();
        if(CollectionUtils.isNotEmpty(actions))
        {
            return getConsumedOrderEntryInfoForOrderEntry(orderEntryRao, actions);
        }
        return Sets.newHashSet();
    }


    protected Set<OrderEntryConsumedRAO> getConsumedOrderEntryInfoForOrderEntry(OrderEntryRAO orderEntryRAO, Set<AbstractRuleActionRAO> actions)
    {
        Set<OrderEntryConsumedRAO> consumedRAOSet = Sets.newHashSet();
        for(AbstractRuleActionRAO action : actions)
        {
            if(action instanceof DiscountRAO)
            {
                DiscountRAO discountRAO = (DiscountRAO)action;
                consumedRAOSet.addAll((Collection<? extends OrderEntryConsumedRAO>)discountRAO.getConsumedEntries().stream().filter(e -> e.getOrderEntry().equals(orderEntryRAO))
                                .collect(Collectors.toSet()));
            }
        }
        return consumedRAOSet;
    }


    @Deprecated(since = "2205", forRemoval = true)
    protected void putEligibleEntryQuantities(Map<Integer, Integer> entriesToBeConsumedMap, EntriesSelectionStrategyRPD strategy)
    {
        int quantityToBeConsumed = strategy.getQuantity();
        List<OrderEntryRAO> orderEntries = strategy.getOrderEntries();
        int orderEntryCnt = 0;
        for(OrderEntryRAO orderEntry : orderEntries)
        {
            int eligibleEntryQuantity;
            orderEntryCnt++;
            Integer entryNumber = orderEntry.getEntryNumber();
            Integer entryConsumedQty = entriesToBeConsumedMap.get(entryNumber);
            if(Objects.isNull(entryConsumedQty))
            {
                entryConsumedQty = Integer.valueOf(0);
            }
            if(orderEntryCnt < orderEntries.size())
            {
                int orderEntryConsumableQuantity = getConsumableQuantity(orderEntry);
                int availableOrderEntryQuantity = orderEntryConsumableQuantity - entryConsumedQty.intValue();
                if(availableOrderEntryQuantity <= quantityToBeConsumed)
                {
                    eligibleEntryQuantity = orderEntryConsumableQuantity;
                    quantityToBeConsumed -= availableOrderEntryQuantity;
                }
                else
                {
                    eligibleEntryQuantity = entryConsumedQty.intValue() + quantityToBeConsumed;
                    quantityToBeConsumed -= eligibleEntryQuantity;
                }
            }
            else
            {
                eligibleEntryQuantity = entryConsumedQty.intValue() + quantityToBeConsumed;
            }
            entriesToBeConsumedMap.put(entryNumber, Integer.valueOf(eligibleEntryQuantity));
        }
    }


    protected void putEligibleEntryQuantities(Map<Integer, Integer> entryQuantities, Map<Integer, Integer> entriesToBeConsumedMap, EntriesSelectionStrategyRPD strategy)
    {
        int qtyToConsume = strategy.getQuantity();
        List<Integer> entryIds = (List<Integer>)strategy.getOrderEntries().stream().map(OrderEntryRAO::getEntryNumber).distinct().collect(Collectors.toUnmodifiableList());
        for(Integer entryId : entryIds)
        {
            if(qtyToConsume <= 0)
            {
                return;
            }
            int entryQtyToConsume = Math.min(qtyToConsume, ((Integer)entryQuantities.get(entryId)).intValue());
            qtyToConsume -= entryQtyToConsume;
            entryQuantities.put(entryId, Integer.valueOf(((Integer)entryQuantities.get(entryId)).intValue() - entryQtyToConsume));
            entriesToBeConsumedMap.put(entryId, Integer.valueOf(((Integer)entriesToBeConsumedMap.getOrDefault(entryId, Integer.valueOf(0))).intValue() + entryQtyToConsume));
        }
        if(qtyToConsume > 0)
        {
            entriesToBeConsumedMap.put(entryIds.get(0), Integer.valueOf(((Integer)entriesToBeConsumedMap.getOrDefault(entryIds.get(0), Integer.valueOf(0))).intValue() + qtyToConsume));
        }
    }


    protected BigDecimal adjustUnitPrice(OrderEntryRAO orderEntryRao)
    {
        return adjustUnitPrice(orderEntryRao, orderEntryRao.getQuantity());
    }


    protected BigDecimal adjustUnitPrice(OrderEntryRAO orderEntryRao, int quantity)
    {
        return getPriceAdjustmentStrategy().get(orderEntryRao, quantity);
    }


    protected int getConsumedQuantityForOrderEntry(OrderEntryRAO orderEntryRao, Set<AbstractRuleActionRAO> actions)
    {
        int consumedQty = 0;
        for(AbstractRuleActionRAO action : actions)
        {
            if(action instanceof DiscountRAO)
            {
                DiscountRAO discountRAO = (DiscountRAO)action;
                Set<OrderEntryConsumedRAO> consumedEntries = discountRAO.getConsumedEntries();
                if(CollectionUtils.isNotEmpty(consumedEntries))
                {
                    consumedQty += consumedEntries.stream().filter(e -> e.getOrderEntry().equals(orderEntryRao))
                                    .mapToInt(OrderEntryConsumedRAO::getQuantity).reduce(0, (q1, q2) -> q1 + q2);
                }
            }
        }
        return consumedQty;
    }


    protected RAOLookupService getRaoLookupService()
    {
        return this.raoLookupService;
    }


    @Required
    public void setRaoLookupService(RAOLookupService raoLookupService)
    {
        this.raoLookupService = raoLookupService;
    }


    protected Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> getEntriesSelectionStrategies()
    {
        return this.entriesSelectionStrategies;
    }


    @Required
    public void setEntriesSelectionStrategies(Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> entriesSelectionStrategies)
    {
        this.entriesSelectionStrategies = entriesSelectionStrategies;
    }


    protected Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> getQualifyingEntriesSelectionStrategies()
    {
        return this.qualifyingEntriesSelectionStrategies;
    }


    @Required
    public void setQualifyingEntriesSelectionStrategies(Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> qualifyingEntriesSelectionStrategies)
    {
        this.qualifyingEntriesSelectionStrategies = qualifyingEntriesSelectionStrategies;
    }


    protected CurrencyUtils getCurrencyUtils()
    {
        return this.currencyUtils;
    }


    @Required
    public void setCurrencyUtils(CurrencyUtils currencyUtils)
    {
        this.currencyUtils = currencyUtils;
    }


    protected SwitchService getSwitchService()
    {
        return this.switchService;
    }


    @Required
    public void setSwitchService(SwitchService switchService)
    {
        this.switchService = switchService;
    }


    protected PriceAdjustmentStrategy<OrderEntryRAO> getPriceAdjustmentStrategy()
    {
        return this.priceAdjustmentStrategy;
    }


    @Required
    public void setPriceAdjustmentStrategy(PriceAdjustmentStrategy<OrderEntryRAO> priceAdjustmentStrategy)
    {
        this.priceAdjustmentStrategy = priceAdjustmentStrategy;
    }
}
