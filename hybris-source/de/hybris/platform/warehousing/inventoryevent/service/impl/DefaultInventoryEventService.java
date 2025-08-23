package de.hybris.platform.warehousing.inventoryevent.service.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import de.hybris.platform.warehousing.inventoryevent.dao.InventoryEventDao;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import de.hybris.platform.warehousing.model.CancellationEventModel;
import de.hybris.platform.warehousing.model.IncreaseEventModel;
import de.hybris.platform.warehousing.model.ShrinkageEventModel;
import de.hybris.platform.warehousing.model.WastageEventModel;
import de.hybris.platform.warehousing.stock.strategies.StockLevelSelectionStrategy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultInventoryEventService implements InventoryEventService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultInventoryEventService.class);
    private ModelService modelService;
    private InventoryEventDao inventoryEventDao;
    private StockLevelSelectionStrategy stockLevelSelectionStrategy;
    private StockService stockService;
    private TimeService timeService;


    public Collection<AllocationEventModel> getAllocationEventsForConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentEntry", consignmentEntry);
        return getInventoryEventDao().getAllocationEventsForConsignmentEntry(consignmentEntry);
    }


    public Collection<AllocationEventModel> getAllocationEventsForOrderEntry(OrderEntryModel orderEntry)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("orderEntry", orderEntry);
        return orderEntry.getConsignmentEntries().isEmpty() ?
                        Collections.<AllocationEventModel>emptyList() :
                        this.inventoryEventDao.getAllocationEventsForOrderEntry(orderEntry);
    }


    public <T extends de.hybris.platform.warehousing.model.InventoryEventModel> Collection<T> getInventoryEventsForStockLevel(StockLevelModel stockLevel, Class<T> eventClassType)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stocklevel", stockLevel);
        ServicesUtil.validateParameterNotNullStandardMessage("eventClassType", eventClassType);
        return getInventoryEventDao().getInventoryEventsForStockLevel(stockLevel, eventClassType);
    }


    public Collection<AllocationEventModel> createAllocationEvents(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        Preconditions.checkArgument(!consignment.getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        List<AllocationEventModel> allocationEvents = (List<AllocationEventModel>)consignment.getConsignmentEntries().stream().map(this::createAllocationEventsForConsignmentEntry).flatMap(Collection::stream).collect(Collectors.toList());
        getModelService().saveAll(allocationEvents);
        return allocationEvents;
    }


    public List<AllocationEventModel> createAllocationEventsForConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        Preconditions.checkArgument(!consignmentEntry.getConsignment().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        LOGGER.debug("Creating allocation event for ConsignmentEntry :: Product [{}], at Warehouse [{}]: \tQuantity: '{}'", new Object[] {consignmentEntry
                        .getOrderEntry().getProduct().getCode(), consignmentEntry.getConsignment().getWarehouse().getCode(), consignmentEntry
                        .getQuantity()});
        Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(consignmentEntry.getOrderEntry().getProduct(),
                        Collections.singletonList(consignmentEntry.getConsignment().getWarehouse()));
        Map<StockLevelModel, Long> stockLevelForAllocation = getStockLevelSelectionStrategy().getStockLevelsForAllocation(stockLevels, consignmentEntry.getQuantity());
        List<AllocationEventModel> allocationEvents = (List<AllocationEventModel>)stockLevelForAllocation.entrySet().stream().map(stockMapEntry -> {
            AllocationEventModel allocationEvent = (AllocationEventModel)getModelService().create(AllocationEventModel.class);
            allocationEvent.setConsignmentEntry(consignmentEntry);
            allocationEvent.setStockLevel((StockLevelModel)stockMapEntry.getKey());
            allocationEvent.setEventDate(getTimeService().getCurrentTime());
            allocationEvent.setQuantity(((Long)stockMapEntry.getValue()).longValue());
            getModelService().save(allocationEvent);
            return allocationEvent;
        }).collect(Collectors.toList());
        return allocationEvents;
    }


    public ShrinkageEventModel createShrinkageEvent(ShrinkageEventModel shrinkageEventModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stockLevel", shrinkageEventModel.getStockLevel());
        Preconditions.checkArgument(!shrinkageEventModel.getStockLevel().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        StockLevelModel stockLevel = shrinkageEventModel.getStockLevel();
        Long quantity = Long.valueOf(shrinkageEventModel.getQuantity());
        LOGGER.debug("Creating Shrinkage event for ConsignmentEntry :: Product [{}], at Warehouse [{}]: \tQuantity: '{}'", new Object[] {stockLevel
                        .getProductCode(), stockLevel.getWarehouse(), quantity});
        ShrinkageEventModel shrinkageEvent = (ShrinkageEventModel)getModelService().create(ShrinkageEventModel.class);
        shrinkageEvent.setStockLevel(stockLevel);
        shrinkageEvent.setEventDate(getTimeService().getCurrentTime());
        shrinkageEvent.setQuantity(quantity.longValue());
        shrinkageEvent.setComments(shrinkageEventModel.getComments());
        getModelService().save(shrinkageEvent);
        return shrinkageEvent;
    }


    public WastageEventModel createWastageEvent(WastageEventModel wastageEventModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stockLevel", wastageEventModel.getStockLevel());
        Preconditions.checkArgument(!wastageEventModel.getStockLevel().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        StockLevelModel stockLevel = wastageEventModel.getStockLevel();
        Long quantity = Long.valueOf(wastageEventModel.getQuantity());
        LOGGER.debug("Creating Wastage event for ConsignmentEntry :: Product [{}], at Warehouse [{}]: \tQuantity: '{}'", new Object[] {stockLevel
                        .getProductCode(), stockLevel.getWarehouse(), quantity});
        WastageEventModel wastageEvent = (WastageEventModel)getModelService().create(WastageEventModel.class);
        wastageEvent.setStockLevel(stockLevel);
        wastageEvent.setEventDate(getTimeService().getCurrentTime());
        wastageEvent.setQuantity(quantity.longValue());
        getModelService().save(wastageEvent);
        return wastageEvent;
    }


    public List<CancellationEventModel> createCancellationEvents(CancellationEventModel cancellationEventModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentEntry", cancellationEventModel.getConsignmentEntry());
        Preconditions.checkArgument(!cancellationEventModel.getConsignmentEntry().getConsignment().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        Collection<AllocationEventModel> allocationEvents = getAllocationEventsForConsignmentEntry(cancellationEventModel
                        .getConsignmentEntry());
        Map<StockLevelModel, Long> stockLevelsForCancellation = getStockLevelSelectionStrategy().getStockLevelsForCancellation(allocationEvents, Long.valueOf(cancellationEventModel.getQuantity()));
        List<CancellationEventModel> cancellationEvents = (List<CancellationEventModel>)stockLevelsForCancellation.entrySet().stream().map(stockMapEntry -> {
            CancellationEventModel cancellationEvent = (CancellationEventModel)getModelService().create(CancellationEventModel.class);
            cancellationEvent.setConsignmentEntry(cancellationEventModel.getConsignmentEntry());
            cancellationEvent.setEventDate(getTimeService().getCurrentTime());
            cancellationEvent.setQuantity(((Long)stockMapEntry.getValue()).longValue());
            cancellationEvent.setStockLevel((StockLevelModel)stockMapEntry.getKey());
            cancellationEvent.setReason(cancellationEventModel.getReason());
            getModelService().save(cancellationEvent);
            return cancellationEvent;
        }).collect(Collectors.toList());
        return cancellationEvents;
    }


    public IncreaseEventModel createIncreaseEvent(IncreaseEventModel increaseEventModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("stockLevel", increaseEventModel.getStockLevel());
        Preconditions.checkArgument(!increaseEventModel.getStockLevel().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        IncreaseEventModel increaseEvent = (IncreaseEventModel)getModelService().create(IncreaseEventModel.class);
        increaseEvent.setEventDate(getTimeService().getCurrentTime());
        increaseEvent.setQuantity(increaseEventModel.getQuantity());
        increaseEvent.setStockLevel(increaseEventModel.getStockLevel());
        increaseEvent
                        .setComments((increaseEventModel.getComments() != null) ? increaseEventModel.getComments() : new ArrayList());
        getModelService().save(increaseEvent);
        return increaseEvent;
    }


    public void reallocateAllocationEvent(DeclineEntry declineEntry, Long quantityToDecline)
    {
        Preconditions.checkArgument(!declineEntry.getConsignmentEntry().getConsignment().getWarehouse().isExternal(), "External warehouses are not allowed to create AllocationEvent");
        Collection<AllocationEventModel> allocationEvents = getAllocationEventsForConsignmentEntry(declineEntry
                        .getConsignmentEntry());
        if(declineEntry.getConsignmentEntry().getQuantity().equals(quantityToDecline))
        {
            getModelService().removeAll(allocationEvents);
        }
        else
        {
            Map<AllocationEventModel, Long> allocationEventsForReallocation = getAllocationEventsForReallocation(allocationEvents, quantityToDecline);
            for(Map.Entry<AllocationEventModel, Long> allocationMapEntry : allocationEventsForReallocation.entrySet())
            {
                AllocationEventModel allocationEvent = allocationMapEntry.getKey();
                if(allocationEvent.getQuantity() == ((Long)allocationMapEntry.getValue()).longValue())
                {
                    getModelService().remove(allocationEvent);
                    continue;
                }
                allocationEvent.setQuantity(allocationEvent.getQuantity() - ((Long)allocationMapEntry.getValue()).longValue());
                getModelService().save(allocationEvent);
            }
        }
    }


    public Map<AllocationEventModel, Long> getAllocationEventsForReallocation(Collection<AllocationEventModel> allocationEvents, Long quantityToDecline)
    {
        Map<AllocationEventModel, Long> allocationMap = new LinkedHashMap<>();
        long quantityLeftToDecline = quantityToDecline.longValue();
        List<AllocationEventModel> sortedAllocationEvents = (List<AllocationEventModel>)allocationEvents.stream().sorted(Comparator.comparing(event -> event.getStockLevel().getReleaseDate(), Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        Iterator<AllocationEventModel> it = sortedAllocationEvents.iterator();
        while(quantityLeftToDecline > 0L && it.hasNext())
        {
            AllocationEventModel allocationEvent = it.next();
            long declinableQty = allocationEvent.getQuantity();
            quantityLeftToDecline = addToAllocationMap(allocationMap, allocationEvent, quantityLeftToDecline, Long.valueOf(declinableQty));
        }
        finalizeStockMap(allocationMap, quantityLeftToDecline);
        return allocationMap;
    }


    protected long addToAllocationMap(Map<AllocationEventModel, Long> allocationMap, AllocationEventModel allocationEvent, long quantityLeft, Long quantityAvailable)
    {
        long quantityToFulfill = quantityLeft;
        if(quantityAvailable == null || quantityAvailable.longValue() >= quantityToFulfill)
        {
            allocationMap.put(allocationEvent, Long.valueOf(quantityLeft));
            quantityToFulfill = 0L;
        }
        else if(quantityAvailable.longValue() > 0L)
        {
            allocationMap.put(allocationEvent, quantityAvailable);
            quantityToFulfill -= quantityAvailable.longValue();
        }
        return quantityToFulfill;
    }


    protected void finalizeStockMap(Map<AllocationEventModel, Long> allocationMap, long quantityLeft)
    {
        if(quantityLeft > 0L && allocationMap.size() > 0)
        {
            Map.Entry<AllocationEventModel, Long> mapEntry = allocationMap.entrySet().iterator().next();
            allocationMap.put(mapEntry.getKey(), Long.valueOf(((Long)mapEntry.getValue()).longValue() + quantityLeft));
        }
    }


    public Collection<AllocationEventModel> getAllocationEventsForConsignment(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        return getInventoryEventDao().getAllocationEventsForConsignment(consignment);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected InventoryEventDao getInventoryEventDao()
    {
        return this.inventoryEventDao;
    }


    @Required
    public void setInventoryEventDao(InventoryEventDao inventoryEventDao)
    {
        this.inventoryEventDao = inventoryEventDao;
    }


    protected StockLevelSelectionStrategy getStockLevelSelectionStrategy()
    {
        return this.stockLevelSelectionStrategy;
    }


    @Required
    public void setStockLevelSelectionStrategy(StockLevelSelectionStrategy stockLevelSelectionStrategy)
    {
        this.stockLevelSelectionStrategy = stockLevelSelectionStrategy;
    }


    protected StockService getStockService()
    {
        return this.stockService;
    }


    @Required
    public void setStockService(StockService stockService)
    {
        this.stockService = stockService;
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
