package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;

public class RulePartnerOrderEntryFixedDiscountRAOAction extends AbstractRulePartnerProductAction
{
    public static final String SELECTION_STRATEGY_PARAM = "selection_strategy";
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";
    public static final String PARTNER_CONTAINERS_PARAM = "target_containers";


    public boolean performActionInternal(RuleActionContext context)
    {
        Map<String, BigDecimal> value = (Map<String, BigDecimal>)context.getParameter("value");
        OrderEntrySelectionStrategy selectionStrategy = (OrderEntrySelectionStrategy)context.getParameter("selection_strategy");
        Map<String, Integer> qualifyingProductsContainers = (Map<String, Integer>)context.getParameter("qualifying_containers");
        Map<String, Integer> partnerProductsContainers = (Map<String, Integer>)context.getParameter("target_containers");
        CartRAO cart = context.getCartRao();
        List<EntriesSelectionStrategyRPD> selectionStrategyRPDs = new ArrayList<>();
        List<EntriesSelectionStrategyRPD> triggeringSelectionStrategyRPDs = createSelectionStrategyRPDsQualifyingProducts(context, selectionStrategy, qualifyingProductsContainers);
        selectionStrategyRPDs.addAll(triggeringSelectionStrategyRPDs);
        List<EntriesSelectionStrategyRPD> targetingSelectionStrategyRPDs = createSelectionStrategyRPDsTargetProducts(context, selectionStrategy, partnerProductsContainers);
        selectionStrategyRPDs.addAll(targetingSelectionStrategyRPDs);
        BigDecimal discountValueForCartCurrency = value.get(cart.getCurrencyIsoCode());
        if(Objects.nonNull(discountValueForCartCurrency))
        {
            return performAction(context, selectionStrategyRPDs, discountValueForCartCurrency);
        }
        return false;
    }


    protected boolean performAction(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs, BigDecimal amount)
    {
        boolean isPerformed = false;
        validateSelectionStrategy(entriesSelectionStrategyRPDs, context);
        if(getConsumptionSupport().hasEnoughQuantity(context, entriesSelectionStrategyRPDs))
        {
            isPerformed = true;
            getConsumptionSupport().adjustStrategyQuantity(entriesSelectionStrategyRPDs, context);
            List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForAction = new ArrayList<>();
            List<EntriesSelectionStrategyRPD> selectionStrategyRPDsForTriggering = new ArrayList<>();
            splitEntriesSelectionStrategies(entriesSelectionStrategyRPDs, selectionStrategyRPDsForAction, selectionStrategyRPDsForTriggering);
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            List<DiscountRAO> discounts = addDiscountAndConsume(context, selectionStrategyRPDsForAction, true, amount, result);
            if(CollectionUtils.isNotEmpty(selectionStrategyRPDsForTriggering))
            {
                getConsumptionSupport().consumeOrderEntries(context, selectionStrategyRPDsForTriggering,
                                discounts.isEmpty() ? null : (AbstractRuleActionRAO)discounts.get(0));
                Objects.requireNonNull(context);
                selectionStrategyRPDsForTriggering.stream().flatMap(t -> t.getOrderEntries().stream()).forEach(xva$0 -> rec$.updateFacts(new Object[] {xva$0}));
            }
        }
        return isPerformed;
    }


    protected List<DiscountRAO> addDiscountAndConsume(RuleActionContext context, List<EntriesSelectionStrategyRPD> selectionStrategies, boolean absolute, BigDecimal price, RuleEngineResultRAO result)
    {
        Map<Integer, Integer> selectedOrderEntryMap = getConsumptionSupport().getSelectedOrderEntryQuantities(context, selectionStrategies);
        Set<OrderEntryRAO> selectedOrderEntryRaos = getConsumptionSupport().getSelectedOrderEntryRaos(selectionStrategies, selectedOrderEntryMap);
        List<DiscountRAO> discounts = getRuleEngineCalculationService().addOrderEntryLevelDiscount(selectedOrderEntryMap, selectedOrderEntryRaos, absolute, price);
        for(DiscountRAO discount : discounts)
        {
            OrderEntryRAO entry = (OrderEntryRAO)discount.getAppliedToObject();
            getConsumptionSupport().consumeOrderEntry(entry, ((Integer)selectedOrderEntryMap.get(entry.getEntryNumber())).intValue(), (AbstractRuleActionRAO)discount);
            if(!mergeDiscounts(context, discount, entry))
            {
                result.getActions().add(discount);
                setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
                context.insertFacts(new Object[] {discount});
                context.insertFacts(discount.getConsumedEntries());
                discount.getConsumedEntries().forEach(coe -> coe.setFiredRuleCode(discount.getFiredRuleCode()));
            }
            context.scheduleForUpdate(new Object[] {discount.getAppliedToObject()});
        }
        return discounts;
    }
}
