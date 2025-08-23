package de.hybris.platform.returns.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.strategy.ReturnableCheck;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

public class DefaultReturnEntryBasedReturnableCheck implements ReturnableCheck
{
    @Resource
    private FlexibleSearchService flexibleSearchService;


    public boolean perform(OrderModel order, AbstractOrderEntryModel orderentry, long returnQuantity)
    {
        if(returnQuantity < 1L || orderentry.getQuantity().longValue() < returnQuantity)
        {
            return false;
        }
        return (maxReturnQuantity(order, orderentry) >= returnQuantity);
    }


    public long maxReturnQuantity(OrderModel order, AbstractOrderEntryModel orderentry)
    {
        List<ReturnEntryModel> returnsEntries = getReturnEntry(orderentry);
        if(returnsEntries.isEmpty())
        {
            return orderentry.getQuantity().longValue();
        }
        long returnedEntriesSoFar = 0L;
        for(ReturnEntryModel returnsEntry : returnsEntries)
        {
            returnedEntriesSoFar += returnsEntry.getExpectedQuantity().longValue();
        }
        return orderentry.getQuantity().longValue() - returnedEntriesSoFar;
    }


    protected List<ReturnEntryModel> getReturnEntry(AbstractOrderEntryModel entry)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("entry", entry);
        String query = "SELECT {ret." + Item.PK + "} FROM { ReturnEntry AS ret} WHERE {orderEntry} = ?entry ORDER BY {ret." + Item.PK + "} ASC";
        return getFlexibleSearchService().search(query, params).getResult();
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
