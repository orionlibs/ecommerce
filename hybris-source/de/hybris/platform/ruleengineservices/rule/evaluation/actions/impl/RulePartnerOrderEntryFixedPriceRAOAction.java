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
import org.assertj.core.util.Lists;

public class RulePartnerOrderEntryFixedPriceRAOAction extends AbstractRulePartnerProductAction
{
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";
    public static final String PARTNER_CONTAINERS_PARAM = "target_containers";


    public boolean performActionInternal(RuleActionContext context)
    {
        Map<String, BigDecimal> value = (Map<String, BigDecimal>)context.getParameter("value");
        OrderEntrySelectionStrategy selectionStrategy = (OrderEntrySelectionStrategy)context.getParameter("selection_strategy");
        Map<String, Integer> qualifyingProductsContainers = (Map<String, Integer>)context.getParameter("qualifying_containers");
        Map<String, Integer> partnerProductsContainers = (Map<String, Integer>)context.getParameter("target_containers");
        CartRAO cart = (CartRAO)context.getValue(CartRAO.class);
        BigDecimal discountValueForCartCurrency = value.get(cart.getCurrencyIsoCode());
        if(Objects.isNull(discountValueForCartCurrency))
        {
            return false;
        }
        List<EntriesSelectionStrategyRPD> selectionStrategyRPDs = Lists.newArrayList();
        List<EntriesSelectionStrategyRPD> triggeringSelectionStrategyRPDs = createSelectionStrategyRPDsQualifyingProducts(context, selectionStrategy, qualifyingProductsContainers);
        selectionStrategyRPDs.addAll(triggeringSelectionStrategyRPDs);
        List<EntriesSelectionStrategyRPD> targetingSelectionStrategyRPDs = createSelectionStrategyRPDsTargetProducts(context, selectionStrategy, partnerProductsContainers);
        selectionStrategyRPDs.addAll(targetingSelectionStrategyRPDs);
        return performAction(context, selectionStrategyRPDs, discountValueForCartCurrency);
    }


    public boolean performAction(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs, BigDecimal amount)
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
            List<DiscountRAO> discounts = addFixedPriceEntryDiscount(selectionStrategyRPDsForAction, amount, context);
            for(DiscountRAO discount : discounts)
            {
                result.getActions().add(discount);
                setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
                context.insertFacts(new Object[] {discount});
                context.insertFacts(discount.getConsumedEntries());
                discount.getConsumedEntries().forEach(coe -> coe.setFiredRuleCode(discount.getFiredRuleCode()));
                context.scheduleForUpdate(new Object[] {discount.getAppliedToObject()});
            }
            context.scheduleForUpdate(new Object[] {((OrderEntryRAO)((EntriesSelectionStrategyRPD)entriesSelectionStrategyRPDs.get(0)).getOrderEntries().get(0)).getOrder(), result});
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


    protected List<DiscountRAO> addFixedPriceEntryDiscount(List<EntriesSelectionStrategyRPD> selectionStrategies, BigDecimal fixedPrice, RuleActionContext context)
    {
        Map<Integer, Integer> selectedOrderEntryMap = getConsumptionSupport().getSelectedOrderEntryQuantities(context, selectionStrategies);
        Set<OrderEntryRAO> selectedOrderEntryRaos = getConsumptionSupport().getSelectedOrderEntryRaos(selectionStrategies, selectedOrderEntryMap);
        CartRAO cartRao = (CartRAO)((OrderEntryRAO)((EntriesSelectionStrategyRPD)selectionStrategies.get(0)).getOrderEntries().get(0)).getOrder();
        List<DiscountRAO> discounts = getRuleEngineCalculationService().addFixedPriceEntriesDiscount(cartRao, selectedOrderEntryMap, selectedOrderEntryRaos, fixedPrice);
        for(DiscountRAO discount : discounts)
        {
            OrderEntryRAO orderEntryRao = (OrderEntryRAO)discount.getAppliedToObject();
            getConsumptionSupport().consumeOrderEntry(orderEntryRao, (int)discount.getAppliedToQuantity(),
                            getPriceAdjustmentStrategy().get(orderEntryRao, orderEntryRao.getQuantity()), (AbstractRuleActionRAO)discount);
        }
        return discounts;
    }
}
