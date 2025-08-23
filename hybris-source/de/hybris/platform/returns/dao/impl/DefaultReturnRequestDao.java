package de.hybris.platform.returns.dao.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.returns.dao.ReturnRequestDao;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultReturnRequestDao extends AbstractItemDao implements ReturnRequestDao
{
    public ReturnRequestModel createReturnRequest(OrderModel order)
    {
        ReturnRequestModel request = (ReturnRequestModel)getModelService().create(ReturnRequestModel.class);
        request.setOrder(order);
        getModelService().save(request);
        return request;
    }


    public List<ReturnRequestModel> getReturnRequests(String code)
    {
        OrderModel order = getOrderByCode(code);
        if(order == null)
        {
            return Collections.emptyList();
        }
        Map<String, Object> values = new HashMap<>();
        values.put("value", order);
        String query = "SELECT {" + Item.PK + "} FROM {ReturnRequest} WHERE { Order} = ?value ORDER BY {" + Item.PK + "} ASC";
        List<ReturnRequestModel> result = getFlexibleSearchService().search(query, values).getResult();
        return (result == null) ? Collections.<ReturnRequestModel>emptyList() : result;
    }


    protected OrderModel getOrderByCode(String code)
    {
        Map<String, Object> values = new HashMap<>();
        values.put("value", code);
        String query = "SELECT {" + Item.PK + "} FROM {Order} WHERE {code} = ?value ORDER BY {" + Item.PK + "} ASC";
        List<OrderModel> result = getFlexibleSearchService().search(query, values).getResult();
        return result.isEmpty() ? null : result.iterator().next();
    }
}
