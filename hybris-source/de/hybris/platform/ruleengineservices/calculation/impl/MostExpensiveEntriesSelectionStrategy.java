package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MostExpensiveEntriesSelectionStrategy extends AbstractEntriesSelectionStrategy
{
    protected List<OrderEntryRAO> getOrderEntriesToProcess(EntriesSelectionStrategyRPD strategy)
    {
        return (List<OrderEntryRAO>)strategy.getOrderEntries().stream().sorted(Comparator.comparing(OrderEntryRAO::getPrice, Comparator.reverseOrder()))
                        .collect(Collectors.toList());
    }
}
