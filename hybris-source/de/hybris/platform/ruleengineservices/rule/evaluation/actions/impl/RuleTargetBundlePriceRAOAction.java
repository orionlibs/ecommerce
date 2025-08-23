package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
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
import java.util.Objects;
import java.util.Set;

public class RuleTargetBundlePriceRAOAction extends AbstractRulePartnerProductAction
{
    public static final String QUALIFYING_CONTAINERS_PARAM = "qualifying_containers";


    public boolean performActionInternal(RuleActionContext context)
    {
        Map<String, BigDecimal> targetBundlePriceValue = (Map<String, BigDecimal>)context.getParameter("value");
        OrderEntrySelectionStrategy selectionStrategy = (OrderEntrySelectionStrategy)context.getParameter("selection_strategy");
        Map<String, Integer> bundleProductQuantitiesPerContainer = (Map<String, Integer>)context.getParameter("qualifying_containers");
        CartRAO cart = (CartRAO)context.getValue(CartRAO.class);
        BigDecimal targetBundlePrice = targetBundlePriceValue.get(cart.getCurrencyIsoCode());
        if(Objects.isNull(targetBundlePrice))
        {
            return false;
        }
        List<EntriesSelectionStrategyRPD> selectionContainers = createSelectionStrategyRPDsTargetProducts(context, selectionStrategy, bundleProductQuantitiesPerContainer);
        return performAction(context, selectionContainers, targetBundlePrice);
    }


    protected boolean performAction(RuleActionContext context, List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs, BigDecimal amount)
    {
        boolean isPerformed = false;
        validateSelectionStrategy(entriesSelectionStrategyRPDs, context);
        if(getConsumptionSupport().hasEnoughQuantity(context, entriesSelectionStrategyRPDs))
        {
            BigDecimal fixedPrice = amount;
            if(!getConsumptionSupport().isConsumptionEnabled())
            {
                int count = getConsumptionSupport().adjustStrategyQuantity(entriesSelectionStrategyRPDs, context);
                fixedPrice = amount.multiply(BigDecimal.valueOf(count));
            }
            Map<Integer, Integer> selectedOrderEntryMap = getConsumptionSupport().getSelectedOrderEntryQuantities(context, entriesSelectionStrategyRPDs);
            Set<OrderEntryRAO> selectedOrderEntryRaos = getConsumptionSupport().getSelectedOrderEntryRaos(entriesSelectionStrategyRPDs, selectedOrderEntryMap);
            BigDecimal currentPriceOfToBeDiscountedOrderEntries = getRuleEngineCalculationService().getCurrentPrice(selectedOrderEntryRaos, selectedOrderEntryMap);
            if(currentPriceOfToBeDiscountedOrderEntries.compareTo(fixedPrice) > 0)
            {
                isPerformed = true;
                RuleEngineResultRAO result = context.getRuleEngineResultRao();
                CartRAO cartRAO = context.getCartRao();
                BigDecimal discountAmount = getCurrencyUtils().applyRounding(currentPriceOfToBeDiscountedOrderEntries.subtract(fixedPrice), cartRAO.getCurrencyIsoCode());
                DiscountRAO discount = getRuleEngineCalculationService().addOrderLevelDiscount(cartRAO, true, discountAmount);
                result.getActions().add(discount);
                setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
                getConsumptionSupport().consumeOrderEntries(selectedOrderEntryRaos, selectedOrderEntryMap, (AbstractRuleActionRAO)discount);
                discount.getConsumedEntries().forEach(coe -> coe.setFiredRuleCode(discount.getFiredRuleCode()));
                context.insertFacts(new Object[] {discount, discount.getConsumedEntries()});
                for(OrderEntryRAO selectedOrderEntry : selectedOrderEntryRaos)
                {
                    getRaoUtils().addAction((AbstractActionedRAO)selectedOrderEntry, (AbstractRuleActionRAO)discount);
                    context.scheduleForUpdate(new Object[] {selectedOrderEntry});
                }
                context.scheduleForUpdate(new Object[] {cartRAO, result});
            }
        }
        return isPerformed;
    }
}
