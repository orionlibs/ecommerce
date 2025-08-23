package de.hybris.platform.ordercancel.dao.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.ordercancel.dao.OrderCancelDao;
import de.hybris.platform.ordercancel.exceptions.AmbiguousOrderCancelConfigurationException;
import de.hybris.platform.ordercancel.exceptions.OrderCancelDaoException;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;

public class DefaultOrderCancelDao extends AbstractItemDao implements OrderCancelDao
{
    private static final String ONE_CONFIG_ALLOWED = "Only one Order Cancel Configuration is Allowed";


    public OrderCancelRecordModel getOrderCancelRecord(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("Order cannot be null");
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {OrderCancelRecord} WHERE {order}=?order ");
        query.addQueryParameter("order", order.getPk());
        SearchResult<OrderCancelRecordModel> result = search(query);
        if(result.getResult().isEmpty())
        {
            return null;
        }
        if(result.getResult().size() == 1)
        {
            return result.getResult().get(0);
        }
        throw new OrderCancelDaoException(order.getCode(), "Only one cancel record allowed");
    }


    public Collection<OrderCancelRecordEntryModel> getOrderCancelRecordEntries(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("Order cannot be null");
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {entries.PK} FROM {OrderCancelRecord as record JOIN OrderCancelRecordEntry as entries ON {record.PK} = {entries.modificationRecord}} WHERE {record.order}=?order ");
        query.addQueryParameter("order", order.getPk());
        SearchResult<OrderCancelRecordEntryModel> result = search(query);
        return result.getResult().isEmpty() ? Collections.<OrderCancelRecordEntryModel>emptyList() : result.getResult();
    }


    public Collection<OrderCancelRecordEntryModel> getOrderCancelRecordEntries(EmployeeModel employee)
    {
        return null;
    }


    public OrderCancelConfigModel getOrderCancelConfiguration()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {OrderCancelConfig}");
        SearchResult<OrderCancelConfigModel> result = search(query);
        if(result.getTotalCount() > 1)
        {
            throw new AmbiguousOrderCancelConfigurationException("Only one Order Cancel Configuration is Allowed");
        }
        return (result.getTotalCount() == 0) ? null : result.getResult().get(0);
    }


    public OrderEntryCancelRecordEntryModel getOrderEntryCancelRecord(OrderEntryModel orderEntry, OrderCancelRecordEntryModel cancelEntry)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {OrderEntryCancelRecordEntry as oe} WHERE {oe.modificationRecordEntry}=?record AND {oe.orderEntry}=?orderEntry");
        query.addQueryParameter("record", cancelEntry.getPk());
        query.addQueryParameter("orderEntry", orderEntry.getPk());
        SearchResult<OrderEntryCancelRecordEntryModel> result = search(query);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }
}
