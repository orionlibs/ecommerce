package de.hybris.platform.warehousing.stock.strategies.impl;

import de.hybris.platform.commerceservices.stock.strategies.CommerceAvailabilityCalculationStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.stock.strategies.StockLevelSelectionStrategy;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultStockLevelSelectionStrategy implements StockLevelSelectionStrategy
{
    private CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy;


    public Map<StockLevelModel, Long> getStockLevelsForAllocation(Collection<StockLevelModel> stockLevels, Long quantityToAllocate)
    {
        Map<StockLevelModel, Long> stockMap = new LinkedHashMap<>();
        List<StockLevelModel> filteredStockLevels = filterAsnCancelledStockLevels(stockLevels);
        long quantityLeftAllocate = quantityToAllocate.longValue();
        List<StockLevelModel> sortedStocks = (List<StockLevelModel>)filteredStockLevels.stream().sorted(Comparator.comparing(StockLevelModel::getReleaseDate, Comparator.nullsFirst(Comparator.naturalOrder()))).collect(Collectors.toList());
        Iterator<StockLevelModel> it = sortedStocks.iterator();
        while(quantityLeftAllocate > 0L && it.hasNext())
        {
            StockLevelModel stockLevel = it.next();
            Long stockAvailability = getCommerceStockLevelCalculationStrategy().calculateAvailability(Collections.singletonList(stockLevel));
            quantityLeftAllocate = addToStockMap(stockMap, stockLevel, quantityLeftAllocate, stockAvailability);
        }
        finalizeStockMap(stockMap, quantityLeftAllocate);
        return stockMap;
    }


    public Map<StockLevelModel, Long> getStockLevelsForCancellation(Collection<AllocationEventModel> allocationEvents, Long quantityToCancel)
    {
        Map<StockLevelModel, Long> stockMap = new LinkedHashMap<>();
        long quantityLeftToCancel = quantityToCancel.longValue();
        List<AllocationEventModel> sortedAllocationEvents = (List<AllocationEventModel>)allocationEvents.stream().sorted(Comparator.comparing(event -> event.getStockLevel().getReleaseDate(), Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        Iterator<AllocationEventModel> it = sortedAllocationEvents.iterator();
        while(quantityLeftToCancel > 0L && it.hasNext())
        {
            AllocationEventModel allocationEvent = it.next();
            long allocatedQuantity = allocationEvent.getQuantity();
            quantityLeftToCancel = addToStockMap(stockMap, allocationEvent.getStockLevel(), quantityLeftToCancel, Long.valueOf(allocatedQuantity));
        }
        finalizeStockMap(stockMap, quantityLeftToCancel);
        return stockMap;
    }


    protected long addToStockMap(Map<StockLevelModel, Long> stockMap, StockLevelModel stockLevel, long quantityLeft, Long quantityAvailable)
    {
        long quantityToFulfill = quantityLeft;
        if(quantityAvailable == null || quantityAvailable.longValue() >= quantityToFulfill)
        {
            stockMap.put(stockLevel, Long.valueOf(quantityLeft));
            quantityToFulfill = 0L;
        }
        else if(quantityAvailable.longValue() > 0L)
        {
            stockMap.put(stockLevel, quantityAvailable);
            quantityToFulfill -= quantityAvailable.longValue();
        }
        return quantityToFulfill;
    }


    protected void finalizeStockMap(Map<StockLevelModel, Long> stockMap, long quantityLeft)
    {
        if(quantityLeft > 0L && stockMap.size() > 0)
        {
            Map.Entry<StockLevelModel, Long> mapEntry = stockMap.entrySet().iterator().next();
            stockMap.put(mapEntry.getKey(), Long.valueOf(((Long)mapEntry.getValue()).longValue() + quantityLeft));
        }
    }


    protected List<StockLevelModel> filterAsnCancelledStockLevels(Collection<StockLevelModel> stockLevels)
    {
        return (List<StockLevelModel>)stockLevels.stream().filter(stockLevel -> (stockLevel.getAsnEntry() == null || !AsnStatus.CANCELLED.equals(stockLevel.getAsnEntry().getAsn().getStatus())))
                        .collect(Collectors.toList());
    }


    protected CommerceAvailabilityCalculationStrategy getCommerceStockLevelCalculationStrategy()
    {
        return this.commerceStockLevelCalculationStrategy;
    }


    @Required
    public void setCommerceStockLevelCalculationStrategy(CommerceAvailabilityCalculationStrategy commerceStockLevelCalculationStrategy)
    {
        this.commerceStockLevelCalculationStrategy = commerceStockLevelCalculationStrategy;
    }
}
