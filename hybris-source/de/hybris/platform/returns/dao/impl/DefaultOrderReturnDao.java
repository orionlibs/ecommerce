package de.hybris.platform.returns.dao.impl;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.exceptions.OrderCancelDaoException;
import de.hybris.platform.returns.dao.OrderReturnDao;
import de.hybris.platform.returns.model.OrderEntryReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordEntryModel;
import de.hybris.platform.returns.model.OrderReturnRecordModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.Collections;

public class DefaultOrderReturnDao extends AbstractItemDao implements OrderReturnDao
{
    public OrderEntryReturnRecordEntryModel getOrderEntryReturnRecord(OrderEntryModel orderEntry, OrderReturnRecordEntryModel returnEntry)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {OrderReturnRecordEntry as oe} WHERE {oe.modificationRecordEntry}=?record AND {oe.orderEntry}=?orderEntry");
        query.addQueryParameter("record", returnEntry.getPk());
        query.addQueryParameter("orderEntry", orderEntry.getPk());
        SearchResult<OrderEntryReturnRecordEntryModel> result = search(query);
        return result.getResult().isEmpty() ? null : result.getResult().get(0);
    }


    public OrderReturnRecordModel getOrderReturnRecord(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("Order cannot be null");
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {OrderReturnRecord} WHERE {order}=?order ");
        query.addQueryParameter("order", order.getPk());
        SearchResult<OrderReturnRecordModel> result = search(query);
        if(result.getResult().isEmpty())
        {
            return null;
        }
        if(result.getResult().size() == 1)
        {
            return result.getResult().get(0);
        }
        throw new OrderCancelDaoException(order.getCode(), "Only one return record allowed");
    }


    public Collection<OrderReturnRecordEntryModel> getOrderReturnRecordEntries(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("Order cannot be null");
        }
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {entries.PK} FROM {OrderReturnRecord as record JOIN OrderReturnRecordEntry as entries ON {record.PK} = {entries.modificationRecord}} WHERE {record.order}=?order ");
        query.addQueryParameter("order", order.getPk());
        SearchResult<OrderReturnRecordEntryModel> result = search(query);
        return result.getResult().isEmpty() ? Collections.<OrderReturnRecordEntryModel>emptyList() : result.getResult();
    }
}
