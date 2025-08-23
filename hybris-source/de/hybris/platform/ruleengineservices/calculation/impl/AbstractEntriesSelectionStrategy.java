package de.hybris.platform.ruleengineservices.calculation.impl;

import com.google.common.collect.Maps;
import de.hybris.platform.ruleengineservices.calculation.EntriesSelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractEntriesSelectionStrategy implements EntriesSelectionStrategy
{
    public Map<Integer, Integer> pickup(EntriesSelectionStrategyRPD strategy, Map<Integer, Integer> consumableQuantities)
    {
        Map<Integer, Integer> result = Maps.newHashMap();
        int itemsToConsume = strategy.getQuantity();
        for(OrderEntryRAO orderEntry : getOrderEntriesToProcess(strategy))
        {
            Integer consumableQuantity = consumableQuantities.get(orderEntry.getEntryNumber());
            if(Objects.isNull(consumableQuantity))
            {
                consumableQuantity = Integer.valueOf(orderEntry.getQuantity());
            }
            if(itemsToConsume > 0)
            {
                int applicableUnits;
                if(itemsToConsume >= consumableQuantity.intValue())
                {
                    applicableUnits = consumableQuantity.intValue();
                    itemsToConsume -= consumableQuantity.intValue();
                }
                else
                {
                    applicableUnits = itemsToConsume;
                    itemsToConsume = 0;
                }
                result.put(orderEntry.getEntryNumber(), Integer.valueOf(applicableUnits));
            }
        }
        if(itemsToConsume > 0)
        {
            throw new IllegalArgumentException("The Order Entries have less units than required to pickup.");
        }
        return result;
    }


    protected abstract List<OrderEntryRAO> getOrderEntriesToProcess(EntriesSelectionStrategyRPD paramEntriesSelectionStrategyRPD);
}
