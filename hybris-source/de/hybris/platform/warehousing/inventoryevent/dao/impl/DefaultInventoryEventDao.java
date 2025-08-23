package de.hybris.platform.warehousing.inventoryevent.dao.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.warehousing.inventoryevent.dao.InventoryEventDao;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultInventoryEventDao extends AbstractItemDao implements InventoryEventDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInventoryEventDao.class);
    protected static final String ALLOCATION_EVENTS_FOR_CONSIGNMENT_ENTRIES_QUERY = "SELECT {pk} FROM {AllocationEvent} WHERE {consignmentEntry} IN (?consignmentEntries)";


    public Collection<AllocationEventModel> getAllocationEventsForConsignmentEntry(ConsignmentEntryModel consignmentEntry)
    {
        String query = "SELECT {pk} FROM {AllocationEvent} WHERE {consignmentEntry} = ?consignmentEntry";
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AllocationEvent} WHERE {consignmentEntry} = ?consignmentEntry");
        fsQuery.addQueryParameter("consignmentEntry", consignmentEntry);
        return getInventoryEvents(fsQuery);
    }


    public Collection<AllocationEventModel> getAllocationEventsForOrderEntry(OrderEntryModel orderEntry)
    {
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AllocationEvent} WHERE {consignmentEntry} IN (?consignmentEntries)");
        fsQuery.addQueryParameter("consignmentEntries", orderEntry.getConsignmentEntries());
        return getInventoryEvents(fsQuery);
    }


    public <T extends de.hybris.platform.warehousing.model.InventoryEventModel> Collection<T> getInventoryEventsForStockLevel(StockLevelModel stockLevel, Class<T> eventClassType)
    {
        try
        {
            Field eventType = eventClassType.getDeclaredField("_TYPECODE");
            eventType.setAccessible(true);
            String eventClassTypeString = (String)eventType.get(null);
            String query = "SELECT PK FROM {" + eventClassTypeString + "} WHERE {stocklevel} = ?stockLevel";
            FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(query);
            fsQuery.addQueryParameter("stockLevel", stockLevel);
            return getInventoryEvents(fsQuery);
        }
        catch(NoSuchFieldException | IllegalAccessException e)
        {
            LOGGER.info("Invalid inventory event type {}", eventClassType);
            return Collections.emptyList();
        }
    }


    public Collection<AllocationEventModel> getAllocationEventsForConsignment(ConsignmentModel consignment)
    {
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AllocationEvent} WHERE {consignmentEntry} IN (?consignmentEntries)");
        fsQuery.addQueryParameter("consignmentEntries", consignment.getConsignmentEntries());
        return getInventoryEvents(fsQuery);
    }


    protected <T extends de.hybris.platform.warehousing.model.InventoryEventModel> Collection<T> getInventoryEvents(FlexibleSearchQuery query)
    {
        SearchResult<T> result = getFlexibleSearchService().search(query);
        return result.getResult();
    }
}
