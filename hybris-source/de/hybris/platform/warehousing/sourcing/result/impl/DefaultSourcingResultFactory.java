package de.hybris.platform.warehousing.sourcing.result.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultSourcingResultFactory implements SourcingResultFactory
{
    public SourcingResult create(AbstractOrderEntryModel orderEntry, SourcingLocation sourcingLocation, Long quantity)
    {
        SourcingResult sourcingResult = new SourcingResult();
        Map<AbstractOrderEntryModel, Long> allocation = new HashMap<>();
        if(quantity.longValue() > 0L)
        {
            allocation.put(orderEntry, quantity);
        }
        sourcingResult.setAllocation(allocation);
        sourcingResult.setWarehouse(sourcingLocation.getWarehouse());
        return sourcingResult;
    }


    public SourcingResult create(Collection<AbstractOrderEntryModel> orderEntries, SourcingLocation sourcingLocation)
    {
        SourcingResult sourcingResult = new SourcingResult();
        Map<AbstractOrderEntryModel, Long> allocation = new HashMap<>();
        orderEntries.stream().filter(entry -> (((OrderEntryModel)entry).getQuantityUnallocated().longValue() > 0L))
                        .forEach(entry -> allocation.put(entry, ((OrderEntryModel)entry).getQuantityUnallocated()));
        sourcingResult.setAllocation(allocation);
        sourcingResult.setWarehouse(sourcingLocation.getWarehouse());
        return sourcingResult;
    }


    public SourcingResult create(Map<AbstractOrderEntryModel, Long> allocations, SourcingLocation sourcingLocation)
    {
        SourcingResult sourcingResult = new SourcingResult();
        sourcingResult.setAllocation(Maps.newHashMap(allocations));
        sourcingResult.setWarehouse(sourcingLocation.getWarehouse());
        return sourcingResult;
    }


    public SourcingResults create(Collection<SourcingResults> results)
    {
        SourcingResults target = new SourcingResults();
        target.setResults(Sets.newHashSet());
        target.setComplete((!results.isEmpty() && !results.stream().anyMatch(result -> !result.isComplete())));
        results.forEach(source -> mergeResults(source, target));
        return target;
    }


    protected void mergeResults(SourcingResults source, SourcingResults target)
    {
        target.setResults(Sets.newHashSet(Iterables.concat(source.getResults(), target.getResults())));
    }
}
