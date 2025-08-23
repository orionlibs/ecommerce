package de.hybris.platform.ruleengineservices.rule.evaluation.actions;

import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.assertj.core.util.Lists;

public class AbstractRulePartnerProductAction extends AbstractRuleExecutableSupport
{
    protected List<EntriesSelectionStrategyRPD> createSelectionStrategyRPDsQualifyingProducts(RuleActionContext context, OrderEntrySelectionStrategy selectionStrategy, Map<String, Integer> qualifyingProductsContainers)
    {
        List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = Lists.newArrayList();
        if(Objects.nonNull(qualifyingProductsContainers))
        {
            for(Map.Entry<String, Integer> entry : qualifyingProductsContainers.entrySet())
            {
                Set<OrderEntryRAO> orderEntries = getOrderEntries(context, entry);
                entriesSelectionStrategyRPDs
                                .add(createSelectionStrategyRPD(selectionStrategy, entry.getValue(), orderEntries, false));
            }
        }
        return entriesSelectionStrategyRPDs;
    }


    protected List<EntriesSelectionStrategyRPD> createSelectionStrategyRPDsTargetProducts(RuleActionContext context, OrderEntrySelectionStrategy selectionStrategy, Map<String, Integer> targetProductsContainers)
    {
        List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = Lists.newArrayList();
        if(targetProductsContainers != null)
        {
            for(Map.Entry<String, Integer> entry : targetProductsContainers.entrySet())
            {
                Set<OrderEntryRAO> orderEntries = getOrderEntries(context, entry);
                entriesSelectionStrategyRPDs.add(createSelectionStrategyRPD(selectionStrategy, entry.getValue(), orderEntries, true));
            }
        }
        return entriesSelectionStrategyRPDs;
    }


    protected Set<OrderEntryRAO> getOrderEntries(RuleActionContext context, Map.Entry<String, Integer> entry)
    {
        String conditionsContainer = entry.getKey();
        return context.getValues(OrderEntryRAO.class, new String[] {conditionsContainer});
    }


    protected EntriesSelectionStrategyRPD createSelectionStrategyRPD(OrderEntrySelectionStrategy selectionStrategy, Integer quantity, Set<OrderEntryRAO> orderEntries, boolean isAction)
    {
        EntriesSelectionStrategyRPD selectionStrategyRPD = new EntriesSelectionStrategyRPD();
        selectionStrategyRPD.setSelectionStrategy(selectionStrategy);
        selectionStrategyRPD.setOrderEntries(new ArrayList<>(orderEntries));
        selectionStrategyRPD.setQuantity(quantity.intValue());
        selectionStrategyRPD.setTargetOfAction(isAction);
        return selectionStrategyRPD;
    }
}
