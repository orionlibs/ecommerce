package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRulePartnerProductAction;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class RulePartnerOrderEntryPercentageDiscountRAOAction extends AbstractRulePartnerProductAction
{
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";
    public static final String PARTNER_CONTAINERS_PARAM = "target_containers";


    public boolean performActionInternal(RuleActionContext context)
    {
        OrderEntrySelectionStrategy selectionStrategy = (OrderEntrySelectionStrategy)context.getParameter("selection_strategy");
        Map<String, Integer> qualifyingProductsContainers = (Map<String, Integer>)context.getParameter("qualifying_containers");
        Map<String, Integer> partnerProductsContainers = (Map<String, Integer>)context.getParameter("target_containers");
        List<EntriesSelectionStrategyRPD> selectionStrategyRPDs = Lists.newArrayList();
        List<EntriesSelectionStrategyRPD> triggeringSelectionStrategyRPDs = createSelectionStrategyRPDsQualifyingProducts(context, selectionStrategy, qualifyingProductsContainers);
        selectionStrategyRPDs.addAll(triggeringSelectionStrategyRPDs);
        List<EntriesSelectionStrategyRPD> targetingSelectionStrategyRPDs = createSelectionStrategyRPDsTargetProducts(context, selectionStrategy, partnerProductsContainers);
        selectionStrategyRPDs.addAll(targetingSelectionStrategyRPDs);
        return ((Boolean)extractAmountForCurrency(context, context.getParameter("value"))
                        .map(amount -> Boolean.valueOf(performAction(context, selectionStrategyRPDs, amount))).orElse(Boolean.valueOf(false))).booleanValue();
    }


    protected boolean performAction(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs, BigDecimal amount)
    {
        boolean isPerformed = false;
        validateSelectionStrategy(entriesSelectionStrategyRPDs, context);
        if(getConsumptionSupport().hasEnoughQuantity(context, entriesSelectionStrategyRPDs))
        {
            isPerformed = true;
            getConsumptionSupport().adjustStrategyQuantity(entriesSelectionStrategyRPDs, context);
            List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForAction = Lists.newArrayList();
            List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForTriggering = Lists.newArrayList();
            splitEntriesSelectionStrategies(entriesSelectionStrategyRPDs, selectionStrategyRPDsForAction, selectionStrategyRPDsForTriggering);
            List<DiscountRAO> discounts = addDiscountAndConsume(context, selectionStrategyRPDsForAction, false, amount);
            if(CollectionUtils.isNotEmpty(selectionStrategyRPDsForTriggering))
            {
                getConsumptionSupport().consumeOrderEntries(context, selectionStrategyRPDsForTriggering,
                                discounts.isEmpty() ? null : (AbstractRuleActionRAO)discounts.get(0));
                updateFactsWithOrderEntries(context, selectionStrategyRPDsForTriggering);
            }
        }
        return isPerformed;
    }


    protected void updateFactsWithOrderEntries(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDS)
    {
        for(EntriesSelectionStrategyRPD selectionStrategyRPDForTriggering : entriesSelectionStrategyRPDS)
        {
            for(OrderEntryRAO orderEntryRao : selectionStrategyRPDForTriggering.getOrderEntries())
            {
                context.scheduleForUpdate(new Object[] {orderEntryRao});
            }
        }
    }


    protected List<DiscountRAO> addDiscountAndConsume(RuleActionContext context, List<EntriesSelectionStrategyRPD> selectionStrategies, boolean absolute, BigDecimal price)
    {
        Map<Integer, Integer> selectedOrderEntryMap = getConsumptionSupport().getSelectedOrderEntryQuantities(context, selectionStrategies);
        Set<OrderEntryRAO> selectedOrderEntryRaos = getConsumptionSupport().getSelectedOrderEntryRaos(selectionStrategies, selectedOrderEntryMap);
        List<DiscountRAO> discounts = getRuleEngineCalculationService().addOrderEntryLevelDiscount(selectedOrderEntryMap, selectedOrderEntryRaos, absolute, price);
        for(DiscountRAO discount : discounts)
        {
            OrderEntryRAO entry = (OrderEntryRAO)discount.getAppliedToObject();
            getConsumptionSupport().consumeOrderEntry(entry, ((Integer)selectedOrderEntryMap.get(entry.getEntryNumber())).intValue(),
                            getPriceAdjustmentStrategy().get(entry, entry.getQuantity()), (AbstractRuleActionRAO)discount);
            if(!mergeDiscounts(context, discount, entry))
            {
                RuleEngineResultRAO result = context.getRuleEngineResultRao();
                result.getActions().add(discount);
                setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
                context.insertFacts(new Object[] {discount});
                context.insertFacts(discount.getConsumedEntries());
                discount.getConsumedEntries().forEach(coe -> coe.setFiredRuleCode(discount.getFiredRuleCode()));
                context.scheduleForUpdate(new Object[] {result});
            }
            context.scheduleForUpdate(new Object[] {discount.getAppliedToObject()});
        }
        if(CollectionUtils.isNotEmpty(selectedOrderEntryRaos))
        {
            CartRAO cartRAO = (CartRAO)((OrderEntryRAO)selectedOrderEntryRaos.iterator().next()).getOrder();
            context.scheduleForUpdate(new Object[] {cartRAO});
        }
        return discounts;
    }
}
